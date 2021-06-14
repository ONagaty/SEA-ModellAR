/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 19, 2010 5:29:10 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import eu.cessar.ct.core.platform.util.StringUtils;

/**
 *
 */
public class EPackageGenerator implements Opcodes, Constants
{

	/* package */static class InterfaceBinWrapper extends AbstractPMBinaryClassWrapper<EPackage>
	{
		public InterfaceBinWrapper(IPMBinaryGeneratorSettings settings, EPackage element)
		{
			super(settings, element, false);
		}

		@Override
		protected String generateClassName(EPackage elem)
		{
			return ASMNames.getEPackageInterfaceCName(DOT, elem);
		}

		@Override
		protected String generateClassFileName(EPackage elem)
		{
			return ASMNames.getEPackageInterfaceCName(SLASH, elem) + CLASS_EXT;
		}

		@Override
		protected byte[] generateBinary(EPackage elem, boolean genImplCode)
		{
			return genEPackageInterface(getSettings(), elem, genImplCode);
		}

	}

	/* package */static class InterfaceLiteralsBinWrapper extends AbstractPMBinaryClassWrapper<EPackage>
	{
		public InterfaceLiteralsBinWrapper(IPMBinaryGeneratorSettings settings, EPackage element)
		{
			super(settings, element, false);
		}

		@Override
		protected String generateClassName(EPackage elem)
		{
			return ASMNames.getEPackageInterfaceLiteralsCName(DOT, DOLAR, elem);
		}

		@Override
		protected String generateClassFileName(EPackage elem)
		{
			return ASMNames.getEPackageInterfaceLiteralsCName(SLASH, DOLAR, elem) + CLASS_EXT;
		}

		@Override
		protected byte[] generateBinary(EPackage elem, boolean genImplCode)
		{
			return genEPackageInterfaceLiterals(getSettings(), elem);
		}

	}

	/* package */static class ClassBinWrapper extends AbstractPMBinaryClassWrapper<EPackage>
	{
		public ClassBinWrapper(IPMBinaryGeneratorSettings settings, EPackage element)
		{
			super(settings, element, true);
		}

		@Override
		protected String generateClassName(EPackage elem)
		{
			return ASMNames.getEPackageClassCName(DOT, elem);
		}

		@Override
		protected String generateClassFileName(EPackage elem)
		{
			return ASMNames.getEPackageClassCName(SLASH, elem) + CLASS_EXT;
		}

		@Override
		protected byte[] generateBinary(EPackage elem, boolean genImplCode)
		{
			return genEPackageClass(getSettings(), elem);
		}

	}

	/**
	 * @param pack
	 * @return
	 */
	private static List<EEnum> getEnums(EPackage pack)
	{
		List<EEnum> result = new ArrayList<EEnum>();
		for (EClassifier clz: pack.getEClassifiers())
		{
			if (clz instanceof EEnum)
			{
				result.add((EEnum) clz);
			}
		}
		return result;
	}

	/**
	 * @param pack
	 * @return
	 */
	private static List<EDataType> getDataTypes(EPackage pack)
	{
		List<EDataType> result = new ArrayList<EDataType>();
		for (EClassifier clz: pack.getEClassifiers())
		{
			if (clz instanceof EDataType && !(clz instanceof EEnum))
			{
				result.add((EDataType) clz);
			}
		}
		return result;
	}

	/**
	 * @param clz
	 * @return
	 */
	private static EClass getBaseClass(EClass clz)
	{
		EList<EClass> eSuperTypes = clz.getESuperTypes();
		return eSuperTypes.isEmpty() ? null : eSuperTypes.get(0);
	}

	/**
	 * @param pack
	 * @return
	 */
	private static List<EClass> getOrderedClasses(EPackage pack)
	{
		List<EClass> result = new ArrayList<EClass>();
		Set<EClass> resultSet = new HashSet<EClass>();

		for (Iterator<EClassifier> iter = pack.getEClassifiers().iterator(); iter.hasNext();)
		{
			EClassifier clz = iter.next();
			if (clz instanceof EClass)
			{
				EClass genClass = (EClass) clz;
				List<EClass> extendChain = new LinkedList<EClass>();
				for (; genClass != null; genClass = getBaseClass(genClass))
				{
					if (pack == genClass.getEPackage() && resultSet.add(genClass))
					{
						extendChain.add(0, genClass);
					}
				}
				result.addAll(extendChain);
			}
		}
		return result;
	}

