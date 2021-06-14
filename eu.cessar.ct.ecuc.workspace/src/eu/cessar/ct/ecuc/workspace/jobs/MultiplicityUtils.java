package eu.cessar.ct.ecuc.workspace.jobs;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.ecuc.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.internal.Messages;
import gautosar.gecucparameterdef.GParamConfMultiplicity;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.math.BigInteger;

import org.eclipse.osgi.util.NLS;

/**
 * Introduced to handle invalid multiplicity data, e.g.:
 * 
 * <li>lower multiplicity star</li>
 * 
 * <li>lower multiplicity >1, upper multiplicity missing</li>
 * 
 * <li>lower multiplicity > upper multiplicity</li>
 * 
 * <li>upper multiplicity 0</li>
 * 
 * @author tiziana_brinzas
 * 
 */
public final class MultiplicityUtils
{
	public static final long LIMIT_ON_MULTIPLICITY_STAR = 8;

	private MultiplicityUtils()
	{
		/* Hidden constructor */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IEcucMMService#getLowerMultiplicity(gautosar.gecucparameterdef.GParamConfMultiplicity,
	 * java.math.BigInteger, boolean)
	 */
	public static BigInteger getLowerMultiplicity(GParamConfMultiplicity multiplicity)
	{
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(multiplicity).getEcucMMService();
		/* Satisfies_ecuc_sws_2009 from AUTOSAR_TPS_ECUConfiguration.pdf */
		BigInteger lower = ecucMMService.getLowerMultiplicity(multiplicity, BigInteger.ONE, true);
		BigInteger upper = ecucMMService.getUpperMultiplicity(multiplicity, BigInteger.ONE, true);

		// Lower multiplicity infinite: log the error and continue
		if (lower.equals(IEcucMMService.MULTIPLICITY_STAR))
		{
			CessarPluginActivator.getDefault().logError(invalidMultiplicity(multiplicity));
		}

		// Upper multiplicity zero: log the error and continue
		if (upper.equals(BigInteger.ZERO))
		{
			CessarPluginActivator.getDefault().logError(invalidMultiplicity(multiplicity));
		}

		if (isGreaterThan(lower, upper))
		{
			// Lower multiplicity bigger than upper multiplicity: log the error
			// and return the smallest value (i.e. upper multiplicity)
			CessarPluginActivator.getDefault().logError(invalidMultiplicity(multiplicity));
			return upper;
		}
		return lower;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IEcucMMService#getUpperMultiplicity(gautosar.gecucparameterdef.GParamConfMultiplicity,
	 * java.math.BigInteger, boolean)
	 */
	public static BigInteger getUpperMultiplicity(GParamConfMultiplicity multiplicity)
	{
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(multiplicity).getEcucMMService();
		/* Satisfies_ecuc_sws_2009 from AUTOSAR_TPS_ECUConfiguration.pdf */
		BigInteger lower = ecucMMService.getLowerMultiplicity(multiplicity, BigInteger.ONE, true);
		BigInteger upper = ecucMMService.getUpperMultiplicity(multiplicity, BigInteger.ONE, true);

		// Lower multiplicity infinite: log the error and continue
		if (lower.equals(IEcucMMService.MULTIPLICITY_STAR))
		{
			CessarPluginActivator.getDefault().logError(invalidMultiplicity(multiplicity));
		}

		// Upper multiplicity zero: log the error and continue
		if (upper.equals(BigInteger.ZERO))
		{
			CessarPluginActivator.getDefault().logError(invalidMultiplicity(multiplicity));
		}

		if (isSmallerThan(upper, lower))
		{
			// Upper multiplicity smaller than lower multiplicity: log the error
			// and return the biggest value (i.e. lower multiplicity)
			CessarPluginActivator.getDefault().logError(invalidMultiplicity(multiplicity));
			return lower;
		}
		return upper;
	}

	public static boolean isZero(BigInteger value)
	{
		return value.equals(BigInteger.ZERO);
	}

	public static boolean isInfinite(BigInteger value)
	{
		return value.equals(IEcucMMService.MULTIPLICITY_STAR);
	}

	private static boolean isGreaterThan(BigInteger value1, BigInteger value2)
	{
		if (value1.equals(value2))
		{
			return false;
		}
		if (isInfinite(value1))
		{
			return true;
		}
		if (isInfinite(value2))
		{
			return false;
		}
		return value1.longValue() > value2.longValue();
	}

	private static boolean isSmallerThan(BigInteger value1, BigInteger value2)
	{
		if (value1.equals(value2))
		{
			return false;
		}
		if (isInfinite(value1))
		{
			return false;
		}
		if (isInfinite(value2))
		{
			return true;
		}
		return value1.longValue() < value2.longValue();
	}

	private static String invalidMultiplicity(GParamConfMultiplicity multiplicity)
	{
		String objectName = (multiplicity instanceof GIdentifiable ? ((GIdentifiable) multiplicity).gGetShortName()
			: multiplicity.toString());

		String lower = multiplicity.gGetLowerMultiplicityAsString();
		if (null == lower)
		{
			lower = Messages.Error_InvalidMultiplicityMissing;
		}
		String upper = multiplicity.gGetUpperMultiplicityAsString();
		if (null == upper)
		{
			upper = Messages.Error_InvalidMultiplicityMissing;
		}

		String errorDetails = NLS.bind(Messages.Error_InvalidMultiplicityDetails, lower, upper);
		return NLS.bind(Messages.Error_InvalidMultiplicity, objectName, errorDetails);
	}
}
