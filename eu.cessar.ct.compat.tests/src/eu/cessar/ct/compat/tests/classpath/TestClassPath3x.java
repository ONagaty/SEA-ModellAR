package eu.cessar.ct.compat.tests.classpath;

import org.eclipse.core.runtime.IPath;

import eu.cessar.ct.compat.tests.IConstants;

public class TestClassPath3x extends AbstractClassPathTests
{

	@Override
	protected IPath getCompatProjectPath()
	{
		return IConstants.PROJECT_COMPLEX_3x_COMPAT;
	}

}
