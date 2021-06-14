/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 19, 2010 4:38:43 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.asm;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.objectweb.asm.AnnotationVisitor;
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
public class EFactoryGenerator implements Opcodes, Constants
{

	/* package */static class ClassBinWrapper extends AbstractPMBinaryClassWrapper<EPackage>
	{
		public ClassBinWrapper(IPMBinaryGeneratorSettings settings, EPackage element)
		{
			super(settings, element, true);
		}

		@Override
		protected String generateClassName(EPackage elem)
		{
			return ASMNames.getEFactoryClassCName(DOT, elem);
		}

		@Override
		protected String generateClassFileName(EPackage elem)
		{
			return ASMNames.getEFactoryClassCName(SLASH, elem) + CLASS_EXT;
		}

		@Override
		protected byte[] generateBinary(EPackage elem, boolean genImplCode)
		{
			return genEFactoryClass(getSettings(), elem);
		}
	}

	/* package */static class InterfaceBinWrapper extends AbstractPMBinaryClassWrapper<EPackage>
	{
		public InterfaceBinWrapper(IPMBinaryGeneratorSettings settings, EPackage element)
		{
			super(settings, element, false);
		}

		@Override
		protected String generateClassName(EPackage elem)
		{
			return ASMNames.getEFactoryInterfaceCName(DOT, elem);
		}

		@Override
		protected String generateClassFileName(EPackage elem)
		{
			return ASMNames.getEFactoryInterfaceCName(SLASH, elem) + CLASS_EXT;
		}

		@Override
		protected byte[] generateBinary(EPackage elem, boolean genImplCode)
		{
			return genEFactoryInterface(getSettings(), elem, genImplCode);
		}
	}

	/**
	 * @param pack
	 * @return
	 */
	public static byte[] genEFactoryInterface(IPMBinaryGeneratorSettings settings, EPackage pack, boolean genImplCode)
	{
		String iName = ASMNames.getEFactoryInterfaceCName(pack);
		String cName = ASMNames.getEFactoryClassCName(pack);

		ClassWriter cw = new ClassWriter(0);
		cw.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, iName, null, CSNAME_OBJECT,
			new String[] {CSNAME_EFACTORY});

		// create eINSTANCE field
		{
			FieldVisitor field = cw.visitField(ACC_IFACE_FIELD, FIELD_EINSTANCE, ASMNames.getClassSignature(iName),
				null, null);
			field.visitEnd();
		}
		// create class init method
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
		// create one create method for each defined EClass
		{
			List<EClass> classes = getNonAbstractClasses(pack);
			for (EClass cls: classes)
			{
				MethodVisitor mv = cw.visitMethod(ACC_IFACE_METHOD, PREFIX_CREATE + cls.getName(),
					ASMNames.getMethodSignature(ASMNames.getEClassInterfaceCName(cls)), null, null);
				mv.visitEnd();
			}
		}

