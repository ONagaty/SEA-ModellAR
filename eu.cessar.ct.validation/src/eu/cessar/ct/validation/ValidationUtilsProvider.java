package eu.cessar.ct.validation;

import eu.cessar.ct.core.platform.util.IServiceProvider;
import eu.cessar.ct.sdk.utils.ValidationUtils;
import eu.cessar.ct.validation.internal.SDKValidationUtils;

/**
 * Service provider for ValidationUtils in the SDK.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Mon Sep  9 15:20:37 2013 %
 * 
 *         %version: 2 %
 */
public class ValidationUtilsProvider implements IServiceProvider<ValidationUtils.Service>
{
	public SDKValidationUtils getService(Class<ValidationUtils.Service> serviceClass, Object... args)
	{
		return SDKValidationUtils.eINSTANCE;
	}

}
