/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 27, 2012 4:34:57 PM </copyright>
 */
package eu.cessar.ct.workspace.model.documentation.generator;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.pm.IPMModuleConfiguration;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.sdk.utils.PMUtils;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.internal.modeldocumentation.Attribute;
import eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef;
import eu.cessar.ct.workspace.internal.modeldocumentation.DefElement;
import eu.cessar.ct.workspace.internal.modeldocumentation.DocElement;
import eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationFactory;
import eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfMultiplicity;
import gautosar.ggenericstructure.ggeneraltemplateclasses.gdocumentation.gtextmodel.glanguagedatamodel.GLOverviewParagraph;
import gautosar.ggenericstructure.ggeneraltemplateclasses.gdocumentation.gtextmodel.gmultilanguagedata.GMultiLanguageOverviewParagraph;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

/**
 * @author uidt2045
 * 
 */
public final class ModelDocumentationGenerator
{
	private ModelDocumentationGenerator()
	{
	}

	/**
	 * Generates documentation for given <code>modules</code> into specified <code>file</code>
	 * 
	 * @param outputFile
	 * @param modules
	 */
	public static void generateDocumentation(File outputFile, Set<GModuleConfiguration> modules)
	{
		if (modules.isEmpty())
		{
			return;
		}
		IProject project = getProject(modules);
		if (project == null)
		{
			CessarPluginActivator.getDefault().logError(
				"There is no project attached to the given module configurations ", modules); //$NON-NLS-1$
			return;
		}
		ICessarTaskManager<IFile> manager = (ICessarTaskManager<IFile>) ExecutionService.createManager(project,
			ExecutionService.TASK_TYPE_PLUGET);
		CessarRuntime.getExecutionSupport().acquireExecutionLoader(manager);

		try
		{
			DocElement root = ModeldocumentationFactory.eINSTANCE.createDocElement();
			root.setShortName("PMRoot"); //$NON-NLS-1$
			for (GModuleConfiguration conf: modules)
			{
				// we should compute the configuration element
				DocElement docElement = computeModuleConfigurationInfo(conf);
				root.getContains().add(docElement);
			}
			putInFile(outputFile, root);
		}
		finally
		{
			CessarRuntime.getExecutionSupport().releaseExecutionLoader(manager);
		}
	}

	/**
	 * @param outputFile
	 * @param defElement
	 * @throws IOException
	 */
	private static void putInFile(File outputFile, final EObject rootElement)
	{
		TransactionalEditingDomain editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();

		String pathName = outputFile.toURI().toString();
		URI tempURI = URI.createURI(pathName, true);

		ResourceSet tempResourceSet = editingDomain.getResourceSet();
		final Resource[] resources = new Resource[1];
		Resource tempResource = tempResourceSet.createResource(tempURI, // contentType);
			XMLResource.OPTION_KEEP_DEFAULT_CONTENT);
		resources[0] = tempResource;

		// Set options
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
		options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);
		options.put(XMLResource.OPTION_SKIP_ESCAPE, Boolean.TRUE);

