package eu.cessar.ct.core.internal.platform.ui.ant.refactoring;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

public class AntUIParticipant extends RenameParticipant
{
	private static String CESSAR_ANTUI_REFACT_PROPERTY_UPDATE = "Update Cessar Ant properties"; //$NON-NLS-1$
	private IProject project;

	@Override
	protected boolean initialize(Object element)
	{
		project = (IProject) element;
		return true;
	}

	@Override
	public String getName()
	{
		return CESSAR_ANTUI_REFACT_PROPERTY_UPDATE;
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context)
		throws OperationCanceledException
	{
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
		OperationCanceledException
	{
		return LaunchPropertyUpdateChange.createChangesForProjectRename(project,
			getArguments().getNewName());
	}

}
