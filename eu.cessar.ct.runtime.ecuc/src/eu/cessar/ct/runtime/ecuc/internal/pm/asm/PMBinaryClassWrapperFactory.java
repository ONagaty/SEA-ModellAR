/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 26, 2010 11:48:17 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.asm;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;

/**
 * 
 */
public class PMBinaryClassWrapperFactory implements IPMBinaryGeneratorSettings
{

	/**
	 * if true generics will be used on lists
	 */
	private boolean useGenerics = true;

	private boolean useNewEnum = true;

	private Map<Class<? extends Object>, String> datatypeMapping = new HashMap<Class<? extends Object>, String>();

	private boolean useTCaseFeatureName = true;

	/**
	 * 
	 */
	public PMBinaryClassWrapperFactory()
	{
		datatypeMapping.put(Boolean.TYPE, "java.lang.Boolean"); //$NON-NLS-1$
		datatypeMapping.put(Boolean.class, "java.lang.Boolean"); //$NON-NLS-1$
		datatypeMapping.put(Byte.TYPE, "java.lang.Byte"); //$NON-NLS-1$
		datatypeMapping.put(Byte.class, "java.lang.Byte"); //$NON-NLS-1$
		datatypeMapping.put(Character.TYPE, "java.lang.Character"); //$NON-NLS-1$
		datatypeMapping.put(Character.class, "java.lang.Character"); //$NON-NLS-1$
		datatypeMapping.put(Double.TYPE, "java.lang.Double"); //$NON-NLS-1$
		datatypeMapping.put(Double.class, "java.lang.Double"); //$NON-NLS-1$
		datatypeMapping.put(Float.TYPE, "java.lang.Float"); //$NON-NLS-1$
		datatypeMapping.put(Float.class, "java.lang.Float"); //$NON-NLS-1$
		datatypeMapping.put(Integer.TYPE, "java.lang.Integer"); //$NON-NLS-1$
		datatypeMapping.put(Integer.class, "java.lang.Integer"); //$NON-NLS-1$
		datatypeMapping.put(Long.TYPE, "java.lang.Long"); //$NON-NLS-1$
		datatypeMapping.put(Long.class, "java.lang.Long"); //$NON-NLS-1$
		datatypeMapping.put(Short.TYPE, "java.lang.Short"); //$NON-NLS-1$
		datatypeMapping.put(Short.class, "java.lang.Short"); //$NON-NLS-1$
	}

	/**
	 * @param pack
	 * @return
	 */
	public IPMBinaryClassWrapper createEPackageImplBinWrapper(EPackage pack)
	{
		return new EPackageGenerator.ClassBinWrapper(this, pack);
	}

	/**
	 * @param pack
	 * @return
	 */
	public IPMBinaryClassWrapper createEPackageBinWrapper(EPackage pack)
	{
		return new EPackageGenerator.InterfaceBinWrapper(this, pack);
	}

	/**
	 * @param pack
	 * @return
	 */
	public IPMBinaryClassWrapper createEPackageLiteralsBinWrapper(EPackage pack)
	{
		return new EPackageGenerator.InterfaceLiteralsBinWrapper(this, pack);
	}

	/**
	 * @param pack
	 * @return
	 */
	public IPMBinaryClassWrapper createEFactoryImplBinWrapper(EPackage pack)
	{
		return new EFactoryGenerator.ClassBinWrapper(this, pack);
	}

	/**
	 * @param pack
	 * @return
	 */
	public IPMBinaryClassWrapper createEFactoryBinWrapper(EPackage pack)
	{
		return new EFactoryGenerator.InterfaceBinWrapper(this, pack);
	}

	/**
	 * @param clz
	 * @return
	 */
	public IPMBinaryClassWrapper createEClassImplBinWrapper(EClass clz)
	{
		return new EClassGenerator.ClassBinWrapper(this, clz);
	}

	/**
	 * @param clz
	 * @return
	 */
	public IPMBinaryClassWrapper createEClassBinWrapper(EClass clz)
	{
		return new EClassGenerator.InterfaceBinWrapper(this, clz);
	}

	/**
	 * @param en
	 * @return
	 */
	public IPMBinaryClassWrapper createEEnumImplBinWrapper(EEnum en)
	{
		return new EEnumGenerator.EnumBinWrapper(this, en);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.asm.IPMBinaryGeneratorSettings#isUsingGenerics()
	 */
	public boolean isUsingGenerics()
	{
		return useGenerics;
	}

	public void setUsingGenerics(boolean useGenerics)
	{
		this.useGenerics = useGenerics;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.asm.IPMBinaryGeneratorSettings#getEDataTypeClass(org.eclipse.emf.ecore.EDataType)
	 */
	public String getEDataTypeClass(EDataType dataType)
	{
		Class<?> iClass = dataType.getInstanceClass();
		if (datatypeMapping.containsKey(iClass))
		{
			return datatypeMapping.get(iClass);
		}
		else
		{
			return iClass.getName();
		}
	}

	/**
	 * @param dtClass
	 * @param mapping
	 */
	public void setEDataTypeClass(Class<? extends Object> dtClass, String mapping)
	{
		if (mapping == null)
		{
			datatypeMapping.remove(dtClass);
		}
		else
		{
			datatypeMapping.put(dtClass, mapping);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.asm.IPMBinaryGeneratorSettings#isUsingNewEnum()
	 */
	public boolean isUsingNewEnum()
	{
		return useNewEnum;
	}

	/**
	 * 
	 */
	public void setUsingNewEnum(boolean useNewEnum)
	{
		this.useNewEnum = useNewEnum;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.asm.IPMBinaryGeneratorSettings#isUsingTitleCaseFeatureNames()
	 */
	public boolean isUsingTitleCaseFeatureNames()
	{
		return useTCaseFeatureName;
	}

	public void setUsingTitleCaseFeatureNames(boolean useTCaseFeature)
	{
		useTCaseFeatureName = useTCaseFeature;
	}
}
