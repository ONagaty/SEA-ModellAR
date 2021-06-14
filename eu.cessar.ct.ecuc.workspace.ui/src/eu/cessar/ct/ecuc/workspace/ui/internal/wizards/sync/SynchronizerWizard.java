package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.sync;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import eu.cessar.ct.core.mms.EResourceUtils;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem;
import eu.cessar.ct.ecuc.workspace.sync.SyncAnalizer;
import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.ui.internal.SyncMessages;
import eu.cessar.ct.sdk.utils.ModelUtils;

/**
 * TODO: Please comment this class
 *
 * @author uidu8153
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class SynchronizerWizard extends Wizard
{

	private ISelection selection;
	private ContentSelectionPage contentPage;
	private DecisionPage decisionPage;
	private ReportPage reportPage;
	private WarningPage warningPage;

	/**
	 * @param selection
	 */
	public SynchronizerWizard(ISelection selection)
	{
		this.selection = selection;
		setWindowTitle(SyncMessages.WizardTitle);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish()
	{
		boolean result = false;
		result = decisionPage.isPageComplete() || reportPage.isPageComplete();
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish()
	{
		SyncAnalizer analizer = contentPage.getAnalizer();
		List<AbstractProblem> list = reportPage.getProblems();
		try
		{
			analizer.synchronize(decisionPage.getInitialProblems(), list);
			EResourceUtils.saveResources(getResources(list), new NullProgressMonitor(), true);
		}
		catch (OperationCanceledException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (ExecutionException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return true;
	}

	private List<Resource> getResources(List<AbstractProblem> list)
	{
		List<Resource> resources = new ArrayList<Resource>();
		if (list != null)
		{
			for (AbstractProblem problem: list)
			{
				EObject affectedElement = problem.getAffectedElement();
				if (affectedElement != null)
				{
					IFile definingFile = ModelUtils.getDefiningFile(affectedElement);
					if (definingFile != null)
					{
						if (!resources.contains(definingFile))
						{
							resources.add(ModelUtils.getDefinedResource(definingFile));
						}
					}
				}
			}
		}
		return resources;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page)
	{
		return super.getNextPage(page);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.Wizard#addPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public void addPages()
	{
		contentPage = new ContentSelectionPage(SyncMessages.ContentPage, selection);
		super.addPage(contentPage);

		warningPage = new WarningPage(SyncMessages.DecisionPage);
		super.addPage(warningPage);

		decisionPage = new DecisionPage(SyncMessages.DecisionPage);
		super.addPage(decisionPage);

		reportPage = new ReportPage(SyncMessages.ReportPage);
		super.addPage(reportPage);
	}

	@Override
	public boolean needsPreviousAndNextButtons()
	{
		return true;
	}

}
