package eu.cessar.ct.testutils.tests;

import java.util.Random;

import eu.cessar.ct.testutils.CessarPerformanceTestCase;
import eu.cessar.ct.testutils.EDimension;
import eu.cessar.ct.testutils.EPerformanceCategory;

public class APerformanceTestCase extends CessarPerformanceTestCase
{
	public void test_single()
	{
		startMeasuring();
		runLong(5000);
		stopMeasuring();

		commitMeasurements();
		assertPerformanceCountersList(EDimension.UsedJavaHeap, EDimension.KernelTime);
	}

	public void test_loop()
	{
		int n = 20;

		for (int i = 0; i < n; ++i)
		{
			startMeasuring();
			runLong(500000);
			stopMeasuring();
		}
		commitMeasurements();
		assertPerformanceCountersList(EDimension.CPUTime, EDimension.UsedJavaHeap, EDimension.PageFaults);
	}

	public void test_custom_performance()
	{
		int n = 10;

		for (int i = 0; i < n; ++i)
		{
			startMeasuring();
			recurse(30);
			stopMeasuring();
		}
		commitMeasurements();
		assertPerformance(150, false, EDimension.ElapsedProcess, EDimension.UsedJavaHeap, EDimension.HandleCount);
	}

	private void runLong(int len)
	{
		Random r = new Random();
		for (int i = 0; i < len; ++i)
		{
			r.nextDouble();
		}
	}

	private void recurse(int n)
	{
		if (n <= 0)
		{
			return;
		}

		for (int i = 0; i < n; ++i)
		{
			new Double(232434532);
			recurse(i - 1);
		}
	}

	@Override
	public String getTestDescription()
	{
		return "This is a Test that verifies the correctness of the methods in PerformanceTestCase class"; //$NON-NLS-1$
	}

	@Override
	public EPerformanceCategory getPerformanceCategory()
	{
		return EPerformanceCategory.Other;
	}

	@Override
	public double getPerformanceIncreaseRatio()
	{
		return 3.0;
	}
}
