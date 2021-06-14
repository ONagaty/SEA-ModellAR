package eu.cessar.ct.testutils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.eclipse.test.internal.performance.OSPerformanceMeter;
import org.eclipse.test.internal.performance.data.DataPoint;
import org.eclipse.test.internal.performance.data.Dim;
import org.eclipse.test.internal.performance.data.Scalar;
import org.eclipse.test.performance.Performance;
import org.eclipse.test.performance.PerformanceMeter;

public abstract class CessarPerformanceTestCase extends CessarTestCase
{
	private static final String LOG_PATH = "PerformanceLogPath"; //$NON-NLS-1$
	private static final String REFERENCE_PATH = "PerformanceReferenceLogPath"; //$NON-NLS-1$
	private static int DEFAULT_HIGH_BOUND_RATIO = 125;

	private PerformanceMeter fPerformanceMeter;

	private double[] averages;
	private boolean committed = false;
	private DataPoint refPoint = null;
	private int highBoundReferenceValue;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		Performance performance = Performance.getDefault();
		fPerformanceMeter = performance.createPerformanceMeter(getClass().getSimpleName());
	}

	@Override
	protected void tearDown() throws Exception
	{
		fPerformanceMeter.dispose();

		super.tearDown();
	}

	protected void startMeasuring()
	{
		fPerformanceMeter.start();
	}

	protected void stopMeasuring()
	{
		fPerformanceMeter.stop();
	}

	@SuppressWarnings("nls")
	protected void commitMeasurements()
	{
		Properties logger = new Properties();
		String logPath = System.getProperty(LOG_PATH);

		// fail if log path is not specified
		if (logPath == null)
		{
			fail("System property -D" + LOG_PATH + " is not set. Cannot log performance.");
		}

		// load logger, create a new if none exists
		try
		{
			logger.load(new FileInputStream(logPath));
		}
		catch (FileNotFoundException e1)
		{
			logger = new Properties();
		}
		catch (IOException e)
		{
			fail(e);
		}

		// call super.commit and disable System.out for this call
		PrintStream tmp = null;
		try
		{
			tmp = System.out;
			System.setOut(new PrintStream(new OutputStream()
			{
				@Override
				public void write(int b) throws IOException
				{ /* does nothing */
				}
			}));
			fPerformanceMeter.commit();
		}
		finally
		{
			if (tmp != null)
			{
				System.setOut(tmp);
			}
		}

		// obtain data from the performance meter
		OSPerformanceMeter osMeter = (OSPerformanceMeter) fPerformanceMeter;

		DataPoint[] dataPoints = osMeter.getSample().getDataPoints();
		Scalar[][] readings = new Scalar[dataPoints.length / 2][dataPoints[0].getScalars().length];
		averages = new double[dataPoints[0].getScalars().length];
		refPoint = dataPoints[0];

		DataPoint dp0, dp1;

		for (int i = 0; i < dataPoints.length / 2; i++)
		{
			dp0 = dataPoints[2 * i];
			dp1 = dataPoints[2 * i + 1];

			for (int j = 0; j < dp0.getScalars().length; ++j)
			{
				readings[i][j] = deltaUnit(dp0, dp1, dp0.getScalars()[j].getDimension().getId());
			}
		}

		// for each dimension, compute average
		for (int i = 0; i < readings[0].length; ++i)
		{
			double avg = 0.0;
			for (int j = 0; j < readings.length; ++j)
			{
				avg += readings[j][i].getMagnitude();
			}
			avg /= readings.length;
			averages[i] = avg / readings[0][i].getDimension().getMultiplier();
		}

		// write to properties log
		// method.dimension name=magnitude
		for (int i = 0; i < averages.length; ++i)
		{
			// sets "fullname.dimension name=value unit"
			String avStr = MessageFormat.format("{0,number,0.##}", averages[i]);
			logger.setProperty(osMeter.getScenarioName() + "." + readings[0][i].getDimension().getName(), avStr);

			logger.setProperty(osMeter.getScenarioName() + "." + "Category", getPerformanceCategory().name());
			logger.setProperty(osMeter.getScenarioName() + "." + "Description", getTestDescription());
		}

		// store file
		try
		{
			logger.store(new PrintStream(logPath), "CESSAR-CT Build Performance Log");
		}
		catch (Exception e)
		{
			fail(e);
		}
		committed = true;
	}

	/**
	 * Compares current test results to a given reference result. <br>
	 * Must specify <b>-DPerformanceReferenceLogPath</b> to be able to compare. <br>
	 * This method must be called after commitMeasurements().
	 *
	 * @param highLimit
	 *        - specifies the upper margin of acceptance for a test, expressed in percent (e.g. use 150 to specify that
	 *        a new test may not take more than 150% of the reference time).
	 * @param isOverallPerformance
	 *        - specify if an overall performance result should be checked against the reference overall performance
	 *        result.
	 * @param dimensions
	 *        - specify a variable number of dimensions that are checked against the reference. The test passes only if
	 *        all given dimensions pass.
	 */
	@SuppressWarnings("nls")
	protected void assertPerformance(int highLimit, boolean isOverallPerformance, EDimension... dimensions)
	{
		if (!committed)
		{
			fail("Must call commitMeasurements before assertPerformance can be called!");
		}

		Properties refLogger = new Properties();
		OSPerformanceMeter osMeter = (OSPerformanceMeter) fPerformanceMeter;

		String refPath = System.getProperty(REFERENCE_PATH);
		if (refPath != null)
		{
			try
			{
				refLogger.load(new FileInputStream(refPath));
				double[] refAverages = new double[averages.length];

				// for each average dimension, obtain reference value
				// ! values are stored as "12.45 ms", so a split(" ")[0] is
				// necessary
				for (int i = 0; i < averages.length; ++i)
				{
					String value = refLogger.getProperty(osMeter.getScenarioName() + "."
						+ refPoint.getScalars()[i].getDimension().getName());
					if (value != null)
					{
						refAverages[i] = Double.parseDouble(value.split(" ")[0]);
					}
					else
					{
						refAverages[i] = 0;
					}
				}

				// compare necessary dimensions
				for (int i = 0; i < averages.length; ++i)
				{
					if (needsChecking(dimensions, refPoint.getScalars()[i]))
					{
						if (!acceptPerformance(refAverages[i], averages[i], highLimit))
						{
							fail("Performance failed on dimension : "
								+ refPoint.getScalars()[i].getDimension().getName() + " (" + "ref=" + refAverages[i]
								+ ", current=" + averages[i] + ")");
						}
					}
				}
				if (isOverallPerformance)
				{
					double sumValue = 0.0;
					double sumReferenceValues = 0.0;
					for (int i = 0; i < averages.length; ++i)
					{
						sumValue = sumValue + averages[i];
						sumReferenceValues = sumReferenceValues + refAverages[i];
					}

					if (!acceptPerformance(sumReferenceValues, sumValue, highLimit))
					{
						fail("Overall performance evaluation failed: \nReference overall performance: "
							+ sumReferenceValues + "\nCurrent overall performance:" + sumValue);
					}
				}

				assertTrue(true);
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				fail(e.getMessage());
			}
		}
		else
		{
			assertTrue(
				"No reference performance log found ( -DPerformanceReferenceLogPath not provided ). Test passed without any comparison. ",
				true);
		}

		committed = false;
	}

	/**
	 * Compares the specified current test results to a given reference result. <br>
	 * Must specify <b>-DPerformanceReferenceLogPath</b> to be able to compare. <br>
	 * This method must be called after commitMeasurements().
	 *
	 * @param dimensions
	 *        - specify a variable number of dimensions that are checked against the reference. The test passes only if
	 *        all given dimensions pass.
	 */
	protected void assertPerformanceCountersList(EDimension... dimensions)
	{
		highBoundReferenceValue = getReferenceHighBoundRatioValueByCategory(getPerformanceCategory());
		assertPerformance(highBoundReferenceValue, false, dimensions);
	}

	/**
	 * Compares the specified current test results to a given/previous reference result <br>
	 * for performance counters depending on the system resources.<br>
	 * Must specify <b>-DPerformanceReferenceLogPath</b> to be able to compare. <br>
	 * This method must be called after commitMeasurements().
	 */
	protected void assertSystemResourcesDependingPerformance()
	{
		EDimension[] perfCountersList = new EDimension[] {EDimension.ElapsedProcess, EDimension.UsedJavaHeap};
		highBoundReferenceValue = getReferenceHighBoundRatioValueByCategory(getPerformanceCategory());
		assertPerformance(highBoundReferenceValue, false, perfCountersList);
	}

	/**
	 * Compares the specified current test results to a given/previous reference result <br>
	 * for performance counters independent of the system resources.<br>
	 * Must specify <b>-DPerformanceReferenceLogPath</b> to be able to compare. <br>
	 * This method must be called after commitMeasurements().
	 */
	protected void assertSystemResourcesIndependentPerformance()
	{
		EDimension[] perfCountersList = new EDimension[] {EDimension.CPUTime, EDimension.KernelTime,
			EDimension.UsedJavaHeap};
		highBoundReferenceValue = getReferenceHighBoundRatioValueByCategory(getPerformanceCategory());
		assertPerformance(highBoundReferenceValue, false, perfCountersList);
	}

	/**
	 * Compares the specified current test overall performance indicator to a given/previous reference result.<br>
	 * Must specify <b>-DPerformanceReferenceLogPath</b> to be able to compare. <br>
	 * This method must be called after commitMeasurements().
	 */
	protected void assertOverallPerformance()
	{
		EDimension[] perfCountersList = new EDimension[] {EDimension.CPUTime, EDimension.KernelTime,
			EDimension.UsedJavaHeap, EDimension.UsedMemory};
		highBoundReferenceValue = getReferenceHighBoundRatioValueByCategory(getPerformanceCategory());

		assertPerformance(highBoundReferenceValue, true, perfCountersList);
	}

	/**
	 * Returns a performance reading for the given parameter, for one measurement step.
	 *
	 * @param dp0
	 *        - start of measurement
	 * @param dp1
	 *        - end of measurement
	 * @param dimID
	 *        - identifier for the measured parameter (See org.eclipse.test.internal.performance.InternalDimensions)
	 * @return - long value representing the positive difference between the two measured values
	 */
	private Scalar deltaUnit(DataPoint dp0, DataPoint dp1, int dimID)
	{
		Scalar point0 = dp0.getScalar(Dim.getDimension(dimID));
		long mag0 = point0.getMagnitude();

		Scalar point1 = dp1.getScalar(Dim.getDimension(dimID));
		long mag1 = point1.getMagnitude();

		return new Scalar(point0.getDimension(), Math.abs(mag1 - mag0));
	}

	/**
	 * @param dimensions
	 *        - dimensions that have to pass the assertion test (given by the tester)
	 * @param scalar
	 *        - a scalar with a unique dimension
	 * @return - true if the given scalar matches one of the needed dimensions
	 */
	private boolean needsChecking(EDimension[] dimensions, Scalar scalar)
	{
		for (EDimension dim: dimensions)
		{
			if (dim.getCode() == scalar.getDimension().getId())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Does the actual comparison between the reference value and the new value of a dimension
	 *
	 * @param oldValue
	 *        - reference value
	 * @param newValue
	 *        - newly obtained value
	 * @param highBoundRatio
	 *        - defines the maximum acceptable ratio between the new value and the reference value. Given in percent
	 *        (i.e. normally >100)
	 * @return - true if the ratio is acceptable
	 */
	private boolean acceptPerformance(double oldValue, double newValue, int highBoundRatio)
	{
		if (oldValue == 0)
		{
			return true;
		}

		// High bound check
		boolean isHighBoundCheck = (newValue / oldValue) <= (highBoundRatio / 100.0);

		// A high increase of performance can be caused also by missing
		// or faulty resources and this case is a candidate for verification.
		// The low bound check is made for a specified performance increase ratio

		boolean isLowBoundCheck = true;

		if (getPerformanceIncreaseRatio() > 1)
		{
			isLowBoundCheck = (oldValue / newValue) <= getPerformanceIncreaseRatio();
		}

		return isHighBoundCheck && isLowBoundCheck;

	}

	/**
	 * Gets the reference high bound value by category.
	 *
	 * @param performanceCategory
	 *        the performance category
	 * @return the reference high bound value by category
	 */
	private int getReferenceHighBoundRatioValueByCategory(EPerformanceCategory performanceCategory)
	{
		// Provide context-sensitive percentage reference values.
		// By default is returned a medium sensitivity percentage value.

		switch (performanceCategory)
		{
			case Create:
				return 125;

			case GenerateCode:
				return 125;

			case Import:
				return 125;

			case PresentationModel:
				return 125;

			case Other:
				return 150;

			default:
				return DEFAULT_HIGH_BOUND_RATIO;
		}
	}

	/**
	 * [the type of the project]description.<br>
	 * ex:<br>
	 * [3.x][compatibility]Tests the performance of importing a project in compatibility mode on 3.x model
	 *
	 * @return an explicit description of what the test does.
	 */
	public abstract String getTestDescription();

	/**
	 * @return the category under which the test will be classified
	 */
	public abstract EPerformanceCategory getPerformanceCategory();

	/**
	 * Gets the performance increase ratio alert. A high increase of performance can be caused also by missing or faulty
	 * resources and this case is a candidate for verification.
	 *
	 * @return the performance increase ratio alert defining the maximum acceptable increase of performance ratio
	 *         between the new value and the reference value. Set value to 1 for no interest in performance boosts.
	 */
	public abstract double getPerformanceIncreaseRatio();

}
