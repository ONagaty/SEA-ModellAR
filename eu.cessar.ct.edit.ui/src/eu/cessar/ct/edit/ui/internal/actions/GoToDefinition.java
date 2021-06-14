package eu.cessar.ct.edit.ui.internal.actions;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.edit.ui.utils.EditUtils;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Class implementing GOToDefinition action
 * 
 * @author uidv3687
 * 
 *         %created_by: uidl7321 %
 * 
 *         %date_created: Thu Sep  5 17:53:25 2013 %
 * 
 *         %version: 4 %
 */
public class GoToDefinition implements IObjectActionDelegate
{
	GIdentifiable selected;

	/**
	 * 
	 */
	public GoToDefinition()
	{
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		EObject definition = EcucMetaModelUtils.getDefinition(selected);
		if (definition != null)
		{
			EditUtils.openEditor(definition, true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		selected = PlatformUIUtils.getObjectFromSelection(GIdentifiable.class, selection);
		EObject definition = EcucMetaModelUtils.getDefinition(selected);

		if (definition != null)
		{
			action.setEnabled(!definition.eIsProxy());
		}
		else
		{
			action.setEnabled(false);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 * org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		// TODO Auto-generated method stub
	}

}
