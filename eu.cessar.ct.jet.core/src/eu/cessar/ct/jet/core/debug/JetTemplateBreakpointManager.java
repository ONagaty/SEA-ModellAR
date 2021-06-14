/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Jun 4, 2010 4:30:03 PM </copyright>
 */
package eu.cessar.ct.jet.core.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.debug.core.IJavaLineBreakpoint;
import org.eclipse.jdt.debug.core.JDIDebugModel;

import eu.cessar.ct.jet.core.compiler.IJETServicesProvider;
import eu.cessar.ct.jet.core.internal.CessarPluginActivator;
import eu.cessar.ct.jet.core.internal.compiler.SourceRange;

/**
 * @author uidl7321
 * 
 */
public class JetTemplateBreakpointManager
{
	private IBreakpointManager breakpointManager;

	/**
	 * @param breakpointManager
	 */
	public JetTemplateBreakpointManager(final IBreakpointManager breakpointManager)
	{
		super();
		this.breakpointManager = breakpointManager;
	}

	/**
	 * @param translator
	 * @param lineNumber
	 *        1 relative
	 */
	public void toggleJetTemplateBreakPoint(IJETServicesProvider translator, final int lineNumber)
	{
		if (translator == null)
		{
			throw new IllegalArgumentException("translator must not be null!"); //$NON-NLS-1$
		}
		if (lineNumber < 0)
		{
			throw new IllegalArgumentException("lineNumber can't be smaller than 0, " + "but is " //$NON-NLS-1$ //$NON-NLS-2$
				+ lineNumber);
		}
		try
		{
			safeToggleTemplateBreakPoint(translator, lineNumber);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * @param translator
	 * @param jetLineNumber
	 * @throws CoreException
	 */
	private void safeToggleTemplateBreakPoint(final IJETServicesProvider translator, final int jetLineNumber)
		throws CoreException
	{
		ICompilationUnit compilationUnit = translator.getCompilationUnit(false, new NullProgressMonitor());
		if (compilationUnit == null || !compilationUnit.exists())
		{
			// recompile the jet
			translator.compile(false, new NullProgressMonitor());
		}
		IFile jetFile = translator.getJetFile();
		Assert.isNotNull(jetFile);
		if (existsBreakpoint(jetFile, jetLineNumber))
		{
			removeJetBreakpoint(jetLineNumber, jetFile);
		}
		else
		{

			final SourceRange jetRange = translator.findNextScripletOrExpression(jetLineNumber);

			if (jetRange != null)
			{
				if (jetRange.getStartLine() <= jetLineNumber && jetRange.getEndLine() >= jetLineNumber)
				{
					if (canAddBreakpoint(jetLineNumber, jetFile))
					{
						// addJavaBreakpointFor(jetLineNumber, translator);
						// TODO: REMOVE->put in listener
						addJetBreakpoint(jetLineNumber, jetFile, null);
					}
				}

				else
				{
					if (canAddBreakpoint(jetRange.getStartLine(), jetFile))
					{
						// addJavaBreakpointFor(jetRange.getStartLine(),
						// translator);
						// TODO: REMOVE->put in listener
						addJetBreakpoint(jetRange.getStartLine() - 1, jetFile, null);

						// jet breakpoint will be added by the
						// JavaBreakpointListener in response
						// to notification
					}
				}
			}
		}
	}

	/**
	 * @param jetLineNumber
	 * @param file
	 * @return
	 * @throws CoreException
	 */
	private boolean canAddBreakpoint(final int jetLineNumber, final IFile file) throws CoreException
	{
		return jetLineNumber >= 0 && !existsBreakpoint(file, jetLineNumber);
	}

	/**
	 * @param jetTemplateBreakpoint
	 * @param translator
	 * @throws CoreException
	 */
	private static void addJavaBreakpointFor(final JetTemplateBreakpoint jetTemplateBreakpoint,
		final IJETServicesProvider translator) throws CoreException
	{
		int javaLine = translator.convertJet2JavaLine(jetTemplateBreakpoint.getJETLineNumber());
		if (javaLine != -1)
		{
			Map<String, Object> attributes = new HashMap<String, Object>();
			// attributes.put(IJETServicesProvider.class.getName(), translator);

			IFile javaFile = translator.getJavaFile(new NullProgressMonitor());
			String typeFullName = javaFile.getFullPath().removeFirstSegments(2).removeFileExtension().toString().replace(
				"/", "."); //$NON-NLS-1$ //$NON-NLS-2$

			IJavaLineBreakpoint javaBreakpoint = JDIDebugModel.createLineBreakpoint(javaFile, typeFullName,
				javaLine + 1, -1, -1, 0, true, attributes);
			javaBreakpoint.setEnabled(jetTemplateBreakpoint.isEnabled());
			jetTemplateBreakpoint.setCorrespondingJavaBreakpoint(javaBreakpoint);
		}

	}

	/**
	 * @param jetFile
	 * @param translator
	 * @throws CoreException
	 */
	public void addJavaBreakpoints(final IFile jetFile, final IJETServicesProvider translator) throws CoreException
	{
		List<JetTemplateBreakpoint> existingBreakpoints = getExistingBreakpoints(jetFile);
		for (JetTemplateBreakpoint jetTemplateBreakpoint: existingBreakpoints)
		{
			addJavaBreakpointFor(jetTemplateBreakpoint, translator);
		}

	}

	/**
	 * @param lineNumber
	 * @param file
	 * @param corrJavaBreakpoint
	 * @throws CoreException
	 */
	private void addJetBreakpoint(final int lineNumber, final IFile file, final IBreakpoint corrJavaBreakpoint)
		throws CoreException
	{
		getManager().addBreakpoint(new JetTemplateBreakpoint(file, lineNumber, corrJavaBreakpoint));
	}

	/**
	 * @param lineNumber
	 * @param file
	 * @throws CoreException
	 */
	public void removeJetBreakpoint(final int lineNumber, final IFile file) throws CoreException
	{
		JetTemplateBreakpoint bp = getExistingBreakpoint(file, lineNumber);
		getManager().removeBreakpoint(bp.getCorrespondingJavaBreakpoint(), true);
		getManager().removeBreakpoint(bp, true);
	}

	/**
	 * @return
	 */
	private IBreakpointManager getManager()
	{
		return breakpointManager;
	}

	/**
	 * @param jetFile
	 * @return
	 */
	public List<JetTemplateBreakpoint> getExistingBreakpoints(final IFile jetFile)
	{
		IBreakpoint[] breakpoints = getManager().getBreakpoints();
		List<JetTemplateBreakpoint> result = new ArrayList<JetTemplateBreakpoint>();
		for (int i = 0; i < breakpoints.length; i++)
		{
			IBreakpoint breakpoint = breakpoints[i];
			IMarker marker = breakpoint.getMarker();
			if (marker.getResource().equals(jetFile) && breakpoint instanceof JetTemplateBreakpoint)
			{
				result.add((JetTemplateBreakpoint) breakpoint);
			}
		}
		return result;
	}

	/**
	 * @param file
	 * @param lineNumber
	 * @return
	 * @throws CoreException
	 */
	public JetTemplateBreakpoint getExistingBreakpoint(final IFile file, final int lineNumber) throws CoreException
	{
		List<JetTemplateBreakpoint> bps = getExistingBreakpoints(file);
		JetTemplateBreakpoint result = null;
		for (JetTemplateBreakpoint breakpoint: bps)
		{
			int line = breakpoint.getLineNumber();
			if (line == lineNumber + 1 && line >= 0)
			{
				result = breakpoint;
			}
		}
		return result;
	}

	/**
	 * @param file
	 * @param lineNumber
	 * @return
	 * @throws CoreException
	 */
	public boolean existsBreakpoint(final IFile file, final int lineNumber) throws CoreException
	{
		return getExistingBreakpoint(file, lineNumber) != null;
	}

	/**
	 * @param bp
	 * @param enabled
	 */
	public void setEnabled(final JetTemplateBreakpoint bp, final boolean enabled)
	{
		Assert.isNotNull(bp);
		try
		{
			bp.setEnabled(enabled);
			getManager().fireBreakpointChanged(bp);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * @param file
	 * @param lineNumber
	 * @param enabled
	 * @throws CoreException
	 */
	public void setEnabled(final IFile file, final int lineNumber, final boolean enabled) throws CoreException
	{
		JetTemplateBreakpoint existingBreakpoint = getExistingBreakpoint(file, lineNumber);
		if (existingBreakpoint != null)
		{
			setEnabled(existingBreakpoint, enabled);
		}

	}
}
