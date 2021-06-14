package eu.cessar.ct.edit.ui.facility.expansion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Enumeration for type of relations that are established between master editors
 * (expandable editors) and the editors that appear inside the expanded area
 * 
 * @author uidl6870
 */
public enum EExpansionType
{
	MASTER_DETAIL, SIBLING, ;

	/**
	 * 
	 */
	private static final EExpansionType[] VALUES_ARRAY = new EExpansionType[] {MASTER_DETAIL,
		SIBLING,};

	/**
 * 
 */
	public static final List<EExpansionType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));
}
