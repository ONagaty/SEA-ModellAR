package eu.cessar.ct.validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sphinx.emf.workspace.loading.ModelLoadManager;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.runtime.execution.AbstractCommandLineRunner;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.sdk.utils.ProjectUtils;
import eu.cessar.ct.sdk.utils.ValidationUtils;
import eu.cessar.ct.validation.internal.CessarPluginActivator;
import eu.cessar.ct.validation.internal.Messages;

/**
 * Performs an AUTOSAR validation from the command line, with the following arguments:
 * <ul>
 * <li>
 * -project project_name the name of the project, have to be available inside the workspace</li>
 * <li>
 * [-fileinclude regexp_pattern] a regular expression denoting the files to include in the validation</li>
 * <li>
 * [-uriinclude uri] the fully qualified name of the object to validate</li>
 * </ul>
 * <br>
 * <b>Note:</b> The -fileinclude/-uriinclude arguments are mutual exclusive. If none of the these arguments are given
 * all the files from the project are validated. If multiple -fileinclude arguments appear, all their values are
 * collected.
 */

public class AutosarValidatorRunner extends AbstractCommandLineRunner
{
	/**
	 * error codes
	 */

	/**
	 * No matching file Error Code
	 */
	public static final int EXIT_NO_MATCHING_FILE = 6;

	/**
	 * No matching EObject Error Code
	 */
	public static final int EXIT_NO_MATCHING_EOBJECT = 7;

	/**
	 * No EObject to Validate Error Code
	 */
	public static final int EXIT_NO_EOBJECT_TO_VALIDATE = 8;

	private static final String REPORT_EXTENSION_XML = ".xml"; //$NON-NLS-1$
	private static final String REPORT_EXTENSION_CSV = ".csv"; //$NON-NLS-1$
	private static final String REPORT_EXTENSION_XLS = ".xls"; //$NON-NLS-1$

	private static final String ARG_FILEINCLUDE = "fileinclude"; //$NON-NLS-1$
	private static final String ARG_URIINCLUDE = "uriinclude"; //$NON-NLS-1$
	private static final String ARG_OUTFILE = "outputFile"; //$NON-NLS-1$
	private static final String ARG_STYLESHEET = "stylesheet"; //$NON-NLS-1$
	private static final String ARG_MERGED = "merged"; //$NON-NLS-1$

	private static final String AUTOSAR_VALIDATOR_RUNNER = "AutosarValidator"; //$NON-NLS-1$

	// arguments values
	// Values from fileinclude, received from console command
	private String[] filesIncludedRegexps;
	// Found files from fileIncludedRegexps
	private List<IFile> filesIncluded;
	// Values from uriInclude, received from console command
	private String uriIncluded;

	// eObjects that corespunf to uriInclude
	private List<EObject> uriIncludedObjects;

	// output that will be printed in the output file
	private String outFile;

	// Stylesheet file location
	private String stylesheetFile;

	// the type of the output can be xml xls or csv
	private String outType;

	private String merged;

