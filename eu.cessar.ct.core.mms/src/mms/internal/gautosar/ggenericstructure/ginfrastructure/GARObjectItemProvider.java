package mms.internal.gautosar.ggenericstructure.ginfrastructure;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.FeatureMapEntryWrapperItemProvider;

import eu.cessar.ct.core.mms.providers.FilteredItemProvider;

/**
 * Item provider that filters the child descriptors and children for a
 * {@link gautosar.ggenericstructure.ginfrastructure.GARObject} object.
 */
public class GARObjectItemProvider extends FilteredItemProvider
{
	/**
	 * @author uidu3379
	 * 
	 */
	private static class ParentFilter implements IFilter
	{
		/**
		 * @return false for CommandParameters that that have a
		 *         feature.attribute.etype of type FeatureMap$Entry
		 */
		public boolean acceptChildDescriptor(Object childDescriptor)
		{
			if (childDescriptor instanceof CommandParameter)
			{
				CommandParameter cmd = (CommandParameter) childDescriptor;
				if (cmd.getFeature() instanceof EAttribute)
				{
					EAttribute feature = (EAttribute) cmd.getFeature();
					if (feature.getEType().getInstanceClassName().equals(FEATURE_MAP_TAG))
					{
						return false;
					}
				}
			}
			return true;
		}

		/**
		 * @return false for FeatureMapEntryWrapperItemProviders that that have
		 *         a feature.attribute.etype of type FeatureMap$Entry
		 */
		public boolean acceptChild(Object child)
		{
			if (child instanceof FeatureMapEntryWrapperItemProvider)
			{
				FeatureMapEntryWrapperItemProvider provider = (FeatureMapEntryWrapperItemProvider) child;
				if (provider.getFeature() instanceof EAttribute)
				{
					EAttribute feature = (EAttribute) provider.getFeature();
					if (feature.getEType().getInstanceClassName().equals(FEATURE_MAP_TAG))
					{
						return false;
					}
				}
			}
			return true;
		}
	}

	private static ParentFilter filter = new ParentFilter();

	private static final String FEATURE_MAP_TAG = "org.eclipse.emf.ecore.util.FeatureMap$Entry"; //$NON-NLS-1$

	/**
	 * Creates a {@link GARObjectItemProvider} instance using the provided
	 * {@link AdapterImpl} parameter.
	 * 
	 * @param parent
	 *        the parent adapter
	 */
	public GARObjectItemProvider(AdapterImpl parent)
	{
		super(parent, filter);
	}
}
