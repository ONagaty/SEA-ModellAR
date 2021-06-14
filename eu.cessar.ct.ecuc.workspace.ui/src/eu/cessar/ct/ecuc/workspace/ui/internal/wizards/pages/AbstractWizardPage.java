package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard;

abstract public class AbstractWizardPage extends WizardPage
{
	public static String PAGE_TITLE = "Page_Title"; // $NON-NLS$ //$NON-NLS-1$
	public static String PAGE_DESCRIPTION = "Page_Description"; // $NON-NLS$ //$NON-NLS-1$
	public static String CONTROL_ID = "ControlID"; // $NON-NLS$ //$NON-NLS-1$

	protected IStructuredSelection selection;
	protected int layoutsOffsets = 5;

	protected AbstractWizardPage(String pageName)
	{
		super(pageName);
	}

	protected AbstractWizardPage(String pageName, IStructuredSelection selection)
	{
		this(pageName);
		this.selection = selection;
	}

	abstract protected void doCreateControl(Composite parent);

	public final void createControl(Composite parent)
	{
		doCreateControl(parent);
		setPageTexts();
		updateControlVisibility();
	}

	/**
	 * This method provide similar GridLayouts to all wizard pages that call it.
	 */
	public GridLayout getGridLayout(int numCols, boolean equalCols)
	{
		GridLayout gridLayout = new GridLayout(numCols, equalCols);
		gridLayout.horizontalSpacing = layoutsOffsets;
		gridLayout.verticalSpacing = layoutsOffsets;
		gridLayout.marginWidth = layoutsOffsets;
		gridLayout.marginTop = layoutsOffsets;
		gridLayout.marginRight = layoutsOffsets;
		gridLayout.marginLeft = layoutsOffsets;
		gridLayout.marginHeight = layoutsOffsets;
		gridLayout.marginBottom = layoutsOffsets;
		return gridLayout;
	}

	protected IStructuredSelection getSelection()
	{
		return selection;
	}

	/**
	 * Subclasses may override to show/hide various controls
	 */
	protected void updateControlVisibility()
	{
		// Do nothing by default.
	}

	protected void setPageTexts()
	{
		IPageTextProvider textProvider = ((AbstractWizard) getWizard()).getTextProvider(this);
		if (null == textProvider)
		{
			return;
		}

		HashMap<String, String> pageTexts = textProvider.getPageTexts();
		Assert.isNotNull(pageTexts);

		if (pageTexts.containsKey(PAGE_TITLE))
		{
			setTitle(pageTexts.get(PAGE_TITLE));
		}
		if (pageTexts.containsKey(PAGE_DESCRIPTION))
		{
			setDescription(pageTexts.get(PAGE_DESCRIPTION));
		}
		setPageTexts(getControl(), pageTexts);
	}

	protected final void setPageTexts(Widget ctrl, HashMap<String, String> pageTexts)
	{
		Object ctrlData = ctrl.getData(CONTROL_ID);
		if (null != ctrlData)
		{
			String ctrlId = ctrlData.toString();
			String ctrlText = (pageTexts.containsKey(ctrlId) ? pageTexts.get(ctrlId) : null);
			if (null != ctrlText)
			{
				try
				{
					Method setTextMethod = (ctrl.getClass().getDeclaredMethod("setText", //$NON-NLS-1$
						String.class));
					setTextMethod.invoke(ctrl, ctrlText);
				}
				catch (Exception e)
				{
					// Ignore
				}
			}
		}

		// Recurse children
		if (ctrl instanceof Composite)
		{
			for (Control child: ((Composite) ctrl).getChildren())
			{
				setPageTexts(child, pageTexts);
			}

			// Special case for columns, not listed as children of their parent
			// control
			try
			{
				Method getColumnsMethod = (ctrl.getClass().getDeclaredMethod("getColumns")); //$NON-NLS-1$
				Widget[] children = (Widget[]) getColumnsMethod.invoke(ctrl);
				for (Widget child: children)
				{
					setPageTexts(child, pageTexts);
				}
			}
			catch (Exception e)
			{
				// Ignore
			}
		}
	}

	/**
	 * Set the default data for the page
	 */
	public void performDefault()
	{
		// by default... do nothing
	}

	protected void setNextPageDefault(IWizardPage page)
	{
		if (page instanceof AbstractWizardPage)
		{
			// IWizardPage nextPage = page.getNextPage();
			IWizardPage nextPage = page;
			while (nextPage != null && nextPage instanceof AbstractWizardPage)
			{
				((AbstractWizardPage) nextPage).performDefault();
				nextPage = nextPage.getNextPage();
			}
		}
	}

}
