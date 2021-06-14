package eu.cessar.ct.core.platform.ui.widgets;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import eu.cessar.ct.core.internal.platform.ui.CessarPluginActivator;

/**
 * Editor for multiple GregorianCalendar values.
 * 
 */
public class MultiGregorianCalendarEditor extends AbstractMultiEditor<XMLGregorianCalendar>
{

	/**
	 * @param handler
	 */
	public MultiGregorianCalendarEditor(MultiDatatypeValueHandler<XMLGregorianCalendar> handler)
	{
		super(handler);
		DatatypeFactory newInstance;
		try
		{
			newInstance = DatatypeFactory.newInstance();
			XMLGregorianCalendar xmlGregorianCalendar = newInstance.newXMLGregorianCalendar(new GregorianCalendar());
			// handler.setDefaultData(xmlGregorianCalendar);
		}
		catch (DatatypeConfigurationException e)
		{
			// log and ignore, the handler will use null default data
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#createSingleDatatypeEditor()
	 */
	public IDatatypeEditor<XMLGregorianCalendar> createSingleDatatypeEditor()
	{
		return new SingleGregorianCalendarEditor(isAcceptingNull());
	}

}
