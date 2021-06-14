/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 5, 2010 1:04:24 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility;

/**
 * @author uidl6458
 *
 */
public final class FacilityConstants
{

	public static final String NAMESPACE = "eu.cessar.ct.edit.ui"; //$NON-NLS-1$

	public static final String EXTENSION_MODEL_EDITORS = "editors"; //$NON-NLS-1$

	public static final String EXTENSION_COMPOSITIONS = "editorComposition"; //$NON-NLS-1$

	public static final String EXTENSION_NEWUIFACTORY = "newEObjectUIFactory"; //$NON-NLS-1$

	// plugin registry element names
	public static final String ELEM_CLASSIFIER_DEFINITION = "classifierDefinition"; //$NON-NLS-1$
	public static final String ELEM_FEATURE_DEFINITION = "featureDefinition"; //$NON-NLS-1$
	public static final String ELEM_CLASS_DEFINITION = "classDefinition"; //$NON-NLS-1$
	public static final String ELEM_RELATION_DEFINITION = "relationDefinition"; //$NON-NLS-1$
	public static final String ELEM_SELECTOR = "selector"; //$NON-NLS-1$

	// plugin registry attribute names
	public static final String ATT_ID = "id"; //$NON-NLS-1$
	public static final String ATT_FACTORY = "factory"; //$NON-NLS-1$
	public static final String ATT_MASTER_EDITOR = "masterEditor"; //$NON-NLS-1$
	public static final String ATT_CATEGORY = "category"; //$NON-NLS-1$
	public static final String ATT_PRIORITY = "priority"; //$NON-NLS-1$
	public static final String ATT_CLASS = "class"; //$NON-NLS-1$
	public static final String ATT_ECLASS = "eclass"; //$NON-NLS-1$
	public static final String ATT_PROVIDER = "provider"; //$NON-NLS-1$
	public static final String ATT_FEATURE_TYPE = "featureType"; //$NON-NLS-1$
	public static final String ATT_CLASSIFIER_CLASS = "classifierClass"; //$NON-NLS-1$
	public static final String ATT_CLASSIFIER_TYPE = "classifierType"; //$NON-NLS-1$
	public static final String ATT_DEFAULT = "default"; //$NON-NLS-1$
	public static final String ATT_PROPERTIES = "properties"; //$NON-NLS-1$
	public static final String ATT_CACHEABLE = "cacheableType"; //$NON-NLS-1$

	// plugin registry selector names
	public static final String SELECTOR_AND = "and"; //$NON-NLS-1$
	public static final String SELECTOR_OR = "or"; //$NON-NLS-1$
	public static final String SELECTOR_NOT = "not"; //$NON-NLS-1$
	public static final String SELECTOR_EFEATURE = "eFeature"; //$NON-NLS-1$
	public static final String SELECTOR_ECLASS = "eClass"; //$NON-NLS-1$
	public static final String SELECTOR_SELECTORPROVIDER = "selectorProvider"; //$NON-NLS-1$
	public static final String ATT_VALUE = "value"; //$NON-NLS-1$
	public static final String ATT_REGEXP = "regexp"; //$NON-NLS-1$

	public static final String EDITOR_ID_KEY = "eu.cessar.ct.edit.ui.menu_manager"; //$NON-NLS-1$

	public static final String WARNING_CLASS_MISSING = "input class is not specified"; //$NON-NLS-1$
	public static final String WARNING_FEATURE_MISSING = "no feature"; //$NON-NLS-1$

	// the constants are kept here for compatibility reasons
	public static final String WORKSPACE_NAMESPACE = "eu.cessar.ct.edit.table.workspace.preferences."; //$NON-NLS-1$
	public static final String PROJECT_NAMESPACE = "eu.cessar.ct.edit.table.project.preferences."; //$NON-NLS-1$

	// non-instantiable
	private FacilityConstants()
	{
	}

}
