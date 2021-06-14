package eu.cessar.ct.sdk.ant;

import java.util.List;

import org.apache.tools.ant.BuildException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import gautosar.gecucdescription.GModuleConfiguration;

/**
 * Sets a property defined by user with the String representation of the
 * Implementation Configuration Variant of a module configuration if set,
 * otherwise with project's variant. <br>
 * The module configuration can be given by it's qualified name:
 * <code>ecuc_config</code> attribute or by the name of the file that holds the
 * module configuration: <code>ecuc_file_name</code> attribute. <br>
 * NOTE: Exactly 1 module configuration must exist inside the file, otherwise a
 * {@link BuildException} is thrown. As well, if more than 1 module
 * configuration is found inside the project for the given qName, a
 * {@link BuildException} will be thrown.
 * 
 */
public class GetVariantTask extends AbstractGetVariantTask
{

	/** the name of .ecuconfig file */
	private String ecuc_file_name;

	/** the qualified name of a module configuration */
	private String ecuc_config;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ant.tasks.AbstractTask#isTaskInCompatibilityMode()
	 */
	@Override
	protected ETaskType getTaskCompatType()
	{
		return ETaskType.NON_COMPAT_TASK;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.ant.AbstractGetVariantTask#checkArgs()
	 */
	@Override
	protected void checkArgs()
	{
		super.checkArgs();
		if (getEcuc_file_name() == null && ecuc_config == null)
		{
			throw new BuildException("ecuc_file_name or ecuc_config must be set"); //$NON-NLS-1$
		}

		if (getEcuc_file_name() != null && ecuc_config != null)
		{
			throw new BuildException("ecuc_file_name and ecuc_config cannot be both set"); //$NON-NLS-1$
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.ant.AbstractGetVariantTask#doRetrieveModuleConfig()
	 */
	@Override
	protected void doRetrieveModuleConfig()
	{
		if (ecuc_config != null)
		{
			List<EObject> list = EObjectLookupUtils.getEObjectsWithQName(iProject, ecuc_config);
			if (list.size() == 1)
			{
				EObject eObj = list.get(0);
				if (eObj instanceof GModuleConfiguration)
				{
					modConfig = (GModuleConfiguration) eObj;
				}
				else
				{
					throw new BuildException(
						NLS.bind(
							"the value of ecuc_config: {0} must represent the qualified name of a module configuration, but actually it's a {1} ", //$NON-NLS-1$
							new Object[] {ecuc_config, eObj.getClass()}));
				}
			}
			else
			{// 0 or more elements with the same qualified name found
				throw new BuildException(
					NLS.bind(
						"{1} module configuration(s) with {0} qualified name found inside project. Expected exactly 1", //$NON-NLS-1$
						new Object[] {ecuc_config, list.size()}));
			}
		}
		else
		{
			modConfig = getModuleConfigFromFile(ecuc_file_name);
		}

	}

	public String getEcuc_config()
	{
		return ecuc_config;
	}

	public void setEcuc_config(String qName)
	{
		ecuc_config = qName;
	}

	public String getEcuc_file_name()
	{
		return ecuc_file_name;
	}

	public void setEcuc_file_name(String ecuc_file_name)
	{
		this.ecuc_file_name = ecuc_file_name;

	}

}
