package eu.cessar.ct.pluget.debug.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.debug.core.sourcelookup.ISourceContainerType;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;
import org.eclipse.debug.core.sourcelookup.containers.ProjectSourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.WorkspaceSourceContainer;
import org.eclipse.jdt.launching.sourcelookup.containers.JavaSourceLookupParticipant;

import eu.cessar.ct.pluget.debug.PlugetDebugConstants;
import eu.cessar.ct.runtime.execution.AbstractCessarSourceLookupDirector;

public class PlugetSourceLocator extends AbstractCessarSourceLookupDirector
{
	private static Set<String> fFilteredTypes;

	static
	{
		fFilteredTypes = new HashSet<String>();
		fFilteredTypes.add(ProjectSourceContainer.TYPE_ID);
		fFilteredTypes.add(WorkspaceSourceContainer.TYPE_ID);
		// can't reference UI constant
		fFilteredTypes.add("org.eclipse.debug.ui.containerType.workingSet"); //$NON-NLS-1$
	}


	@Override
	protected String getProjectAttribute() {
		return PlugetDebugConstants.PLUGET_DEBUG_PROJECT;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.core.sourcelookup.ISourceLookupDirector#initializeParticipants()
	 */
	public void initializeParticipants()
	{
		ISourceLookupParticipant participants[];
		participants = new ISourceLookupParticipant[] {new JavaSourceLookupParticipant()};
		addParticipants(participants);
	}

	@Override
	public boolean supportsSourceContainerType(ISourceContainerType type) {
		return !fFilteredTypes.contains(type.getId());
	}
	
}
