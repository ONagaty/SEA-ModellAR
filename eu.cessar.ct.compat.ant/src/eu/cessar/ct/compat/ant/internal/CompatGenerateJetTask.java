package eu.cessar.ct.compat.ant.internal;

import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import eu.cessar.ct.jet.ant.internal.AbstractGenerateJetTask;
import eu.cessar.ct.jet.core.JETCoreUtils;

/**
 * The task is used to generate the jet files, for projects in compatibility mode.
 *
 * projectPath [Required] Absolute path toward the Basic Software Project.<br>
 *
 * targetJets [Required] A list of comma separated jet files to generate. The paths must be relative to the given BSW
 * project.<br>
 *
 * jars [Optional] Extra libraries to add to the classpath<br>
 *
 * output [Optional]Output folder for the generated files. If relative, it will be relative to the project path. If not
 * set, each jet will be generated in a jet-relative sub-folder named "generated". <br>
 *
 * rebuild [Optional] default value: false - true : force the the jet generation - false : the jet files will be
 * generated if needed (see prepare) <br>
 *
 * stopOnError [Optional] default value: project specific setting - true : stop execution of jets if an exception is
 * thrown - false: continue execution of jets if an exception is thrown
 *
 */
public class CompatGenerateJetTask extends AbstractGenerateJetTask
{
	private boolean rebuild;

	private String jars;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.ant.GenerateJetTask#isTaskInCompatibilityMode()
	 */
	@Override
	protected ETaskType getTaskCompatType()
	{
		return ETaskType.COMPAT_TASK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.ant.AbstractGenerateJetTask#checkArgs()
	 */
	@Override
	protected void checkArgs()
	{
		if (projectPath == null)
		{
			throw new BuildException("Project path is not set"); //$NON-NLS-1$
		}
		super.checkArgs();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.ant.GenerateJetTask#processArgs()
	 */
	@Override
	protected void processArgs()
	{
		super.processArgs();

		// filter jet files that don't need regeneration
		IFolder outputFolder = getOutputFolder();
		if (outputFolder != null)
		{
			try
			{
				outputFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
			}
			catch (CoreException e)
			{
				// don't do anything
			}
			List<IFile> jetJarFiles = getJetJarFiles();
			Iterator<IFile> iterator = jetJarFiles.iterator();
			while (iterator.hasNext())
			{
				IFile jetFile = iterator.next();
				if (!rebuild && !needGeneration(jetFile, outputFolder))
				{
					iterator.remove();
				}
			}
		}

	}

	private boolean needGeneration(final IFile jetFile, IFolder outputFolder)
	{
		String jetTargetFilename = JETCoreUtils.getJetOutputFileName(jetFile.getName());

		if (jetTargetFilename != null)
		{
			return (outputFolder.findMember(jetTargetFilename) == null);
		}

		return true;
	}

	/**
	 * Returns the value of rebuild.
	 *
	 * @return true if rebuild
	 */
	public boolean getRebuild()
	{
		return rebuild;
	}

	/**
	 * Sets whether to force the jet generation, or to keep the generation, only if needed.
	 *
	 * @param rebuild
	 *        the value of the rebuild, to set.
	 */
	public void setRebuild(boolean rebuild)
	{
		this.rebuild = rebuild;
	}

	/**
	 *
	 * Returns the list of jars libraries added to the classpath.
	 *
	 * @return the list of jars
	 */
	public String getJars()
	{
		return jars;
	}

	/**
	 * Sets the list of jars libraries to add to the classpath.
	 *
	 * @param jars
	 */
	public void setJars(String jars)
	{
		this.jars = jars;
	}

}