	/**
	 * @param file
	 */
	public void setOutputFile(String file)
	{
		outFile = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.AbstractCommandLineRunner#getOptions()
	 */
	@Override
	protected Options getOptions()
	{
		// read the options that are provided by the command line
		Options options = super.getOptions();

		OptionBuilder.withArgName(Messages.arg_Fileinclude_Name);
		OptionBuilder.hasArgs();
		OptionBuilder.withDescription(Messages.arg_Fileinclude_Desc);
		Option fileIncludeRegexp = OptionBuilder.create(ARG_FILEINCLUDE);
		options.addOption(fileIncludeRegexp);

		OptionBuilder.withArgName(Messages.arg_UriInclude_Desc);
		OptionBuilder.hasArg();
		OptionBuilder.withDescription(Messages.arg_UriInclude_Name);
		Option uriInclude = OptionBuilder.create(ARG_URIINCLUDE);
		options.addOption(uriInclude);

		OptionBuilder.withArgName(Messages.arg_OutFile_Name);
		OptionBuilder.hasArgs();
		OptionBuilder.withDescription(Messages.arg_OutFile_Desc);
		Option filePath = OptionBuilder.create(ARG_OUTFILE);
		options.addOption(filePath);

		OptionBuilder.withArgName(Messages.arg_StyleSheet_Name);
		OptionBuilder.hasArgs();
		OptionBuilder.withDescription(Messages.arg_StyleSheet_Desc);
		Option stylesheetPath = OptionBuilder.create(ARG_STYLESHEET);
		options.addOption(stylesheetPath);

		OptionBuilder.withArgName(Messages.arg_Merged_Name);
		OptionBuilder.hasArgs();
		OptionBuilder.withDescription(Messages.arg_Merged_Desc);
		Option mergedOption = OptionBuilder.create(ARG_MERGED);
		options.addOption(mergedOption);

		return options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.AbstractCommandLineRunner#processCmdLineOptions()
	 */
	@Override
	protected IStatus processCmdLineOptions(CommandLine commandLine) throws CoreException, IOException
	{
		IStatus status = super.processCmdLineOptions(commandLine);
		if (!status.isOK())
		{
			return status;
		}

		if (commandLine.hasOption(ARG_FILEINCLUDE) && commandLine.hasOption(ARG_URIINCLUDE))
		{
			printUsage();
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG, Messages.error_Wrong_Arg,
				null);
		}

		IProject project = getProject();
		// PlatformUtils.waitForModelLoading(project, new NullProgressMonitor());
		PlatformUtils.waitForModelLoading(project, null);

		// fileinclude argument
		filesIncludedRegexps = commandLine.getOptionValues(ARG_FILEINCLUDE);
		if (filesIncludedRegexps != null)
		{
			filesIncluded = new ArrayList<>();

			for (String regexp: filesIncludedRegexps)
			{
				// collect files matching each regexp
				filesIncluded.addAll(ProjectUtils.getMembers(project, regexp, true));
			}

			if (filesIncluded.isEmpty())
			{
				return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_NO_MATCHING_FILE,
					Messages.error_No_Match_File, null);
			}
		}

		// uriinclude argument
		uriIncluded = commandLine.getOptionValue(ARG_URIINCLUDE);
		if (uriIncluded != null)
		{
			uriIncludedObjects = EObjectLookupUtils.getEObjectsWithQName(project, uriIncluded);
			if (uriIncludedObjects.isEmpty())
			{
				return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_NO_MATCHING_EOBJECT,
					Messages.error_No_Match_eObject, null);
			}
		}

		String outFileOption = commandLine.getOptionValue(ARG_OUTFILE);

		if (outFileOption != null)
		{
			outFile = outFileOption;
		}

		stylesheetFile = commandLine.getOptionValue(ARG_STYLESHEET);

		merged = commandLine.getOptionValue(ARG_MERGED);

		fileExtension();

