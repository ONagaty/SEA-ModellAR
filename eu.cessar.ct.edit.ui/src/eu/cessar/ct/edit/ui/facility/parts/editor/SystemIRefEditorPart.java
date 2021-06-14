/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jun 10, 2010 10:15:51 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.instanceref.InstanceReferenceUtils;
import eu.cessar.ct.edit.ui.dialogs.AbstractChooseInstanceReferenceHandler;
import eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentReferenceEditor;
import eu.cessar.ct.edit.ui.facility.InstanceReferenceEditor;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.instanceref.IInstReferenceLabelProvider;
import eu.cessar.ct.edit.ui.instanceref.SystemIRefFeatureWrapper;
import eu.cessar.ct.edit.ui.instanceref.Wrapper;
import eu.cessar.ct.edit.ui.instanceref.widgets.IInstanceRefWidget;
import eu.cessar.ct.edit.ui.instanceref.widgets.SingleInstanceRefWidget;
import eu.cessar.ct.edit.ui.internal.facility.editors.SystemIRefEditor;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl6870
 * 
 */
public class SystemIRefEditorPart extends AbstractFeatureEditorPart implements IEditorPart
{
	private IInstanceRefWidget<Wrapper<SystemIRefFeatureWrapper>> widget;

	private IChooseIRefHandler handler;

