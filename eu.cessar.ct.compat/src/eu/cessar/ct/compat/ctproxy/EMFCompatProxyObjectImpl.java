package eu.cessar.ct.compat.ctproxy;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.req.Requirement;

/**
 * A subclass of an emf proxy object, for compatibility mode.
 * 
 * @Review uidl7321 - Apr 11, 2012
 */
@Requirement(
	reqID = "REQ_COMPAT#1")
public class EMFCompatProxyObjectImpl extends EMFProxyObjectImpl
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.impl.BasicEObjectImpl#eResource()
	 */
	@Override
	public Resource eResource()
	{
		IMasterObjectWrapper<?> masterWrapper = eGetMasterWrapper();
		if (masterWrapper != null)
		{
			List<?> masterObjects = masterWrapper.getAllMasterObjects();
			if (masterObjects.size() > 0)
			{
				Object master = masterObjects.get(0);
				if (master instanceof EObject)
				{
					return ((EObject) master).eResource();
				}
			}
		}
		return null;
	}
}
