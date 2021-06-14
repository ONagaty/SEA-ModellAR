/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Oct 14, 2009 5:18:06 PM </copyright>
 */
package eu.cessar.ct.core.mms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.artop.aal.common.metamodel.AutosarMetaModelVersionData;
import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.artop.aal.common.resource.AutosarURIFactory;
import org.artop.aal.workspace.preferences.IAutosarWorkspacePreferences;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.model.IModelDescriptor;
import org.eclipse.sphinx.emf.model.ModelDescriptorRegistry;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.EcoreResourceUtil;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
import org.eclipse.sphinx.emf.workspace.domain.WorkspaceEditingDomainManager;

import eu.cessar.ct.core.mms.internal.CessarPluginActivator;
import eu.cessar.ct.core.mms.internal.mdl.ModelDependencyLookup;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.core.platform.emf.IAltEditingDomainProvider;
import eu.cessar.ct.core.platform.util.DelegatingSubEList;
import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import eu.cessar.ct.core.platform.util.StringUtils;
import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.pm.IPMElement;
import eu.cessar.ct.sdk.pm.IPMInstanceRef;
import eu.cessar.ct.sdk.pm.IPMModuleConfiguration;
import eu.cessar.ct.sdk.pm.IPMPackage;
import eu.cessar.ct.sdk.pm.IPresentationModel;
import eu.cessar.ct.sdk.utils.IGenericModelDependencyLookup;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.sdk.utils.PMUtils;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Various model utilities that are independent of autosar metamodel version. This class can not be instantiated, only
 * used as a utility class.
 *
 * @Review uidl6458 - 30.03.2012
 *
 */
public final class MetaModelUtils
{

	/** */
	public static final char QNAME_SEPARATOR = '/';

	/** */
	public static final String QNAME_SEPARATOR_STR = Character.toString(QNAME_SEPARATOR);

	/** */
	public static final String GEN_MODEL_PACKAGE_NS_URI = "http://www.eclipse.org/emf/2002/GenModel"; //$NON-NLS-1$

	/** */
	public static final String ANN_STEREOTYPE = "Stereotype"; //$NON-NLS-1$

	/** Annotation value for the Stereotype type. */
	public static final String STEREOTYPE_VALUE_INSTANCEREF = "instanceRef"; //$NON-NLS-1$
	/**  */
	public static final String STEREOTYPE_VALUE_INSTANCEREF_TARGET = "instanceRef.target"; //$NON-NLS-1$
	/** */
	public static final String STEREOTYPE_VALUE_INSTANCEREF_CONTEXT = "instanceRef.context"; //$NON-NLS-1$

	/** */
	public static final String ANN_TAGGEDVALUES = "TaggedValues"; //$NON-NLS-1$

	/** Annotation key for the TaggedValues type (2.1 and 3.x). */
	public static final String TAGGEDVALUES_KEY_INSTANCEREF_CONTEXT = "instanceRef.context"; //$NON-NLS-1$

	/** Annotation key for the TaggedValues type (4.0). */
	public static final String TAGGEDVALUES_KEY_CONTEXT_ORDER = "contextOrder"; //$NON-NLS-1$

	/** */
	public static final String XML_ROLE_WRAPPER_ELEMENT = "xml.roleWrapperElement"; //$NON-NLS-1$
	/** */
	public static final String XML_NAME_PLURAL = "xml.namePlural"; //$NON-NLS-1$
	/** */
	public static final String XML_NAME = "xml.name"; //$NON-NLS-1$
	/** */
	public static final String XML_ROLE_ELEMENT = "xml.roleElement"; //$NON-NLS-1$
	/** */
	public static final String XML_TYPE_ELEMENT = "xml.typeElement"; //$NON-NLS-1$

	/** */
	public static final String SCHEMA_LOCATION_ATTR = "xsi:schemaLocation"; //$NON-NLS-1$

	/** */
	public static final String TAGGEDVALUES_XML_SEQUENCEOFFSET = "xml.sequenceOffset"; //$NON-NLS-1$
	/** */
	public static final String TAGGEDVALUES_PUREMM_MINOCCURS = "pureMM.minOccurs"; //$NON-NLS-1$
	/** */
	public static final String TAGGEDVALUES_PUREMM_MAXOCCURS = "pureMM.maxOccurs"; //$NON-NLS-1$

