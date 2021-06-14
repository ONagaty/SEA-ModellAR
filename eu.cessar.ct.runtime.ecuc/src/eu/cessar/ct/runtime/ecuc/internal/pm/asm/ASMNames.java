/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 19, 2010 7:00:30 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.asm;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import eu.cessar.ct.core.platform.util.StringUtils;

/**
 * 
 */
public class ASMNames implements Opcodes, Constants
{
	private ASMNames()
	{
		// do nothing
	}

	/**
	 * @param pack
	 * @return
	 */
	public static String getEClassInterfaceCName(String separator, EClass clz)
	{
		if (clz.getInstanceClass() != null)
		{
			return clz.getInstanceClassName().replaceAll("\\.", separator);
		}
		else
		{
			return ASMNames.getPackageQName(separator, clz.getEPackage()) + separator
				+ clz.getName();
		}
	}

	/**
	 * @param clz
	 * @return
	 */
	public static String getEClassInterfaceCName(EClass clz)
	{
		return getEClassInterfaceCName(SLASH, clz);
	}

	/**
	 * @param pack
	 * @return
	 */
	public static String getEClassClassCName(String separator, EClass clz)
	{
		return ASMNames.getPackageQName(separator, clz.getEPackage()) + separator + PACKAGE_IMPL
			+ separator + clz.getName() + SUFFIX_IMPL;
	}

	/**
	 * @param clz
	 * @return
	 */
	public static String getEClassClassCName(EClass clz)
	{
		return getEClassClassCName(SLASH, clz);
	}

	/**
	 * @param separator
	 * @param type
	 * @param settings
	 * @return
	 */
	@SuppressWarnings("nls")
	public static String getEClassifierCName(String separator, EClassifier type,
		IPMBinaryGeneratorSettings settings)
	{
		if (type instanceof EClass)
		{
			return getEClassInterfaceCName(separator, (EClass) type);
		}
		else if (type instanceof EEnum)
		{
			return ASMNames.getPackageQName(separator, type.getEPackage()) + separator
				+ type.getName();
		}
		else
		{
			String result = settings.getEDataTypeClass((EDataType) type);
			return result.replace(".", separator);
		}
	}

	public static String getEClassifierCName(EClassifier type, IPMBinaryGeneratorSettings settings)
	{
		return getEClassifierCName(SLASH, type, settings);
	}

	/**
	 * @param separator
	 * @param eEnum
	 * @return
	 */
	public static String getEEnumCName(String separator, EEnum eEnum)
	{
		return ASMNames.getPackageQName(separator, eEnum.getEPackage()) + separator
			+ eEnum.getName();
	}

	/**
	 * @param eEnum
	 * @return
	 */
	public static String getEEnumCName(EEnum eEnum)
	{
		return getEEnumCName(SLASH, eEnum);
	}

	/**
	 * @param pack
	 * @return
	 */
	public static String getEFactoryInterfaceCName(String separator, EPackage pack)
	{
		return ASMNames.getPackageQName(separator, pack) + separator
			+ StringUtils.toTitleCase(pack.getName()) + SUFFIX_FACTORY;
	}

	/**
	 * @param pack
	 * @return
	 */
	public static String getEFactoryInterfaceCName(EPackage pack)
	{
		return getEFactoryInterfaceCName(SLASH, pack);
	}

	/**
	 * @param pack
	 * @return
	 */
	public static String getEFactoryClassCName(String separator, EPackage pack)
	{
		return ASMNames.getPackageQName(separator, pack) + separator + PACKAGE_IMPL + separator
			+ StringUtils.toTitleCase(pack.getName()) + SUFFIX_FACTORY + SUFFIX_IMPL;
	}

	public static String getEFactoryClassCName(EPackage pack)
	{
		return getEFactoryClassCName(SLASH, pack);
	}

	/**
	 * @param pack
	 * @return
	 */
	public static String getEPackageInterfaceCName(String separator, EPackage pack)
	{
		return getPackageQName(separator, pack) + separator
			+ StringUtils.toTitleCase(pack.getName()) + SUFFIX_PACKAGE;
	}

	/**
	 * @param pack
	 * @return
	 */
	public static String getEPackageInterfaceCName(EPackage pack)
	{
		return getEPackageInterfaceCName(SLASH, pack);
	}

	/**
	 * @param pathSep
	 * @param innerSep
	 * @param pack
	 * @return
	 */
	public static String getEPackageInterfaceLiteralsCName(String pathSep, String innerSep,
		EPackage pack)
	{
		return getEPackageInterfaceCName(pathSep, pack) + innerSep + IFACE_LITERALS;
	}

	/**
	 * @param pack
	 * @return
	 */
	public static String getEPackageInterfaceLiteralsCName(EPackage pack)
	{
		return getEPackageInterfaceLiteralsCName(SLASH, DOLAR, pack);
	}

