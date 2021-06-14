package eu.cessar.ct.jet.core.internal.compiler;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import eu.cessar.ct.jet.core.compiler.IJETServicesProvider;

class Java2JetMethodTransformer extends MethodVisitor
{
	private IJETServicesProvider jetServicesProvider;

	/**
	 *
	 * @param mv
	 * @param jetServicesProvider
	 */
	public Java2JetMethodTransformer(final MethodVisitor mv, final IJETServicesProvider jetServicesProvider)
	{
		super(Opcodes.ASM5, mv);
		this.jetServicesProvider = jetServicesProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.MethodVisitor#visitLineNumber(int, org.objectweb.asm.Label)
	 */
	@Override
	public void visitLineNumber(final int line, final Label start)
	{
		int jetLineNumber = jetServicesProvider.convertJava2JetLine(line);
		mv.visitLineNumber(jetLineNumber, start);
	}
}
