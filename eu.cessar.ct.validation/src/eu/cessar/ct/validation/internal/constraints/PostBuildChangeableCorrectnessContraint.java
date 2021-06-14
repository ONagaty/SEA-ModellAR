/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4020<br/>
 * Apr 16, 2014 2:08:21 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.internal.constraints;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import gautosar.gecucparameterdef.GContainerDef;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

/**
 * Constraint for asserting the way the postBuildChangeable attribute is set
 *
 * @author uidg4020
 *
 *         %created_by: uidg4020 %
 *
 *         %date_created: Thu Jun 26 16:36:11 2014 %
 *
 *         %version: 2 %
 */
public class PostBuildChangeableCorrectnessContraint extends AbstractModelConstraint
{

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse.emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(IValidationContext ctx)
	{
		EObject target = ctx.getTarget();

		if (target instanceof GContainerDef)
		{
			GContainerDef container = (GContainerDef) target;
			Boolean gGetPostBuildChangeable = container.gGetPostBuildChangeable();
			if (gGetPostBuildChangeable)
			{
				boolean canSetPostBuild = EcucMetaModelUtils.isLocatedInMultipleConfigurationContainer(container);
				if (!canSetPostBuild)
				{
					return ctx.createFailureStatus(Messages.postBuildChangeableCorrectnessContraint);
				}
			}
			return ctx.createSuccessStatus();
		}
		return null;
	}

}
