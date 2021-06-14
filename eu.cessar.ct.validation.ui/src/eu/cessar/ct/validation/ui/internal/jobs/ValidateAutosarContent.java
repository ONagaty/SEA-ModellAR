package eu.cessar.ct.validation.ui.internal.jobs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.validation.ValidationUtilsCommon;
import eu.cessar.ct.validation.ui.AbstractValidateAutosarContent;

/**
 * This class performs validation on the selected objects
 *
 * @author uidg4020
 *
 *         %created_by: uidg4020 %
 *
 *         %date_created: Tue Oct 21 11:49:47 2014 %
 *
 *         %version: 2 %
 */
public class ValidateAutosarContent extends AbstractValidateAutosarContent
{
	List<Object> selectedObjects = new ArrayList<>();
	Shell parentShell;
	/**
	 * When this flag is true, all objects from selection will be validated. Else, only selectedObjects will be
	 * validated.
	 */
	boolean validateAllBehavior;

	/**
	 * @param selectedObjects
	 * @param parentShell
	 * @param currentSelection
	 */
	public ValidateAutosarContent(List<Object> selectedObjects, Shell parentShell, IStructuredSelection currentSelection)
	{
		this.currentSelection = currentSelection;
		this.selectedObjects = selectedObjects;
		this.parentShell = parentShell;
		validateAllBehavior = false;
	}

	/**
	 * @param currentSelection
	 * @param parentShell
	 * @param selectedObjects
	 */
	public ValidateAutosarContent(IStructuredSelection currentSelection, Shell parentShell)
	{
		this.currentSelection = currentSelection;
		this.parentShell = parentShell;
		validateAllBehavior = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.internal.actions.ValidateAutosarAction#afterValidation()
	 */
	@Override
	protected void afterValidation()
	{
		// nothing needed

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.AbstractValidateAutosarContent#getParentShell()
	 */
	@Override
	protected Shell getParentShell()
	{
		return parentShell;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.AbstractValidateAutosarContent#getSelectedObjects(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	protected List<Object> getSelectedObjects(IProgressMonitor progressMonitor)
	{
		if (validateAllBehavior)
		{
			List<Object> allObjectsFromSelection = getAllObjectsFromSelection(progressMonitor);
			return allObjectsFromSelection;
		}
		return selectedObjects;
	}

	/**
	 * Return all objects from current selection.
	 *
	 * @param progressMonitor
	 * @param currentSelection
	 * @return the selected objects
	 */
	protected List<Object> getAllObjectsFromSelection(final IProgressMonitor progressMonitor)
	{
		Object obj = null;
		if (currentSelection != null)
		{
			obj = currentSelection.getFirstElement();
		}
		IProject project = MetaModelUtils.getProject(obj);
		if (project != null)
		{
			PlatformUtils.waitForModelLoading(project, progressMonitor);
		}
		// be sure that the constraints are loaded
		ModelValidationService.getInstance().loadXmlConstraintDeclarations();
		List<Object> objectsFromSelection = currentSelection.toList();
		return objectsFromSelection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.AbstractValidateAutosarContent#getModelObjects(java.util.List)
	 */
	@Override
	protected List<EObject> getModelObjects(List<Object> lst)
	{
		Set<EObject> differentModelObjects = new HashSet<>();
		List<EObject> modelObjects = ValidationUtilsCommon.getModelObjects(lst);
		differentModelObjects.addAll(modelObjects);
		List<EObject> result = new ArrayList<>(differentModelObjects);
		return result;
	}
}