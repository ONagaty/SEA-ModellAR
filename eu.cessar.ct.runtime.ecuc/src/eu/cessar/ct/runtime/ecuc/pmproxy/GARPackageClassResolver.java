package eu.cessar.ct.runtime.ecuc.pmproxy;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GPackageObjectWrapper;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

/**
 * 
 */
public class GARPackageClassResolver extends AbstractClassResolver<GARPackage>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getMasterClassType(eu.cessar.ct.emfproxy.IEMFProxyEngine)
	 */
	public Class<GARPackage> getMasterClassType(IEMFProxyEngine engine)
	{
		return GARPackage.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getEClass(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	public EClass getEClass(IEMFProxyEngine engine, Object context, GARPackage master,
		Map<String, Object> parameters)
	{
		IEcucPresentationModel model = getEcucPresentationModel(engine);
		return (EClass) model.getPMClassifier(master);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.sdk.pm.IEMFProxyObject, java.lang.Object, java.util.Map)
	 */
	public IMasterObjectWrapper<GARPackage> getWrapper(IEMFProxyEngine engine, Object context,
		IEMFProxyObject slave, GARPackage master, Map<String, Object> parameters)
	{
		IEcucModel model = getEcucModel(engine);
		List<GARPackage> packs = model.getSplitedPackagesWithModuleCfg(MetaModelUtils.getAbsoluteQualifiedName(master));
		return new GPackageObjectWrapper(engine, packs);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#createWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public IMasterObjectWrapper<GARPackage> createWrapper(IEMFProxyEngine proxyEngine,
		Object context, EMFProxyObjectImpl emfProxyObjectImpl)
	{
		throw new PMRuntimeException("Cannot create packages using the presentation model"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#supportMultiContext(eu.cessar.ct.emfproxy.IEMFProxyEngine)
	 */
	public boolean supportMultiContext(IEMFProxyEngine engine)
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getStandardContext(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.sdk.pm.IEMFProxyObject)
	 */
	public Object getStandardContext(IEMFProxyEngine engine, IEMFProxyObject slave)
	{
		// no support for multi context
		return DEFAULT_CONTEXT;
	}
}