	/** */
	public static final String ANN_METADATA = "MetaData"; //$NON-NLS-1$
	/** */
	public static final String TEMPLATE = "Template"; //$NON-NLS-1$
	/** */
	public static final String ANN_NAME = "Name"; //$NON-NLS-1$

	/**
	 * The prefix in absolute qualified name segments of EObjects which are no Identifiables and don't have a shortName.
	 */
	public static final String NON_IDENTIFIABLE_SEGMENT_PREFIX = "@"; //$NON-NLS-1$

	/** The name of the feature that holds an identifiable element's short name */
	public static final String SHORT_NAME_FEATURE = "shortName"; //$NON-NLS-1$

	private static final char QNAME_SEPARATOR_ALT = '\\';

	private static final Pattern DUP_SLASH_PATTERN = Pattern.compile("//+"); //$NON-NLS-1$

	private static final String AUTOSAR_NSURI_PREFIX = "http://autosar.org/"; //$NON-NLS-1$
	private static final String GENERIC_NSURI_PREFIX = "http://artop.org/gautosar"; //$NON-NLS-1$

	// can not instantiate
	private MetaModelUtils()
	{

	}

	/**
	 * Returns the absolute qualified name of the given Identifiable or ARObject. It starts with a "/" separated
	 * concatenation of the shortNames of the Identifiable(s) which contain the Identifiable or ARObject in question. It
	 * ends with the shortName of the given object in case that this one is an Identifiable, e.g.
	 * <code>/topLevelPackageAShortName/subPackageBShortName/moduleDefCShortName</code> . Otherwise, i.e. if the given
	 * object is an ARObject, the absolute qualified name ends with a "@containingFeatureName(.listPosition)?" formatted
	 * identifier of that ARObject, e.g. <code>/topLevelPackageAShortName/containerBShortName/@parameterValues.5</code>
	 *
	 *
	 * @param object
	 *        The Identifiable or ARObject whose absolute qualified name is to be returned.
	 * @return The given Identifiable or ARObject's absolute qualified name or an empty string if no such could be
	 *         established.
	 */
	public static String getAbsoluteQualifiedName(EObject object)
	{
		return AutosarURIFactory.getAbsoluteQualifiedName(object);
	}

	/**
	 * Determine the number of unique short names inside a collection of identifiables.
	 *
	 * @param identifiables
	 *        the identifiables
	 * @return the number of unique short names encountered
	 */
	public static int getNumberOfUniqueShortNames(Collection<GIdentifiable> identifiables)
	{
		Set<String> shortNames = new HashSet<String>();
		for (GIdentifiable gIdentifiable: identifiables)
		{
			shortNames.add(gIdentifiable.gGetShortName());
		}
		return shortNames.size();
	}

	/**
	 * Returns whether an <code>EObject</code> is read-only. The object will be considered read-only if the containing
	 * resource is read-only. If the object is not stored into a resource it will be read-write
	 *
	 * @param obj
	 *        the object to edit
	 * @return <code>true</code> if the containing resource is read-only<br>
	 *         <code>false</code>, otherwise
	 */
	public static boolean isReadOnly(EObject obj)
	{
		Resource eResource = obj.eResource();

		if (eResource == null)
		{
			return false;
		}
		return EcoreResourceUtil.isReadOnly(eResource.getURI());
	}

	/**
	 * Return a normalized representation of the <code>qName</code>. A normalized qualified name have the following
	 * characteristics:<br/>
	 * <ul>
	 * <li>use forward slashes as separators("/")</li>
	 * <li>any duplicate forward slashes are changed with single slashes</li>
	 * <li>starts with a slash even for empty (but not null) qNames</li>
	 * <li>the last character is not slash</li>
	 * </ul>
	 *
	 * @param qName
	 *        a qualified name
	 * @return the normalized qName or null if the <code>qName</code> is null
	 */
	public static String normalizeQualifiedName(String qName)
	{
		String normalizedQName = qName;

		if (qName == null)
		{
			// null qName
			return null;
		}
		normalizedQName = qName.replace(QNAME_SEPARATOR_ALT, QNAME_SEPARATOR);
		Matcher matcher = DUP_SLASH_PATTERN.matcher(normalizedQName);
		normalizedQName = matcher.replaceAll(QNAME_SEPARATOR_STR);

		if (normalizedQName.endsWith(QNAME_SEPARATOR_STR))
		{
			// remove it
			normalizedQName = normalizedQName.substring(0, normalizedQName.length() - 1);
		}
		if (!normalizedQName.startsWith(QNAME_SEPARATOR_STR))
		{
			normalizedQName = QNAME_SEPARATOR_STR + normalizedQName;
		}
		return normalizedQName;

	}

