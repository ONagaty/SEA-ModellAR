package eu.cessar.ct.validation.internal.constraints;

import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

public class IdentifiableShortNameCorrectnessConstraint extends AbstractModelConstraint
{

	private static final String REG_EX = "[a-zA-Z]([a-zA-Z0-9]|_[a-zA-Z0-9])*_?"; //$NON-NLS-1$
	private static Pattern pattern = Pattern.compile(REG_EX);

	@Override
	public IStatus validate(IValidationContext ctx)
	{

		EObject target = ctx.getTarget();

		if (target instanceof GIdentifiable)
		{
			GIdentifiable identifiable = (GIdentifiable) target;
			String shortName = identifiable.gGetShortName();
			if (shortName == null)
			{
				return ctx.createFailureStatus("Short name  does not conform to the regular expression"); //$NON-NLS-1$
			}
			Matcher matcher = pattern.matcher(shortName);
			boolean matches = matcher.matches();
			if (matches)
			{

				return ctx.createSuccessStatus();

			}
			else
			{

				return ctx.createFailureStatus("Short name  does not conform to the regular expression"); //$NON-NLS-1$
			}
		}
		return ctx.createSuccessStatus();
	}
}
