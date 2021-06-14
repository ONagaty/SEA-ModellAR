/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 19, 2010 5:19:32 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.asm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import eu.cessar.ct.core.platform.util.StringUtils;

/**
 *
 */
public class EClassGenerator implements Opcodes, Constants
{
	/* package */static class ClassBinWrapper extends AbstractPMBinaryClassWrapper<EClass>
	{
		public ClassBinWrapper(IPMBinaryGeneratorSettings settings, EClass element)
		{
			super(settings, element, true);
		}

		@Override
		protected String generateClassName(EClass elem)
		{
			return ASMNames.getEClassClassCName(DOT, elem);
		}

		@Override
		protected String generateClassFileName(EClass elem)
		{
			return ASMNames.getEClassClassCName(SLASH, elem) + CLASS_EXT;
		}

		@Override
		protected byte[] generateBinary(EClass elem, boolean genImplCode)
		{
			return genEClassClass(getSettings(), elem);
		}

	}

	/* package */static class InterfaceBinWrapper extends AbstractPMBinaryClassWrapper<EClass>
	{
		public InterfaceBinWrapper(IPMBinaryGeneratorSettings settings, EClass element)
		{
			super(settings, element, false);
		}

		@Override
		protected String generateClassName(EClass elem)
		{
			return ASMNames.getEClassInterfaceCName(DOT, elem);
		}

		@Override
		protected String generateClassFileName(EClass elem)
		{
			return ASMNames.getEClassInterfaceCName(SLASH, elem) + CLASS_EXT;
		}

		@Override
		protected byte[] generateBinary(EClass elem, boolean genImplCode)
		{
			return genEClassInterface(getSettings(), elem);
		}

	}

	/**
	 * @param cls
	 * @return
	 */
	public static List<EStructuralFeature> getAllFeatures(EClass cls)
	{
		EList<EClass> superTypes = cls.getEAllSuperTypes();
		// we have to use only the superTypes that are part of the same model.
		Set<EClass> resultSet = new HashSet<EClass>();
		List<EStructuralFeature> result = new ArrayList<EStructuralFeature>();
		EPackage rootPack = EPackageGenerator.getRootPackage(cls.getEPackage());
		for (EClass eClass: superTypes)
		{
			EPackage aRoot = EPackageGenerator.getRootPackage(eClass.getEPackage());
			if (aRoot == rootPack && resultSet.add(eClass))
			{
				// process this one
				result.addAll(eClass.getEStructuralFeatures());
			}
		}
		result.addAll(cls.getEStructuralFeatures());
		return result;
	}

