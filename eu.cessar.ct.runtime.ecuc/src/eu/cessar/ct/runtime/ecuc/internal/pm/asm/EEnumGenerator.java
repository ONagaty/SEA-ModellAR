/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 25, 2010 10:55:08 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.asm;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 *
 */
public class EEnumGenerator implements Opcodes, Constants
{
	/* package */static class EnumBinWrapper extends AbstractPMBinaryClassWrapper<EEnum>
	{
		public EnumBinWrapper(IPMBinaryGeneratorSettings settings, EEnum element)
		{
			super(settings, element, false);
		}

		@Override
		protected String generateClassFileName(EEnum elem)
		{
			String name = ASMNames.getEEnumCName(SLASH, elem) + CLASS_EXT;
			return name;
		}

		@Override
		protected String generateClassName(EEnum elem)
		{
			String name = ASMNames.getEEnumCName(DOT, elem);
			return name;
		}

		@Override
		protected byte[] generateBinary(EEnum elem, boolean genImplCode)
		{
			return genEEnumClass(getSettings(), elem);
		}
	}

	/**
	 * @param settings
	 * @param eEnum
	 * @return
	 */
	public static byte[] genEEnumClass(IPMBinaryGeneratorSettings settings, EEnum eEnum)
	{
		if (settings.isUsingNewEnum())
		{
			return doGetEEnumAsJavaEnum(settings, eEnum);
		}
		else
		{
			return doGetEEnumAsEMFEnum(settings, eEnum);
		}
	}

