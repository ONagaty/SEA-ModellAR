package eu.cessar.ct.compat.tests.classpath;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.IModelConstants;
import eu.cessar.ct.compat.tests.IConstants;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.testutils.CessarTestCase;

public abstract class AbstractClassPathTests extends CessarTestCase
{

	protected abstract IPath getCompatProjectPath();

	protected void checkTypes(IJavaProject jProject, IModelConstants cnst)
		throws JavaModelException
	{
		assertNotNull(jProject.findType(cnst.getCNameModuleDef()));
	}

	public void testClassPathCompat() throws JavaModelException
	{
		IProject project = importProject(IConstants.PLUGIN_ID, getCompatProjectPath(), false, false);
		assertTrue(project.isAccessible());
		PlatformUtils.waitForModelLoading(project, null);
		// The JDT should be capable to access some classes
		IJavaProject jProject = JavaCore.create(project);
		assertNotNull(jProject.findType(IConstants.CNAME_JET_UTIL));
		checkTypes(jProject, CompatibilitySupport.getModelConstants(project));
	}
}
