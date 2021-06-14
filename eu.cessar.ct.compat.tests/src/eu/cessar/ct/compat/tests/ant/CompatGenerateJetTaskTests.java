package eu.cessar.ct.compat.tests.ant;

import java.io.File;
import java.util.Date;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;

import eu.cessar.ct.ant.tasks.EclipseDependSet;
import eu.cessar.ct.compat.ant.internal.CompatGenerateJetTask;

public class CompatGenerateJetTaskTests extends AbstractCompatAntTaskTests
{
	private CompatGenerateJetTask compatGenJet;
	private static final String OUTPUT_FOLDER = "generated"; //$NON-NLS-1$

	private static final String JET_FILE1 = "/AUTOSAR/Adc/Adc_conf.cjet"; //$NON-NLS-1$
	private static final String JET_FILE_WITH_EXCEPTION = "/AUTOSAR/Adc/Adc_conf-exception.cjet"; //$NON-NLS-1$

	private static final String JET_FILE2 = "/AUTOSAR/Can/Can_conf.cjet"; //$NON-NLS-1$
	private static final String SOURCE_FILE = "/AUTOSAR/Can/Can.autosar"; //$NON-NLS-1$
	private static final String TARGET_FILE = "Can_conf.c"; //$NON-NLS-1$

	private static final String GENERATED_FILE1 = "/generated/Adc_conf.c"; //$NON-NLS-1$
	private static final String GENERATED_FILE2 = "/generated/Can_conf.c"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.ant.tests.compat.AbstractCompatAntTaskTests#doCreateAntTask()
	 */
	@Override
	protected void doCreateAntTask()
	{
		compatGenJet = new CompatGenerateJetTask();
		compatGenJet.setProject(antProject);
	}