	/**
	 * @param editor
	 */
	public SystemIRefEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent)
	{
		IInstReferenceLabelProvider<SystemIRefFeatureWrapper> lp = (IInstReferenceLabelProvider) ((IModelFragmentReferenceEditor) getEditor()).getLabelProvider();

		initHandler();
		SingleInstanceRefWidget<SystemIRefFeatureWrapper> w = new SingleInstanceRefWidget<SystemIRefFeatureWrapper>(lp,
			handler, getEditor());
		widget = w;

		// widget.setReadOnly(getEditor().getEditableStatus());
		Control control = widget.createEditor(parent);

		return control;
	}

	/**
	 * 
	 */
	private void initHandler()
	{
		if (handler == null)
		{
			handler = new ChooseInstRefHandler((InstanceReferenceEditor) getEditor());
			handler.setIsSystemInstanceRef(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractEditorPart#setFocus()
	 */
	@Override
	public void setFocus()
	{
		if (widget != null)
		{
			widget.setFocus();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#dispose()
	 */
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#refresh()
	 */
	public void refresh()
	{
		if (handler != null && handler.getProject() == null)
		{
			IFile file = EcorePlatformUtil.getFile(getEditor().getInput());
			handler.setProject(file.getProject());

		}
		if (widget != null)
		{
			widget.setReadOnly(!getEditor().getEditableStatus().isOK());
			widget.setInputData(getInputData());
		}
	}

	/**
	 * @return
	 */
	private Wrapper<SystemIRefFeatureWrapper> getInputData()
	{
		List<SystemIRefFeatureWrapper> featureWrapperList = new ArrayList<SystemIRefFeatureWrapper>();

		EObject input = getInputObject();
		EClass eClass = input.eClass();
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(eClass);

		EReference targetFeature = mmService.getTargetFeature(eClass);
		List<EReference> contextFeatures = mmService.getContextFeatures(eClass);

		SystemIRefFeatureWrapper targetWrapper = new SystemIRefFeatureWrapper(input, targetFeature);
		targetWrapper.setValue(input.eGet(targetFeature));
		for (EReference ref: contextFeatures)
		{
			// create the context wrapper only if there is an object set on that
			// feature (don't create a wrapper for a null )
			Object object = input.eGet(ref);

			if (object != null)
			{
				if ((object instanceof List<?>) && ((List<?>) object).size() == 0)
				{
					continue;
				}

				SystemIRefFeatureWrapper featureWrapper = new SystemIRefFeatureWrapper(input, ref);
				featureWrapper.setValue(object);
				featureWrapperList.add(featureWrapper);
			}
		}

		Wrapper<SystemIRefFeatureWrapper> wrapper = new Wrapper<SystemIRefFeatureWrapper>(targetWrapper,
			featureWrapperList);

		return wrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	public Image getImage()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	public String getText()
	{
		StringBuffer result = new StringBuffer();
		initHandler();
		GIdentifiable target = handler.getTarget();
		if (target != null)
		{
			List<GIdentifiable> contextList = handler.getContext();
			result.append("Target:" + MetaModelUtils.getAbsoluteQualifiedName(target) + ";"); //$NON-NLS-1$//$NON-NLS-2$
			int i = 0;
			for (GIdentifiable context: contextList)
			{
				if (i == 0)
				{
					result.append("Context:"); //$NON-NLS-1$
				}
				result.append(MetaModelUtils.getAbsoluteQualifiedName(context));
				if (i < contextList.size() - 1)
				{
					result.append(","); //$NON-NLS-1$
				}
				i++;
			}
		}
		return result.toString();
	}

	class ChooseInstRefHandler extends AbstractChooseInstanceReferenceHandler
	{
		private String filteringString;

		/**
		 * @param editor
		 */
		public ChooseInstRefHandler(InstanceReferenceEditor editor)
		{
			super(editor);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.edit.ui.facility.IChooseIRefHandler#getContext()
		 */
		@SuppressWarnings("unchecked")
		public List<GIdentifiable> getContext()
		{
			List<GIdentifiable> contextList = new ArrayList<GIdentifiable>();
			EObject input = getInputObject();
			for (EReference ref: ((SystemIRefEditor) getEditor()).getContextFeatures())
			{
				if (input.eIsSet(ref))
				{
					Object value = input.eGet(ref);
					if (value instanceof List<?>)
					{
						contextList.addAll((List<GIdentifiable>) value);
					}
					else
					{
						contextList.add((GIdentifiable) value);
					}
				}
			}
			return contextList;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.edit.ui.facility.IChooseIRefHandler#getDialogTitle()
		 */
		public String getDialogTitle()
		{
			return getInputFeature().getName();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.edit.ui.facility.IChooseIRefHandler#getTableLabelProvider()
		 */
		public ITableLabelProvider getTableLabelProvider()
		{

			return new TableLabelProvider();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.edit.ui.facility.IChooseIRefHandler#getTarget()
		 */
		public GIdentifiable getTarget()
		{
			return (GIdentifiable) getInputObject().eGet(getInputFeature());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * eu.cessar.ct.edit.ui.facility.IChooseIRefHandler#setReference(gautosar.ggenericstructure.ginfrastructure.
		 * GIdentifiable, java.util.List)
		 */
		public void setReference(GIdentifiable newTarget, List<GIdentifiable> newContext)
		{
			if (newTarget != null)
			{
				doAcceptData(newTarget, newContext);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#setFilterString(java.lang.String)
		 */
		@Override
		public void setFilterString(String filterString)
		{
			filteringString = filterString;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.edit.ui.dialogs.IChooseIRefHandler#getFilterString()
		 */
		@Override
		public String getFilterString()
		{
			return filteringString;
		}
	}

	/**
	 * 
	 * @param oldData
	 * @param newData
	 * @return
	 */
	private boolean doAcceptData(final GIdentifiable target, final List<GIdentifiable> newContext)
	{
		// perform the change into the model and accept data
		return performChangeWithChecks(new Runnable()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			public void run()
			{
				updateData(target, newContext);
			}

		}, "Updating data..."); //$NON-NLS-1$
	}

	private void updateData(GIdentifiable target, List<GIdentifiable> newContext)
	{
		EObject input = getInputObject();
		input.eSet(getInputFeature(), target);

		List<EReference> features = ((SystemIRefEditor) getEditor()).getContextFeatures();

		for (EReference ref: features)
		{
			input.eUnset(ref);
		}

		for (GIdentifiable value: newContext)
		{
			for (EReference ref: features)
			{
				EClassifier eType = ref.getEType();
				if (eType.isInstance(value))
				{
					if (ref.isMany())
					{
						@SuppressWarnings("unchecked")
						List<GIdentifiable> list = (List<GIdentifiable>) input.eGet(ref);

						List<GIdentifiable> res = new ArrayList<GIdentifiable>();
						res.addAll(list);
						res.add(value);

						input.eSet(ref, res);
					}
					else
					{
						input.eSet(ref, value);
					}
					break;
				}
			}
		}

		refresh();

	}

	/**
	 * Label provider for the table displayed inside the {@link InstanceReferenceSelectionDialog}, passed through
	 * {@link ChooseInstanceRefHandler} handler
	 */
	class TableLabelProvider implements ITableLabelProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex)
		{
			AdapterFactory adapterFactory = ((AdapterFactoryEditingDomain) getEditingDomain()).getAdapterFactory();
			if (columnIndex == 0)
			{
				IItemLabelProvider provider = (IItemLabelProvider) adapterFactory.adapt((EObject) element,
					IItemLabelProvider.class);
				return ExtendedImageRegistry.getInstance().getImage(provider.getImage(element));
			}
			else if (columnIndex == 1)
			{
				EObject typeOfPrototype = InstanceReferenceUtils.getTypeOfPrototype((EObject) element);
				if (typeOfPrototype != null)
				{
					IItemLabelProvider provider = (IItemLabelProvider) adapterFactory.adapt((EObject) element,
						IItemLabelProvider.class);
					return ExtendedImageRegistry.getInstance().getImage(provider.getImage(element));
				}
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
		 */
		public String getColumnText(Object element, int columnIndex)
		{
			String colText = element.toString();

			AdapterFactory adapterFactory = ((AdapterFactoryEditingDomain) getEditingDomain()).getAdapterFactory();

			if (columnIndex == 0)
			{

				IItemLabelProvider provider = (IItemLabelProvider) adapterFactory.adapt((EObject) element,
					IItemLabelProvider.class);
				colText = provider.getText(element);

			}
			else if (columnIndex == 1)
			{
				EObject typeOfPrototype = InstanceReferenceUtils.getTypeOfPrototype((EObject) element);
				if (typeOfPrototype != null)
				{
					String qName = ModelUtils.getAbsoluteQualifiedName(typeOfPrototype);
					colText = qName;
				}
				else
				{
					colText = ""; //$NON-NLS-1$
				}
			}
			return colText;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener listener)
		{
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
		 */
		public boolean isLabelProperty(Object element, String property)
		{
			// TODO Auto-generated method stub
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener listener)
		{
			// TODO Auto-generated method stub
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		public void dispose()
		{
			// TODO Auto-generated method stub
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		if (widget != null)
		{
			widget.setEnabled(enabled);
		}

	}

}
