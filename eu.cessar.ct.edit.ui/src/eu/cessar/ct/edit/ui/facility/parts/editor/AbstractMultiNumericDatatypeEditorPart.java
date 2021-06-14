/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Jul 6, 2011 3:51:37 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.List;

import org.artop.aal.common.datatypes.INumericalDataType;
import org.artop.aal.common.datatypes.INumericalDataTypeEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.platform.util.ERadix;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * @author uidu0944
 * 
 */
public abstract class AbstractMultiNumericDatatypeEditorPart<T> extends
	AbstractMultiDatatypeEditorPart<T>
{

	/**
	 * @author uidu3379
	 * 
	 */
	private final class PerformChangeRunnable implements Runnable
	{
		private final List<T> newData;

		private PerformChangeRunnable(List<T> newData)
		{
			this.newData = newData;
		}

		public void run()
		{
			@SuppressWarnings("unchecked")
			EList<T> data = (EList<T>) getInputObject().eGet(getInputFeature());
			data.clear();
			if (newData != null && !newData.isEmpty())
			{
				data.addAll(newData);
			}
			if (data instanceof INumericalDataTypeEList<?, ?>)
			{
				INumericalDataTypeEList<?, INumericalDataType> iNum = ((INumericalDataTypeEList<?, INumericalDataType>) data);
				for (int i = 0; i < data.size(); i++)
				{
					INumericalDataType numericalData = iNum.getNumericalData(i);
					numericalData.setRadix(getRadix().getRadixNumber());
					iNum.setNumericalData(i, numericalData);
				}
			}
		}
	}

	protected Label radixLabel;

	/**
	 * @param editor
	 */
	public AbstractMultiNumericDatatypeEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Control createContents(Composite parent)
	{
		Composite composite = getFormToolkit().createComposite(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		radixLabel = getFormToolkit().createLabel(composite, ""); //$NON-NLS-1$
		GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		radixLabel.setLayoutData(gridData);

		Control control = super.createContents(composite);
		control.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		return composite;
	}

	@Override
	protected boolean doAcceptData(List<T> oldData, final List<T> newData)
	{
		// perform the change into the model and accept data
		return performChangeWithChecks(new PerformChangeRunnable(newData), "Updating data..."); //$NON-NLS-1$
	}

	protected abstract ERadix getRadix();

	protected abstract void updateRadixLabel();

	/**
	 * Retrieve the radix as specified in the model
	 * 
	 * @param input
	 * @param attr
	 * @return
	 */
	protected ERadix retrieveRadixFromModel(EObject input, EAttribute attr)
	{
		if (input != null)
		{
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(input);
			if (mmService.canStoreRadixInformation(input, attr))
			{
				return mmService.getRadix(input, attr, -1);
			}
		}
		return null;
	}

}