	/**
	 * Split a qualified name in the corresponding segments.
	 *
	 * @param qName
	 *        a qualified name, could be also null
	 * @return a string array, never null
	 */
	public static String[] splitQualifiedName(String qName)
	{
		String splitQName = qName;

		splitQName = normalizeQualifiedName(qName);
		if (splitQName == null || splitQName.equals(QNAME_SEPARATOR_STR))
		{
			return new String[0];
		}
		// ignore the first char, is always "/"
		return splitQName.substring(1).split(QNAME_SEPARATOR_STR);
	}

	/**
	 * Make the <code>childrenQName</code> relative to <code>parentQName</code> or null if the
	 * <code>childrenQName</code> is not a children of <code>parentQName</code>
	 *
	 * @param parentQName
	 *        a valid qualified name, should not be null
	 * @param childrenQName
	 *        a valid qualified name, should not be null
	 * @return a relative children QName or null if cannot be made
	 */
	public static String getRelativeQName(String parentQName, String childrenQName)
	{
		Assert.isNotNull(parentQName);
		Assert.isNotNull(childrenQName);
		String normalizedParentQName = normalizeQualifiedName(parentQName);
		String normalizedChildrenQName = normalizeQualifiedName(childrenQName);
		if (normalizedChildrenQName.equals(normalizedParentQName))
		{
			return QNAME_SEPARATOR_STR;
		}
		else
		{
			normalizedParentQName = normalizedParentQName + QNAME_SEPARATOR_STR;
			if (normalizedChildrenQName.startsWith(normalizedParentQName))
			{
				return normalizedChildrenQName.substring(normalizedParentQName.length() - 1);
			}
			else
			{
				// not a children
				return null;
			}
		}
	}

	/**
	 * Return a path for the child that is relative to the parent or null if the path cannot be made
	 *
	 * @param parent
	 * @param child
	 * @return relative qualified name
	 */
	public static String getRelativeQName(GIdentifiable parent, GIdentifiable child)
	{
		Assert.isNotNull(parent);
		Assert.isNotNull(child);

		if (parent == child)
		{
			return QNAME_SEPARATOR_STR;
		}

		String relativeQName = null;

		List<String> result = new ArrayList<String>();
		EObject obj = child;
		while (obj != null)
		{
			if (obj instanceof GIdentifiable)
			{
				String name = ((GIdentifiable) obj).gGetShortName();
				if (name == null)
				{
					break;
				}
				result.add(name);
			}
			obj = obj.eContainer();
			if (obj == parent)
			{
				// we found it, stop now
				StringBuilder sb = new StringBuilder(QNAME_SEPARATOR);
				for (int i = result.size() - 1; i >= 0; i--)
				{
					sb.append(result.get(i));
					if (i > 0)
					{
						sb.append(QNAME_SEPARATOR);
					}
				}
				relativeQName = sb.toString();
				break;
			}
		}
		// if we reach this point we don't found the parent
		return relativeQName;
	}

	/**
	 * Locate inside <code>parent</code> an {@link GIdentifiable} that have a relative qualified name identical with
	 * <code>relativeQName</code>. Return null if there is no such object or the object is not an identifiable.
	 *
	 * @param parent
	 *        the GIdentifiable parent
	 * @param relativeQName
	 *        the relative qualified name
	 * @return the relative children of parent or null if no such children can be found
	 */
	public static GIdentifiable getRelativeIdentifiables(GIdentifiable parent, String relativeQName)
	{
		Assert.isNotNull(parent);
		Assert.isNotNull(relativeQName);
		String[] path = splitQualifiedName(normalizeQualifiedName(relativeQName));

		GIdentifiable children = null;
		for (int i = 0; i < path.length; i++)
		{
			EList<EReference> references = parent.eClass().getEAllReferences();
			children = null;
			for (EReference childrenRef: references)
			{
				if (!childrenRef.isContainment())
				{
					// go only through containments
					continue;
				}
				Object object = parent.eGet(childrenRef, true);
				if (object instanceof GIdentifiable && path[i].equals(((GIdentifiable) object).gGetShortName()))
				{
					// children located
					children = (GIdentifiable) object;
				}
				else if (object instanceof EList<?>)
				{
					children = findChildrenObjectInList(object, path[i]);
				}
				if (children != null)
				{
					// located, skip the rest of references
					break;
				}
			}
			if (children == null)
			{
				// not found, return
				break;
			}
			else
			{
				// go deeper
				parent = children; // SUPPRESS CHECKSTYLE recurse
			}
		}
		return children;
	}