	/**
	 * @param pack
	 * @return
	 */
	public static EPackage getRootPackage(EPackage pack)
	{
		EPackage parent = pack;
		while (pack != null)
		{
			pack = pack.getESuperPackage();
			if (pack != null)
			{
				parent = pack;
			}

		}
		return parent;
	}

	/**
	 * @param pack
	 * @return
	 */
	private static List<EClassifier> getOrderedClassifiers(EPackage pack)
	{
		List<EClassifier> result = new ArrayList<EClassifier>(getOrderedClasses(pack));
		result.addAll(getEnums(pack));
		result.addAll(getDataTypes(pack));
		return result;
	}

	/**
	 * @param pack
	 * @return
	 */
	public static List<EDataType> getAllDataTypes(EPackage pack)
	{
		List<EDataType> result = new ArrayList<EDataType>();
		result.addAll(getEnums(pack));
		result.addAll(getDataTypes(pack));
		return result;
	}

	private static void collectPackages(List<EPackage> result, EPackage pack)
	{
		result.add(pack);
		for (EPackage sub: pack.getESubpackages())
		{
			collectPackages(result, sub);
		}
	}

	private static List<EPackage> getAllInnerPackages(EPackage pack)
	{
		List<EPackage> result = new ArrayList<EPackage>();
		collectPackages(result, getRootPackage(pack));
		return result;
	}

	/**
	 * @param pack
	 * @return
	 */
	public static byte[] genEPackageInterface(IPMBinaryGeneratorSettings settings, EPackage pack, boolean genImplCode)
	{
		String iName = ASMNames.getEPackageInterfaceCName(pack);
		String cName = ASMNames.getEPackageClassCName(pack);
		String litName = ASMNames.getEPackageInterfaceLiteralsCName(pack);
		List<EClassifier> classifiers = getOrderedClassifiers(pack);

		ClassWriter cw = new ClassWriter(0);
		cw.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, iName, null, CSNAME_OBJECT,
			new String[] {CSNAME_EPACKAGE});
		cw.visitInnerClass(litName, iName, IFACE_LITERALS, ACC_PUBLIC + ACC_STATIC + ACC_ABSTRACT + ACC_INTERFACE);

		// standard fields production (eNAME, eNS_URI, eNS_PREFIX, eINSTANCE)
		{
			FieldVisitor fv = cw.visitField(ACC_IFACE_FIELD, FIELD_ENAME, Type.getDescriptor(String.class), null,
				pack.getName());
			fv.visitEnd();

			fv = cw.visitField(ACC_IFACE_FIELD, FIELD_ENS_URI, Type.getDescriptor(String.class), null, pack.getNsURI());
			fv.visitEnd();

			fv = cw.visitField(ACC_IFACE_FIELD, FIELD_ENS_PREFIX, Type.getDescriptor(String.class), null,
				pack.getNsPrefix());
			fv.visitEnd();

			fv = cw.visitField(ACC_IFACE_FIELD, FIELD_EINSTANCE, ASMNames.getClassSignature(iName), null, null);
			fv.visitEnd();
		}

		{
			for (EClassifier cls: classifiers)
			{
				FieldVisitor fv = cw.visitField(ACC_IFACE_FIELD, ASMNames.getEClassifierID(cls),
					Type.getDescriptor(Integer.TYPE), null, new Integer(pack.getEClassifiers().indexOf(cls)));
				fv.visitEnd();
				if (cls instanceof EClass)
				{
					EClass eCls = (EClass) cls;
					List<EStructuralFeature> features = EClassGenerator.getAllFeatures(eCls);
					for (int i = 0; i < features.size(); i++)
					{
						EStructuralFeature feature = features.get(i);
						fv = cw.visitField(ACC_IFACE_FIELD, ASMNames.getEStructuralFeatureID(eCls, feature),
							Type.getDescriptor(Integer.TYPE), null, new Integer(i));
						fv.visitEnd();
					}
					fv = cw.visitField(ACC_IFACE_FIELD, ASMNames.getEFeatureCountID(eCls),
						Type.getDescriptor(Integer.TYPE), null, new Integer(features.size()));
					fv.visitEnd();
				}
			}
		}

