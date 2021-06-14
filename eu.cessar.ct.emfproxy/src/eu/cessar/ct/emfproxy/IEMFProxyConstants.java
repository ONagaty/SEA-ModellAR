package eu.cessar.ct.emfproxy;

import org.eclipse.emf.ecore.EStructuralFeature;

public interface IEMFProxyConstants
{
	public static final Object NULL_VALUE = EStructuralFeature.Internal.DynamicValueHolder.NIL;

	public static final String PROXY_ANN_NAME = "EMF_PROXY"; //$NON-NLS-1$

	public static final String ANN_KEY_TYPE = "TYPE"; //$NON-NLS-1$

	public static final Object DEFAULT_CONTEXT = new Object()
	{

		@Override
		public String toString()
		{
			return "DEFAULT_CONTEXT"; //$NON-NLS-1$
		}

	};

}
