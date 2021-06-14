/**
 *
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.menu;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

/**
 * @author uidt2045
 *
 */
public class PlugetHistoryArgManager
{

	/**
	 * Launch an modal dialog that asks the user for parameters that will be further passed to the plugets, and also
	 * kept in history
	 *
	 * @return
	 */
	public static String[] getUserInput(Shell shell, IProject project)
	{
		HistoryDialog dialog = new HistoryDialog(shell, project);

		dialog.create();

		String[] history = HistoryParametersUtils.getParametersHistory(project);

		dialog.setHistory(history);

		if (dialog.open() == IDialogConstants.CANCEL_ID)
		{
			return null;
		}

		String introducedValues = dialog.getIntroducedValues();
		HistoryParametersUtils.addHistoryParameters(project, introducedValues);

		try
		{
			introducedValues = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(
				introducedValues);
			return DebugPlugin.parseArguments(introducedValues);
		}
		catch (CoreException e)
		{
			ErrorDialog.openError(shell, "Cessar-CT", "Error occured while evaluating the input", //$NON-NLS-1$//$NON-NLS-2$
				e.getStatus());
			return null;
		}
	}
}
