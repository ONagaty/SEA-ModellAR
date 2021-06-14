package eu.cessar.ct.core.platform.ui.viewers;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

/**
 *
 * @author uidl6870
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class ExpandingTreeViewer extends TreeViewer
{
	private boolean autoExpand = true;

	/**
	 * @param shell
	 */
	public ExpandingTreeViewer(Shell shell)
	{
		super(shell);
		initializeTreeViewer();
	}

	/**
	 * @param tree
	 */
	public ExpandingTreeViewer(Tree tree)
	{
		super(tree);
		initializeTreeViewer();
	}

	/**
	 * @param tree
	 * @param autoExpand
	 *        whether descendants with one child should be automatically expanded
	 */
	public ExpandingTreeViewer(Tree tree, boolean autoExpand)
	{
		super(tree);
		initializeTreeViewer();
		this.autoExpand = autoExpand;
	}

	/**
	 * @param parent
	 * @param i
	 */
	public ExpandingTreeViewer(Composite parent, int i)
	{
		super(parent, i);
		initializeTreeViewer();
	}

	private void initializeTreeViewer()
	{
		setUseHashlookup(true);
	}

	@Override
	protected void handleTreeExpand(TreeEvent event)
	{
		super.handleTreeExpand(event);

		if (autoExpand)
		{
			recursivelyExpandTree(event.item.getData());
		}
	}

	private void recursivelyExpandTree(Object element)
	{
		ITreeContentProvider cp = (ITreeContentProvider) getContentProvider();
		Object[] children = cp.getChildren(element);
		if (children.length == 1)
		{
			expandToLevel(children[0], 1);
			recursivelyExpandTree(children[0]);
		}
	}

}
