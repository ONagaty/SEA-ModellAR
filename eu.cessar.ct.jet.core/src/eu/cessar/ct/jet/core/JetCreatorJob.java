package eu.cessar.ct.jet.core;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;

@SuppressWarnings("nls")
public class JetCreatorJob extends Job
{
	private IFile iFile;
	private final boolean errorFlag;

	public JetCreatorJob(IFile iFile)
	{
		super("jet job");

		if (iFile == null)
		{
			errorFlag = true;
		}
		else
		{
			errorFlag = false;
			this.iFile = iFile;

			setRule(createRule(iFile));
		}
	}

	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		try
		{
			if (monitor.isCanceled())
			{
				return Status.CANCEL_STATUS;
			}
			if (errorFlag)
			{
				return Status.CANCEL_STATUS;
			}

			return createNewFileTemplate(iFile, monitor);
		}
		catch (Exception e)
		{
			return Status.CANCEL_STATUS;
		}
	}

	/**
	 * 
	 * @param iFile
	 *        - IFile for the new JET file that will be created
	 * @param monitor
	 *        - progress
	 * @return - the result of the operation
	 */
	private IStatus createNewFileTemplate(IFile iFile, IProgressMonitor monitor)
	{
		String jetDirective = JETCoreUtils.getJetDirective(iFile);
		String jetBody = "<%" + JETCoreConstants.LNS + JETCoreConstants.LNS + JETCoreConstants.LNS
			+ "%>";

		String jetTemplate = jetDirective + jetBody;

		ByteArrayInputStream iStream = new ByteArrayInputStream(jetTemplate.getBytes());

		try
		{
			if (!iFile.exists())
			{
				iFile.create(iStream, true, monitor);
			}
			else
			{
				iFile.setContents(iStream, true, true, monitor);
			}
		}
		catch (CoreException e)
		{
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}

	protected ISchedulingRule createRule(IResource resource)
	{
		IResource parent = resource.getParent();
		while (parent != null)
		{
			if (parent.exists())
			{
				return resource.getWorkspace().getRuleFactory().createRule(resource);
			}
			resource = parent;
			parent = parent.getParent();
		}
		return resource.getWorkspace().getRoot();
	}
}