		// the class initialization
		{
			MethodVisitor mv = cw.visitMethod(ACC_STATIC, METHOD_CLASS_CONSTRUCTOR, SIG_M_VOID_VOID, null, null);
			mv.visitCode();
			if (genImplCode)
			{
				mv.visitMethodInsn(INVOKESTATIC, cName, METHOD_INIT, ASMNames.getMethodSignature(iName));
			}
			else
			{
				mv.visitInsn(ACONST_NULL);
			}
			mv.visitFieldInsn(PUTSTATIC, iName, FIELD_EINSTANCE, ASMNames.getClassSignature(iName));
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 0);
			mv.visitEnd();
		}

		// getClassifier() methods
		for (EClassifier classifier: pack.getEClassifiers())
		{
			String returnType = ASMNames.getEMFType(classifier);
			String signature = ASMNames.getMethodSignature(returnType);
			MethodVisitor mv = cw.visitMethod(ACC_IFACE_METHOD, PREFIX_GET + classifier.getName(), signature, null,
				null);
			mv.visitEnd();
			if (classifier instanceof EClass)
			{
				// getClassifier_Feature() methods
				EClass cls = (EClass) classifier;
				List<EStructuralFeature> features = getSortedFeatures(cls);
				for (EStructuralFeature feature: features)
				{
					String fName = settings.isUsingTitleCaseFeatureNames() ? StringUtils.toTitleCase(feature.getName())
						: feature.getName();
					returnType = ASMNames.getEMFType(feature);
					signature = ASMNames.getMethodSignature(returnType);
					mv = cw.visitMethod(ACC_IFACE_METHOD, PREFIX_GET + classifier.getName() + "_" //$NON-NLS-1$
						+ fName, signature, null, null);
					mv.visitEnd();
				}
			}
		}

		// get factory method
		{
			MethodVisitor mv = cw.visitMethod(ACC_IFACE_METHOD, PREFIX_GET + StringUtils.toTitleCase(pack.getName())
				+ SUFFIX_FACTORY, ASMNames.getMethodSignature(ASMNames.getEFactoryInterfaceCName(pack)), null, null);
			mv.visitEnd();
		}

		return cw.toByteArray();
	}

	/**
	 * @param cls
	 * @return
	 */
	private static List<EStructuralFeature> getSortedFeatures(EClass cls)
	{
		List<EStructuralFeature> result = new ArrayList<EStructuralFeature>(cls.getEStructuralFeatures());
		Collections.sort(result, new Comparator<EStructuralFeature>()
			{

			public int compare(EStructuralFeature o1, EStructuralFeature o2)
			{
				return StringUtils.toTitleCase(o1.getName()).compareTo(StringUtils.toTitleCase(o2.getName()));
			}

			});
		return result;
	}

	/**
	 * @param pack
	 * @return
	 */
	public static byte[] genEPackageInterfaceLiterals(IPMBinaryGeneratorSettings settings, EPackage pack)
	{
		String iName = ASMNames.getEPackageInterfaceCName(pack);
		String litName = ASMNames.getEPackageInterfaceLiteralsCName(pack);

		ClassWriter cw = new ClassWriter(0);
		cw.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, litName, null, CSNAME_OBJECT, null);

		cw.visitInnerClass(litName, iName, IFACE_LITERALS, ACC_PUBLIC + ACC_STATIC + ACC_ABSTRACT + ACC_INTERFACE);

		// Classifier meta field
		for (EClassifier classifier: pack.getEClassifiers())
		{
			String returnType = ASMNames.getEMFType(classifier);
			FieldVisitor fv = cw.visitField(ACC_IFACE_FIELD, ASMNames.getEClassifierID(classifier),
				ASMNames.getClassSignature(returnType), null, null);
			fv.visitEnd();
			if (classifier instanceof EClass)
			{
				// getClassifier_Feature() methods
				EClass cls = (EClass) classifier;
				for (EStructuralFeature feature: cls.getEStructuralFeatures())
				{
					returnType = ASMNames.getEMFType(feature);
					fv = cw.visitField(ACC_IFACE_FIELD, ASMNames.getEStructuralFeatureID(cls, feature),
						ASMNames.getClassSignature(returnType), null, null);
					fv.visitEnd();
				}
			}
		}
		// static constructor
		{
			MethodVisitor mv = cw.visitMethod(ACC_STATIC, METHOD_CLASS_CONSTRUCTOR, SIG_M_VOID_VOID, null, null);
			mv.visitCode();
			for (EClassifier classifier: pack.getEClassifiers())
			{
				String returnType = ASMNames.getEMFType(classifier);
				mv.visitFieldInsn(GETSTATIC, iName, FIELD_EINSTANCE, ASMNames.getClassSignature(iName));
				mv.visitMethodInsn(INVOKEINTERFACE, iName, PREFIX_GET + classifier.getName(),
					ASMNames.getMethodSignature(returnType));
				mv.visitFieldInsn(PUTSTATIC, litName, ASMNames.getEClassifierID(classifier),
					ASMNames.getClassSignature(returnType));

				if (classifier instanceof EClass)
				{
					// getClassifier_Feature() methods
					EClass cls = (EClass) classifier;
					for (EStructuralFeature feature: cls.getEStructuralFeatures())
					{
						String fName = settings.isUsingTitleCaseFeatureNames() ? StringUtils.toTitleCase(feature.getName())
							: feature.getName();
						returnType = ASMNames.getEMFType(feature);
						mv.visitFieldInsn(GETSTATIC, iName, FIELD_EINSTANCE, ASMNames.getClassSignature(iName));
						mv.visitMethodInsn(INVOKEINTERFACE, iName, PREFIX_GET + classifier.getName() + "_" + fName, //$NON-NLS-1$
							ASMNames.getMethodSignature(returnType));
						mv.visitFieldInsn(PUTSTATIC, litName, ASMNames.getEStructuralFeatureID(cls, feature),
							ASMNames.getClassSignature(returnType));
					}
				}
			}
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 0);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}

	/**
	 * @param pack
	 * @return
	 */
	public static byte[] genEPackageClass(IPMBinaryGeneratorSettings settings, EPackage pack)
	{
		String iName = ASMNames.getEPackageInterfaceCName(pack);
		String iNameSig = ASMNames.getClassSignature(iName);
		String cName = ASMNames.getEPackageClassCName(pack);

		List<EPackage> allPacks = getAllInnerPackages(pack);

		ClassWriter cw = new ClassWriter(0);
		cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, cName, null, CSNAME_EPACKAGE_IMPL, new String[] {iName});

		// private fields to hold the metadata, one for each classifier
		for (EClassifier classifier: pack.getEClassifiers())
		{
			String classifierInstanceName = ASMNames.getClassifierInstanceName(classifier);
			String classSignature = ASMNames.getClassSignature(ASMNames.getEMFType(classifier));

			FieldVisitor fv = cw.visitField(ACC_PRIVATE, classifierInstanceName, classSignature, null, null);
			fv.visitEnd();
		}
		// standard EPackage impl fields (eInstance, eWrappedInstance, isInited)
		{

			FieldVisitor fv = cw.visitField(ACC_PRIVATE + ACC_STATIC, FIELD_IMPL_EINSTANCE, iNameSig, null, null);
			fv.visitEnd();

			fv = cw.visitField(ACC_PRIVATE, FIELD_IMPL_EWRAPPEDINSTANCE, CSNAME_EPACKAGE_SIG, null, null);
			fv.visitEnd();

			fv = cw.visitField(ACC_PRIVATE + ACC_STATIC, FIELD_IMPL_ISINITED, Type.getDescriptor(Boolean.TYPE), null,
				null);
			fv.visitEnd();

		}
		// class static constructor
		{
			MethodVisitor mv = cw.visitMethod(ACC_STATIC, METHOD_CLASS_CONSTRUCTOR, SIG_M_VOID_VOID, null, null);
			mv.visitCode();
			mv.visitInsn(ACONST_NULL);
			mv.visitFieldInsn(PUTSTATIC, cName, FIELD_IMPL_EINSTANCE, iNameSig);
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(PUTSTATIC, cName, FIELD_IMPL_ISINITED, Type.getDescriptor(Boolean.TYPE));
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 0);
			mv.visitEnd();
		}

		// constructor
		{
			String iFName = ASMNames.getEFactoryInterfaceCName(pack);
			String iFNameSig = ASMNames.getClassSignature(iFName);
			MethodVisitor mv = cw.visitMethod(ACC_PRIVATE, METHOD_CONSTRUCTOR, SIG_M_VOID_VOID, null, null);
			mv.visitCode();
			// CODE: super(EcucFactory.eINSTANCE);
			{
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETSTATIC, iFName, FIELD_EINSTANCE, iFNameSig);
				mv.visitMethodInsn(INVOKESPECIAL, CSNAME_EPACKAGE_IMPL, METHOD_CONSTRUCTOR,
					ASMNames.getMethodSignature(Void.TYPE, ASMNames.getClassSignature(CSNAME_EFACTORY)));
			}
			// init classifier fields with null
			for (EClassifier classifier: pack.getEClassifiers())
			{
				String classifierInstanceName = ASMNames.getClassifierInstanceName(classifier);
				String classSignature = ASMNames.getClassSignature(ASMNames.getEMFType(classifier));

				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, cName, classifierInstanceName, classSignature);
			}

			// init eWrappedInstance with null;
			{
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, cName, FIELD_IMPL_EWRAPPEDINSTANCE, CSNAME_EPACKAGE_SIG);
			}
			// init the wrappedInstance
			{
				// CODE: eWrappedInstance = EMFPMUtils.getPMPackage(eNS_URI);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitLdcInsn(pack.getNsURI());
				mv.visitMethodInsn(INVOKESTATIC, CSNAME_PMRUNTIMEUTILS, METHOD_PMU_GETPMPACKAGE, SIG_PMU_GETPMPACKAGE);
				mv.visitFieldInsn(PUTFIELD, cName, FIELD_IMPL_EWRAPPEDINSTANCE, CSNAME_EPACKAGE_SIG);

			}
			// done
			mv.visitInsn(RETURN);
			mv.visitMaxs(2, 1);
			mv.visitEnd();
		}

		// the init() method
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, METHOD_INIT, ASMNames.getMethodSignature(iName),
				null, null);
			mv.visitCode();

			// CODE: if (isInited) return eInstance;
			{
				mv.visitFieldInsn(GETSTATIC, cName, FIELD_IMPL_ISINITED, Type.getDescriptor(Boolean.TYPE));
				Label l1 = new Label();
				mv.visitJumpInsn(IFEQ, l1);
				mv.visitFieldInsn(GETSTATIC, cName, FIELD_IMPL_EINSTANCE, iNameSig);
				mv.visitInsn(ARETURN);
				mv.visitLabel(l1);
			}
			// eInstance = new instance()
			{
				mv.visitTypeInsn(NEW, cName);
				mv.visitInsn(DUP);
				mv.visitMethodInsn(INVOKESPECIAL, cName, METHOD_CONSTRUCTOR, SIG_M_VOID_VOID);
				mv.visitFieldInsn(PUTSTATIC, cName, FIELD_IMPL_EINSTANCE, iNameSig);
			}
			// isInited = true;
			{
				mv.visitInsn(ICONST_1);
				mv.visitFieldInsn(PUTSTATIC, cName, FIELD_IMPL_ISINITED, Type.getDescriptor(Boolean.TYPE));
			}

			// CODE: return eInstance
			mv.visitFieldInsn(GETSTATIC, cName, FIELD_IMPL_EINSTANCE, iNameSig);
			mv.visitInsn(ARETURN);
			mv.visitMaxs(2, 0);
			mv.visitEnd();

		}

		// getClassifier() methods
		for (EClassifier classifier: pack.getEClassifiers())
		{
			String clsReturnType = ASMNames.getEMFType(classifier);
			String returnTypeSig = ASMNames.getClassSignature(clsReturnType);
			String clsSignature = ASMNames.getMethodSignature(clsReturnType);
			String classifierInstanceName = ASMNames.getClassifierInstanceName(classifier);
			int index = pack.getEClassifiers().indexOf(classifier);

			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, PREFIX_GET + classifier.getName(), clsSignature, null, null);
			mv.visitCode();

			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cName, classifierInstanceName, returnTypeSig);
			Label ifExistJump = new Label();
			mv.visitJumpInsn(IFNONNULL, ifExistJump);

			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cName, FIELD_IMPL_EWRAPPEDINSTANCE, CSNAME_EPACKAGE_SIG);
			mv.visitMethodInsn(INVOKEINTERFACE, CSNAME_EPACKAGE, "getEClassifiers",
				"()Lorg/eclipse/emf/common/util/EList;");
			mv.visitLdcInsn(new Integer(index));
			mv.visitMethodInsn(INVOKEINTERFACE, CSNAME_ELIST, PREFIX_GET, "(I)Ljava/lang/Object;");
			mv.visitTypeInsn(CHECKCAST, clsReturnType);
			mv.visitFieldInsn(PUTFIELD, cName, classifierInstanceName, returnTypeSig);
			mv.visitLabel(ifExistJump);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cName, classifierInstanceName, returnTypeSig);
			mv.visitInsn(ARETURN);
			mv.visitMaxs(3, 1);
			mv.visitEnd();

			if (classifier instanceof EClass)
			{
				// getClassifier_Feature() methods
				EClass cls = (EClass) classifier;
				List<EStructuralFeature> features = getSortedFeatures(cls);
				for (EStructuralFeature feature: features)
				{
					String fName = settings.isUsingTitleCaseFeatureNames() ? StringUtils.toTitleCase(feature.getName())
						: feature.getName();
					String featureReturnType = ASMNames.getEMFType(feature);
					String featureSignature = ASMNames.getMethodSignature(featureReturnType);
					mv = cw.visitMethod(ACC_PUBLIC, PREFIX_GET + classifier.getName() + "_" //$NON-NLS-1$
						+ fName, featureSignature, null, null);
					mv.visitCode();
					mv.visitVarInsn(ALOAD, 0);
					mv.visitMethodInsn(INVOKEVIRTUAL, cName, PREFIX_GET + classifier.getName(), clsSignature);
					mv.visitMethodInsn(INVOKEINTERFACE, CSNAME_ECLASS, "getEStructuralFeatures",
						"()Lorg/eclipse/emf/common/util/EList;");
					mv.visitLdcInsn(new Integer(cls.getEStructuralFeatures().indexOf(feature)));
					mv.visitMethodInsn(INVOKEINTERFACE, CSNAME_ELIST, PREFIX_GET, "(I)Ljava/lang/Object;");
					mv.visitTypeInsn(CHECKCAST, featureReturnType);
					mv.visitInsn(ARETURN);
					mv.visitMaxs(2, 1);
					mv.visitEnd();
				}
			}
		}

		// get factory method
		{
			String iNameFactory = ASMNames.getEFactoryInterfaceCName(pack);
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, PREFIX_GET + StringUtils.toTitleCase(pack.getName())
				+ SUFFIX_FACTORY, ASMNames.getMethodSignature(iNameFactory), null, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, cName, "getEFactoryInstance", "()Lorg/eclipse/emf/ecore/EFactory;");
			mv.visitTypeInsn(CHECKCAST, iNameFactory);
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}

		cw.visitEnd();

		return cw.toByteArray();
	}

}
