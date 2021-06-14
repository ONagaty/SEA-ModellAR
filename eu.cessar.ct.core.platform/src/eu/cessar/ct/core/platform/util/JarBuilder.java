package eu.cessar.ct.core.platform.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;

/**
 * The class <code>JarBuilder</code> creates a jar archive.
 *
 * @Review uidl6458 - 18.04.2012
 */
public class JarBuilder
{
	private static final String VER_1_0 = "1.0"; //$NON-NLS-1$

	private static final int BUFFER_SIZE = 1024;

	private Manifest manifest;

	private JarOutputStream jarStream;
	private ByteArrayOutputStream outByteStream;
	private IFile jarIFile;
	private List<String> entryList;

	/**
	 * Creates a new <code>JarBuilder</code>.<br>
	 * Note:A default manifest file is created with the MANIFEST_VERSION attribute.
	 *
	 * @param outStream
	 *        the output stream corresponding to the jar file
	 * @throws IOException
	 *         if an I/O error has occurred
	 */
	public JarBuilder(OutputStream outStream) throws IOException
	{
		manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, VER_1_0);
		jarStream = new JarOutputStream(outStream);
		entryList = new ArrayList<String>();
	}

	/**
	 * Creates a new <code>JarBuilder</code>.<br>
	 * Note:A default manifest file is created with the MANIFEST_VERSION attribute.
	 *
	 * @param outFile
	 *        the file corresponding to the jar archive
	 * @throws IOException
	 *         if an I/O error has occurred
	 * @throws FileNotFoundException
	 *         if the <code>outFile</code> cannot be found.
	 */
	public JarBuilder(File outFile) throws FileNotFoundException, IOException
	{
		manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, VER_1_0);
		jarStream = new JarOutputStream(new FileOutputStream(outFile));
		entryList = new ArrayList<String>();
	}

	/**
	 * Creates a new <code>JarBuilder</code>.<br>
	 * Note:A default manifest file is created with the MANIFEST_VERSION attribute.
	 *
	 * @param outIFile
	 *        the file corresponding to the jar archive
	 * @throws IOException
	 *         if an I/O error has occurred
	 */

	public JarBuilder(IFile outIFile) throws IOException
	{
		jarIFile = outIFile;
		outByteStream = new ByteArrayOutputStream();
		manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, VER_1_0);
		jarStream = new JarOutputStream(outByteStream);
		entryList = new ArrayList<String>();
	}

	/**
	 * Adds a new file in the jar archive.<br>
	 * Note: The client is responsible for closing the <code>content</code> input stream when finished.</li>
	 *
	 * @param name
	 *        the fully qualified name of the file to add.<br>
	 *        Example: for <code>name</code>= dir/dir1/resource.class,the jar archive will have an entry for
	 *        <code>dir</code> folder. This entry will contain the <code>dir1</code> folder,and <code>dir1</code> folder
	 *        will contain <code>resource.class</code>
	 * @param content
	 *        the input stream corresponding to the content of the file to add
	 * @param monitor
	 *        a progress monitor, or <code>null</code> if progress reporting and cancellation are not desired
	 * @throws JarException
	 *         if an error has occurred while writing to the jar archive.
	 */
	public void addToJar(String name, InputStream content, IProgressMonitor monitor) throws JarException
	{
		IPath fullPath = new Path(name);
		if (!ResourceUtils.isValidPath(fullPath.makeAbsolute().toString(), IResource.FILE))
		{
			throw new JarException("The name of the file to add is not a valid path"); //$NON-NLS-1$
		}

		try
		{
			JarEntry entry;
			String[] seg = fullPath.segments();
			String destPath = ""; //$NON-NLS-1$

			for (int i = 0; i < seg.length - 1; i++)
			{
				destPath += seg[i] + "/"; //$NON-NLS-1$
				entry = new JarEntry(destPath);
				if (!entryList.contains(entry.getName()))
				{
					jarStream.putNextEntry(entry);
					jarStream.flush();
					jarStream.closeEntry();
					entryList.add(entry.getName());
				}
			}

			// read the content input stream
			byte[] buffer = new byte[BUFFER_SIZE];
			int byteNr;
			// last segment,the file
			destPath += fullPath.lastSegment();
			entry = new JarEntry(destPath);
			jarStream.putNextEntry(entry);
			String fileName = fullPath.lastSegment().toString();
			if (monitor != null)
			{
				monitor.beginTask("Adding file " + fileName, content.available()); //$NON-NLS-1$
			}
			while ((byteNr = content.read(buffer, 0, BUFFER_SIZE)) != -1)
			{
				if (monitor != null && monitor.isCanceled())
				{
					throw (JarException) new JarException().initCause(new OperationCanceledException());
				}
				jarStream.write(buffer, 0, byteNr);
				if (monitor != null)
				{
					monitor.worked(byteNr);
				}
			}
			jarStream.flush();
			jarStream.closeEntry();
		}
		catch (IOException e)
		{
			throw (JarException) new JarException().initCause(e);
		}
		finally
		{
			if (monitor != null)
			{
				monitor.done();
			}
		}
	}

	/**
	 * Adds a new file in the jar archive.
	 *
	 * @param name
	 *        the fully qualified name of the file to add.<br>
	 *        Example: for <code>name</code>= dir/dir1/resource.class,the jar archive will have an entry for
	 *        <code>dir</code> folder. This entry will contain the <code>dir1</code> folder,and <code>dir1</code> folder
	 *        will contain <code>resource.class</code>
	 * @param file
	 *        the file to add.
	 * @param monitor
	 *        a progress monitor, or <code>null</code> if progress reporting and cancellation are not desired
	 * @throws JarException
	 *         if an error has occurred while writing to the jar archive.
	 */
	public void addToJar(String name, File file, IProgressMonitor monitor) throws JarException
	{
		try
		{
			InputStream content = new FileInputStream(file);
			try
			{
				addToJar(name, content, monitor);
			}
			finally
			{
				try
				{
					content.close();
				}
				catch (IOException e)
				{
					throw (JarException) new JarException().initCause(e);
				}
			}
		}
		catch (FileNotFoundException e)
		{
			throw (JarException) new JarException().initCause(e);
		}
	}

	/**
	 * Adds a new file in the jar archive.
	 *
	 * @param name
	 *        the fully qualified name of the file to add.<br>
	 *        Example: for <code>name</code>= dir/dir1/resource.class,the jar archive will have an entry for
	 *        <code>dir</code> folder. This entry will contain the <code>dir1</code> folder,and <code>dir1</code> folder
	 *        will contain <code>resource.class</code>
	 * @param file
	 *        the file to add.
	 * @param monitor
	 *        a progress monitor, or <code>null</code> if progress reporting and cancellation are not desired
	 * @throws JarException
	 *         if an error has occurred while writing to the jar archive.
	 */
	public void addToJar(String name, IFile file, IProgressMonitor monitor) throws JarException
	{
		try
		{
			InputStream content = file.getContents();
			try
			{
				addToJar(name, content, monitor);
			}
			finally
			{
				try
				{
					content.close();
				}
				catch (IOException e)
				{
					throw (JarException) new JarException().initCause(e);
				}
			}
		}
		catch (CoreException e)
		{
			throw (JarException) new JarException().initCause(e);
		}
	}

	/**
	 * Returns the manifest file for the created jar archive.
	 *
	 * @return the manifest file corresponding to the jar archive
	 */
	public Manifest getManifest()
	{
		return manifest;
	}

	/**
	 * Signals that the process of file addition to the jar archive is finished.<br>
	 * Note: In order for this method to work correctly, the following should be taken in consideration:
	 * <ul>
	 * <li>The output stream corresponding to the jar archive will be completely flushed when finished.</li>
	 * <li>The client is not responsible for closing the output stream corresponding to the jar archive when finished.</li>
	 * </ul>
	 *
	 * @param monitor
	 *        a progress monitor, or <code>null</code> if progress reporting and cancellation are not desired
	 *
	 * @throws JarException
	 *         if an error has occurred while finishing the jar archive's build.
	 */
	public void done(IProgressMonitor monitor) throws JarException
	{
		InputStream jarIFileStream = null;
		try
		{
			ZipEntry e = new ZipEntry(JarFile.MANIFEST_NAME);
			jarStream.putNextEntry(e);
			manifest.write(jarStream);
			jarStream.closeEntry();
			jarStream.flush();
			jarStream.close();

			if (jarIFile != null)
			{
				jarIFileStream = new ByteArrayInputStream(outByteStream.toByteArray());
				if (jarIFile.exists())
				{
					jarIFile.setContents(jarIFileStream, IResource.FORCE, monitor);
				}
				else
				{
					jarIFile.create(jarIFileStream, true, monitor);
				}
			}

		}
		catch (Exception e)
		{
			throw (JarException) new JarException().initCause(e);
		}
		finally
		{
			try
			{
				if (outByteStream != null)
				{
					outByteStream.close();
				}

				if (jarIFileStream != null)
				{
					jarIFileStream.close();
				}
			}
			catch (IOException e)
			{
				throw (JarException) new JarException().initCause(e);

			}
		}
	}
}