	/**
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("nls")
	public static byte[] genEClassInterface(IPMBinaryGeneratorSettings settings, EClass cls)
	{
		String iName = ASMNames.getEClassInterfaceCName(cls);
		MethodVisitor mv;

		EList<EClass> superTypes = cls.getESuperTypes();
		String[] superTypeClasses = new String[superTypes.size() + 1];
		superTypeClasses[0] = CSNAME_IEMFPROXYOBJECT;
		for (int i = 0; i < superTypes.size(); i++)
		{
			superTypeClasses[i + 1] = ASMNames.getEClassInterfaceCName(superTypes.get(i));
		}
		ClassWriter cw = new ClassWriter(0);
		cw.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, iName, null, CSNAME_OBJECT, superTypeClasses);

		EList<EStructuralFeature> features = cls.getEStructuralFeatures();
		for (EStructuralFeature feature: features)
		{
			String typeSignature;
			String genericSignature = null;
			String singularFeatureType = ASMNames.getEClassifierCName(feature.getEType(), settings);
			if (feature.isMany())
			{
				// EList<Type>
				if (settings.isUsingGenerics())
				{
					genericSignature = "()L" + CSNAME_ELIST + "<L" + singularFeatureType + ";>;";
				}
				typeSignature = CSNAME_ELIST;
			}
			else
			{
				typeSignature = singularFeatureType;
			}
			String tCaseName = StringUtils.toTitleCase(feature.getName());
			typeSignature = ASMNames.getClassSignature(typeSignature);
			String prefix = typeSignature.equals("Z") ? PREFIX_IS : PREFIX_GET;
			// generate GET method
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, prefix + tCaseName, "()" + typeSignature, genericSignature,
				null);
			mv.visitEnd();

			if (!feature.isMany() && feature.isChangeable())
			{
				// generate SET method
				mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "set" + tCaseName, "(" + typeSignature + ")V", null,
					null);
				mv.visitEnd();
			}

			if (feature.isUnsettable())
			{
				// generate ISSET method
				mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "isSet" + tCaseName, "()Z", null, null);
				mv.visitEnd();

				if (feature.isChangeable())
				{
					mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "unset" + tCaseName, "()V", null, null);
					mv.visitEnd();
				}
			}
		}

		// generate operation impl
		EList<EOperation> operations = cls.getEAllOperations();
		for (EOperation eOp: operations)
		{
			EAnnotation eAnnotation = eOp.getEAnnotation(ANN_EMF_PROXY);
			if (eAnnotation != null)
			{
				if (eAnnotation.getDetails().containsKey(KEY_GENERATE))
				{
					if (!Boolean.valueOf(eAnnotation.getDetails().get(KEY_GENERATE)))
					{
						continue;
					}
				}
			}
			EList<EParameter> parameters = eOp.getEParameters();
			StringBuilder description = generateDescription(eOp, settings, parameters);
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, eOp.getName(), description.toString(), null, null);
			mv.visitEnd();
		}

		cw.visitEnd();

		return cw.toByteArray();
	}

	/**
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("nls")
	public static byte[] genEClassClass(IPMBinaryGeneratorSettings settings, EClass cls)
	{
		String packIName = ASMNames.getEPackageInterfaceCName(cls.getEPackage());
		String packLitName = ASMNames.getEPackageInterfaceLiteralsCName(cls.getEPackage());
		String cName = ASMNames.getEClassClassCName(cls);
		String iName = ASMNames.getEClassInterfaceCName(cls);
		MethodVisitor mv;

		// EList<EClass> superTypes = cls.getESuperTypes();
		// String[] superTypeClasses = new String[superTypes.size()];
		// for (int i = 0; i < superTypes.size(); i++)
		// {
		// superTypeClasses[i] =
		// ASMNames.getEClassInterfaceCName(superTypes.get(i));
		// }
		ClassWriter cw = new ClassWriter(0);
		cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, cName, null, CSNAME_EMFPROXYOBJECT_IMPL, new String[] {iName});

		cw.visitInnerClass(packLitName, packIName, IFACE_LITERALS, ACC_PUBLIC + ACC_STATIC + ACC_ABSTRACT
			+ ACC_INTERFACE);

		// the constructor
		{
			mv = cw.visitMethod(ACC_PROTECTED, METHOD_CONSTRUCTOR, SIG_M_VOID_VOID, null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, CSNAME_EMFPROXYOBJECT_IMPL, METHOD_CONSTRUCTOR, SIG_M_VOID_VOID);
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		// protected EClass eStaticClass()
		{
			mv = cw.visitMethod(ACC_PROTECTED, "eStaticClass", "()Lorg/eclipse/emf/ecore/EClass;", null, null);
			mv.visitCode();
			mv.visitFieldInsn(GETSTATIC, packLitName, ASMNames.getEClassifierID(cls), Type.getDescriptor(EClass.class));
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		// protected EClass eStaticFeatureCount()
		{
			mv = cw.visitMethod(ACC_PROTECTED, "eStaticFeatureCount", "()I", null, null);
			mv.visitCode();
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}

		// set/get/isSet/unset methods
		EList<EStructuralFeature> features = cls.getEStructuralFeatures();
		for (EStructuralFeature feature: features)
		{
			String singularFeatureType = ASMNames.getEClassifierCName(feature.getEType(), settings);
			String typeSignature;
			String genericSignature = null;
			if (feature.isMany())
			{
				// EList<Type>
				if (settings.isUsingGenerics())
				{
					genericSignature = "()L" + CSNAME_ELIST + "<L" + singularFeatureType + ";>;";
				}
				typeSignature = CSNAME_ELIST;
			}
			else
			{
				typeSignature = singularFeatureType;
			}
			String tCaseName = StringUtils.toTitleCase(feature.getName());
			// typeSignature = ASMNames.getClassSignature(typeSignature);
			String featureEMFType = ASMNames.getEMFType(feature);
			// generate GET method
			{
				String prefix = typeSignature.equals("Z") ? PREFIX_IS : PREFIX_GET;
				mv = cw.visitMethod(ACC_PUBLIC, prefix + tCaseName, "()" + ASMNames.getClassSignature(typeSignature),
					genericSignature, null);
				mv.visitCode();
				if (typeSignature.equals("Z"))
				{ // boolean primitive
					mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETSTATIC, packLitName, ASMNames.getEStructuralFeatureID(cls, feature),
						ASMNames.getClassSignature(featureEMFType));
					mv.visitInsn(ICONST_1);
					mv.visitMethodInsn(INVOKEVIRTUAL, cName, "eGet",
						"(Lorg/eclipse/emf/ecore/EStructuralFeature;Z)Ljava/lang/Object;");
					mv.visitVarInsn(ASTORE, 1);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitTypeInsn(INSTANCEOF, "java/lang/Boolean");
					Label instanceOfLabel = new Label();
					mv.visitJumpInsn(IFEQ, instanceOfLabel);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
					mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
					Label elseLabel = new Label();
					mv.visitJumpInsn(GOTO, elseLabel);
					mv.visitLabel(instanceOfLabel);
					mv.visitInsn(ICONST_0);
					mv.visitLabel(elseLabel);
					mv.visitInsn(IRETURN);
					mv.visitMaxs(3, 2);
				}
				else if (typeSignature.equals("D"))
				{ // double primitive
					mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETSTATIC, packLitName, ASMNames.getEStructuralFeatureID(cls, feature),
						ASMNames.getClassSignature(featureEMFType));
					mv.visitInsn(ICONST_1);
					mv.visitMethodInsn(INVOKEVIRTUAL, cName, "eGet",
						"(Lorg/eclipse/emf/ecore/EStructuralFeature;Z)Ljava/lang/Object;");
					mv.visitVarInsn(ASTORE, 1);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitTypeInsn(INSTANCEOF, "java/lang/Double");
					Label instanceOfLabel = new Label();
					mv.visitJumpInsn(IFEQ, instanceOfLabel);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
					mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
					Label elseLabel = new Label();
					mv.visitJumpInsn(GOTO, elseLabel);
					mv.visitLabel(instanceOfLabel);
					mv.visitInsn(DCONST_0);
					mv.visitLabel(elseLabel);
					mv.visitInsn(DRETURN);
					mv.visitMaxs(3, 2);
				}
				else
				{
					mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETSTATIC, packLitName, ASMNames.getEStructuralFeatureID(cls, feature),
						ASMNames.getClassSignature(featureEMFType));
					mv.visitInsn(ICONST_1);
					mv.visitMethodInsn(INVOKEVIRTUAL, cName, "eGet",
						"(Lorg/eclipse/emf/ecore/EStructuralFeature;Z)Ljava/lang/Object;");
					mv.visitTypeInsn(CHECKCAST, typeSignature);
					mv.visitInsn(ARETURN);
					mv.visitMaxs(3, 1);
				}
				mv.visitEnd();
			}
			if (!feature.isMany() && feature.isChangeable())
			{
				// generate SET method
				mv = cw.visitMethod(ACC_PUBLIC, "set" + tCaseName, "(" + ASMNames.getClassSignature(typeSignature)
					+ ")V", null, null);
				mv.visitCode();
				if (typeSignature.equals("Z"))
				{ // boolean primitive
					mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETSTATIC, packLitName, ASMNames.getEStructuralFeatureID(cls, feature),
						ASMNames.getClassSignature(featureEMFType));
					mv.visitVarInsn(ILOAD, 1);
					mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
					mv.visitMethodInsn(INVOKEVIRTUAL, cName, "eSet",
						"(Lorg/eclipse/emf/ecore/EStructuralFeature;Ljava/lang/Object;)V");
					mv.visitInsn(RETURN);
					mv.visitMaxs(3, 2);
				}
				else if (typeSignature.equals("D"))
				{ // double primitive
					mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETSTATIC, packLitName, ASMNames.getEStructuralFeatureID(cls, feature),
						ASMNames.getClassSignature(featureEMFType));
					mv.visitVarInsn(DLOAD, 1);
					mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
					mv.visitMethodInsn(INVOKEVIRTUAL, cName, "eSet",
						"(Lorg/eclipse/emf/ecore/EStructuralFeature;Ljava/lang/Object;)V");
					mv.visitInsn(RETURN);
					mv.visitMaxs(4, 3);
				}
				else
				{
					mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETSTATIC, packLitName, ASMNames.getEStructuralFeatureID(cls, feature),
						ASMNames.getClassSignature(featureEMFType));
					mv.visitVarInsn(ALOAD, 1);
					mv.visitMethodInsn(INVOKEVIRTUAL, cName, "eSet",
						"(Lorg/eclipse/emf/ecore/EStructuralFeature;Ljava/lang/Object;)V");
					mv.visitInsn(RETURN);
					mv.visitMaxs(3, 2);
				}
				mv.visitEnd();
			}

			if (feature.isUnsettable())
			{
				// generate ISSET method
				mv = cw.visitMethod(ACC_PUBLIC, "isSet" + tCaseName, "()Z", null, null);
				mv.visitCode();
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETSTATIC, packLitName, ASMNames.getEStructuralFeatureID(cls, feature),
					ASMNames.getClassSignature(featureEMFType));
				mv.visitMethodInsn(INVOKEVIRTUAL, cName, "eIsSet", "(Lorg/eclipse/emf/ecore/EStructuralFeature;)Z");
				mv.visitInsn(IRETURN);
				mv.visitMaxs(2, 1);
				mv.visitEnd();

				if (feature.isChangeable())
				{
					mv = cw.visitMethod(ACC_PUBLIC, "unset" + tCaseName, "()V", null, null);
					mv.visitCode();
					mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETSTATIC, packLitName, ASMNames.getEStructuralFeatureID(cls, feature),
						ASMNames.getClassSignature(featureEMFType));
					mv.visitMethodInsn(INVOKEVIRTUAL, cName, "eUnset", "(Lorg/eclipse/emf/ecore/EStructuralFeature;)V");
					mv.visitInsn(RETURN);
					mv.visitMaxs(2, 1);
					mv.visitEnd();
				}
			}
		}
		// generate operation impl
		EList<EOperation> operations = cls.getEAllOperations();
		for (EOperation eOp: operations)
		{
		
			EAnnotation eAnnotation = eOp.getEAnnotation(ANN_EMF_PROXY);
			if (eAnnotation != null)
			{
				if (eAnnotation.getDetails().containsKey(KEY_GENERATE))
				{
					if (!Boolean.valueOf(eAnnotation.getDetails().get(KEY_GENERATE)))
					{
						continue;
					}
				}
			}

			EList<EParameter> parameters = eOp.getEParameters();
			StringBuilder description = generateDescription(eOp, settings, parameters);

			mv = cw.visitMethod(ACC_PUBLIC, eOp.getName(), description.toString(), null, null);
			mv.visitCode();

			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, cName, "eClass", "()Lorg/eclipse/emf/ecore/EClass;");
			mv.visitIntInsn(BIPUSH, eOp.getOperationID());
			mv.visitMethodInsn(INVOKEINTERFACE, "org/eclipse/emf/ecore/EClass", "getEOperation",
				"(I)Lorg/eclipse/emf/ecore/EOperation;");
			if (parameters.size() > 0)
			{
				// construct the array
				mv.visitIntInsn(BIPUSH, parameters.size());
				mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
				for (int i = 0; i < parameters.size(); i++)
				{
					mv.visitInsn(DUP);
					mv.visitIntInsn(BIPUSH, i);
					mv.visitVarInsn(ALOAD, i + 1);
					mv.visitInsn(AASTORE);
				}
				mv.visitMethodInsn(INVOKEVIRTUAL, cName, "eExecOperation",
					"(Lorg/eclipse/emf/ecore/EOperation;[Ljava/lang/Object;)Ljava/lang/Object;");
				if (eOp.getEType() != null)
				{
					mv.visitTypeInsn(CHECKCAST, ASMNames.getEClassifierCName(eOp.getEType(), settings));
					mv.visitInsn(ARETURN);
				}
				else
				{
					mv.visitInsn(POP);
					mv.visitInsn(RETURN);
				}
			}
			if (parameters.size() > 0)
			{
				mv.visitMaxs(6, parameters.size() + 1);
			}
			else
			{
				mv.visitMaxs(3, parameters.size() + 1);
			}
			mv.visitEnd();
		}
		cw.visitEnd();
		return cw.toByteArray();
	}

	private static StringBuilder generateDescription(EOperation eOp, IPMBinaryGeneratorSettings settings,
		EList<EParameter> parameters)
	{
		StringBuilder description = new StringBuilder();
		description.append('(');

		for (int i = 0; i < parameters.size(); i++)
		{
			EParameter eParam = parameters.get(i);
			if (eParam.isMany())
			{
				description.append('[');
			}
			description.append(ASMNames.getClassSignature(ASMNames.getEClassifierCName(eParam.getEType(), settings)));
		}
		description.append(')');
		if (eOp.getEType() != null)
		{
			description.append(ASMNames.getClassSignature(ASMNames.getEClassifierCName(eOp.getEType(), settings)));
		}
		else
		{
			description.append('V');
		}

		return description;
	}

	public boolean isSomething()
	{
		Object result = eGet(1);
		return result instanceof Boolean ? ((Boolean) result).booleanValue() : false;
	}

	public void setSomething(boolean arg)
	{
		eSet(0, arg);
	}

	public void eSet(int x, Object value)
	{

	}

	public Object eGet(int x)
	{
		return null;
	}
}
