/**
 * 
 */
package eu.cessar.ct.ant.tasks;

import java.io.File;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.FileUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

/**
 * 
 */
public class EclipseFileScanner extends DirectoryScanner implements IResourceVisitor
{

	private final IProject eclipseProject;
	private IResource rootNode;
	private final Project antProject;
	private IPath rootPath;

	/**
	 * @param eclipseProject
	 */
	public EclipseFileScanner(Project antProject, IProject eclipseProject)
	{
		this.antProject = antProject;
		this.eclipseProject = eclipseProject;
		rootNode = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.DirectoryScanner#setBasedir(java.io.File)
	 */
	@Override
	public synchronized void setBasedir(File basedir)
	{
		rootNode = null;
		String leadingPath = FileUtils.getFileUtils().removeLeadingPath(antProject.getBaseDir(),
			basedir);
		if (leadingPath.length() == 0)
		{
			rootNode = eclipseProject;
		}
		else if (!FileUtils.isAbsolutePath(leadingPath))
		{
			// locate the node for this leadingPath and set it to the
			// rootNode
			IFolder folder = eclipseProject.getFolder(leadingPath);
			if (folder.exists())
			{
				rootNode = folder;
			}
		}
		else
		{
			// for absolute path directory we cannot do anything
		}
		super.setBasedir(basedir);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.DirectoryScanner#scan()
	 */
	@Override
	public void scan() throws BuildException
	{
		if (rootNode == null)
		{
			if (basedir.exists())
			{
				super.scan();
			}
			else
			{
				dirsIncluded = new Vector<String>();
				filesIncluded = new Vector<String>();
				// do nothing, the target does not exists -> we have to
				// regenerate
			}
		}
		else
		{
			clearResults();
			try
			{
				rootPath = rootNode.getProjectRelativePath();
				rootNode.accept(this);
			}
			catch (CoreException e)
			{
				throw new BuildException(e.getMessage(), e);
				// e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources
	 * .IResource)
	 */
	public boolean visit(IResource resource) throws CoreException
	{
		// the resource should be a contain rootPath
		if (!rootPath.isPrefixOf(resource.getProjectRelativePath()))
		{
			return false;
		}
		IPath path = resource.getProjectRelativePath();
		path = path.removeFirstSegments(rootPath.segmentCount());
		String resName = path.toString().replace('/', File.separatorChar).replace('\\',
			File.separatorChar);
		int resType = resource.getType();
		if (resType == IResource.FILE)
		{
			if (isIncluded(resName))
			{
				if (!isExcluded(resName))
				{
					filesIncluded.add(resName);
				}
			}
		}
		else
		{
			if (isIncluded(resName))
			{
				if (!isExcluded(resName))
				{
					dirsIncluded.add(resName);
				}
			}
			// FOLDER, PROJECT or WORKSPACE ROOT
			boolean result = couldHoldIncluded(resName);
			return result;
		}
		// TODO Auto-generated method stub
		// System.err.println("Visit:" + resource.getProjectRelativePath());
		return true;
	}

	/**
	 * @return the rootNode
	 */
	public IResource getRootNode()
	{
		return rootNode;
	}

	/**
	 * @param string
	 * @return
	 */
	public File getFile(String filePath)
	{
		if (rootNode == null)
		{
			return new File(getBasedir(), filePath);
		}
		return eclipseProject.findMember(rootNode.getProjectRelativePath().append(filePath)).getLocation().toFile();
	}

}
