/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * May 10, 2013 4:10:29 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.testutils.tests;

import eu.cessar.ct.test.annotations.ExpectedFailure;
import eu.cessar.ct.testutils.CessarTestCase;

/**
 * Tests the behavior of CessarTestCase in combination with the ExpectedFailure annotation.
 * 
 * This allows for behavior similar to JUnit 4's ExpectedException, but that cannot be used due to CessarTestCase
 * extending TestCase, which turns the test into a JUnit 3 test, making it disregard any JUnit 4 style annotations like
 * {@code @Test} and {@code @ExpectedException}.
 * 
 * Each method below tests a combination of expected exception with or without message and thrown exception with or
 * without message. For easy validation, the only passing cases are at the beginning (if expected failure logging is
 * configured, otherwise all but the first test fail).
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Mon May 20 09:41:17 2013 %
 * 
 *         %version: 2 %
 */
public class DummyForExpectedFailureTests extends CessarTestCase
{
	private static final String MOCK_STRING = "2-10-2-10"; //$NON-NLS-1$
	private static final String WRONG_STRING = "11-3"; //$NON-NLS-1$

	/* No expected failure, no thrown failure - trivial pass */

	public void testNoExpectedFailureThrownNoFailure()
	{
		return;
	}

	/* Expected failure with no expected message - must pass */

	@ExpectedFailure(
		expected = NullPointerException.class,
		CRInfo = "Dummy CR: TAUTOSAR~####")
	public void testExpectedFailureNoExpectedMessageThrownCorrectFailureNoMessage()
	{
		throw new NullPointerException();
	}

	/* Expected failure with an expected message - must pass */

	@ExpectedFailure(
		expected = NullPointerException.class,
		message = MOCK_STRING,
		CRInfo = "Dummy CR: TAUTOSAR~####")
	public void testExpectedFailureExpectedMessageThrownCorrectFailureCorrectMessage()
	{
		throw new NullPointerException("Some prefix" + MOCK_STRING + "Some suffix");
	}

	/* !! All tests below in this file must fail !! */

	/* Expected failure with an expected message - all tests below must fail */

	@ExpectedFailure(
		expected = NullPointerException.class,
		message = MOCK_STRING,
		CRInfo = "Dummy CR: TAUTOSAR~####")
	public void testExpectedFailureExpectedMessageThrownNoFailure()
	{
		return;
	}

	@ExpectedFailure(
		expected = NullPointerException.class,
		message = MOCK_STRING,
		CRInfo = "Dummy CR: TAUTOSAR~####")
	public void testExpectedFailureExpectedMessageThrownCorrectFailureNoMessage()
	{
		throw new NullPointerException();
	}

	@ExpectedFailure(
		expected = NullPointerException.class,
		message = MOCK_STRING,
		CRInfo = "Dummy CR: TAUTOSAR~####")
	public void testExpectedFailureExpectedMessageThrownCorrectFailureIncorrectMessage()
	{
		throw new NullPointerException(WRONG_STRING);
	}

	@ExpectedFailure(
		expected = NullPointerException.class,
		message = MOCK_STRING,
		CRInfo = "Dummy CR: TAUTOSAR~####")
	public void testExpectedFailureExpectedMessageThrownIncorrectFailureCorrectMessage()
	{
		throw new IndexOutOfBoundsException(MOCK_STRING);
	}

	@ExpectedFailure(
		expected = NullPointerException.class,
		message = MOCK_STRING,
		CRInfo = "Dummy CR: TAUTOSAR~####")
	public void testExpectedFailureExpectedMessageThrownIncorrectFailureNoMessage()
	{
		throw new IndexOutOfBoundsException();
	}

	@ExpectedFailure(
		expected = NullPointerException.class,
		CRInfo = "Dummy CR: TAUTOSAR~####")
	public void testExpectedFailureExpectedMessageThrownIncorrectFailureIncorrectMessage()
	{
		throw new IndexOutOfBoundsException(WRONG_STRING);
	}

	/* Expected failure without expected message - all tests below must fail */

	@ExpectedFailure(
		expected = NullPointerException.class,
		CRInfo = "Dummy CR: TAUTOSAR~####")
	public void testExpectedFailureNoExpectedMessageThrownNoFailure()
	{
		return;
	}

	@ExpectedFailure(
		expected = NullPointerException.class,
		CRInfo = "Dummy CR: TAUTOSAR~####")
	public void testExpectedFailureNoExpectedMessageThrownCorrectFailureIncorrectMessage()
	{
		throw new NullPointerException(WRONG_STRING);
	}

	@ExpectedFailure(
		expected = NullPointerException.class,
		CRInfo = "Dummy CR: TAUTOSAR~####")
	public void testExpectedFailureNoExpectedMessageThrownIncorrectFailureNoMessage()
	{
		throw new IndexOutOfBoundsException();
	}

	@ExpectedFailure(
		expected = NullPointerException.class,
		CRInfo = "Dummy CR: TAUTOSAR~####")
	public void testExpectedFailureNoExpectedMessageThrownIncorrectFailureIncorrectMessage()
	{
		throw new IndexOutOfBoundsException(WRONG_STRING);
	}

	/* No expected failure - must fail */

	public void testNoExpectedFailureThrownIncorrectFailureIncorrectMessage()
	{
		throw new NullPointerException(WRONG_STRING);
	}

	public void testNoExpectedFailureThrownIncorrectFailureNoMessage()
	{
		throw new NullPointerException();
	}

}