		try
		{
			try
			{
				WorkspaceTransactionUtil.executeInWriteTransaction(editingDomain, new Runnable()
				{
					public void run()
					{
						resources[0].getContents().add(rootElement);
					}
				}, "add model content to resource"); //$NON-NLS-1$
			}
			catch (org.eclipse.core.commands.ExecutionException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
		catch (OperationCanceledException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		try
		{
			resources[0].save(options);
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * @param moduleConfiguration
	 * @return
	 */
	private static DocElement computeModuleConfigurationInfo(GModuleConfiguration conf)
	{
		IPMModuleConfiguration ipmModuleConfig = PMUtils.getPMModuleConfiguration(conf);
		DocElement docElement = ModeldocumentationFactory.eINSTANCE.createDocElement();
		docElement.setShortName(ipmModuleConfig.getShortName());
		// docElement.setShortName(conf.gGetShortName());

		docElement.setType(MetaModelUtils.getTypeOfElement(conf));
		// set the definition to the configuration element
		DefElement defElement = computeModuleDefinitionInfo(PMUtils.getModuleDefinition(ipmModuleConfig));
		docElement.setInstanceOf(defElement);

		EClass complex = ipmModuleConfig.eClass();
		// compute the possible children
		EList<EReference> eAllReferences = complex.getEAllReferences();
		for (EReference reference: eAllReferences)
		{
			Object object = ipmModuleConfig.eGet(reference);
			if (object == null)
			{
				computeNotCreatedContainer(docElement, reference);
			}
			else
			{
				if (object instanceof IPMContainer)
				{
					computeContainer(docElement, (IPMContainer) object);
				}
				else
				{
					if (object instanceof List<?>)
					{
						if (((List<?>) object).isEmpty())
						{
							computeNotCreatedContainer(docElement, reference);
						}
						else
						{
							Iterator<?> iterator = ((List<?>) object).iterator();
							while (iterator.hasNext())
							{
								Object next = iterator.next();
								if (next == null)
								{
									computeNotCreatedContainer(docElement, reference);
								}
								if (next instanceof IPMContainer)
								{
									computeContainer(docElement, (IPMContainer) next);
								}
							}
						}
					}
				}
			}
		}
		return docElement;
	}

	private static void computeNotCreatedContainer(DocElement root, EReference reference)
	{
		// a reference that is not set,(probably lower multiplicity 0)
		// but still need to compute the info from the definition
		DocElement notInstantiated = ModeldocumentationFactory.eINSTANCE.createDocElement();
		notInstantiated.setIsInstance(false);

		EList<EClass> eSuperTypes = reference.getEReferenceType().getESuperTypes();

		if (eSuperTypes != null && !eSuperTypes.isEmpty())
		{
			notInstantiated.setType(eSuperTypes.get(0).getName());
		}

		notInstantiated.setType(ModelUtils.getAbsoluteQualifiedName(reference));

		DefElement defElement2 = ModeldocumentationFactory.eINSTANCE.createDefElement();
		defElement2.setShortName(reference.getName());
		Multiplicity multiplicity = ModeldocumentationFactory.eINSTANCE.createMultiplicity();

		multiplicity.setLower(String.valueOf(reference.getLowerBound()));
		multiplicity.setUpper(String.valueOf(reference.getUpperBound()));

		defElement2.setMultiplicity(multiplicity);

		notInstantiated.setInstanceOf(defElement2);
		root.getContains().add(notInstantiated);
	}

	/**
	 * @param docElement
	 * @param object
	 */
	private static void computeContainer(DocElement docElement, IPMContainer container)
	{
		DocElement containerElement = ModeldocumentationFactory.eINSTANCE.createDocElement();
		List<GContainer> gContainers = PMUtils.getContainers(container);
		if (!gContainers.isEmpty())
		{
			GContainer gContainer = PMUtils.getContainers(container).get(0);
			containerElement.setShortName(container.getShortName());

			containerElement.setIsInstance(true);
			containerElement.setType(MetaModelUtils.getTypeOfElement(gContainer));
			// compute the container def info
			DefElement defContainer = computeContainerDefInfo(PMUtils.getContainerDefinition(container));
			containerElement.setInstanceOf(defContainer);

			EClass containerClass = container.eClass();

			// compute the attributes
			EList<EAttribute> eAllAttributes = containerClass.getEAllAttributes();
			for (EAttribute attr: eAllAttributes)
			{
				// the value
				Attribute attribute = ModeldocumentationFactory.eINSTANCE.createAttribute();
				attribute.setType(attr.getEAttributeType().getName());
				attribute.setName(attr.getName());
				Object object = container.eGet(attr);
				attribute.setValue(String.valueOf(object));
				// compute the attribute Def
				GContainerDef containerDefinition = PMUtils.getContainerDefinition(container);
				GIdentifiable eGet = ModelUtils.getEObjectWithRelativeName(containerDefinition, attr.getName());
				if (eGet != null)
				{
					AttributeDef attributeDef = ModeldocumentationFactory.eINSTANCE.createAttributeDef();
					attributeDef.setMultiplicity(computeMultiplicity((GParamConfMultiplicity) eGet));
					attributeDef.setName(ModelUtils.getAbsoluteQualifiedName(eGet));
					attributeDef.setTypeOfValue(MetaModelUtils.getTypeOfElement(eGet));

					attribute.setInstanceOf(attributeDef);
				}
				containerElement.getAttributes().add(attribute);
			}

			// compute the possible children
			for (EReference ref: containerClass.getEAllReferences())
			{
				// if an instance does not exist object is null
				Object object = container.eGet(ref);
				if (object == null)
				{
					computeNotCreatedContainer(containerElement, ref);
				}
				else
				{
					if (object instanceof IPMContainer)
					{
						computeContainer(containerElement, (IPMContainer) object);
					}
					else
					{
						if (object instanceof List<?>)
						{
							if (((List<?>) object).isEmpty())
							{
								computeNotCreatedContainer(containerElement, ref);
							}
							else
							{
								Iterator<?> iterator = ((EList<?>) object).iterator();
								while (iterator.hasNext())
								{
									Object next = iterator.next();
									if (next instanceof IPMContainer)
									{
										computeContainer(containerElement, (IPMContainer) next);
									}
									else
									{
										if (next == null)
										{
											computeNotCreatedContainer(containerElement, ref);
										}
									}
								}
							}
						}
					}
				}
			}
			docElement.getContains().add(containerElement);
		}
	}

	/**
	 * @param defElement
	 * @param gGetDesc
	 */
	private static void setDescDefElement(DefElement defElement, GMultiLanguageOverviewParagraph gGetDesc)
	{

		if (gGetDesc != null)
		{
			defElement.setDescription(getDescription(gGetDesc).trim());
		}
		else
		{
			defElement.setDescription(""); //$NON-NLS-1$
		}
	}

	private static String getDescription(GMultiLanguageOverviewParagraph desc)
	{
		EList<GLOverviewParagraph> l2s = desc.gGetL2s();
		StringBuilder strBuilder = new StringBuilder();
		for (GLOverviewParagraph l2: l2s)
		{
			strBuilder.append(l2.gGetMixedText());
		}
		return strBuilder.toString();
	}

	/**
	 * @param moduleDefinition
	 * @return
	 */
	private static DefElement computeModuleDefinitionInfo(GModuleDef moduleDefinition)
	{
		DefElement defElement = ModeldocumentationFactory.eINSTANCE.createDefElement();
		defElement.setShortName(moduleDefinition.gGetShortName());

		Multiplicity multiplicity = computeMultiplicity(moduleDefinition);
		defElement.setMultiplicity(multiplicity);
		GMultiLanguageOverviewParagraph gGetDesc = moduleDefinition.gGetDesc();
		setDescDefElement(defElement, gGetDesc);
		return defElement;
	}

	private static DefElement computeContainerDefInfo(GContainerDef containerDefinition)
	{
		DefElement defElement = ModeldocumentationFactory.eINSTANCE.createDefElement();
		defElement.setShortName(containerDefinition.gGetShortName());

		Multiplicity multiplicity = computeMultiplicity(containerDefinition);
		defElement.setMultiplicity(multiplicity);

		GMultiLanguageOverviewParagraph gGetDesc = containerDefinition.gGetDesc();
		setDescDefElement(defElement, gGetDesc);
		// computeAttributeAndParamDefs(defElement, containerDefinition);

		return defElement;
	}

	/**
	 * @param modules
	 * @return the project corresponding to the first module configuration from the given set
	 */
	private static IProject getProject(Set<GModuleConfiguration> modules)
	{
		IProject proj = null;
		for (GModuleConfiguration m: modules)
		{
			if (proj == null)
			{
				proj = MetaModelUtils.getProject(m);
			}
			if (proj != MetaModelUtils.getProject(m))
			{
				CessarPluginActivator.getDefault().logError("The modules must be from the same project", modules); //$NON-NLS-1$
			}
		}
		return proj;
	}

	/**
	 * @param element
	 * @return
	 */
	private static Multiplicity computeMultiplicity(GParamConfMultiplicity element)
	{
		Multiplicity multiplicity = ModeldocumentationFactory.eINSTANCE.createMultiplicity();
		multiplicity.setLower(element.gGetLowerMultiplicityAsString());

		if (element.gGetUpperMultiplicityInfinite())
		{
			multiplicity.setUpper("*"); //$NON-NLS-1$
		}
		else
		{
			multiplicity.setUpper(element.gGetUpperMultiplicityAsString());
		}
		return multiplicity;
	}

}