		return status;
	}

	/**
	 * Determined the correct file extension
	 */
	private void fileExtension()
	{
		// if no file is provided XML is printed to the screen
		if (outFile == null)
		{
			outType = REPORT_EXTENSION_XML;
			return;
		}
		// Gets extension from file name
		String auxExtension = outFile.substring(outFile.lastIndexOf("."), outFile.length()).toLowerCase(); //$NON-NLS-1$
		if (!(auxExtension.equals(REPORT_EXTENSION_CSV) || auxExtension.endsWith(REPORT_EXTENSION_XLS) || auxExtension.equals(REPORT_EXTENSION_XML)))
		{
			CessarPluginActivator.getDefault().logError(Messages.error_Wrong_File_Extension);
		}
		outType = auxExtension;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.AbstractCommandLineRunner#doExecute()
	 */
	@Override
	protected IStatus doExecute() throws CoreException
	{
		List<ValidationMessage> validationMessages = new ArrayList<>();
		IProject project = getProject();
		IStatus iStatus;

		// Validation on file
		if (filesIncluded != null)
		{

			iStatus = ValidationUtils.validateFiles(filesIncluded, isMerged());
			validationMessages = ValidationMessageStore.extractMesseages(iStatus);
			processResults(validationMessages);
			return Status.OK_STATUS;
		}

		// Validation on URI
		if (uriIncluded != null)
		{

			ModelLoadManager.INSTANCE.loadProject(project, Boolean.FALSE, Boolean.FALSE, new NullProgressMonitor());

			List<EObject> eObjectsWithQualifiedName = ModelUtils.getEObjectsWithQualifiedName(project, uriIncluded);

			List<IStatus> iStatusList = new ArrayList<>();
			for (EObject eObject: eObjectsWithQualifiedName)
			{
				IStatus result = ValidationUtils.validate(eObject);

				iStatusList.add(result);
			}

			for (IStatus iStatu: iStatusList)
			{
				List<ValidationMessage> messagesList = ValidationMessageStore.extractMesseages(iStatu);
				validationMessages.addAll(messagesList);
			}

			processResults(validationMessages);

			return Status.OK_STATUS;
		}

		// Validation on whole project
		iStatus = ValidationUtils.validate(project, isMerged());
		validationMessages = ValidationMessageStore.extractMesseages(iStatus);
		processResults(validationMessages);
		return Status.OK_STATUS;
	}

	/**
	 *
	 * @param styleFile
	 *        style sheet file
	 * @return a string containing the content of the style sheet file
	 */
	private static String styleSheetFileToString(String styleSheetFile)
	{
		File styleFile = new File(styleSheetFile);
		FileInputStream inputStream = null;

		try
		{
			inputStream = new FileInputStream(styleFile);
		}
		catch (FileNotFoundException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		Scanner scanner = new Scanner(inputStream);
		String styleString = scanner.useDelimiter("\\A").next(); //$NON-NLS-1$
		scanner.close();
		return styleString;
	}

	/**
	 *
	 * @param out
	 *        Writes to file
	 * @param file
	 *        the path to file
	 */
	private static void writeToFile(String out, String file)
	{
		if (file != null)
		{
			try
			{
				File outF = new File(file);
				FileWriter fileWriter = new FileWriter(outF);
				fileWriter.write(out);
				fileWriter.close();

			}
			catch (IOException io)
			{
				CessarPluginActivator.getDefault().logError(io);
			}
			return;
		}
		// In case that no file was specified print to screen
		System.out.println(out);

	}

	/**
	 * processes the result
	 */
	private void processResults(List<ValidationMessage> messages)
	{
		String styleSheet = null;
		String out;
		// Get default style sheet in case no style sheet is provided
		if (stylesheetFile == null)
		{
			if (outType.equals(REPORT_EXTENSION_XLS))
			{
				URL entry = Platform.getBundle(CessarPluginActivator.PLUGIN_ID).getEntry(Messages.default_Stylesheet);
				if (entry == null)
				{
					System.out.println(Messages.error_No_StyleSheet);
					CessarPluginActivator.getDefault().logError(Messages.error_No_StyleSheet);
				}
				try
				{
					stylesheetFile = FileLocator.toFileURL(entry).getFile();
				}
				catch (IOException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}

			}
			else
			{
				if (outType.equals(REPORT_EXTENSION_CSV))
				{
					out = ValidationMessageStore.messagesToCSV(messages);
					writeToFile(out, outFile);
					return;
				}

				if (outType.equals(REPORT_EXTENSION_XML))
				{
					out = ValidationMessageStore.messagesToXml(messages);
					writeToFile(out, outFile);
					return;
				}

			}

		}

		if (outType.equals(REPORT_EXTENSION_XLS))
		{
			styleSheet = styleSheetFileToString(stylesheetFile);
			out = ValidationMessageStore.messagesWithStyleSheet(messages, styleSheet);
			writeToFile(out, outFile);
			return;
		}

		System.out.println(Messages.error_Wrong_Style_NoXLS);
		CessarPluginActivator.getDefault().logError(Messages.error_Wrong_Style_NoXLS);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.AbstractCommandLineRunner#getRunnerName()
	 */
	@Override
	protected String getRunnerName()
	{
		return AUTOSAR_VALIDATOR_RUNNER;
	}

	private boolean isMerged()
	{
		return (merged != null) && ("true".equalsIgnoreCase(merged)); //$NON-NLS-1$
	}
}
