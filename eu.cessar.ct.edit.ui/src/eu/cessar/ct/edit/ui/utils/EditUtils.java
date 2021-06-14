/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jun 17, 2010 12:27:45 PM </copyright>
 */
package eu.cessar.ct.edit.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.artop.aal.gautosar.services.splitting.Splitable;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.editors.forms.BasicTransactionalFormEditor;
import org.eclipse.sphinx.emf.ui.util.EcoreUIUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.platform.util.ExtendedPlatform;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.themes.ITheme;

import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.sdk.utils.ModelUtils;

/**
 * @author uidl6458
 *
 */
public final class EditUtils
{
	/**
	 * Id of tree editor.
	 */
	public static final String EDITOR_ID_CESSAR_TREE = "eu.cessar.ct.editor.ui.tree"; //$NON-NLS-1$

	/**
	 * Id of model editor.
	 */
	public static final String EDITOR_ID_CESSAR_MODEL = "eu.cessar.ct.editor.ui.model"; //$NON-NLS-1$

	/**
	 * Id of table editor.
	 */
	public static final String EDITOR_ID_CESSAR_TABLE = "eu.cessar.ct.editor.ui.TableView"; //$NON-NLS-1$

	/**
	 * CESSAR-CT specific prefix.
	 */
	public static final String PREFIX_EU_CESSAR_CT = "eu.cessar.ct"; //$NON-NLS-1$

	/**
	 * Id of Artop example editor.
	 */
	public static final String EDITOR_ID_ARTOP_EXAMPLE = "org.artop.aal.examples.editor.autosar"; //$NON-NLS-1$

	/**
	 * Settings used by editing facility
	 */

	/**
	 * Setting for the font of an optional feature that is set.
	 */
	public static final String ID_FONT_EDIT_OPTIONAL_SET = "eu.cessar.ct.editfacility.optsetfont"; //$NON-NLS-1$

	/**
	 * Setting for the font of an optional feature that is unset.
	 */
	public static final String ID_FONT_EDIT_OPTIONAL_UNSET = "eu.cessar.ct.editfacility.optunsetfont"; //$NON-NLS-1$

	/**
	 * Setting for the font of a mandatory feature that is set.
	 */
	public static final String ID_FONT_EDIT_MANDATORY_SET = "eu.cessar.ct.editfacility.mndsetfont"; //$NON-NLS-1$

	/**
	 * Setting for the font of a mandatory feature that is unset.
	 */
	public static final String ID_FONT_EDIT_MANDATORY_UNSET = "eu.cessar.ct.editfacility.mndunsetfont"; //$NON-NLS-1$

	/**
	 * Setting for the color of an optional feature that is set.
	 */
	public static final String ID_COLOR_EDIT_OPTIONAL_SET = "eu.cessar.ct.editfacility.optsetcolor"; //$NON-NLS-1$

	/**
	 * Setting for the color of an optional feature that is unset.
	 */
	public static final String ID_COLOR_EDIT_OPTIONAL_UNSET = "eu.cessar.ct.editfacility.optunsetcolor"; //$NON-NLS-1$

	/**
	 * Setting for the color of a mandatory feature that is set.
	 */
	public static final String ID_COLOR_EDIT_MANDATORY_SET = "eu.cessar.ct.editfacility.mndsetcolor"; //$NON-NLS-1$

	/**
	 * Setting for the color of an optional feature that is unset.
	 */
	public static final String ID_COLOR_EDIT_MANDATORY_UNSET = "eu.cessar.ct.editfacility.mndunsetcolor"; //$NON-NLS-1$

	/**
	 * Opens an editor for a target object.
	 *
	 */
	private static final class OpenEditorRunnable implements Runnable
	{
		private final boolean[] result;
		private final EObject eObject;
		private final boolean ownerLookup;
		private final String editorID;

		/**
		 * @param result
		 * @param eObject
		 * @param editorID
		 * @param ownerLookup
		 */
		private OpenEditorRunnable(boolean[] result, EObject eObject, boolean ownerLookup, String editorID)
		{
			this.result = result;
			this.eObject = eObject;
			this.ownerLookup = ownerLookup;
			this.editorID = editorID;
		}

