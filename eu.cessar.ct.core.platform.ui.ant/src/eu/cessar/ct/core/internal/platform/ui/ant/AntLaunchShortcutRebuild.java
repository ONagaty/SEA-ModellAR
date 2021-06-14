package eu.cessar.ct.core.internal.platform.ui.ant;


/**
 * @author uidl7321
 * 
 */
public class AntLaunchShortcutRebuild extends AntLaunchShortcut
{
	/**
	 * The sole constructor, it will just set the rebuild flag to true
	 */
	public AntLaunchShortcutRebuild()
	{
		super();
		setRebuild(true);
	}
}
