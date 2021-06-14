/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 19, 2010 4:56:16 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.asm;

import org.objectweb.asm.Opcodes;

/**
 * String constants used by generators
 */
@SuppressWarnings("nls")
public interface Constants
{
	String DOT = ".";

	String SLASH = "/";

	String DOLAR = "$";

	String ANN_EMF_PROXY = "EMF_PROXY";

	String KEY_GENERATE = "GENERATE";

	String CLASS_EXT = ".class";

	String PACKAGE_IMPL = "impl";

	String FIELD_EINSTANCE = "eINSTANCE";

	String FIELD_ENAME = "eNAME";

	String FIELD_ENS_URI = "eNS_URI";

	String FIELD_ENS_PREFIX = "eNS_PREFIX";

	String FIELD_IMPL_EINSTANCE = "eInstance";

	String FIELD_IMPL_EWRAPPEDINSTANCE = "eWrappedInstance";

	String FIELD_IMPL_ISINITED = "isInited";

	String PREFIX_CREATE = "create";

	String PREFIX_IS = "is";

	String PREFIX_GET = "get";

	String METHOD_CONSTRUCTOR = "<init>";

	String METHOD_CLASS_CONSTRUCTOR = "<clinit>";

	String METHOD_INIT = "init";

	/**
	 * Signature for "()V"
	 */
	String SIG_M_VOID_VOID = ASMNames.getMethodSignature(Void.TYPE);

	int ACC_IFACE_FIELD = Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC;

	int ACC_IFACE_METHOD = Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT;

	String SUFFIX_FACTORY = "Factory";

	String SUFFIX_PACKAGE = "Package";

	String IFACE_LITERALS = "Literals";

	String SUFFIX_IMPL = "Impl";

	String CSNAME_ILLEGAL_ARGUMENT_EXCEPTION = "java/lang/IllegalArgumentException";

	String METHOD_MSG_NOT_A_VALID_CLASSIFIER = "getNotAValidClassifierMessage";

	String CSNAME_PM_RUNTIME_MESSAGES = "eu/cessar/ct/runtime/ecuc/internal/pm/PMRuntimeMessages";

	String CSNAME_IEMFPROXYOBJECT = "eu/cessar/ct/sdk/pm/IEMFProxyObject";

	String CSNAME_EMFPROXYOBJECT_IMPL = "eu/cessar/ct/emfproxy/impl/EMFProxyObjectImpl";

	String CSNAME_PMRUNTIMEUTILS = "eu/cessar/ct/runtime/ecuc/internal/pm/PMRuntimeUtils";

	String METHOD_PMU_GETPMPACKAGE = "getPMPackage";

	String SIG_PMU_GETPMPACKAGE = "(Ljava/lang/String;)Lorg/eclipse/emf/ecore/EPackage;";

	String METHOD_PMU_UPDATEPMFACTORY = "updatePMFactory";

	String SIG_PMU_UPDATEPMFACTORY = "(Lorg/eclipse/emf/ecore/EFactory;Ljava/lang/String;)V";

	String CSNAME_OBJECT = "java/lang/Object";

	String CSNAME_ECLASS = "org/eclipse/emf/ecore/EClass";

	/**
	 * "Lorg/eclipse/emf/ecore/EClass;"
	 */
	String CSNAME_ECLASS_SIG = ASMNames.getClassSignature(CSNAME_ECLASS);

	String CSNAME_EDATATYPE = "org/eclipse/emf/ecore/EDataType";

	String CSNAME_EENUM = "org/eclipse/emf/ecore/EEnum";

	String CSNAME_EREFERENCE = "org/eclipse/emf/ecore/EReference";

	String CSNAME_EATTRIBUTE = "org/eclipse/emf/ecore/EAttribute";

	String CSNAME_EFACTORY = "org/eclipse/emf/ecore/EFactory";

	String CSNAME_EFACTORY_IMPL = "org/eclipse/emf/ecore/impl/EFactoryImpl";

	String CSNAME_EPACKAGE = "org/eclipse/emf/ecore/EPackage";

	/**
	 * "Lorg/eclipse/emf/ecore/EPackage;"
	 */
	String CSNAME_EPACKAGE_SIG = ASMNames.getClassSignature(CSNAME_EPACKAGE);

	String CSNAME_EPACKAGE_IMPL = "org/eclipse/emf/ecore/impl/EPackageImpl";

	String CSNAME_ELIST = "org/eclipse/emf/common/util/EList";
}
