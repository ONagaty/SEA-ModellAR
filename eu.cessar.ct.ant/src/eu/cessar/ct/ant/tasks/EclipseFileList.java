/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 2, 2010 10:30:01 AM </copyright>
 */
package eu.cessar.ct.ant.tasks;

import java.io.File;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResourceIterator;

/**
 * @author uidl6458
 * 
 */
public class EclipseFileList extends DataType implements ResourceCollection
{

	private Vector filenames = new Vector();
	private String dirName;
	private File dir;

	/**
	 * The default constructor.
	 * 
	 */
	public EclipseFileList()
	{
		super();
	}

	/**
	 * Makes this instance in effect a reference to another FileList instance.
	 * 
	 * <p>
	 * You must not set another attribute or nest elements inside this element
	 * if you make it a reference.
	 * </p>
	 * 
	 * @param r
	 *        the reference to another filelist.
	 * @exception BuildException
	 *            if an error occurs.
	 */
	@Override
	public void setRefid(Reference r) throws BuildException
	{
		if ((dirName != null) || (filenames.size() != 0))
		{
			throw tooManyAttributes();
		}
		super.setRefid(r);
	}

	/**
	 * Set the dir attribute.
	 * 
	 * @param dir
	 *        the directory this filelist is relative to.
	 * @exception BuildException
	 *            if an error occurs
	 */
	public void setDir(String dir) throws BuildException
	{
		checkAttributesAllowed();
		this.dirName = dir;
	}

	/**
	 * @return
	 */
	public String getDirName()
	{
		return dirName;
	}

	/**
	 * @param dir
	 */
	/*package*/void doSetDir(File dir)
	{
		this.dir = dir;
	}

	/**
	 * @param p
	 *        the current project
	 * @return the directory attribute
	 */
	public File getDir(Project p)
	{
		if (isReference())
		{
			return getRef(p).getDir(p);
		}
		return dir;
	}

	/**
	 * Set the filenames attribute.
	 * 
	 * @param filenames
	 *        a string contains filenames, separated by , or by whitespace.
	 */
	public void setFiles(String filenames)
	{
		checkAttributesAllowed();
		if (filenames != null && filenames.length() > 0)
		{
			StringTokenizer tok = new StringTokenizer(filenames, ", \t\n\r\f", false);
			while (tok.hasMoreTokens())
			{
				this.filenames.addElement(tok.nextToken());
			}
		}
	}

	/**
	 * Returns the list of files represented by this FileList.
	 * 
	 * @param p
	 *        the current project
	 * @return the list of files represented by this FileList.
	 */
	public String[] getFiles(Project p)
	{
		if (isReference())
		{
			return getRef(p).getFiles(p);
		}

		if (dir == null)
		{
			throw new BuildException("No directory specified for filelist.");
		}

		if (filenames.size() == 0)
		{
			throw new BuildException("No files specified for filelist.");
		}

		String[] result = new String[filenames.size()];
		filenames.copyInto(result);
		return result;
	}

	/**
	 * Performs the check for circular references and returns the referenced
	 * FileList.
	 * 
	 * @param p
	 *        the current project
	 * @return the FileList represented by a referenced filelist.
	 */
	protected EclipseFileList getRef(Project p)
	{
		return (EclipseFileList) getCheckedRef(p);
	}

	/**
	 * Inner class corresponding to the &lt;file&gt; nested element.
	 */
	public static class FileName
	{
		private String name;

		/**
		 * The name attribute of the file element.
		 * 
		 * @param name
		 *        the name of a file to add to the file list.
		 */
		public void setName(String name)
		{
			this.name = name;
		}

		/**
		 * @return the name of the file for this element.
		 */
		public String getName()
		{
			return name;
		}
	}

	/**
	 * Add a nested &lt;file&gt; nested element.
	 * 
	 * @param name
	 *        a configured file element with a name.
	 * @since Ant 1.6.2
	 */
	public void addConfiguredFile(FileName name)
	{
		if (name.getName() == null)
		{
			throw new BuildException("No name specified in nested file element");
		}
		filenames.addElement(name.getName());
	}

	/**
	 * Fulfill the ResourceCollection contract.
	 * 
	 * @return an Iterator of Resources.
	 * @since Ant 1.7
	 */
	public Iterator iterator()
	{
		if (isReference())
		{
			return (getRef(getProject())).iterator();
		}
		return new FileResourceIterator(dir,
			(String[]) (filenames.toArray(new String[filenames.size()])));
	}

	/**
	 * Fulfill the ResourceCollection contract.
	 * 
	 * @return number of elements as int.
	 * @since Ant 1.7
	 */
	public int size()
	{
		if (isReference())
		{
			return (getRef(getProject())).size();
		}
		return filenames.size();
	}

	/**
	 * Always returns true.
	 * 
	 * @return true indicating that all elements will be FileResources.
	 * @since Ant 1.7
	 */
	public boolean isFilesystemOnly()
	{
		return true;
	}

}