	/**
	 *
	 */
	public void testInvalidArgs()
	{
		try
		{
			compatGenJet.setProjectPath(iProject.getLocation().toString());
			compatGenJet.execute();
			fail();
		}
		catch (BuildException e)
		{
			assertEquals("At least one target jet should be set", e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Test the generation of code from 2 jets.
	 */
	public void testGenerateJet()
	{
		try
		{
			IFile jet1 = iProject.getFile(JET_FILE1);
			assertTrue(jet1.isAccessible());
			IFile jet2 = iProject.getFile(JET_FILE2);
			assertTrue(jet2.isAccessible());
			compatGenJet.setOutput(OUTPUT_FOLDER);
			compatGenJet.setProjectPath(iProject.getLocation().toString());
			compatGenJet.setTargetJets(JET_FILE1 + "," + JET_FILE2); //$NON-NLS-1$
			compatGenJet.execute();

			iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			IFile generated1 = iProject.getFile(new Path(GENERATED_FILE1));
			assertTrue(generated1.isAccessible());
			IFile generated2 = iProject.getFile(new Path(GENERATED_FILE2));
			assertTrue(generated2.isAccessible());

		}
		catch (Exception e)
		{
			fail();
		}
	}

	/**
	 * Test the generation of code from 2 jets, with rebuild=false
	 */
	public void testGenerateJetRebuildFalse()
	{
		try
		{
			IFile jet1 = iProject.getFile(JET_FILE1);
			assertTrue(jet1.isAccessible());
			IFile jet2 = iProject.getFile(JET_FILE2);
			assertTrue(jet2.isAccessible());

			IFile generated1 = iProject.getFile(new Path(GENERATED_FILE1));
			assertTrue(generated1.isAccessible());
			IFile generated2 = iProject.getFile(new Path(GENERATED_FILE2));
			assertTrue(generated2.isAccessible());
			long modificationStamp1 = generated1.getModificationStamp();
			long modificationStamp2 = generated2.getModificationStamp();

			compatGenJet.setOutput(OUTPUT_FOLDER);
			compatGenJet.setProjectPath(iProject.getLocation().toString());
			compatGenJet.setTargetJets(JET_FILE1 + "," + JET_FILE2); //$NON-NLS-1$
			compatGenJet.execute();

			iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			long modificationStamp3 = generated1.getModificationStamp();
			long modificationStamp4 = generated2.getModificationStamp();
			assertEquals(modificationStamp1, modificationStamp3);
			assertEquals(modificationStamp2, modificationStamp4);
		}
		catch (Exception e)
		{
			fail();
		}
	}

	/**
	 * Test the generation of code from 2 jets, with rebuild=false
	 */
	public void testGenerateJetEcuconfigChanged()
	{
		try
		{
			IFile jet1 = iProject.getFile(JET_FILE1);
			assertTrue(jet1.isAccessible());
			IFile jet2 = iProject.getFile(JET_FILE2);
			assertTrue(jet2.isAccessible());

			IFile generated1 = iProject.getFile(new Path(GENERATED_FILE1));
			assertTrue(generated1.isAccessible());
			IFile generated2 = iProject.getFile(new Path(GENERATED_FILE2));
			assertTrue(generated2.isAccessible());
			long modificationStamp1 = generated1.getModificationStamp();
			long modificationStamp2 = generated2.getModificationStamp();

			// one ecuconfig file changed, rebuild = false
			// IFile changedFile = (iProject.getFolder("AUTOSAR")).getFile(SOURCE_FILE);
			File changedFile = iProject.getFile(SOURCE_FILE).getLocation().toFile();
			// create the EclipseDependSet task
			EclipseDependSet dependSet = new EclipseDependSet();
			dependSet.setProject(antProject);
			dependSet.setProjectPath(iProject.getLocation().toString());

			FileSet srcFileSet = new FileSet();
			srcFileSet.setIncludes("**/Can.autosar"); //$NON-NLS-1$
			srcFileSet.setDir(iProject.getFile("AUTOSAR").getLocation().toFile());
			srcFileSet.setProject(antProject);
			// srcFileSet.setLocation(Location.UNKNOWN_LOCATION);
			dependSet.addConfiguredSrcfileset(srcFileSet);

			FileSet targetFileSet = new FileSet();
			targetFileSet.setIncludes(TARGET_FILE);
			targetFileSet.setDir(iProject.getFile(OUTPUT_FOLDER).getLocation().toFile());
			targetFileSet.setProject(antProject);
			// targetFileSet.setLocation(Location.UNKNOWN_LOCATION);
			dependSet.addConfiguredTargetfileset(targetFileSet);

			// changedFile.touch(null);

			assertTrue(changedFile.canWrite());
			boolean setLastModified = changedFile.setLastModified(new Date().getTime());
			assertTrue(setLastModified);

			dependSet.execute();

			compatGenJet.setOutput(OUTPUT_FOLDER);
			compatGenJet.setProjectPath(iProject.getLocation().toString());
			compatGenJet.setTargetJets(JET_FILE1 + "," + JET_FILE2); //$NON-NLS-1$
			compatGenJet.execute();

			iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			long modificationStamp3 = generated1.getModificationStamp();
			long modificationStamp4 = generated2.getModificationStamp();
			assertEquals(modificationStamp1, modificationStamp3);
			assertTrue(modificationStamp2 != modificationStamp4);
		}
		catch (Exception e)
		{
			fail();
		}
	}

	/**
	 * Test the generation of code from 2 jets, with rebuild=true
	 */
	public void testGenerateJetRebuildTrue()
	{
		try
		{
			IFile jet1 = iProject.getFile(JET_FILE1);
			assertTrue(jet1.isAccessible());
			IFile jet2 = iProject.getFile(JET_FILE2);
			assertTrue(jet2.isAccessible());

			IFile generated1 = iProject.getFile(new Path(GENERATED_FILE1));
			assertTrue(generated1.isAccessible());
			IFile generated2 = iProject.getFile(new Path(GENERATED_FILE2));
			assertTrue(generated2.isAccessible());
			long modificationStamp1 = generated1.getModificationStamp();
			long modificationStamp2 = generated2.getModificationStamp();

			compatGenJet.setOutput(OUTPUT_FOLDER);
			compatGenJet.setRebuild(true);
			compatGenJet.setProjectPath(iProject.getLocation().toString());
			compatGenJet.setTargetJets(JET_FILE1 + "," + JET_FILE2); //$NON-NLS-1$
			compatGenJet.execute();

			iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			long modificationStamp3 = generated1.getModificationStamp();
			long modificationStamp4 = generated2.getModificationStamp();
			assertTrue(modificationStamp1 != modificationStamp3);
			assertTrue(modificationStamp2 != modificationStamp4);
		}
		catch (Exception e)
		{
			fail();
		}
	}

	/**
	 * Tests the generation of a jet with an exception.
	 */
	public void testGenerateJetStopOnError()
	{
		try
		{
			IFile jet1 = iProject.getFile(JET_FILE_WITH_EXCEPTION);
			assertTrue(jet1.isAccessible());

			compatGenJet.setOutput(OUTPUT_FOLDER);
			compatGenJet.setProjectPath(iProject.getLocation().toString());
			compatGenJet.setTargetJets(JET_FILE_WITH_EXCEPTION);
			compatGenJet.setStopOnError("true"); //$NON-NLS-1$
			compatGenJet.execute();
			fail();
		}
		catch (Exception e)
		{
			assertEquals("Jet generation stopped because an error ocurred: java.lang.RuntimeException", e.getMessage()); //$NON-NLS-1$
		}
	}

}
