package eu.cessar.ct.sdk;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

import eu.cessar.ct.sdk.logging.ILogger;
import eu.cessar.ct.sdk.pm.IPresentationModel;

/**
 * Interface from which inherit all Java classes that generate output files.
 */
public interface ICodeGenerator extends IPluggable
{
	/**
	 * Set the name of the output file name.
	 * 
	 * @param fileName
	 */
	public void setOutputFileName(String fileName);

	/**
	 * Return the name of the output file
	 * 
	 * @return
	 */
	public String getOutputFileName();

	/**
	 * Return the project that the jet is working on
	 * 
	 * @return
	 */
	public IProject getProject();

	/**
	 * Return the folder that will be used for code generation
	 * 
	 * @return
	 */
	public IFolder getOutputFolder();

	/**
	 * The main method that will be executed by the task manager
	 * 
	 * @param argument
	 * @return
	 */
	public String generate(IPresentationModel argument);

	/**
	 * Convenient method for accessing the logger.
	 * 
	 * @return a logger
	 */
	public ILogger getLogger();

}
