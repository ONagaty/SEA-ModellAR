/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu2337<br/>
 * Sep 4, 2013 1:03:11 PM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.utils;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.sdk.pm.IPMElement;
import eu.cessar.req.Requirement;

/**
 * AUTOSAR validation API
 *
 * All methods in this class treat {@code null} arguments as invalid, that is, if they are encountered, the method
 * returns an error status with a corresponding message attached.
 *
 * @author uidu2337
 *
 *         %created_by: uidu2337 %
 *
 *         %date_created: Mon Sep 16 14:14:31 2013 %
 *
 *         %version: 3 %
 */
public final class ValidationUtils
{
	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	private ValidationUtils()
	{
		// hide c-tor
	}

	/**
	 * INTERNAL API, DO NOT USE
	 */
	public static interface Service
	{
		@SuppressWarnings("javadoc")
		public IStatus validate(IPMElement object);

		@SuppressWarnings("javadoc")
		public IStatus validate(Collection<? extends IPMElement> objects);

		@SuppressWarnings("javadoc")
		public IStatus validate(EObject object);

		@SuppressWarnings("javadoc")
		public IStatus validate(IProject project);

		@SuppressWarnings("javadoc")
		public IStatus validate(IProject project, boolean merged);

		@SuppressWarnings("javadoc")
		public IStatus validate(IFile file);

		@SuppressWarnings("javadoc")
		public IStatus validate(IFile file, boolean merged);

		@SuppressWarnings("javadoc")
		public IStatus validateFiles(Collection<IFile> files);

		@SuppressWarnings("javadoc")
		public IStatus validateFiles(Collection<IFile> files, boolean merged);

		@SuppressWarnings("javadoc")
		public IStatus validate(IFolder folder);

		@SuppressWarnings("javadoc")
		public IStatus validate(IFolder folder, boolean merged);
	}

	/**
	 * Validate an AUTOSAR model object using the enabled AUTOSAR validation constraints.
	 *
	 * @param object
	 *        the non-null model object
	 * @return {@code Status.OK_STATUS} if validation was successful, or a combined validation status from which all
	 *         warnings and errors can be extracted
	 *
	 *         <p>
	 *         <b>Usage</b>:
	 *
	 *         <pre>
	 * EObject det = ModelUtils.getEObjectWithQualifiedName(getProject(), &quot;/CESSAR/myDet&quot;);
	 * status = ValidationUtils.validate(det);
	 * if (!status.isOK())
	 * {
	 * 	throw new Exception(&quot;Validation failed for the configuration of the Det module!&quot;);
	 * }
	 * </pre>
	 */
	public static IStatus validate(EObject object)
	{
		return SERVICE.validate(object);
	}

	/**
	 * Validate a {@code PresentationModel} element using the enabled AUTOSAR validation constraints.
	 *
	 * @param object
	 *        the non-null PM element corresponding to an AUTOSAR model element; <br>
	 *        it is restricted to instances of {@code IPMModuleConfiguration} and {@code IPMContainer objects}
	 *
	 * @return {@code Status.OK_STATUS} if validation was successful, or a combined validation status from which all
	 *         warnings and errors can be extracted
	 *
	 *         <p>
	 *         <b>Usage</b>:
	 *
	 *         <pre>
	 * status = ValidationUtils.validate(argument.getAUTOSAR().getEcucDefs().getDet());
	 * if (!status.isOK())
	 * {
	 * 	throw new Exception(&quot;Validation failed for the configuration of the Det module!&quot;);
	 * }
	 * </pre>
	 */
	public static IStatus validate(IPMElement object)
	{
		return SERVICE.validate(object);
	}

	/**
	 * Validate a collection of {@code PresentationModel} elements using the enabled AUTOSAR validation constraints.
	 *
	 * @param objects
	 *        the list of non-null PM elements corresponding to AUTOSAR model elements;<br>
	 *        the objects should be instances of {@code IPMModuleConfiguration} or {@code IPMContainer}
	 *
	 * @return {@code Status.OK_STATUS} if validation was successful, or a combined validation status from which all
	 *         warnings and errors can be extracted
	 *
	 *         <p>
	 *         <b>Usage</b>:
	 *
	 *         <pre>
	 * status = ValidationUtils.validate(argument.getAUTOSAR().getEcucDefs().getCan());
	 * if (!status.isOK())
	 * {
	 * 	throw new Exception(&quot;Validation failed for the configuration(s) of the Can module!&quot;);
	 * }
	 * </pre>
	 */
	public static IStatus validate(Collection<? extends IPMElement> objects)
	{
		return SERVICE.validate(objects);
	}

