/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * May 9, 2013 5:27:01 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.test.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ExpectedFailure allows a Cessar CT developer to annotate test methods that are expected to fail with expected failure
 * information.
 * 
 * This must only be used for tests with known failures and with associated CRs, the ids of which must be supplied via
 * the {@code CRInfo} parameter. These tests will undergo a separate checking and logging, for example, if a test that
 * is annotated as having an expected failure does _not_ fail, then this is itself a "true" failure and gets logged with
 * the failures of tests that are not expected to fail. Otherwise, if a test fails with the expected failure attributes
 * (Throwable class and error message), the failure is still logged but separately from the other test failures.
 * 
 * @author uidu2337
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpectedFailure
{
	/**
	 * Specify <code>expected</code>, a Throwable, to cause a test method to pass if-and-only-if an exception of the
	 * specified class is thrown by the method.
	 */
	Class<? extends Throwable> expected();

	/**
	 * Adds to the list of requirements for any thrown exception that it should <em>contain</em> string {@code message}
	 */
	String message() default "";

	/**
	 * Adds mandatory information about the known issue, usually the CR id.
	 */
	String CRInfo();
}