	/**
	 * @param pack
	 * @return
	 */
	public static String getEPackageClassCName(String separator, EPackage pack)
	{
		return getPackageQName(separator, pack) + separator + PACKAGE_IMPL + separator
			+ StringUtils.toTitleCase(pack.getName()) + SUFFIX_PACKAGE + SUFFIX_IMPL;
	}

	/**
	 * @param cls
	 * @return
	 */
	public static String getEClassifierID(EClassifier cls)
	{
		return CodeGenUtil.format(cls.getName(), '_', "", true, true).toUpperCase(); //$NON-NLS-1$
	}

	/**
	 * @param lit
	 * @return
	 */
	public static String getELiteralID(EEnumLiteral lit)
	{
		return CodeGenUtil.format(lit.getName(), '_', "", true, true).toUpperCase(); //$NON-NLS-1$
	}

	/**
	 * @param cls
	 * @return
	 */
	public static String getClassifierInstanceName(EClassifier cls)
	{
		String name = CodeGenUtil.uncapPrefixedName(cls.getName(), false);
		if (cls instanceof EClass)
		{
			return name + "EClass"; //$NON-NLS-1$
		}
		else if (cls instanceof EEnum)
		{
			return name + "EEnum"; //$NON-NLS-1$
		}
		else
		{
			return name + "EDataType"; //$NON-NLS-1$
		}
	}

	/**
	 * @param eCls
	 * @param feature
	 * @return
	 */
	public static String getEStructuralFeatureID(EClass eCls, EStructuralFeature feature)
	{
		// TODO Auto-generated method stub
		return getEClassifierID(eCls) + "__" //$NON-NLS-1$
			+ CodeGenUtil.format(feature.getName(), '_', null, true, true).toUpperCase();
	}

	/**
	 * @param eCls
	 * @return
	 */
	public static String getEFeatureCountID(EClass eCls)
	{
		return getEClassifierID(eCls) + "_FEATURE_COUNT"; //$NON-NLS-1$
	}

	/**
	 * @param pack
	 * @return
	 */
	public static String getEPackageClassCName(EPackage pack)
	{
		return getEPackageClassCName(SLASH, pack);
	}

	/**
	 * @param pack
	 * @return
	 */
	public static String getPackageQName(String separator, EPackage pack)
	{
		List<String> segments = new ArrayList<String>();
		int maxLength = 0;
		for (EPackage superPack = pack.getESuperPackage(); superPack != null; superPack = superPack.getESuperPackage())
		{
			String name = superPack.getName();
			segments.add(name);
			maxLength += name.length() + 1;
		}

		StringBuilder sb = new StringBuilder(maxLength);
		for (int i = segments.size() - 1; i >= 0; i--)
		{
			sb.append(segments.get(i));
			sb.append(separator);
		}
		sb.append(pack.getName());
		return sb.toString();
	}

	/**
	 * @param cName
	 * @return
	 */
	public static String getClassSignature(String cName)
	{
		if (!isPrimitiveSignature(cName))
		{
			return "L" + cName + ";"; //$NON-NLS-1$//$NON-NLS-2$
		}
		else
		{
			return cName;
		}
	}

	/**
	 * @param cName
	 * @return
	 */
	private static boolean isPrimitiveSignature(String cName)
	{
		return cName.length() == 1 && "BCDFIJSZ".indexOf(cName) > -1;
	}

	/**
	 * @param c
	 * @param args
	 * @return
	 */
	public static String getMethodSignature(Class<?> c, String... args)
	{
		return getMethodSignatureFromSigned(Type.getDescriptor(c), args);
	}

	/**
	 * @param cName
	 * @param args
	 * @return
	 */
	public static String getMethodSignature(String cName, String... args)
	{
		return getMethodSignatureFromSigned(getClassSignature(cName), args);
	}

	/**
	 * @param cName
	 * @param args
	 * @return
	 */
	private static String getMethodSignatureFromSigned(String cName, String... args)
	{
		StringBuilder sb = new StringBuilder("("); //$NON-NLS-1$
		if (args != null)
		{
			for (int i = 0; i < args.length; i++)
			{
				sb.append(args[i]);
			}
		}
		sb.append(")"); //$NON-NLS-1$
		sb.append(cName);
		return sb.toString();
	}

	/**
	 * @param cls
	 * @return
	 */
	public static String getEMFType(EClassifier cls)
	{
		if (cls instanceof EClass)
		{
			return Constants.CSNAME_ECLASS;
		}
		else if (cls instanceof EEnum)
		{
			return Constants.CSNAME_EENUM;
		}
		else
		{
			return Constants.CSNAME_EDATATYPE;
		}
	}

	/**
	 * @param feature
	 * @return
	 */
	public static String getEMFType(EStructuralFeature feature)
	{
		if (feature instanceof EReference)
		{
			return Constants.CSNAME_EREFERENCE;
		}
		else
		{
			return Constants.CSNAME_EATTRIBUTE;

		}
	}

}
