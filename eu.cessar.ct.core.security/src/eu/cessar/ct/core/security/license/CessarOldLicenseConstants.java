package eu.cessar.ct.core.security.license;

/**
 * Constants defining product and module used for the license
 */
public interface CessarOldLicenseConstants
{
	/**
	 * The product identifier
	 */
	public static final String PRODUCT_CESSAR = "CESSAR-CT";

	public static final String PRODUCT_ADVANCED = "ADVANCED";

	public static final String[] ALL_PRODUCTS = {PRODUCT_CESSAR, PRODUCT_ADVANCED};

	/**
	 * The module corresponding to the product.</p> Check:
	 * <ul>
	 * <li>MosartSplashHandler</li>
	 * <li></li>
	 * </ul>
	 */
	public static final String MODULE_CESSAR = "Cessar-Ct";

	public static final String[] ALL_PRODUCTS_INFOS = {Messages.licenseInfoCessar, Messages.licenseInfoAdvanced};

	/**
	 * System tree editor
	 */
	public static final String MODULE_SYSTEMGENERICEDITOR = "SystemGenericEditor";

	/**
	 * The SWC diagram
	 */
	public static final String MODULE_SYSTEMSWCGRAPHICALEDITOR = "SystemSWCGraphicalEditor";

	/**
	 * Used for composition, custom and dependency diagrams
	 */
	public static final String MODULE_SYSTEMCOMPOSITIONGRAPHICALEDITOR = "SystemCompositionGraphicalEditor";

	/**
	 * Used for topology
	 */
	public static final String MODULE_SYSTEMTOPOLOGYGRAPHICALEDITOR = "SystemTopologyGraphicalEditor";

	/**
	 * BSW .autosar editor
	 */
	public static final String MODULE_BSWPARAMETERDEFINITIONEDITOR = "BswParameterDefinitionEditor";

	/**
	 * BSW .autosar editor, preconfiguration tab
	 */
	public static final String MODULE_BSWPRECONFIGURATIONEDITOR = "BswPreConfigurationEditor";

	/**
	 * BSW .ecuconfing editor
	 */
	public static final String MODULE_BSWECUCONFIGURATIONEDITOR = "BswEcuConfigurationEditor";

	public static final String MODULE_MERGEECUCONFIGURATION = "MergeEcuConfiguration";

	public static final String MODULE_CODEGENERATORFRAMEWORK = "CodeGeneratorFramework";

	public static final String MODULE_FORMCOMPOSERFRAMEWORK = "FormComposerFramework";
}
