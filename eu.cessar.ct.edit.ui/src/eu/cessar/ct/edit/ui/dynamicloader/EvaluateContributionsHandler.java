package eu.cessar.ct.edit.ui.dynamicloader;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.e4.core.di.annotations.Execute;
import org.osgi.framework.Bundle;

import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * Handles the loaders for the Dynamic Loaders
 *
 * @author uidg4449
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class EvaluateContributionsHandler
{
	private static final String ILOADER = "eu.cessar.ct.edit.ui.dynamicLoader"; //$NON-NLS-1$
	private static final String ICONTEXT = "eu.cessar.ct.edit.ui.dynamicLoaderContext"; //$NON-NLS-1$

	private static final String ID = "id"; //$NON-NLS-1$
	private static final String CLASS = "class"; //$NON-NLS-1$
	private static final String LOADERS = "Loader"; //$NON-NLS-1$
	private static final String INPUT_TYPE = "InputType"; //$NON-NLS-1$
	private String ctx;
	private ArrayList<DynamicTreeElement> allWrapped;

	// private o input;

	/**
	 * @param context
	 */
	public EvaluateContributionsHandler(String context)
	{
		super();
		ctx = context;
	}

	/**
	 * @param input
	 */
	@Execute
	public void execute(DynamicTreeElement input)
	{
		if (input.getWrappedObject() == null)
		{
			return;
		}
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();

		IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(ICONTEXT);

		IExtension extensionCTX = extensionPoint.getExtension(ctx);

		IConfigurationElement[] configurationElements = extensionCTX.getConfigurationElements();

		for (IConfigurationElement configurationElement: configurationElements)
		{
			String loaderName = configurationElement.getAttribute(LOADERS);
			if (loaderName != null)
			{
				IConfigurationElement[] loaderConfigs = Platform.getExtensionRegistry().getConfigurationElementsFor(
					ILOADER);
				for (IConfigurationElement loaderConfig: loaderConfigs)
				{
					String attributeName = loaderConfig.getAttribute(ID);
					if (attributeName.equals(loaderName))
					{
						String attribute2 = loaderConfig.getAttribute(INPUT_TYPE);
						try
						{
							String pluginId = loaderConfig.getContributor().getName();
							Bundle bundle = Platform.getBundle(pluginId);
							Class<?> theClass = bundle.loadClass(attribute2);
							if (theClass.isAssignableFrom(input.getWrappedObject().getClass()))
							{
								try
								{
									String img = loaderConfig.getAttribute("icon"); //$NON-NLS-1$
									Object createExecutableExtension = loaderConfig.createExecutableExtension(CLASS);
									executeExtension(input, createExecutableExtension, img);
								}
								catch (CoreException e1)
								{
									CessarPluginActivator.getDefault().logError(e1);
								}
							}
						}
						catch (ClassNotFoundException e1)
						{
							CessarPluginActivator.getDefault().logError(e1);
						}

					}
				}
			}
		}
	}

	private void executeExtension(DynamicTreeElement input, final Object o, String img)
	{
		ISafeRunnable runnable = new ISafeRunnable()
		{
			@Override
			public void handleException(Throwable e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}

			@Override
			public void run() throws Exception
			{// Nothing

			}
		};
		SafeRunner.run(runnable);
		Object wrappedInputObject = input.getWrappedObject();
		@SuppressWarnings("unchecked")
		List<Object> load = ((IDynamicLoader<Object, Object>) o).load(wrappedInputObject);
		if (allWrapped == null)
		{
			allWrapped = new ArrayList<>();

		}
		allWrapped.addAll(DynamicTreeElement.getAllWrapped(load, wrappedInputObject, img));

	}

	/**
	 * @param withChildren
	 * @return Loaded Results
	 */
	public List<DynamicTreeElement> getresult(boolean withChildren)
	{
		if (allWrapped == null)
		{
			return new ArrayList<>();
		}
		for (DynamicTreeElement dynamicTreeElement: allWrapped)
		{

			if (withChildren)
			{
				if (getResultWithoutChildren(dynamicTreeElement).isEmpty())
				{
					dynamicTreeElement.setHasChildren(false);
				}
				else
				{
					dynamicTreeElement.setHasChildren(true);
				}
			}
		}
		return allWrapped;
	}

	private List<DynamicTreeElement> getResultWithoutChildren(DynamicTreeElement input)
	{
		EvaluateContributionsHandler evaluateContributionsHandler = new EvaluateContributionsHandler(ctx);
		evaluateContributionsHandler.execute(input);
		List<DynamicTreeElement> getresult = evaluateContributionsHandler.getresult(false);
		return getresult;

	}
	/**
	 * @param registry
	 */
}