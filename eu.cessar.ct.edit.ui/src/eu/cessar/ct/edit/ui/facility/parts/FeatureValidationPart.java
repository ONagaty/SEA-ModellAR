/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Jun 30, 2010 2:38:46 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sphinx.emf.validation.diagnostic.ExtendedDiagnostic;
import org.eclipse.sphinx.emf.validation.markers.util.FeatureAttUtil;

import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextEditingStrategy;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextSingleFeatureEditingManager;
import eu.cessar.ct.validation.FeatureBoundDiagnostic;
import eu.cessar.ct.validation.ValidationModeDiagnostic;
import eu.cessar.req.Requirement;

/**
 * Validation part to be used by feature editors
 */

@Requirement(
	reqID = "REQ_EDIT_PROP#4")
public class FeatureValidationPart extends AbstractEMFValidationPart
{
	private Diagnostic featureDiagnostic;

	/**
	 * @param editor
	 */
	public FeatureValidationPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractEditorPart#getEditor()
	 */
	@Override
	public IModelFragmentFeatureEditor getEditor()
	{
		return (IModelFragmentFeatureEditor) super.getEditor();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.edit.ui.facility.parts.EMFValidationPart#isValidDiagnostic(org.eclipse.sphinx.emf.validation.diagnostic
	 * .ExtendedDiagnostic)
	 */
	@Override
	protected boolean isValidDiagnostic(Diagnostic diag)
	{
		if (diag != null)
		{
			featureDiagnostic = diag;
		}
		if (diag instanceof ValidationModeDiagnostic)
		{
			return true;
		}

		boolean isValid = false;
		List<?> data = diag.getData();

		if (data != null)
		{
			EObject input = getInputObject();
			String inputFeature = getEditor().getInputFeature().getName();
			for (Object object: data)
			{
				if (input == object)
				{
					// check the feature
					if (diag instanceof ExtendedDiagnostic)
					{
						Set<String> features = FeatureAttUtil.getRulesFeaturesForEObj(
							((ExtendedDiagnostic) diag).getConstraintId(), input);
						isValid = features.contains(inputFeature);
					}
					else
					{
						if (diag instanceof FeatureBoundDiagnostic)
						{
							List<String> boundFeatures = ((FeatureBoundDiagnostic) diag).getFeatures();
							isValid = boundFeatures.contains(inputFeature);
						}
						else
						{
							isValid = false;
							break;
						}
					}
					if (isValid)
					{
						break;
					}
				}
			}
		}
		return isValid;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.EMFValidationPart#getValidationRequestData()
	 */
	@Override
	protected List<EMFValidationRequestData> getValidationRequestData()
	{
		return Collections.singletonList(new EMFValidationRequestData(getInputObject()));
	}

	@Override
	protected boolean hasMissingProxyReferences()
	{
		EObject input = getInputObject();
		EStructuralFeature inputFeature = getEditor().getInputFeature();

		if (inputFeature instanceof EReference)
		{
			Object values = input.eGet(inputFeature);
			if (values instanceof EList)
			{
				for (Object elem: (EList) values)
				{
					if (elem instanceof EObject && ((EObject) elem).eIsProxy())
					{
						return true;
					}
				}
			}
			else
			{
				if (values instanceof EObject && ((EObject) values).eIsProxy())
				{
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.AbstractEMFValidationPart#getValidationStatus()
	 */
	@Override
	public IStatus getValidationStatus()
	{
		IStatus currentStatus = super.getValidationStatus();

		if (!getEditor().isInputSplitable())
		{
			return currentStatus;
		}
		else
		{
			ISplitableContextSingleFeatureEditingManager editorManager = getEditor().getSplitableContextEditingManager();
			ISplitableContextEditingStrategy strategy = editorManager.getStrategy();
			IStatus spliStatus = getStatusInSplitableContext(strategy);

			return getUpdatedStatus(currentStatus, spliStatus);
		}
	}

}
