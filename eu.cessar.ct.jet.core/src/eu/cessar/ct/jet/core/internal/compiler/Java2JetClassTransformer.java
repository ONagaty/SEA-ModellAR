package eu.cessar.ct.jet.core.internal.compiler;

import org.eclipse.core.resources.IFile;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import eu.cessar.ct.jet.core.compiler.IJETServicesProvider;

/**
 * Class used to instrument Java binary code with JET information (file name and line numbers).
 *
 */
public class Java2JetClassTransformer extends ClassVisitor
{
	IJETServicesProvider jetServicesProvider;
	IFile jetFile;

	/**
	 * @param cv
	 * @param jetServicesProvider
	 * @param jetFile
	 */
	public Java2JetClassTransformer(ClassVisitor cv, final IJETServicesProvider jetServicesProvider, final IFile jetFile)
	{
		super(Opcodes.ASM5, cv);
		this.jetServicesProvider = jetServicesProvider;
		this.jetFile = jetFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.ClassVisitor#visitSource(java.lang.String, java.lang.String)
	 */
	@Override
	public void visitSource(final String source, final String debug)
	{
		cv.visitSource(jetFile.getName(), debug);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.objectweb.asm.ClassVisitor#visitMethod(int, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String[])
	 */
	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
		final String[] exceptions)
	{
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		return new Java2JetMethodTransformer(mv, jetServicesProvider);
	}
}
