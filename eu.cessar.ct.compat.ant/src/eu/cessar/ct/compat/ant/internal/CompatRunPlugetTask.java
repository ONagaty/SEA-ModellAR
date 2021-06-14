package eu.cessar.ct.compat.ant.internal;

import org.apache.tools.ant.BuildException;

import eu.cessar.ct.pluget.ant.internal.AbstractRunPlugetTask;

/**
 * Abstract Task for running pluget(s), for projects that are in compatibility
 * mode.
 * 
 */
public class CompatRunPlugetTask extends AbstractRunPlugetTask
{
	private String jars;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ant.tasks.AbstractTask#isTaskInCompatibilityMode()
	 */
	@Override
	protected ETaskType getTaskCompatType()
	{
		return ETaskType.COMPAT_TASK;
	}

	/* (non-Javadoc)
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

	/**
	 * @return the jars
	 */
	public String getJars()
	{
		return jars;
	}

	/**
	 * @param jars
	 *        the jars to set
	 */
	public void setJars(final String jars)
	{
		this.jars = jars;
	}

}