		/**
		 *
		 *
		 * @param page
		 * @param editorInput
		 * @param editorDescriptor
		 * @param target
		 * @throws PartInitException
		 */
		private void doOpenEditor(IWorkbenchPage page, IEditorInput editorInput, IEditorDescriptor editorDescriptor,
			EObject target) throws PartInitException
		{
			String id = editorDescriptor.getId();
			IEditorPart openEditor = page.openEditor(editorInput, id);
			// if the editor could not be opened on the editorInput, it will have as selection the model root
			// then try to open the editor on the parent of the target object
			EObject targetParent = target.eContainer();
			if (openEditor instanceof BasicTransactionalFormEditor && ownerLookup && targetParent != null)
			{
				ISelection selection = ((BasicTransactionalFormEditor) openEditor).getSelection();
				if (selection instanceof StructuredSelection)
				{
					List<EObject> selectedModelObj = getSelectedModelObjects((StructuredSelection) selection);
					Object modelRoot = ((BasicTransactionalFormEditor) openEditor).getModelRoot();
					if (selectedModelObj.size() == 1 && selectedModelObj.get(0) == modelRoot)
					{
						URIEditorInput uriEditorInput = EcoreUIUtil.createURIEditorInput(targetParent);
						if (editorInput == null)
						{
							CessarPluginActivator.getDefault().logError(
								NLS.bind(Messages.TreeEditor_noEditorInput, targetParent));
						}
						else
						{
							uriEditorInput = new CessarURIEditorInput(uriEditorInput.getURI());
							page.openEditor(uriEditorInput, id);
						}
					}

				}
			}
		}

		public void run()
		{
			EObject target = eObject;
			boolean continueLoop = true;

			while (target != null && continueLoop)
			{
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if (page == null)
				{
					continueLoop = false;
					continue;
				}
				URIEditorInput editorInput = EcoreUIUtil.createURIEditorInput(target);
				if (editorInput == null)
				{
					CessarPluginActivator.getDefault().logError(NLS.bind(Messages.TreeEditor_noEditorInput, target));
					continueLoop = false;
					continue;
				}
				editorInput = new CessarURIEditorInput(editorInput.getURI());
				IFile definingFile = ModelUtils.getDefiningFile(target);
				if (definingFile == null)
				{
					CessarPluginActivator.getDefault().logError(NLS.bind(Messages.TreeEditor_noEditorInput, target));
					continueLoop = false;
					continue;
				}

				// try to find the editor with the id = editorID
				IEditorDescriptor foundEditor = PlatformUI.getWorkbench().getEditorRegistry().findEditor(editorID);
				if (foundEditor == null)
				{
					String fileName = definingFile.getName();
					foundEditor = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(fileName);
				}
				try
				{

					if (foundEditor != null)
					{
						doOpenEditor(page, editorInput, foundEditor, target);
						result[0] = true;
						continueLoop = false;
						continue;
					}
				}
				catch (PartInitException e)
				{
					CessarPluginActivator.getDefault().logError(e);
					continueLoop = false;
					continue;
				}
			}
		}
	}

	private EditUtils()
	{
		// do nothing
	}

	/**
	 * Open editor associate with the <code>eObject</code>. If there is no such editor the Cessar tree editor will be
	 * opened. If the editor implements IShowEditorInput it's input will be updated with a new one made from the
	 * provided object.
	 *
	 * @param eObject
	 *        An EObject
	 * @param ownerLookup
	 *        if no editor for the eObject can be found and ownerLookup is true it will try to lookup an editor of the
	 *        eObject.eContainer().
	 * @return true if the editor is successfully opened, false otherwise
	 */
	public static final boolean openEditor(final EObject eObject, final boolean ownerLookup)
	{
		String id = (eObject instanceof Splitable) ? EDITOR_ID_CESSAR_MODEL : EDITOR_ID_CESSAR_TREE;

		return openEditor(eObject, ownerLookup, id);
	}

	/**
	 * @param eObject
	 * @param ownerLookup
	 * @param editorID
	 * @return
	 */
	private static final boolean openEditor(final EObject eObject, final boolean ownerLookup,
		final String cessarEditorID)
	{
		if (!PlatformUI.isWorkbenchRunning())
		{
			return false;
		}
		final boolean[] result = new boolean[] {false};
		PlatformUI.getWorkbench().getDisplay().syncExec(
			new OpenEditorRunnable(result, eObject, ownerLookup, cessarEditorID));
		return result[0];
	}

	/**
	 * @return
	 */
	private static ITheme getCurrentTheme()
	{
		return PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
	}

	/**
	 * Return the font setting associated with the provided ID.
	 *
	 * @param id
	 * @return
	 */
	public static Font getFontSettings(String id)
	{
		return getCurrentTheme().getFontRegistry().get(id);
	}

