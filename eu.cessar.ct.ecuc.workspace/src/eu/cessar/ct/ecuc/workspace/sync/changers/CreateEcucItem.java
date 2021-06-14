package eu.cessar.ct.ecuc.workspace.sync.changers;

public abstract class CreateEcucItem extends AbstractEcucItemChanger
{

	protected abstract void create();

	public void run()
	{
		// TODO Auto-generated method stub
		// it should execute only if canExecute passes
		if (canExecute())
		{
			create();
		}
	}

}
