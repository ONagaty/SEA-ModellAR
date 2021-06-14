package eu.cessar.ct.core.platform.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import eu.cessar.ct.core.internal.platform.ui.CessarPluginActivator;

/**
 * Various platform UI utilities.
 *
 */
public final class PlatformUIUtils
{

	private PlatformUIUtils()
	{
		// do not instantiate
	}

	/**
	 * @param key
	 * @return the image associated with a particular key or null if there is no such image
	 */
	public static Image getImage(String key)
	{
		return CessarPluginActivator.getDefault().getImage(key);
	}

	/**
	 * Check if the selection is a structured selection and returns a list with all objects from selection that are
	 * instanceof <code>clz</code>. Otherwise, it returns an empty list.
	 *
	 * @param <T>
	 * @param clz
	 * @param selection
	 * @return list with all objects from selection that are instanceof <code>clz</code>
	 */
	public static <T> List<T> getAllObjectsFromSelection(Class<T> clz, ISelection selection)
	{
		List<T> filteredList = new ArrayList<T>();

		if (selection instanceof StructuredSelection)
		{
			StructuredSelection sSelection = (StructuredSelection) selection;
			List<?> list = sSelection.toList();
			for (Object object: list)
			{
				if (clz.isInstance(object))
				{
					@SuppressWarnings("unchecked")
					T result = (T) object;
					filteredList.add(result);
				}
			}
		}
		return filteredList;
	}

	/**
	 * Check if the selection is a structured selection that contain exactly one element. If it does and it's instanceof
	 * <code>clz</code> then return it, otherwise return <code>null</code>
	 *
	 * @param clz
	 *
	 * @param selection
	 * @return the first object from selection if there is one and is of the right class
	 */
	public static <T> T getObjectFromSelection(Class<T> clz, ISelection selection)
	{
		if (selection instanceof StructuredSelection)
		{
			StructuredSelection sSelection = (StructuredSelection) selection;
			if (sSelection.size() == 1)
			{
				Object firstElement = sSelection.getFirstElement();
				if (clz.isInstance(firstElement))
				{
					@SuppressWarnings("unchecked")
					T result = (T) firstElement;
					return result;
				}
			}
		}
		return null;
	}

	/**
	 * Opens the given file in the default editor.
	 *
	 * @param file
	 *        The resource to open in the default editor.
	 */
	public static void openEditor(final IFile file)
	{
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		try
		{
			if (activeWorkbenchWindow != null)
			{
				IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
				if (page != null)
				{
					IDE.openEditor(page, file, true);
				}
			}
		}
		catch (PartInitException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * Return the Eclipse resource behind an {@link IEditorInput} or null if there is no such resource. Note that the
	 * resource might be either an {@link IFile} or an {@link IProject}
	 *
	 * @param input
	 * @return the resource behind or null
	 */
	public static IResource getResource(IEditorInput input)
	{
		if (input == null)
		{
			return null;
		}
		if (input instanceof URIEditorInput)
		{
			URI uri = ((URIEditorInput) input).getURI();
			IPath path = EcorePlatformUtil.createPath(uri);
			return ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		}
		else
		{
			return (IFile) input.getAdapter(IFile.class);
		}
	}

	/**
	 * Computes the content types for the given file types
	 *
	 * @param acceptedFileTypes
	 * @return the list of content types
	 */
	public static List<IContentType> getContentTypesForFilesTypes(List<String> acceptedFileTypes)
	{
		List<IContentType> contentTypes = new ArrayList<IContentType>();

		for (String acceptedFileType: acceptedFileTypes)
		{
			IContentType contentType = Platform.getContentTypeManager().getContentType(acceptedFileType);
			if (contentType != null)
			{
				contentTypes.add(contentType);
			}
		}

		return contentTypes;
	}

	/**
	 * Computes the allowed file extensions for the given content types
	 *
	 * @param contentTypes
	 * @return the list of content types
	 */
	public static List<String> getFileExtensionsForContentTypes(List<IContentType> contentTypes)
	{
		List<String> allowedExtensions = new ArrayList<String>();
		for (IContentType contentType: contentTypes)
		{
			String[] fileSpecs = contentType.getFileSpecs(IContentType.FILE_EXTENSION_SPEC);

			allowedExtensions.addAll(Arrays.asList(fileSpecs));
		}
		return allowedExtensions;
	}

	/**
	 *
	 * Computes the content types for the given autosar release descriptors
	 *
	 * @param autosarReleaseDescriptors
	 * @return the list of content types
	 */
	public static List<IContentType> getContentTypesForReleaseDescriptors(
		List<AutosarReleaseDescriptor> autosarReleaseDescriptors)
	{
		List<IContentType> contentTypes = new ArrayList<IContentType>();
		IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
		for (AutosarReleaseDescriptor autosarReleaseDescriptor: autosarReleaseDescriptors)
		{
			List<String> contentTypeIds = autosarReleaseDescriptor.getContentTypeIds();
			for (String contentTypeId: contentTypeIds)
			{
				IContentType contentType = contentTypeManager.getContentType(contentTypeId);
				if (contentType != null)
				{
					contentTypes.add(contentType);
				}
			}
		}
		return contentTypes;
	}
}
