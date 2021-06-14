package eu.cessar.ct.compat.ant.internal;

import org.apache.tools.ant.BuildException;

import eu.cessar.ct.sdk.ant.AbstractGetVariantTask;

/**
 * Sets a property defined by user with the String representation of the Implementation Configuration Variant of a
 * module configuration if set, otherwise with project's variant. <br>
 * The module configuration can be given by the name of the file that holds the module configuration:
 * <code>ecuc_file_name</code> attribute. <br>
 * NOTE: Exactly 1 module configuration must exist inside the file, otherwise a {@link BuildException} is thrown.
 */
public class CompatGetVariantTask extends AbstractGetVariantTask
{

	/** the name of .ecuconfig file */
	private String ecuconfig;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.ant.AbstractGetVariantTask#isTaskInCompatibilityMode()
	 */
	@Override
	protected ETaskType getTaskCompatType()
	{
		return ETaskType.COMPAT_TASK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.ant.AbstractGetVariantTask#checkArgs()
	 */
	@Override
	protected void checkArgs()
	{
		super.checkArgs();
		if (projectPath == null || "".equals(projectPath)) //$NON-NLS-1$
		{
			throw new BuildException("projectPath attribute is mandatory!"); //$NON-NLS-1$
		}

		if (ecuconfig == null)
		{
			throw new BuildException("ecuconfig attribute is mandatory!"); //$NON-NLS-1$

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.ant.AbstractGetVariantTask#doRetrieveModuleConfig()
	 */
	@Override
	protected void doRetrieveModuleConfig()
	{
		modConfig = getModuleConfigFromFile(ecuconfig);

	}

	/**
	 *
	 * @return The name of the ecuconfig file
	 */
	public String getEcuconfig()
	{
		return ecuconfig;
	}

	/**
	 * Set the name of the ecuconfig file
	 * 
	 * @param ecuconfig
	 */
	public void setEcuconfig(String ecuconfig)
	{
		this.ecuconfig = ecuconfig;

	}
}
