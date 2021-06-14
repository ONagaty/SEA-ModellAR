package eu.cessar.ct.edit.ui.utils;

import eu.cessar.ct.core.mms.MetaModelUtils;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import org.eclipse.emf.ecore.EObject;

/**
 * Utility methods used for adding identification to controls within Property view
 * 
 * @author uidl6870
 * 
 */
public final class IdentificationUtil
{

	public static final String PATH_SEPARATOR = "/"; //$NON-NLS-1$

	// non-instantiable
	private IdentificationUtil()
	{
	}

	/**
	 * The method composes and returns the widget's name that will be set subsequently with <code>
	 * setData("name","composed name") </code> method
	 * 
	 * @param qName
	 * @param page
	 * @param attribute
	 * @param control
	 * @return widget's composed name
	 */
	public static String createNamedWidgetString(final String qName, final String page, final String attribute,
		final String control)
	{

		return "qname=" + qName + ";" + "page=" + page + ";" + "attribute=" + attribute + ";" + "control=" + control
			+ ";";
	}

	/**
	 * The method composes and returns the <code>object</code>'s qualified name
	 * 
	 * @param object
	 * @throws Exception
	 */
	public static String getQualifiedNameFullPath(final EObject object)
	{

		String qName = "not identifiable"; //$NON-NLS-1$

		if (object instanceof GReferrable)
		{
			qName = MetaModelUtils.getAbsoluteQualifiedName(object);
		}
		else
		{
			// first part of qName
			String subQName = ""; //$NON-NLS-1$

			EObject current = object;
			// A string composed of substrings such as className:position
			String strTemp = ""; //$NON-NLS-1$
			int indexIN = 0;

			// list of names of non-identifiable elements
			List<String> listIN = new ArrayList<String>();

			while (current != null && !(current instanceof GReferrable))
			{

				listIN.add(indexIN, current.eClass().getName());
				indexIN++;

				current = current.eContainer();
			}

			StringTokenizer strT = new StringTokenizer(strTemp, "|"); //$NON-NLS-1$

			// list of names of className:position (without IN)
			List<String> list = new ArrayList<String>();

			int index = 0;
			while (strT.hasMoreTokens())
			{

				String token = strT.nextToken();
				list.add(index, token);
				index++;

			}

			Collections.reverse(list);
			Collections.reverse(listIN);

			if (current instanceof GReferrable)
			{
				subQName = MetaModelUtils.getAbsoluteQualifiedName(current);

			}
			qName = subQName + "#"; //$NON-NLS-1$

			for (ListIterator<String> it = list.listIterator(); it.hasNext();)
			{

				qName += it.next() + "|"; //$NON-NLS-1$
			}
			// delete the last "|"
			StringBuffer sb = new StringBuffer(qName);
			sb.deleteCharAt(sb.length() - 1);
			String qNameTemp = sb.toString();

			qName = qNameTemp;

		}
		return qName;
	}

	/**
	 * Method is used for computing the name of the given attribute named <code>label</code> eliminating ":" from the
	 * end of it
	 * 
	 * @param label
	 * @return
	 */
	public static String computeAttribute(String label)
	{
		String attribute = label;

		if (label != null)
		{
			if (label.charAt(label.length() - 1) == ':')
			{

				StringBuffer stringB = new StringBuffer(label);
				stringB.deleteCharAt(stringB.length() - 1);
				attribute = stringB.toString();
			}
		}
		return attribute;
	}

	/**
	 * Method is used for computing the name of certain features that are composed of a main feature named
	 * <code>feature</code> and a sub feature named <code>subfeature</code>
	 * 
	 * @param label
	 * @return e.g.: InstanceRef0#Target
	 */
	public static String computeAttribute(final String feature, final String subfeature)
	{
		String attribute1 = computeAttribute(feature);
		String attribute2 = computeAttribute(subfeature);
		if (attribute1 != null && attribute2 != null)
		{
			return attribute1 + "#" + attribute2; //$NON-NLS-1$
		}
		return null;
	}

}
