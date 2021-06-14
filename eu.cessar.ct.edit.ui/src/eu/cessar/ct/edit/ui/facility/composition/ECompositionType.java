package eu.cessar.ct.edit.ui.facility.composition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 
 * Enumeration for editor composition types
 * 
 */
public enum ECompositionType
{
	SIMPLE("SIMPLE"), ECUC("ECUC"), SYSTEM("SYSTEM"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	private String type;

	ECompositionType(String type)
	{
		this.type = type;
	}

	/**
	 * 
	 */
	private static final ECompositionType[] VALUES_ARRAY = new ECompositionType[] {SIMPLE, ECUC,
		SYSTEM,};

	/**
	 * 
	 */
	public static final List<ECompositionType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	public String getName()
	{
		return type;
	}

	/**
	 * 
	 * @param name
	 * @return the <em><b>CompositionType</b></em> literal with the specified
	 *         name
	 */
	public static ECompositionType getLiteral(String name)
	{
		for (int i = 0; i < VALUES_ARRAY.length; ++i)
		{
			ECompositionType result = VALUES_ARRAY[i];
			if (result.getName().equals(name))
			{
				return result;
			}
		}
		return null;
	}
}
