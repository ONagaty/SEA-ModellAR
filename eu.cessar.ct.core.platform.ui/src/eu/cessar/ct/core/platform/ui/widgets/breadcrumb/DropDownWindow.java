package eu.cessar.ct.core.platform.ui.widgets.breadcrumb;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public abstract class DropDownWindow extends Window
{

	protected StructuredViewer structuredViewer;
	protected CLabel lblMessage;
	protected BreadcrumbItemExpand itemExpand;

	/**
	 * @return the treeViewer
	 */
	protected StructuredViewer getDropDownViewer()
	{
		return structuredViewer;
	}

	public DropDownWindow(BreadcrumbItemExpand itemExpand, Shell shell)
	{
		super(shell);
		setShellStyle(SWT.NO_TRIM | SWT.TOOL | SWT.ON_TOP | SWT.DOUBLE_BUFFERED);
		this.itemExpand = itemExpand;
	}

	@Override
	public boolean close()
	{
		this.itemExpand.menuIsShown = false;
		Label imageElement = this.itemExpand.imageElement;
		if (imageElement != null && !imageElement.isDisposed())
		{
			imageElement.setImage(this.itemExpand.getArrowImage(false));
		}
		structuredViewer = null;
		return super.close();
	}

	@Override
	protected void configureShell(final Shell newShell)
	{
		super.configureShell(newShell);
		newShell.addListener(SWT.Deactivate, new Listener()
		{
			public void handleEvent(final Event event)
			{
				close();
			}
		});
	}

	protected void resizeShell(Shell shell)
	{
		this.itemExpand.resizeShell(shell);
	}

	protected boolean isLTR()
	{
		return this.itemExpand.isLTR();
	}

	protected boolean isShellDisposed()
	{
		return getShell() == null || getShell().isDisposed();
	}

}