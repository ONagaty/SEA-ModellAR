package eu.cessar.ct.runtime.internal.execution;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

import eu.cessar.ct.runtime.CessarRuntimeUtils;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;

/**
 * A preference initializer that initializes CESSAR-CT specific preferences.
 *
 */
public class CessarPreferenceInitializer extends AbstractPreferenceInitializer
{
	@SuppressWarnings("nls")
	private static final String VM_ARGS_DEFAULT = "-Xss1M\n"
		+ "-Xmx512M\n"
		+ "-XX:MaxHeapFreeRatio=30\n" + "-XX:MinHeapFreeRatio=20\n" + "-D" + CessarRuntimeUtils.SYS_PROP_MM3x + "=${system_property:" //$NON-NLS-1$ //$NON-NLS-2$
		+ CessarRuntimeUtils.SYS_PROP_MM3x + "}\n" + "-D" + CessarRuntimeUtils.SYS_PROP_MM40 //$NON-NLS-1$//$NON-NLS-2$
		+ "=${system_property:" + CessarRuntimeUtils.SYS_PROP_MM40 + "}\n"; //$NON-NLS-1$ //$NON-NLS-2$;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences()
	{
		IEclipsePreferences preferences = DefaultScope.INSTANCE.getNode(CessarPluginActivator.PLUGIN_ID);
		preferences.put(CessarRuntimeUtils.VM_ARGS_DEFAULT, VM_ARGS_DEFAULT);
		try
		{
			preferences.flush();
		}
		catch (BackingStoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

	}
}