	private static GIdentifiable findChildrenObjectInList(Object object, String path)
	{
		GIdentifiable children = null;

		for (Object childrenObject: (EList<?>) object)
		{
			if (childrenObject instanceof GIdentifiable
				&& path.equals(((GIdentifiable) childrenObject).gGetShortName()))
			{
				// children located
				children = (GIdentifiable) childrenObject;
				break;
			}
		}
		return children;
	}

	/**
	 * Return the index to which an identifiable named <code>identName</code> exists inside the list or -1 if there is
	 * none
	 *
	 * @param list
	 *        a list of identifiables
	 * @param identName
	 *        the identifiable name
	 * @return -1 if not found or the list index
	 */
	public static int getIdentifiableIndex(List<? extends GIdentifiable> list, String identName)
	{
		// prefer speed with a small penalty to the class size
		if (identName == null)
		{
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i).gGetShortName() == null)
				{
					return i;
				}
			}
		}
		else
		{
			for (int i = 0; i < list.size(); i++)
			{
				if (identName.equals(list.get(i).gGetShortName()))
				{
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Gets the {@link AutosarReleaseDescriptor} for the given project.
	 *
	 * @param project
	 *        the {@link IProject} for which to retrieve the release descriptor
	 * @return the release descriptor
	 */
	public static AutosarReleaseDescriptor getAutosarRelease(IProject project)
	{
		return IAutosarWorkspacePreferences.AUTOSAR_RELEASE != null
			? IAutosarWorkspacePreferences.AUTOSAR_RELEASE.get(project)
			: null;
	}

	/**
	 * Return the release number of the <code>descriptor</code> as a 3 digit number. It is made by the formula:
	 *
	 * <pre>
	 * 100 * major + 10 * minor + 1 * revision
	 * </pre>
	 *
	 * @param descriptor
	 *        a valid release descriptor, if null a {@link NullPointerException} will be throw
	 * @return the release as a 3 digit number, eg: 315, 402 or 403
	 */
	public static int getAutosarReleaseOrdinal(AutosarReleaseDescriptor descriptor)
	{
		AutosarMetaModelVersionData data = descriptor.getAutosarVersionData();
		return data.getCanonicalVersionNumber();
	}

	/**
	 * Verifies if there is an {@linkplain TransactionalEditingDomain editing domain} associated with the
	 * {@linkplain IResource resource}s contained in the given {@link IProject}.
	 *
	 * @param project
	 *        The {@linkplain IProject project} containing the {@linkplain IResource resource}s for which the
	 *        {@linkplain TransactionalEditingDomain editing domain}s are to be returned.
	 * @return The {@linkplain TransactionalEditingDomain editing domain}s associated with the {@linkplain IResource
	 *         resource}s in the specified {@linkplain IProject project}.
	 */
	public static boolean haveEditingDomain(IProject project)
	{
		// assure that the project is loaded at this point
		// ModelLoadManager.INSTANCE.loadProject(project, false, false, null);
		List<TransactionalEditingDomain> domains = WorkspaceEditingDomainManager.INSTANCE.getEditingDomainMapping().getEditingDomains(
			project);
		return domains != null && domains.size() > 0;
	}

	/**
	 * Return the project where the <code>object</code> is located. The object can be an IResource or an EMF object,
	 * including EMF resource object
	 *
	 * @param object
	 *        a suitable object
	 * @return the corresponding project or <code>null</code> if the object is not recognized or not part of a project
	 * @see EcorePlatformUtil#getFile(Object)
	 */
	public static IProject getProject(Object object)
	{
		if (object instanceof IContainer)
		{
			return ((IContainer) object).getProject();
		}
		else
		{
			IFile file = EcorePlatformUtil.getFile(object);
			if (file != null)
			{
				return file.getProject();
			}
			else
			{
				return null;
			}
		}
	}

	/**
	 * Return the project where the <code>objects</code> are located (assumed unique). The objects can be IResource or
	 * an EMF objects, including EMF resource objects.
	 *
	 * @param objects
	 *        a collection of objects
	 * @return the corresponding project or <code>null</code> if none of the objects are recognized or part of a project
	 * @see EcorePlatformUtil#getFile(Object)
	 */
	public static IProject getProjectForObjects(Collection<EObject> objects)
	{
		if (objects != null)
		{
			if (!objects.isEmpty())
			{
				for (Object object: objects)
				{
					IProject project = getProject(object);
					if (project != null)
					{
						return project;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Extract the name of specified element from its <code>MetaData</code> annotation and cut 'Template' from the end
	 * of the name if exists. If second parameter is <code>true</code>, then convert the element name into a human
	 * readable element (e.g. 'SWComponentPrototype' is converted to 'SW Component Prototype')
	 *
	 * @param element
	 *        The annotated element to be used to extract information
	 * @param humanReadable
	 *        if <code>true</code>, return the human readable name of specified element, if <code>false</code> return
	 *        the 'MetaData' string
	 * @return The (human readable) name of specified element.
	 */
	public static String extractAnnotationName(ENamedElement element, boolean humanReadable)
	{
		// extract 'MetaData'annotation
		String name = null;
		EAnnotation annotation = element.getEAnnotation(ANN_METADATA);
		if (annotation != null)
		{
			name = annotation.getDetails().get(ANN_NAME);
		}
		else
		{
			name = element.getName();
		}

		// cut 'Template' from the end of the name
		int index = name.lastIndexOf(TEMPLATE);
		if (index != -1)
		{
			name = name.substring(0, index);
		}

		// if requested, convert to human readable name
		if (humanReadable)
		{
			return StringUtils.humaniseCamelCase(name);
		}
		else
		{
			return name;
		}
	}

	/**
	 * Look into the annotation named <code>annotationName</code> for the detail named <code>detailName</code> of the
	 * <code>element</code> and return it.
	 *
	 * @param element
	 * @param annotationName
	 * @param detailName
	 * @return the value of the annotation or null if there is no such annotation
	 */
	public static String getAnnotationDetail(EModelElement element, String annotationName, String detailName)
	{
		EAnnotation annotation = element.getEAnnotation(annotationName);
		if (annotation != null)
		{
			return annotation.getDetails().get(detailName);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Return the editing domain associated with a particular project
	 *
	 * @param project
	 * @return the associated editing domain
	 */
	public static TransactionalEditingDomain getEditingDomain(IProject project)
	{
		return WorkspaceEditingDomainUtil.getEditingDomain(project, getAutosarRelease(project));
	}

	/**
	 * Return the editing domain associated with the given object
	 *
	 * @param object
	 * @return the associated editing domain
	 */
	public static TransactionalEditingDomain getEditingDomain(Object object)
	{
		if (object instanceof IAltEditingDomainProvider)
		{
			return (TransactionalEditingDomain) ((IEditingDomainProvider) object).getEditingDomain();
		}
		else
		{
			return WorkspaceEditingDomainUtil.getEditingDomain(object);
		}
	}

	/**
	 * Return a modifiable list of elements of type <code>subType</code> from the list <code>ownerList</code>.<br/>
	 * Usage example:
	 *
	 * <pre>
	 * GARPackage arPack = ...
	 * EList&lt;GModuleConfiguration&gt; modules =
	 * 	ModelUtils.getFilteredList(arPack.gGetElements(), GModuleConfiguration.class);
	 * </pre>
	 *
	 * @param <T>
	 * @param ownerList
	 * @param subType
	 * @return
	 */
	public static <T> EList<T> getFilteredList(Class<T> subType, EList<? super T> ownerList,
		IModelChangeStampProvider changeProvider)
	{
		return new DelegatingSubEList<T>(subType, ownerList, changeProvider);
	}

	/**
	 * Return an array of Strings representing context feature names
	 *
	 * @param value
	 *        This value is taken from an EClass's TaggedValues annotation <br>
	 *        It contains space separated tokens, from which context feature names can be computed. If the token is
	 *        suffixed by "*" or "+" , to obtain the name of that feature, the suffix has to be replaces by "s". If the
	 *        token is suffixed by "?", the suffix will be removed in order to obtain the correct name of the feature.
	 *
	 * @return
	 */
	public static String[] processSysInstRefAnnoDetailValue(String value)
	{
		StringTokenizer st = new StringTokenizer(value, " "); //$NON-NLS-1$
		String[] array = new String[st.countTokens()];
		int index = 0;
		while (st.hasMoreTokens())
		{
			String nextToken = st.nextToken();
			String item = nextToken.trim();
			if (item.endsWith("*") || item.endsWith("+")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				// replace "*" with "s"
				item = item.substring(0, item.length() - 1);
				item += "s"; //$NON-NLS-1$
			}
			// remove trailing "+" or "?"
			else if (item.endsWith("+") || item.endsWith("?")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				item = item.substring(0, item.length() - 1);
			}

			array[index++] = item;
		}

		return array;
	}

	/**
	 * Compute an unique Short Name for an Identifiable instance. It receive the parent {@link EObject} under which the
	 * new entity will be created, and a suggested value for the Short Name. If suggested Short Name is
	 * <code>null</code>, then a default value is used.<br/>
	 * The method check that the parent does not contains any children having the same suggested Short Name, and if yes,
	 * concatenate an index to the suggested short name value.
	 *
	 * @param parent
	 *        the parent {@link EObject} instance
	 * @param suggestedShortName
	 *        the suggested name value
	 * @return A short name string value that is unique through the children of specified parent.
	 */
	public static String computeUniqueChildShortName(EObject parent, String suggestedShortName)
	{
		String resultShortName = (suggestedShortName == null) ? "NewElement" : suggestedShortName; //$NON-NLS-1$
		if (parent != null)
		{
			List<String> childNames = new ArrayList<String>();
			EList<EObject> contents = parent.eContents();
			for (EObject eObject: contents)
			{
				if (eObject instanceof GIdentifiable)
				{
					childNames.add(((GIdentifiable) eObject).gGetShortName());
				}
			}

			String tmpString = resultShortName;
			int index = 1;
			while (childNames.contains(tmpString))
			{
				// uidt1392:
				// Fixed according to what I believe was author's intention
				tmpString = resultShortName + String.valueOf(index);
				// tmpString += resultShortName + String.valueOf(index);
				index++;
			}
			resultShortName = tmpString;
		}
		return resultShortName;
	}

	/**
	 * Return true if the class is a generic class
	 *
	 * @param clz
	 * @return
	 */
	public static boolean isGenericClass(EClass clz)
	{
		EPackage ePackage = clz.getEPackage();
		if (ePackage != null)
		{
			String nsURI = ePackage.getNsURI();
			if (nsURI != null)
			{
				return nsURI.startsWith(GENERIC_NSURI_PREFIX);
			}
		}
		return false;
	}

	/**
	 * Return true if the class is a model class
	 *
	 * @param clz
	 * @return
	 */
	public static boolean isModelClass(EClass clz)
	{
		EPackage ePackage = clz.getEPackage();
		if (ePackage != null)
		{
			String nsURI = ePackage.getNsURI();
			if (nsURI != null)
			{
				return nsURI.startsWith(AUTOSAR_NSURI_PREFIX);
			}
		}
		return false;
	}

	/**
	 * The returned string will have the following format:<br>
	 * PREFIX@PROJECT_NAME:QNAME::[TYPE]<br>
	 *
	 * Where<br>
	 * <b>PREFIX</b> is "ar" or "pm"<br>
	 *
	 * <b>PROJECT_NAME</b> is the project name where the object is located <br>
	 *
	 * <b>QNAME</b> is the qualified name as returned by
	 * eu.cessar.ct.core.mms.MetaModelUtils.getAbsoluteQualifiedName(EObject) for a concrete or splitted AUTOSAR object
	 * <br>
	 * <b>TYPE</b> is the name of the EClass or the name of the definition in case of PM object
	 *
	 * @param value
	 * @return
	 */
	public static String getURIForLogging(EObject value)
	{
		String delimiter = ":"; //$NON-NLS-1$
		String prefix = "ar"; //$NON-NLS-1$
		String qName;
		String type;
		String projectName = "No project found"; //$NON-NLS-1$

		EObject element = value;
		if (value == null)
		{
			return "null"; //$NON-NLS-1$
		}

		if (value instanceof IPMElement)
		{
			element = getEObjectFromPMWrapper((IPMElement) value);
			prefix = "pm"; //$NON-NLS-1$
		}
		if (element == null)
		{
			return value.toString();
		}

		IFile definingFile = ModelUtils.getDefiningFile(element);
		if (definingFile != null)
		{
			IProject project = definingFile.getProject();
			if (project != null)
			{
				projectName = project.getName();
			}
		}

		qName = ModelUtils.getAbsoluteQualifiedName(element);
		type = "[" + getTypeOfElement(element) + "]"; //$NON-NLS-1$//$NON-NLS-2$

		StringBuilder result = new StringBuilder();
		result.append(prefix);
		result.append("@"); //$NON-NLS-1$
		result.append(projectName);
		result.append(delimiter);
		result.append(qName);
		result.append(delimiter);
		result.append(delimiter);
		result.append(type);

		return result.toString();
	}

	/**
	 * Will return the corresponding eObject that the given value wraps.<br>
	 * null for IPresentationModel
	 *
	 * @param value
	 * @return
	 */
	private static EObject getEObjectFromPMWrapper(IPMElement value)
	{
		EObject result = null;

		if (value instanceof IPMInstanceRef)
		{
			result = getFirstElement(PMUtils.getInstanceRef((IPMInstanceRef) value));
		}
		else
		{
			if (value instanceof IPMContainer)
			{
				result = getFirstElement(PMUtils.getContainers(((IPMContainer) value)));
			}
			else
			{
				if (value instanceof IPMModuleConfiguration)
				{
					result = getFirstElement(PMUtils.getModuleConfigurations((IPMModuleConfiguration) value));
				}
				else
				{
					if (value instanceof IPMPackage)
					{
						result = getFirstElement(PMUtils.getPackages((IPMPackage) value));
					}
					else
					{
						if (value instanceof IPresentationModel)
						{ // CHECKSTYLE IGNORE check FOR NEXT 2 LINES
							// TODO what to do? return null
						}
					}
				}

			}
		}
		return result;
	}

	/**
	 * @param candidateList
	 * @return
	 */
	private static EObject getFirstElement(List<? extends EObject> candidateList)
	{
		EObject result = null;
		if (candidateList != null && !candidateList.isEmpty())
		{
			result = candidateList.get(0);
		}

		return result;
	}

	/**
	 * Returns the type of the given element, <br>
	 * the qName of the definition for Ecuc side <br>
	 * or the eClass.getInstanceClassName() for system side<br>
	 * 'null' if null is passed
	 *
	 * @param element
	 * @return
	 */
	public static String getTypeOfElement(EObject element)
	{
		String aux = ""; //$NON-NLS-1$
		if (element == null)
		{
			return "null"; //$NON-NLS-1$
		}
		if (element instanceof GModuleConfiguration)
		{
			GModuleDef definition = ((GModuleConfiguration) element).gGetDefinition();
			if (definition != null && definition.gGetShortName() != null)
			{
				aux = MetaModelUtils.getAbsoluteQualifiedName(definition);
			}
			else
			{
				aux = "null"; //$NON-NLS-1$
			}
		}
		else if (element instanceof GContainer)
		{
			GContainerDef definition = ((GContainer) element).gGetDefinition();
			if (definition != null && definition.gGetShortName() != null)
			{
				aux = definition.gGetShortName();
			}
			else
			{
				aux = "null"; //$NON-NLS-1$
			}
		}
		else
		{
			EClass type = element.eClass();
			aux = type.getInstanceClassName();
		}
		if (aux != null)
		{
			aux = aux.substring(aux.lastIndexOf(".") + 1); //$NON-NLS-1$
		}
		else
		{
			aux = ModelUtils.getAbsoluteQualifiedName(element);
		}
		return aux;
	}

	/**
	 * Return the type of compatibility mode a autosar release will require,
	 *
	 * @param descriptor
	 *
	 * @return the compatibility mode, never null
	 */
	public static ECompatibilityMode getDesiredCompatModel(AutosarReleaseDescriptor descriptor)
	{
		int release = getAutosarReleaseOrdinal(descriptor);
		if (release >= 310 && release < 320)
		{
			return ECompatibilityMode.FULL;
		}
		else
		{
			return ECompatibilityMode.NONE;
		}
	}

	/**
	 * Return the value of the feature from the EObject.
	 *
	 * @param object
	 *        the EMF Object, it's an error to be null
	 * @param featureName
	 *        the feature name, if the EClass of the EObject does not have such a feature an unchecked exception will be
	 *        throw.
	 * @return the value, could be null. Will throw error if object is null or feature name is wrong.
	 */
	public static Object eGet(EObject object, String featureName)
	{
		if (object == null)
		{
			throw new IllegalArgumentException("Object shall not be null"); //$NON-NLS-1$
		}
		EStructuralFeature feature = object.eClass().getEStructuralFeature(featureName);
		if (feature == null)
		{
			throw new IllegalArgumentException(object.getClass().getName() + " does not have a feature named " //$NON-NLS-1$
				+ featureName);
		}
		return object.eGet(feature);
	}

	/**
	 * Return true if the value of the feature from the EObject is set.
	 *
	 * @param object
	 *        the EMF Object, it's an error to be null
	 * @param featureName
	 *        the feature name, if the EClass of the EObject does not have such a feature an unchecked exception will be
	 *        throw.
	 * @return true or false if the value is set or not. Will throw error if object is null or feature name is wrong.
	 */
	public static boolean eIsSet(EObject object, String featureName)
	{
		if (object == null)
		{
			throw new IllegalArgumentException("Object shall not be null"); //$NON-NLS-1$
		}
		EStructuralFeature feature = object.eClass().getEStructuralFeature(featureName);
		if (feature == null)
		{
			throw new IllegalArgumentException(object.getClass().getName() + " does not have a feature named " //$NON-NLS-1$
				+ featureName);
		}
		return object.eIsSet(feature);
	}

	/**
	 * Gets the AUTOSAR model of the <code>project</code>
	 *
	 * @param project
	 * @return the Autosar model descriptor or null if none is set
	 */
	public static IModelDescriptor getAutosarModelDescriptor(IProject project)
	{
		Collection<IModelDescriptor> models = ModelDescriptorRegistry.INSTANCE.getModels(project);

		for (IModelDescriptor descriptor: models)
		{
			if (descriptor.getMetaModelDescriptor() instanceof AutosarReleaseDescriptor)
			{
				return descriptor;
			}
		}
		return null;
	}

	/**
	 * Safely updates the model inside a transaction allowing the user to choose the changes he wants to do. Any
	 * exception raised during the execution of the runnable is caught and logged.
	 *
	 * @param eDomain
	 *        the Editing domain on which the transactions will do changes
	 * @param runnable
	 *        the modifications that we want the transaction to do
	 * @return the status, {@link Status#OK_STATUS} if the operation succeeded, or an error status wrapping the thrown
	 *         exception.
	 */
	public static IStatus safelyUpdateModel(TransactionalEditingDomain eDomain, Runnable runnable)
	{
		Assert.isNotNull(eDomain);
		Assert.isNotNull(runnable);

		IStatus status = Status.OK_STATUS;
		Exception ex = null;

		try
		{
			WorkspaceTransactionUtil.executeInWriteTransaction(eDomain, runnable, "Updating model"); //$NON-NLS-1$
		}
		catch (OperationCanceledException e)
		{
			ex = e;
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (ExecutionException e)
		{
			ex = e;
			CessarPluginActivator.getDefault().logError(e);
		}

		if (ex != null)
		{
			status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, "Model update failed!", ex); //$NON-NLS-1$
		}

		return status;
	}

	/**
	 * Try to reduce calls to this method, because frequent calls will affect performance. Return the
	 * {@code IGenericModelDependencyLookup} interface associated with the given qualifiers. The interface can be used
	 * to perform a variety of model-aware searches.
	 *
	 * @param classifiers
	 * @return an {@link IGenericModelDependencyLookup}
	 */
	public static IGenericModelDependencyLookup createModelDependencyLookup(Collection<EClassifier> classifiers)
	{
		return ModelDependencyLookup.fromClassifiers(classifiers);
	}

}
