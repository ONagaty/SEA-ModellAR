package eu.cessar.ct.jet.core.internal.compiler;

import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

import eu.cessar.ct.jet.core.JETCoreConstants;

@SuppressWarnings("restriction")
class JETCompilationUnit implements ICompilationUnit
{
	private String javaSource;
	private String className;

	JETCompilationUnit(final String source, final String strClass)
	{
		className = strClass;
		javaSource = source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.internal.compiler.env.ICompilationUnit#getContents()
	 */
	public char[] getContents()
	{
		return javaSource.toCharArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.internal.compiler.env.ICompilationUnit#getPackageName()
	 */
	public char[][] getPackageName()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.internal.compiler.env.IDependent#getFileName()
	 */
	public char[] getFileName()
	{
		String result = className + JETCoreConstants.JAVA;
		return result.toCharArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.internal.compiler.env.ICompilationUnit#getMainTypeName()
	 */
	public char[] getMainTypeName()
	{
		return className.toCharArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.internal.compiler.env.ICompilationUnit#ignoreOptionalProblems()
	 */
	@Override
	public boolean ignoreOptionalProblems()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