	/**
	 * Return the color setting associated with the provided ID
	 *
	 * @param id
	 * @return
	 */
	public static Color getColorSettings(String id)
	{
		return getCurrentTheme().getColorRegistry().get(id);
	}

	public static List<EObject> getSelectedModelObjects(IStructuredSelection selection)
	{
		// Just retrieve the selection that has been given to this action by the
		// parent action provider

		List<EObject> result = new ArrayList<EObject>();
		List<IFile> files = new ArrayList<IFile>();

		collectSelectedObjects(selection, result, files);

		if (!files.isEmpty())
		{
			// If selected object is a file, get the mapped model root
			for (IFile file: files)
			{
				// Get model from workspace file
				EObject modelRoot = EcorePlatformUtil.getModelRoot(file);
				if (modelRoot != null)
				{
					result.add(modelRoot);
				}
			}
		}
		return result;
	}

	private static void collectSelectedObjects(IStructuredSelection selection, List<EObject> result, List<IFile> files)
	{
		for (Object selectedObject: selection.toList())
		{
			if (selectedObject instanceof IProject)
			{
				IProject project = (IProject) selectedObject;
				if (project.isAccessible())
				{
					files.addAll(ExtendedPlatform.getAllFiles((IProject) selectedObject, true));
				}
			}
			else if (selectedObject instanceof IFolder)
			{
				IFolder folder = (IFolder) selectedObject;
				if (folder.isAccessible())
				{
					files.addAll(ExtendedPlatform.getAllFiles((IFolder) selectedObject));
				}
			}
			else if (selectedObject instanceof IFile)
			{
				IFile file = (IFile) selectedObject;
				if (file.isAccessible())
				{
					files.add((IFile) selectedObject);
				}
			}
			else if (selectedObject instanceof EObject)
			{
				result.add((EObject) selectedObject);
			}
			else if (selectedObject instanceof IWrapperItemProvider)
			{
				Object object = AdapterFactoryEditingDomain.unwrap(selectedObject);
				if (object instanceof EObject)
				{
					result.add((EObject) object);
				}
			}
		}
	}

	/**
	 * @param text
	 *        The textBox for which the font and color of the caption are set
	 * @param isSet
	 *        A boolean parameter that says if the value has been set or not
	 * @param isMnd
	 *        A boolean parameter that says if the value is mandatory or not
	 */
	public static void setCaptionFontAndColor(Text text, boolean isSet, boolean isMnd)
	{
		String fontID = null;
		String colorID = null;
		if (isSet)
		{
			if (isMnd)
			{
				fontID = EditUtils.ID_FONT_EDIT_MANDATORY_SET;
				colorID = EditUtils.ID_COLOR_EDIT_MANDATORY_SET;
			}
			else
			{
				fontID = EditUtils.ID_FONT_EDIT_OPTIONAL_SET;
				colorID = EditUtils.ID_COLOR_EDIT_OPTIONAL_SET;
			}
		}
		else
		{
			if (isMnd)
			{
				fontID = EditUtils.ID_FONT_EDIT_MANDATORY_UNSET;
				colorID = EditUtils.ID_COLOR_EDIT_MANDATORY_UNSET;
			}
			else
			{
				fontID = EditUtils.ID_FONT_EDIT_OPTIONAL_UNSET;
				colorID = EditUtils.ID_COLOR_EDIT_OPTIONAL_UNSET;
			}
		}
		if (fontID != null)
		{
			text.setFont(EditUtils.getFontSettings(fontID));
		}
		if (colorID != null)
		{
			text.setForeground(EditUtils.getColorSettings(colorID));
		}
	}

	/**
	 * Creates the multiplicity verify listener. Check for negative values.
	 *
	 * @return the verify listener
	 */
	public static VerifyListener createMultiplicityVerifyListener()
	{
		return new VerifyListener()
		{
			@Override
			public void verifyText(VerifyEvent e)
			{
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);

				for (int i = 0; i < chars.length; i++)
				{
					if (!('0' <= chars[i] && chars[i] <= '9' && chars[i] != '-'))
					{
						e.doit = false;
						return;
					}
				}
			}
		};
	}

	/**
	 * Creates the multiplicity verify listener. Check for negative values.
	 *
	 * @return the verify listener
	 */
	public static Listener createNumbersVerifyListener()
	{
		return new Listener()
		{

			@Override
			public void handleEvent(Event event)
			{
				String string = event.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++)
				{
					if (!('0' <= chars[i] && chars[i] <= '9'))
					{
						event.doit = false;
						return;
					}
				}

			}
		};
	}
}