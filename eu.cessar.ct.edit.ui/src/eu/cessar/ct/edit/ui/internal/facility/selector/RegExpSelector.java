package eu.cessar.ct.edit.ui.internal.facility.selector;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.edit.ui.facility.ISelector;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.edit.ui.internal.facility.FacilityConstants;

public abstract class RegExpSelector extends AbstractSelectorBuilder
{

	public static class EClassSelector extends RegExpSelector implements ISelector
	{
		/**
		 * Return true if EClass.getName() method result is regular expression
		 * 
		 * @param EClass
		 *        {@link EClass}
		 * @param EStructuralFeature
		 *        feature {@link EStructuralFeature}
		 */
		public boolean isSelected(IMetaModelService mmService, EObject object, EClass clz,
			EStructuralFeature feature)
		{
			return pattern.matcher(clz.getName()).matches();
		}
	}

	public static class EFeatureSelector extends RegExpSelector implements ISelector
	{
		/**
		 * Return true if EStructuralFeature.getName() method result is regular
		 * expression false otherwise
		 * 
		 * @param EClass
		 *        {@link EClass}
		 * @param EStructuralFeature
		 *        feature {@link EStructuralFeature}
		 */
		public boolean isSelected(IMetaModelService mmService, EObject object, EClass clz,
			EStructuralFeature feature)
		{
			if (feature != null)
			{
				return pattern.matcher(feature.getName()).matches();
			}
			return false;
		}
	}

	protected Pattern pattern = null;

	private static final Pattern REG_EXP_PATTERN = Pattern.compile(".*[\\.\\*\\[\\]\\?].*"); //$NON-NLS-1$

	/**
	 * Setter of the property <tt>regExp</tt>
	 * 
	 * @param regExp
	 *        The regExp to set.
	 */
	public void setRegExp(String regExp)
	{
		pattern = Pattern.compile(regExp);
	}

	/**
	 * Return true if the element really have a regexp pattern or false if is a
	 * simple string and as a result a simple and faster selector shall be
	 * created instead
	 * 
	 * @param element
	 * @return
	 */
	public static boolean isRegExpAttribute(IConfigurationElement element)
	{
		String attribute = element.getAttribute(FacilityConstants.ATT_REGEXP);
		if (attribute == null)
		{
			attribute = ".*"; //$NON-NLS-1$
		}
		return REG_EXP_PATTERN.matcher(attribute).matches();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ui.internal.properties.selector.AbstractSelector#init(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	public void init(IConfigurationElement element)
	{
		String attribute = element.getAttribute(FacilityConstants.ATT_REGEXP);
		if (attribute == null)
		{
			CessarPluginActivator.getDefault().logWarning(Messages.No_Regexp_specified, element);
			attribute = ".*"; //$NON-NLS-1$
		}
		setRegExp(attribute);
	}
}
