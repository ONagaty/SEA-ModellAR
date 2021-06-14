package eu.cessar.ct.jet.ant.internal;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.jet.core.JETCoreConstants;
import eu.cessar.ct.jet.core.JETCoreUtils;
import eu.cessar.ct.sdk.ant.AbstractCessarTask;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 *
 * Abstract Ant task for generating a jet.
 *
 */
public abstract class AbstractGenerateJetTask extends AbstractCessarTask
{
	private static final String KEY_OUTPUT_FOLDER = "output.folder"; //$NON-NLS-1$

	private static final String PREFERENCE_NODE = "eu.cessar.ct.jet.core"; //$NON-NLS-1$

	private static final String PREFERENCE_NAME = "stop.on.error"; //$NON-NLS-1$

	/** comma separated project relative ant paths */
	// == mandatory arg
	private String targetJets;

	// == optional args
	private String output;

	private String stopOnError;
	// ====

	private List<IFile> jetJarFiles;

	private IFolder outputFolder;

	private boolean stopJetGenOnError;

	/**
	 * Constructor
	 */
	public AbstractGenerateJetTask()
	{
		jetJarFiles = new ArrayList<IFile>();
	}

	/**
	 * Return the Jet jar files
	 *
	 * @return the list of jar files
	 */
	protected List<IFile> getJetJarFiles()
	{
		return jetJarFiles;
	}

	/**
	 * Return the output folder where code will be generated
	 *
	 * @return the output folder
	 */
	protected IFolder getOutputFolder()
	{
		return outputFolder;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.ant.AbstractTask#checkArgs()
	 */
	@Override
	protected void checkArgs()
	{
		super.checkArgs();

		if (targetJets == null || targetJets.length() == 0)
		{
			throw new BuildException("At least one target jet should be set"); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.ant.AbstractTask#processArgs()
	 */
	@Override
	protected void processArgs()
	{
		super.processArgs();

		// retrieve the compiled jets out of the targetJets arg
		StringTokenizer st = new StringTokenizer(targetJets, ","); //$NON-NLS-1$
		if (GraphicsEnvironment.isHeadless())
		{
			try
			{
				iProject.build(10, null);
			}
			catch (CoreException e)
			{
				throw new BuildException(e.getMessage());
			}
		}
		while (st.hasMoreTokens())
		{

			String relPath = st.nextToken().trim();

			IPath path = new Path(relPath).makeAbsolute();
			IFile file = iProject.getFile(path);

			IFile jarFile = JETCoreUtils.getJarFile(file);
			if (!jarFile.exists())
			{

				throw new BuildException(NLS.bind("Jet {0} is not compiled", relPath)); //$NON-NLS-1$
			}

			jetJarFiles.add(jarFile);
		}

		// check if output is set
		if (output != null)
		{

			outputFolder = iProject.getFolder(output);
		}

		// check stopOnError arg
		if (stopOnError != null)
		{

			if ("true".equalsIgnoreCase(stopOnError)) //$NON-NLS-1$
			{

				stopJetGenOnError = true;

			}
			else if ("false".equalsIgnoreCase(stopOnError)) //$NON-NLS-1$
			{

				stopJetGenOnError = false;
			}
		}
		else
		{
			// check project setting
			ProjectScope scope = new ProjectScope(iProject);
			IEclipsePreferences node = scope.getNode(PREFERENCE_NODE);
			if (node != null)
			{
				// default is true
				stopJetGenOnError = node.getBoolean(PREFERENCE_NAME, true);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.ant.AbstractTask#doExecute()
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void doExecute()
	{
		// populate map with appropriate data for JetTaskManager
		Map<String, Object> map = new HashMap<String, Object>();
		if (outputFolder != null)
		{
			map.put(KEY_OUTPUT_FOLDER, outputFolder);
		}

		ICessarTaskManager<IFile> manager = (ICessarTaskManager<IFile>) ExecutionService.createManager(iProject,
			JETCoreConstants.JET, map);
		if (manager != null)
		{

			manager.initialize(jetJarFiles);

			IStatus status = manager.execute(false, null, new NullProgressMonitor());
			if (status.getSeverity() == IStatus.ERROR)
			{

				if (stopJetGenOnError)
				{
					Throwable exception = status.getException();
					throw new BuildException(
						"Jet generation stopped because an error ocurred: " + (exception != null ? exception : "")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
	}

	/**
	 * @return Returns the output.
	 */
	public String getOutput()
	{
		return output;
	}

	/**
	 * @param output
	 *        The output to set.
	 */
	public void setOutput(final String output)
	{
		this.output = output;
	}

	/**
	 * @return Returns the targetJets.
	 */
	public String getTargetJets()
	{
		return targetJets;
	}

	/**
	 * @param targetJets
	 *        The targetJets to set.
	 */
	public void setTargetJets(final String targetJets)
	{
		this.targetJets = targetJets;
	}

	/**
	 *
	 * @param stopOnError
	 */
	public void setStopOnError(final String stopOnError)
	{
		this.stopOnError = stopOnError;
	}

	/**
	 * @return the stopOnError
	 */
	public String getStopOnError()
	{
		return stopOnError;
	}

}
