package eu.cessar.ct.jet.ant.internal;


/**
 * /**
 * 
 * Ant task for generating a jet, for projects that are not in compatibility
 * mode.
 * 
 */

public class GenerateJetTask extends AbstractGenerateJetTask
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ant.tasks.AbstractTask#isTaskInCompatibilityMode()
	 */
	@Override
	protected ETaskType getTaskCompatType()
	{
		return ETaskType.NON_COMPAT_TASK;
	}

}
