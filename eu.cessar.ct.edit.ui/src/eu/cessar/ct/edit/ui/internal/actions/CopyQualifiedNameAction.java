package eu.cessar.ct.edit.ui.internal.actions;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class CopyQualifiedNameAction implements IObjectActionDelegate
{

	private GReferrable selected;
	private Shell shell;

	public CopyQualifiedNameAction()
	{
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 * org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		shell = targetPart.getSite().getWorkbenchWindow().getShell();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		if (selected != null && shell != null && !shell.isDisposed())
		{
			String qName = MetaModelUtils.getAbsoluteQualifiedName(selected);
			if (qName != null)
			{
				Clipboard clip = new Clipboard(shell.getDisplay());
				TextTransfer transfer = TextTransfer.getInstance();
				clip.setContents(new Object[] {qName}, new Transfer[] {transfer});
				clip.dispose();
			}
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
		selected = PlatformUIUtils.getObjectFromSelection(GReferrable.class, selection);
	}
}