	/**
	 * @param settings
	 * @param eEnum
	 * @return
	 */
	@SuppressWarnings("nls")
	private static byte[] doGetEEnumAsJavaEnum(IPMBinaryGeneratorSettings settings, EEnum eEnum)
	{
		String cName = ASMNames.getEEnumCName(eEnum);

		ClassWriter cw = new ClassWriter(0);

		cw.visit(V1_5, ACC_PUBLIC + ACC_FINAL + ACC_SUPER + ACC_ENUM, cName, "Ljava/lang/Enum<L" + cName
			+ ";>;Lorg/eclipse/emf/common/util/Enumerator;", "java/lang/Enum",
			new String[] {"org/eclipse/emf/common/util/Enumerator"});

		EList<EEnumLiteral> literals = eEnum.getELiterals();
		// the enum literals declaration, no initialization
		for (int i = 0; i < literals.size(); i++)
		{
			EEnumLiteral enumLiteral = literals.get(i);
			FieldVisitor fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC + ACC_ENUM,
				ASMNames.getELiteralID(enumLiteral), ASMNames.getClassSignature(cName), null, null);
			fv.visitEnd();
		}
		// the enum integer value
		for (int i = 0; i < literals.size(); i++)
		{
			EEnumLiteral enumLiteral = literals.get(i);
			FieldVisitor fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, ASMNames.getELiteralID(enumLiteral)
				+ "_VALUE", "I", null, new Integer(i));
			fv.visitEnd();
		}
		// enum fields
		{
			FieldVisitor fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "VALUES_ARRAY",
				"[" + ASMNames.getClassSignature(cName), null, null);
			fv.visitEnd();

			fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "VALUES", "Ljava/util/List;", "Ljava/util/List<"
				+ ASMNames.getClassSignature(cName) + ">;", null);
			fv.visitEnd();

			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "value", "I", null, null);
			fv.visitEnd();

			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "name", "Ljava/lang/String;", null, null);
			fv.visitEnd();

			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "literal", "Ljava/lang/String;", null, null);
			fv.visitEnd();

			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC + ACC_SYNTHETIC, "ENUM$VALUES",
				"[" + ASMNames.getClassSignature(cName), null, null);
			fv.visitEnd();
		}
		// the class init method
		{
			MethodVisitor mv = cw.visitMethod(ACC_STATIC, METHOD_CLASS_CONSTRUCTOR, SIG_M_VOID_VOID, null, null);
			mv.visitCode();

			// the initialization of the enum literals
			for (int i = 0; i < literals.size(); i++)
			{
				EEnumLiteral enumLiteral = literals.get(i);
				mv.visitTypeInsn(NEW, cName);
				mv.visitInsn(DUP);
				mv.visitLdcInsn(ASMNames.getELiteralID(enumLiteral));
				mv.visitLdcInsn(new Integer(i));
				mv.visitLdcInsn(new Integer(i));
				mv.visitLdcInsn(enumLiteral.getName());
				mv.visitLdcInsn(enumLiteral.getName());
				mv.visitMethodInsn(INVOKESPECIAL, cName, METHOD_CONSTRUCTOR,
					"(Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;)V");
				mv.visitFieldInsn(PUTSTATIC, cName, ASMNames.getELiteralID(enumLiteral),
					ASMNames.getClassSignature(cName));
			}

			// initialize the ENUM$VALUES array
			mv.visitLdcInsn(literals.size());
			mv.visitTypeInsn(ANEWARRAY, cName);
			for (int i = 0; i < literals.size(); i++)
			{
				EEnumLiteral enumLiteral = literals.get(i);
				mv.visitInsn(DUP);
				mv.visitLdcInsn(i);
				mv.visitFieldInsn(GETSTATIC, cName, ASMNames.getELiteralID(enumLiteral),
					ASMNames.getClassSignature(cName));
				mv.visitInsn(AASTORE);
			}
			mv.visitFieldInsn(PUTSTATIC, cName, "ENUM$VALUES", "[" + ASMNames.getClassSignature(cName));

			// initialize the VALUES_ARRAY array
			mv.visitLdcInsn(literals.size());
			mv.visitTypeInsn(ANEWARRAY, cName);
			for (int i = 0; i < literals.size(); i++)
			{
				EEnumLiteral enumLiteral = literals.get(i);
				mv.visitInsn(DUP);
				mv.visitLdcInsn(i);
				mv.visitFieldInsn(GETSTATIC, cName, ASMNames.getELiteralID(enumLiteral),
					ASMNames.getClassSignature(cName));
				mv.visitInsn(AASTORE);
			}
			mv.visitFieldInsn(PUTSTATIC, cName, "VALUES_ARRAY", "[" + ASMNames.getClassSignature(cName));

			// initialize the List<Enum> VALUES list

			mv.visitFieldInsn(GETSTATIC, cName, "VALUES_ARRAY", "[" + ASMNames.getClassSignature(cName));
			mv.visitMethodInsn(INVOKESTATIC, "java/util/Arrays", "asList", "([Ljava/lang/Object;)Ljava/util/List;");
			mv.visitMethodInsn(INVOKESTATIC, "java/util/Collections", "unmodifiableList",
				"(Ljava/util/List;)Ljava/util/List;");
			mv.visitFieldInsn(PUTSTATIC, cName, "VALUES", "Ljava/util/List;");

			mv.visitMaxs(7, 0);
			mv.visitInsn(RETURN);
			mv.visitEnd();
		}

		// CODE:
		// public static Enum0 get(String literal)
		// {
		// for (int i = 0; i < VALUES_ARRAY.length; ++i)
		// {
		// Enum0 result = VALUES_ARRAY[i];
		// if (result.toString().equals(literal))
		// {
		// return result;
		// }
		// }
		// return null;
		// }
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "get",
				ASMNames.getMethodSignature(cName, Type.getDescriptor(String.class)), null, null);
			mv.visitCode();
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 1);

			Label ifCndLabel = new Label();
			mv.visitJumpInsn(GOTO, ifCndLabel);
			Label ifStartLabel = new Label();
			mv.visitLabel(ifStartLabel);
			mv.visitFieldInsn(GETSTATIC, cName, "VALUES_ARRAY", "[" + ASMNames.getClassSignature(cName));
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(AALOAD);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, cName, "toString", "()Ljava/lang/String;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z");
			Label elseIfLabel = new Label();
			mv.visitJumpInsn(IFEQ, elseIfLabel);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitInsn(ARETURN);
			mv.visitLabel(elseIfLabel);
			mv.visitIincInsn(1, 1);
			mv.visitLabel(ifCndLabel);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitFieldInsn(GETSTATIC, cName, "VALUES_ARRAY", "[" + ASMNames.getClassSignature(cName));
			mv.visitInsn(ARRAYLENGTH);
			mv.visitJumpInsn(IF_ICMPLT, ifStartLabel);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);

			mv.visitMaxs(2, 3);
			mv.visitEnd();
		}
		// CODE:
		// public static Enum0 getByName(String literal)
		// {
		// for (int i = 0; i < VALUES_ARRAY.length; ++i)
		// {
		// Enum0 result = VALUES_ARRAY[i];
		// if (result.getName().equals(literal))
		// {
		// return result;
		// }
		// }
		// return null;
		// }
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "getByName",
				ASMNames.getMethodSignature(cName, Type.getDescriptor(String.class)), null, null);
			mv.visitCode();
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 1);

			Label ifCndLabel = new Label();
			mv.visitJumpInsn(GOTO, ifCndLabel);
			Label ifStartLabel = new Label();
			mv.visitLabel(ifStartLabel);
			mv.visitFieldInsn(GETSTATIC, cName, "VALUES_ARRAY", "[" + ASMNames.getClassSignature(cName));
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(AALOAD);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, cName, "getName", "()Ljava/lang/String;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z");
			Label elseIfLabel = new Label();
			mv.visitJumpInsn(IFEQ, elseIfLabel);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitInsn(ARETURN);
			mv.visitLabel(elseIfLabel);
			mv.visitIincInsn(1, 1);
			mv.visitLabel(ifCndLabel);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitFieldInsn(GETSTATIC, cName, "VALUES_ARRAY", "[" + ASMNames.getClassSignature(cName));
			mv.visitInsn(ARRAYLENGTH);
			mv.visitJumpInsn(IF_ICMPLT, ifStartLabel);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);

			mv.visitMaxs(2, 3);
			mv.visitEnd();
		}
		// public static Enum0 get(int value)
		// {
		// switch (value)
		// {
		// case E0_1_VALUE: return E0_1;
		// case E0_2_VALUE: return E0_2;
		// }
		// return null;
		// }
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "get",
				"(I)" + ASMNames.getClassSignature(cName), null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitVarInsn(ILOAD, 0);
			Label[] switchCases = new Label[literals.size()];
			for (int i = 0; i < switchCases.length; i++)
			{
				switchCases[i] = new Label();
			}
			Label switchElse = new Label();
			mv.visitTableSwitchInsn(0, literals.size() - 1, switchElse, switchCases);
			for (int i = 0; i < literals.size(); i++)
			{
				EEnumLiteral literal = literals.get(i);
				mv.visitLabel(switchCases[i]);
				mv.visitFieldInsn(GETSTATIC, cName, ASMNames.getELiteralID(literal), ASMNames.getClassSignature(cName));
				mv.visitInsn(ARETURN);
			}
			mv.visitLabel(switchElse);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);

			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}

		// the private enum constructor
		{
			MethodVisitor mv = cw.visitMethod(ACC_PRIVATE, METHOD_CONSTRUCTOR,
				"(Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Enum", "<init>", "(Ljava/lang/String;I)V");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitFieldInsn(PUTFIELD, cName, "value", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitFieldInsn(PUTFIELD, cName, "name", "Ljava/lang/String;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitFieldInsn(PUTFIELD, cName, "literal", "Ljava/lang/String;");
			mv.visitInsn(RETURN);

			mv.visitMaxs(3, 6);
			mv.visitEnd();
		}
		// public int getValue()
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getValue", "()I", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cName, "value", "I");
			mv.visitInsn(IRETURN);

			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		// public String getName()
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getName", "()Ljava/lang/String;", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cName, "name", "Ljava/lang/String;");
			mv.visitInsn(ARETURN);

			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		// public String getLiteral()
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getLiteral", "()Ljava/lang/String;", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cName, "literal", "Ljava/lang/String;");
			mv.visitInsn(ARETURN);

			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		// public String toString()
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cName, "literal", "Ljava/lang/String;");
			mv.visitInsn(ARETURN);

			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}

		// public Enum[] values()
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "values",
				"()[" + ASMNames.getClassSignature(cName), null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitFieldInsn(GETSTATIC, cName, "ENUM$VALUES", "[" + ASMNames.getClassSignature(cName));
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 0);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ARRAYLENGTH);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ISTORE, 1);
			mv.visitTypeInsn(ANEWARRAY, cName);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "arraycopy",
				"(Ljava/lang/Object;ILjava/lang/Object;II)V");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitInsn(ARETURN);

			mv.visitMaxs(5, 3);
			mv.visitEnd();
		}

		// public static Enum valueOf(String)
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "valueOf",
				"(Ljava/lang/String;)" + ASMNames.getClassSignature(cName), null, null);
			mv.visitCode();
			mv.visitLdcInsn(Type.getType(ASMNames.getClassSignature(cName)));
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Enum", "valueOf",
				"(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;");
			mv.visitTypeInsn(CHECKCAST, cName);
			mv.visitInsn(ARETURN);

			mv.visitMaxs(2, 1);
			mv.visitEnd();
		}

		return cw.toByteArray();
	}

	/**
	 * @param settings
	 * @param eEnum
	 * @return
	 */
	@SuppressWarnings("nls")
	private static byte[] doGetEEnumAsEMFEnum(IPMBinaryGeneratorSettings settings, EEnum eEnum)
	{
		String cName = ASMNames.getEEnumCName(eEnum);

		ClassWriter cw = new ClassWriter(0);

		cw.visit(V1_5, ACC_PUBLIC + ACC_FINAL + ACC_SUPER, cName, null,
			"org/eclipse/emf/common/util/AbstractEnumerator", null);

		EList<EEnumLiteral> literals = eEnum.getELiterals();
		// the enum integer values
		for (int i = 0; i < literals.size(); i++)
		{
			// public static final int ENUM.... = #;
			EEnumLiteral enumLiteral = literals.get(i);
			FieldVisitor fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, ASMNames.getELiteralID(enumLiteral),
				"I", null, new Integer(i));
			fv.visitEnd();
		}

		// the enum constants
		for (int i = 0; i < literals.size(); i++)
		{
			// public static final <CName> ENUM..._LITERAL;
			EEnumLiteral enumLiteral = literals.get(i);
			FieldVisitor fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, ASMNames.getELiteralID(enumLiteral)
				+ "_LITERAL", "L" + cName + ";", null, null);
			fv.visitEnd();
		}

		{
			FieldVisitor fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "VALUES_ARRAY", "[L" + cName + ";",
				null, null);
			fv.visitEnd();
		}
		{
			// public static final List VALUES
			FieldVisitor fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "VALUES", "Ljava/util/List;", null,
				null);
			fv.visitEnd();
		}
		{
			// the class init method
			MethodVisitor mv = cw.visitMethod(ACC_STATIC, METHOD_CLASS_CONSTRUCTOR, SIG_M_VOID_VOID, null, null);
			mv.visitCode();

			// init the enums literals
			for (int i = 0; i < literals.size(); i++)
			{
				EEnumLiteral enumLiteral = literals.get(i);
				mv.visitTypeInsn(NEW, cName);
				mv.visitInsn(DUP);
				mv.visitLdcInsn(new Integer(i));
				mv.visitLdcInsn(enumLiteral.getName());
				mv.visitLdcInsn(enumLiteral.getName());
				mv.visitMethodInsn(INVOKESPECIAL, cName, METHOD_CONSTRUCTOR, "(ILjava/lang/String;Ljava/lang/String;)V");
				mv.visitFieldInsn(PUTSTATIC, cName, ASMNames.getELiteralID(enumLiteral) + "_LITERAL",
					ASMNames.getClassSignature(cName));
			}
			// initialize the VALUES_ARRAY array
			{
				mv.visitLdcInsn(literals.size());
				mv.visitTypeInsn(ANEWARRAY, cName);
				for (int i = 0; i < literals.size(); i++)
				{
					EEnumLiteral enumLiteral = literals.get(i);
					mv.visitInsn(DUP);
					mv.visitLdcInsn(i);
					mv.visitFieldInsn(GETSTATIC, cName, ASMNames.getELiteralID(enumLiteral) + "_LITERAL",
						ASMNames.getClassSignature(cName));
					mv.visitInsn(AASTORE);
				}
				mv.visitFieldInsn(PUTSTATIC, cName, "VALUES_ARRAY", "[" + ASMNames.getClassSignature(cName));
			}

			{
				// initialize the List<Enum> VALUES list
				mv.visitFieldInsn(GETSTATIC, cName, "VALUES_ARRAY", "[" + ASMNames.getClassSignature(cName));
				mv.visitMethodInsn(INVOKESTATIC, "java/util/Arrays", "asList", "([Ljava/lang/Object;)Ljava/util/List;");
				mv.visitMethodInsn(INVOKESTATIC, "java/util/Collections", "unmodifiableList",
					"(Ljava/util/List;)Ljava/util/List;");
				mv.visitFieldInsn(PUTSTATIC, cName, "VALUES", "Ljava/util/List;");
			}
			mv.visitInsn(RETURN);
			mv.visitMaxs(5, 0);
			mv.visitEnd();
		}
		// CODE:
		// public static Enum0 get(String literal)
		// {
		// for (int i = 0; i < VALUES_ARRAY.length; ++i)
		// {
		// Enum0 result = VALUES_ARRAY[i];
		// if (result.toString().equals(literal))
		// {
		// return result;
		// }
		// }
		// return null;
		// }
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "get",
				ASMNames.getMethodSignature(cName, Type.getDescriptor(String.class)), null, null);
			mv.visitCode();
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 1);

			Label ifCndLabel = new Label();
			mv.visitJumpInsn(GOTO, ifCndLabel);
			Label ifStartLabel = new Label();
			mv.visitLabel(ifStartLabel);
			mv.visitFieldInsn(GETSTATIC, cName, "VALUES_ARRAY", "[" + ASMNames.getClassSignature(cName));
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(AALOAD);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, cName, "toString", "()Ljava/lang/String;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z");
			Label elseIfLabel = new Label();
			mv.visitJumpInsn(IFEQ, elseIfLabel);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitInsn(ARETURN);
			mv.visitLabel(elseIfLabel);
			mv.visitIincInsn(1, 1);
			mv.visitLabel(ifCndLabel);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitFieldInsn(GETSTATIC, cName, "VALUES_ARRAY", "[" + ASMNames.getClassSignature(cName));
			mv.visitInsn(ARRAYLENGTH);
			mv.visitJumpInsn(IF_ICMPLT, ifStartLabel);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);

			mv.visitMaxs(2, 3);
			mv.visitEnd();
		}
		// CODE:
		// public static Enum0 getByName(String literal)
		// {
		// for (int i = 0; i < VALUES_ARRAY.length; ++i)
		// {
		// Enum0 result = VALUES_ARRAY[i];
		// if (result.getName().equals(literal))
		// {
		// return result;
		// }
		// }
		// return null;
		// }
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "getByName",
				ASMNames.getMethodSignature(cName, Type.getDescriptor(String.class)), null, null);
			mv.visitCode();
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 1);

			Label ifCndLabel = new Label();
			mv.visitJumpInsn(GOTO, ifCndLabel);
			Label ifStartLabel = new Label();
			mv.visitLabel(ifStartLabel);
			mv.visitFieldInsn(GETSTATIC, cName, "VALUES_ARRAY", "[" + ASMNames.getClassSignature(cName));
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(AALOAD);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, cName, "getName", "()Ljava/lang/String;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z");
			Label elseIfLabel = new Label();
			mv.visitJumpInsn(IFEQ, elseIfLabel);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitInsn(ARETURN);
			mv.visitLabel(elseIfLabel);
			mv.visitIincInsn(1, 1);
			mv.visitLabel(ifCndLabel);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitFieldInsn(GETSTATIC, cName, "VALUES_ARRAY", "[" + ASMNames.getClassSignature(cName));
			mv.visitInsn(ARRAYLENGTH);
			mv.visitJumpInsn(IF_ICMPLT, ifStartLabel);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);

			mv.visitMaxs(2, 3);
			mv.visitEnd();
		}

		// public static Enum0 get(int value)
		// {
		// switch (value)
		// {
		// case E0_1_VALUE: return E0_1;
		// case E0_2_VALUE: return E0_2;
		// }
		// return null;
		// }
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "get",
				"(I)" + ASMNames.getClassSignature(cName), null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitVarInsn(ILOAD, 0);
			Label[] switchCases = new Label[literals.size()];
			for (int i = 0; i < switchCases.length; i++)
			{
				switchCases[i] = new Label();
			}
			Label switchElse = new Label();
			mv.visitTableSwitchInsn(0, literals.size() - 1, switchElse, switchCases);
			for (int i = 0; i < literals.size(); i++)
			{
				EEnumLiteral literal = literals.get(i);
				mv.visitLabel(switchCases[i]);
				mv.visitFieldInsn(GETSTATIC, cName, ASMNames.getELiteralID(literal) + "_LITERAL",
					ASMNames.getClassSignature(cName));
				mv.visitInsn(ARETURN);
			}
			mv.visitLabel(switchElse);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);

			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		// the private enum constructor
		{
			MethodVisitor mv = cw.visitMethod(ACC_PRIVATE, Constants.METHOD_CONSTRUCTOR,
				"(ILjava/lang/String;Ljava/lang/String;)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKESPECIAL, "org/eclipse/emf/common/util/AbstractEnumerator", "<init>",
				"(ILjava/lang/String;Ljava/lang/String;)V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(4, 4);
			mv.visitEnd();
		}
		return cw.toByteArray();
	}

}
