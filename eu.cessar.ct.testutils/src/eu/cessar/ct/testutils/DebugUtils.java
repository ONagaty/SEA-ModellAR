package eu.cessar.ct.testutils;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import org.eclipse.core.runtime.jobs.Job;

public class DebugUtils
{

	/**
	 * @param output
	 */
	public static void dumpDebugInformation(String header, PrintStream out, boolean dumpJobs, boolean dumpThreads)
	{
		// dump also the current jobs
		if (!dumpJobs && !dumpThreads)
		{
			// do nothing
			return;
		}
		// get the jobs and the threads before enything else
		Job[] jobs = Job.getJobManager().find(null);
		ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
		ThreadInfo[] threads = mxBean.dumpAllThreads(true, true);
		out.println("---------------------------------");
		out.println("Debug Header:" + header);
		out.println("---------------------------------");
		if (dumpJobs)
		{
			out.println("Running jobs:");
			for (Job job: jobs)
			{
				out.println("\t" + job.getName() + "[" + job.getState() + "]\n\t" + job.getClass().getName());
			}
			out.println("---------------------------------");
		}
		if (dumpThreads)
		{
			dumpThreadStacks(out, threads);
			out.println("---------------------------------");
		}
	}

	private static void dumpThreadStacks(PrintStream output, ThreadInfo[] threads)
	{
		for (ThreadInfo tInfo: threads)
		{
			dumpThreadStack(output, tInfo);
		}
	}

	private static void dumpThreadStack(PrintStream output, ThreadInfo tInfo)
	{
		output.println(tInfo.toString());
	}

}
