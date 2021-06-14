package eu.cessar.ct.test.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * TestDetails allows Cessar CT developer to annotate test methods with specific
 * information (as defined in Cessar CT Wiki test scenarios). The attributes
 * provided with the annotation will be rendered in the test report generated
 * with Cesssar CT builds.
 * 
 * @author uidu0944
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TestDetails {
	/**
	 * The test case identifier as it is defined in Wiki.
	 * 
	 * @return a valid test case ID
	 */
	String testCaseId();

	/**
	 * Test method execution pre condition (if applicable). This is a free text
	 * 
	 * @return
	 */
	String preCondition() default "";

	/**
	 * Test method execution post condition (if applicable). This is a free text
	 * 
	 * @return
	 */
	String postCondition() default "";

	/**
	 * Input data for the test method (if applicable). This is a free text
	 * 
	 * @return
	 */
	String inputData() default "";

	/**
	 * expected result (free text)
	 * 
	 * @return
	 */
	String expectedResult();
}
