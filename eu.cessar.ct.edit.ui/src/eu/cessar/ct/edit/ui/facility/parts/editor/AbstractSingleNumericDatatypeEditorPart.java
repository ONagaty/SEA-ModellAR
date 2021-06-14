/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu0944 Jun 24, 2011 2:23:21 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import org.eclipse.core.resources.IProject;
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
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.IdentificationUtils;
import eu.cessar.ct.core.platform.util.ERadix;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * @author uidu0944
 *
 */
public abstract class AbstractSingleNumericDatatypeEditorPart<T> extends AbstractSingleDatatypeEditorPart<T>
{
	private ERadix radix = ERadix.DECIMAL;
	private Label radixLabel;

	/**
	 * @param editor
	 */
	public AbstractSingleNumericDatatypeEditorPart(IModelFragmentFeatureEditor editor)
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
		radixLabel.setData(IdentificationUtils.KEY_NAME, getEditor().getInstanceId() + "#" //$NON-NLS-1$
			+ IdentificationUtils.LABEL_ID);
		GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		radixLabel.setLayoutData(gridData);

		Control control = super.createContents(composite);
		control.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		return composite;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IIntegerEditorPart#getRadix()
	 */
	public ERadix getRadix()
	{
		EObject input = getEditor().getInput();

		if (input == null)
		{
			radix = ERadix.DECIMAL;
		}
		else
		{
			radix = retrieveRadixFromModel(input, (EAttribute) getInputFeature());
			if (radix == null)
			{
				IProject project = MetaModelUtils.getProject(input);
				if (project != null)
				{
					radix = PlatformUtils.getProjectRadixSettings(project);
				}
				else
				{
					radix = ERadix.DECIMAL;
				}
			}
		}
		return radix;
	}

	/**
	 * Retrieve the radix as specified in the model
	 *
	 * @param input
	 * @param attr
	 * @return
	 */
	private ERadix retrieveRadixFromModel(EObject input, EAttribute attr)
	{
		if (input != null)
		{
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(input);
			if (mmService != null && mmService.canStoreRadixInformation(input, attr))
			{
				return mmService.getRadix(input, attr, -1);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IIntegerEditorPart#setRadix(eu.cessar.ct.core.platform.util.ERadix)
	 */
	public void setRadix(ERadix radix)
	{
		this.radix = radix;
	}

	protected void updateRadixLabel()
	{
		if (radixLabel != null && !radixLabel.isDisposed())
		{
			radixLabel.setText(getRadix().getLiteral().substring(0, 1));
		}
	}

	@Override
	public void dispose()
	{
		super.dispose();
		radixLabel.dispose();
	}

}