	/**
	 * Perform validation on a project using the enabled AUTOSAR validation constraints.
	 *
	 * @param project
	 *        the project on which validation is performed
	 * @return {@code Status.OK_STATUS} if validation was successful, or a combined validation status from which all
	 *         warnings and errors can be extracted
	 *
	 *         <p>
	 *         <b>Usage:</b>
	 *
	 *         <pre>
	 * status = ProjectUtils.validate(getProject());
	 * if (!status.isOK())
	 * {
	 * 	throw new Exception(&quot;Validation failed for the configuration of the USS Enterprise project!&quot;);
	 * }
	 * </pre>
	 * @deprecated use {@link #validate(IProject, boolean)}
	 */
	@Deprecated
	public static IStatus validate(IProject project)
	{
		return SERVICE.validate(project);
	}

	/**
	 * Perform validation on a project using the enabled AUTOSAR validation constraints.
	 *
	 * @param project
	 *        the project on which validation is performed
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 *
	 * @return {@code Status.OK_STATUS} if validation was successful, or a combined validation status from which all
	 *         warnings and errors can be extracted
	 *
	 *         <p>
	 *         <b>Usage:</b>
	 *
	 *         <pre>
	 * status = ProjectUtils.validate(getProject());
	 * if (!status.isOK())
	 * {
	 * 	throw new Exception(&quot;Validation failed for the configuration of the USS Enterprise project!&quot;);
	 * }
	 * </pre>
	 */
	@Requirement(
		reqID = "229567")
	public static IStatus validate(IProject project, boolean merged)
	{
		return SERVICE.validate(project, merged);
	}

	/**
	 * Perform validation on a file using the enabled AUTOSAR validation constraints.
	 *
	 * @param file
	 *        the file on which validation is performed
	 * @return {@code Status.OK_STATUS} if validation is successful, or a combined validation status from which all
	 *         warnings and errors can be extracted
	 *
	 * @deprecated use {@link #validate(IFile, boolean)}
	 */
	@Deprecated
	public static IStatus validate(IFile file)
	{
		return SERVICE.validate(file);
	}

	/**
	 * Perform validation on a file using the enabled AUTOSAR validation constraints.
	 *
	 * @param file
	 *        the file on which validation is performed
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return {@code Status.OK_STATUS} if validation is successful, or a combined validation status from which all
	 *         warnings and errors can be extracted
	 *
	 */
	@Requirement(
		reqID = "229567")
	public static IStatus validate(IFile file, boolean merged)
	{
		return SERVICE.validate(file, merged);
	}

	/**
	 * Perform validation on a collection of files using the enabled AUTOSAR validation constraints.
	 *
	 * @param files
	 *        the IFile collection to validate
	 * @return {@code Status.OK_STATUS} if validation is successful, or a combined validation status from which all
	 *         warnings and errors can be extracted
	 * @deprecated use {@link #validateFiles(Collection, boolean)}
	 */
	@Deprecated
	public static IStatus validateFiles(Collection<IFile> files)
	{
		return SERVICE.validateFiles(files);
	}

	/**
	 * Perform validation on a collection of files using the enabled AUTOSAR validation constraints.
	 *
	 * @param files
	 *        the IFile collection to validate
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return {@code Status.OK_STATUS} if validation is successful, or a combined validation status from which all
	 *         warnings and errors can be extracted
	 */
	@Requirement(
		reqID = "229567")
	public static IStatus validateFiles(Collection<IFile> files, boolean merged)
	{
		return SERVICE.validateFiles(files, merged);
	}

	/**
	 * Validate all AUTOSAR files inside {@code folder} using the enabled AUTOSAR validation constraints.
	 *
	 * @param folder
	 *        the folder containing the files to validate
	 * @return {@code Status.OK_STATUS} if validation is successful, or a combined validation status from which all
	 *         warnings and errors can be extracted
	 *
	 * @deprecated use {@link #validate(IFolder, boolean)}
	 */
	@Deprecated
	public static IStatus validate(IFolder folder)
	{
		return SERVICE.validate(folder);
	}

	/**
	 * Validate all AUTOSAR files inside {@code folder} using the enabled AUTOSAR validation constraints.
	 *
	 * @param folder
	 *        the folder containing the files to validate
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return {@code Status.OK_STATUS} if validation is successful, or a combined validation status from which all
	 *         warnings and errors can be extracted
	 */
	@Requirement(
		reqID = "229567")
	public static IStatus validate(IFolder folder, boolean merged)
	{
		return SERVICE.validate(folder, merged);
	}
}
