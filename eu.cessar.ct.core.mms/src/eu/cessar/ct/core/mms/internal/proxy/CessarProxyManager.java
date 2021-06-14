/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 06.03.2013 15:29:09
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.proxy;


/**
 * Replacement for Sphinx ProxyResolutionBehavior
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Apr 16 19:15:02 2013 %
 * 
 *         %version: 3 %
 */
public class CessarProxyManager // extends ProxyResolutionBehavior
{
	//
	// /**
	// * The instance of the CessarProxyManager
	// */
	// private static final CessarProxyManager eINSTANCE = new CessarProxyManager();
	//
	// /* package */static void install()
	// {
	// // if (ProxyResolutionBehavior.INSTANCE != eINSTANCE)
	// // {
	// // ProxyResolutionBehavior.INSTANCE = eINSTANCE;
	// // }
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// *
	// org.eclipse.sphinx.emf.ecore.proxymanagement.ProxyResolutionBehavior#eResolveProxy(org.eclipse.emf.ecore.EObject,
	// * org.eclipse.emf.ecore.EObject)
	// */
	// @Override
	// public EObject eResolveProxy(EObject contextObject, EObject proxy)
	// {
	// if (proxy == null)
	// {
	// return null;
	// }
	// if (isNoProxy(proxy))
	// {
	// return proxy;
	// }
	// if (proxy instanceof GARObject)
	// {
	// GARObject arProxy = (GARObject) proxy;
	// String ck = arProxy.gGetChecksum();
	// if (ck != null && ck.length() > 0)
	// {
	// long changeTime =
	// ResourceSetListener.eINSTANCE.getLastChangeTime(TransactionUtil.getEditingDomain(contextObject));
	// // try to convert it to a long
	// long lastCheckOfProxy = 0;
	// try
	// {
	// lastCheckOfProxy = Long.valueOf(ck);
	// }
	// catch (NumberFormatException e)
	// {
	// // ignore
	// }
	// if (lastCheckOfProxy > changeTime)
	// {
	// // no need to try to resolve the proxy
	// return proxy;
	// }
	// }
	// // try to resolve the proxy
	// EObject result = super.eResolveProxy(contextObject, proxy);
	// if (result == proxy)
	// {
	// // failed to resolve the proxy, avoid a second try
	// proxy.eSetDeliver(false);
	// try
	// {
	// arProxy.gSetChecksum(String.valueOf(System.currentTimeMillis()));
	// }
	// finally
	// {
	// proxy.eSetDeliver(true);
	// }
	// }
	// return result;
	// }
	// else
	// {
	// return super.eResolveProxy(contextObject, proxy);
	// }
	// }
}