		// create the getNAMEPackage method signature
		{
			String methodName = PREFIX_GET + StringUtils.toTitleCase(pack.getName()) + SUFFIX_PACKAGE;
			MethodVisitor mv = cw.visitMethod(ACC_IFACE_METHOD, methodName,
				ASMNames.getMethodSignature(ASMNames.getEPackageInterfaceCName(pack)), null, null);
			mv.visitEnd();
		}
		return cw.toByteArray();
	}

	/**
	 * @param pack
	 * @return
	 */
	public static byte[] genEFactoryClass(IPMBinaryGeneratorSettings settings, EPackage pack)
	{
		String iName = ASMNames.getEFactoryInterfaceCName(pack);
		String cName = ASMNames.getEFactoryClassCName(pack);
		List<EClass> nonAbstractClasses = getNonAbstractClasses(pack);

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, cName, null, CSNAME_EFACTORY_IMPL, new String[] {iName});
		// public static iName init()
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, METHOD_INIT, ASMNames.getMethodSignature(iName),
				null, null);
			mv.visitCode();
			mv.visitTypeInsn(NEW, cName);
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, cName, METHOD_CONSTRUCTOR, SIG_M_VOID_VOID);
			mv.visitVarInsn(ASTORE, 0);
			mv.visitLdcInsn(pack.getNsURI());
			mv.visitVarInsn(ASTORE, 1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, CSNAME_PMRUNTIMEUTILS, METHOD_PMU_UPDATEPMFACTORY, SIG_PMU_UPDATEPMFACTORY);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		// public iName()
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, METHOD_CONSTRUCTOR, SIG_M_VOID_VOID, null, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, CSNAME_EFACTORY_IMPL, METHOD_CONSTRUCTOR, SIG_M_VOID_VOID);
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		// public EObject create(EClass eClass)
		// A switch will be generated only if there is at least one non abstract
		// EClass in this package
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, PREFIX_CREATE,
				ASMNames.getMethodSignature(EObject.class, Type.getDescriptor(EClass.class)), null, null);
			mv.visitCode();
			if (nonAbstractClasses.size() > 0)
			{
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEINTERFACE, CSNAME_ECLASS, "getClassifierID",
					ASMNames.getMethodSignature(Integer.TYPE));
				// generate a switch
				Label swDefaultLabel = new Label();
				Label[] swLabels = new Label[nonAbstractClasses.size()];
				int[] keys = new int[nonAbstractClasses.size()];
				for (int i = 0; i < swLabels.length; i++)
				{
					swLabels[i] = new Label();
					keys[i] = nonAbstractClasses.get(i).getClassifierID();
				}
				mv.visitLookupSwitchInsn(swDefaultLabel, keys, swLabels);
				for (int i = 0; i < nonAbstractClasses.size(); i++)
				{
					EClass clz = nonAbstractClasses.get(i);
					mv.visitLabel(swLabels[i]);
					mv.visitVarInsn(ALOAD, 0);
					mv.visitMethodInsn(INVOKEVIRTUAL, cName, PREFIX_CREATE + clz.getName(),
						ASMNames.getMethodSignature(ASMNames.getEClassInterfaceCName(clz)));
					mv.visitInsn(ARETURN);
				}
				mv.visitLabel(swDefaultLabel);
			}
			mv.visitTypeInsn(NEW, CSNAME_ILLEGAL_ARGUMENT_EXCEPTION);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, CSNAME_PM_RUNTIME_MESSAGES, METHOD_MSG_NOT_A_VALID_CLASSIFIER,
				ASMNames.getMethodSignature(String.class, Type.getDescriptor(EClass.class)));
			mv.visitMethodInsn(INVOKESPECIAL, CSNAME_ILLEGAL_ARGUMENT_EXCEPTION, METHOD_CONSTRUCTOR,
				ASMNames.getMethodSignature(Void.TYPE, Type.getDescriptor(String.class)));
			mv.visitInsn(ATHROW);
			mv.visitMaxs(3, 2);
			mv.visitEnd();
		}
		// if there are EDataTypes or EEnums created in this package create
		// string to/from conversion methods

		List<EDataType> dataTypes = EPackageGenerator.getAllDataTypes(pack);
		if (!dataTypes.isEmpty())
		{
			// public Object createFromString(EDataType, String)
			{
				MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "createFromString",
					"(Lorg/eclipse/emf/ecore/EDataType;Ljava/lang/String;)Ljava/lang/Object;", null, null);
				mv.visitCode();
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEINTERFACE, CSNAME_EDATATYPE, "getClassifierID", "()I");
				Label[] switchLabels = new Label[dataTypes.size()];
				int[] keys = new int[dataTypes.size()];
				for (int i = 0; i < dataTypes.size(); i++)
				{
					switchLabels[i] = new Label();
					keys[i] = dataTypes.get(i).getClassifierID();
				}
				Label switchDefault = new Label();
				mv.visitLookupSwitchInsn(switchDefault, keys, switchLabels);
				// mv.visitTableSwitchInsn(0, dataTypes.size() - 1,
				// switchDefault, switchLabels);
				for (int i = 0; i < dataTypes.size(); i++)
				{
					EDataType dataType = dataTypes.get(i);
					mv.visitLabel(switchLabels[i]);
					mv.visitVarInsn(ALOAD, 0);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitVarInsn(ALOAD, 2);
					mv.visitMethodInsn(
						INVOKEVIRTUAL,
						cName,
						PREFIX_CREATE + dataType.getName() + "FromString",
						ASMNames.getMethodSignature(ASMNames.getEClassifierCName(dataType, settings),
							Type.getDescriptor(EDataType.class), Type.getDescriptor(String.class)));
					mv.visitInsn(ARETURN);

				}

				mv.visitLabel(switchDefault);
				mv.visitTypeInsn(NEW, CSNAME_ILLEGAL_ARGUMENT_EXCEPTION);
				mv.visitInsn(DUP);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKESTATIC, CSNAME_PM_RUNTIME_MESSAGES, "getNotAValidDataTypeMessage",
					"(Lorg/eclipse/emf/ecore/EDataType;)Ljava/lang/String;");
				mv.visitMethodInsn(INVOKESPECIAL, CSNAME_ILLEGAL_ARGUMENT_EXCEPTION, METHOD_CONSTRUCTOR,
					"(Ljava/lang/String;)V");
				mv.visitInsn(ATHROW);

				mv.visitMaxs(3, 3);
				mv.visitEnd();
			}
			// public String convertToString(EDataType eDataType, Object
			// instanceValue)
			{
				MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "convertToString",
					"(Lorg/eclipse/emf/ecore/EDataType;Ljava/lang/Object;)Ljava/lang/String;", null, null);
				mv.visitCode();
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEINTERFACE, CSNAME_EDATATYPE, "getClassifierID", "()I");

				Label[] switchLabels = new Label[dataTypes.size()];
				int[] keys = new int[dataTypes.size()];
				for (int i = 0; i < dataTypes.size(); i++)
				{
					switchLabels[i] = new Label();
					keys[i] = dataTypes.get(i).getClassifierID();
				}
				Label switchDefault = new Label();
				mv.visitLookupSwitchInsn(switchDefault, keys, switchLabels);
				// mv.visitTableSwitchInsn(0, dataTypes.size() - 1,
				// switchDefault, switchLabels);
				for (int i = 0; i < dataTypes.size(); i++)
				{
					EDataType dataType = dataTypes.get(i);
					mv.visitLabel(switchLabels[i]);
					mv.visitVarInsn(ALOAD, 0);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitVarInsn(ALOAD, 2);
					mv.visitMethodInsn(INVOKEVIRTUAL, cName, "convert" + dataType.getName() + "ToString",
						"(Lorg/eclipse/emf/ecore/EDataType;Ljava/lang/Object;)Ljava/lang/String;");
					mv.visitInsn(ARETURN);

				}

				mv.visitLabel(switchDefault);
				mv.visitTypeInsn(NEW, CSNAME_ILLEGAL_ARGUMENT_EXCEPTION);
				mv.visitInsn(DUP);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKESTATIC, CSNAME_PM_RUNTIME_MESSAGES, "getNotAValidDataTypeMessage",
					"(Lorg/eclipse/emf/ecore/EDataType;)Ljava/lang/String;");
				mv.visitMethodInsn(INVOKESPECIAL, CSNAME_ILLEGAL_ARGUMENT_EXCEPTION, METHOD_CONSTRUCTOR,
					"(Ljava/lang/String;)V");

				mv.visitInsn(ATHROW);
				mv.visitMaxs(5, 3);
				mv.visitEnd();
			}
			// public type createTypeFromString
			for (int i = 0; i < dataTypes.size(); i++)
			{
				EDataType dataType = dataTypes.get(i);
				String dtCName = ASMNames.getEClassifierCName(dataType, settings);
				MethodVisitor mv = cw.visitMethod(
					ACC_PUBLIC,
					PREFIX_CREATE + dataType.getName() + "FromString",
					ASMNames.getMethodSignature(dtCName, Type.getDescriptor(EDataType.class),
						Type.getDescriptor(String.class)), null, null);
				mv.visitCode();
				mv.visitVarInsn(ALOAD, 2);
				mv.visitMethodInsn(INVOKESTATIC, dtCName, "get",
					ASMNames.getMethodSignature(dtCName, Type.getDescriptor(String.class)));
				mv.visitVarInsn(ASTORE, 3);
				mv.visitVarInsn(ALOAD, 3);
				Label l2 = new Label();
				mv.visitJumpInsn(IFNONNULL, l2);
				mv.visitTypeInsn(NEW, CSNAME_ILLEGAL_ARGUMENT_EXCEPTION);
				mv.visitInsn(DUP);
				mv.visitVarInsn(ALOAD, 2);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKESTATIC, CSNAME_PM_RUNTIME_MESSAGES, "getNotAValidLiteralMessage",
					"(Ljava/lang/String;Lorg/eclipse/emf/ecore/EDataType;)Ljava/lang/String;");
				mv.visitMethodInsn(INVOKESPECIAL, CSNAME_ILLEGAL_ARGUMENT_EXCEPTION, "<init>", "(Ljava/lang/String;)V");
				mv.visitInsn(ATHROW);
				mv.visitLabel(l2);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitInsn(ARETURN);
				mv.visitMaxs(5, 4);
				mv.visitEnd();
			}
			// public String convertTypeToString
			for (int i = 0; i < dataTypes.size(); i++)
			{
				EDataType dataType = dataTypes.get(i);
				MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "convert" + dataType.getName() + "ToString",
					"(Lorg/eclipse/emf/ecore/EDataType;Ljava/lang/Object;)Ljava/lang/String;", null, null);
				mv.visitCode();
				mv.visitVarInsn(ALOAD, 2);
				Label l1 = new Label();
				mv.visitJumpInsn(IFNONNULL, l1);
				mv.visitInsn(ACONST_NULL);
				Label l2 = new Label();
				mv.visitJumpInsn(GOTO, l2);
				mv.visitLabel(l1);
				mv.visitVarInsn(ALOAD, 2);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
				mv.visitLabel(l2);
				mv.visitInsn(ARETURN);
				mv.visitMaxs(1, 3);
				mv.visitEnd();
			}

		}

		// create one public ECU_CLASS createECU_CLASS() method
		// for each concrete EClass from this package
		for (EClass clz: nonAbstractClasses)
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, PREFIX_CREATE + clz.getName(),
				ASMNames.getMethodSignature(ASMNames.getEClassInterfaceCName(clz)), null, null);
			mv.visitCode();
			mv.visitTypeInsn(NEW, ASMNames.getEClassClassCName(clz));
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, ASMNames.getEClassClassCName(clz), METHOD_CONSTRUCTOR, SIG_M_VOID_VOID);
			mv.visitVarInsn(ASTORE, 1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

		}
		// public IMPL_PACKAGE getIMPL_PACKAGE()
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, PREFIX_GET + StringUtils.toTitleCase(pack.getName())
				+ SUFFIX_PACKAGE, ASMNames.getMethodSignature(ASMNames.getEPackageInterfaceCName(pack)), null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, cName, "getEPackage", ASMNames.getMethodSignature(EPackage.class));
			mv.visitTypeInsn(CHECKCAST, ASMNames.getEPackageInterfaceCName(pack));
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		// public IMPL_PACKAGE getPackage() deprecated
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC + ACC_DEPRECATED, "getPackage",
				ASMNames.getMethodSignature(ASMNames.getEPackageInterfaceCName(pack)), null, null);
			{
				AnnotationVisitor av = mv.visitAnnotation("Ljava/lang/Deprecated;", true);
				av.visitEnd();
			}
			mv.visitCode();
			mv.visitFieldInsn(GETSTATIC, ASMNames.getEPackageInterfaceCName(pack), "eINSTANCE",
				ASMNames.getClassSignature(ASMNames.getEPackageInterfaceCName(pack)));
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 0);
			mv.visitEnd();
		}

		return cw.toByteArray();
	}

	/**
	 * @param pack
	 * @return
	 */
	private static List<EClass> getNonAbstractClasses(EPackage pack)
	{
		List<EClass> nonAbstractClasses = new ArrayList<EClass>();
		for (EClassifier classifier: pack.getEClassifiers())
		{
			if (classifier instanceof EClass && !((EClass) classifier).isAbstract()
				&& !((EClass) classifier).isInterface())
			{
				nonAbstractClasses.add((EClass) classifier);
			}
		}
		return nonAbstractClasses;
	}
}
