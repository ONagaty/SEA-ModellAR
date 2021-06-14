/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 19, 2009 1:48:24 PM </copyright>
 */
package eu.cessar.ct.sdk.runtime;

import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

/**
 * Each user code type have an associated manager that "knows" how to execute
 * that particular type of code. This class take care of the entire execution
 * process. One such class can be obtained by the
 * {@link ExecutionService#createManager(IProject, String)} method.
 */
public interface ICessarTaskManager<T>
{

	/**
	 * Return the ID of the Cessar task that this manager support
	 * 
	 * @return the ID of the Cessar task
	 */
	public String getCessarTaskID();

	/**
	 * Return the project associated with this execution environment
	 * 
	 * @return
	 */
	public IProject getProject();

	/**
	 * Return true if tasks executed by this manager can alter the project
	 * model. Please note that if this method return true doesn't imply that the
	 * tasks can simply call the "setXXX" methods and expect them to work.
	 * Changing a metamodel usually imply putting all the code inside a
	 * transaction. This flag is merely an indicator that such code could
	 * succeed.
	 * 
	 * @return true if the metamodel accepts writes, false otherwise
	 * @see #updateModel(Runnable)
	 */
	public boolean canWrite();

	/**
	 * A manager once create can be reused by calling this method. All the
	 * already populate field like the input, execution status and execution
	 * result, if any, are forgotten. Optionally it can also remove all attached
	 * listeners.
	 * 
	 * @param removeListeners
	 *        if true also the added listeners will be removed
	 */
	public void reset(boolean removeListeners);

	/**
	 * Initialize the manager with the input object. How the input object looks
	 * like is to be defined by implementors. Possible implementations are:</br>
	 * <ul>
	 * <li>A project, a list of folder or files: all available user code of the
	 * supported particular type shall be executed</li>
	 * </ul>
	 * 
	 * @param input
	 */
	public void initialize(T input);

	/**
	 * @param input
	 */
	public void initialize(List<T> input);

	/**
	 * Add an execution listener to the manager. Nothing happen if the listener
	 * is already added.
	 * 
	 * @param listener
	 *        the listener, should not be null
	 */
	public void addListener(ICessarTaskListener<T> listener);

	/**
	 * Remove an already added listener from the manager. Nothing happen if the
	 * listener has not been added.
	 * 
	 * @param listener
	 *        the listener
	 */
	public void removeListener(ICessarTaskListener<T> listener);

	/**
	 * Execute the task specified by the object passed to
	 * {@link #initialize(Object)}. The monitor will be used for both to provide
	 * progress feedback and to check for interruption request.
	 * 
	 * @param background
	 *        if true the execution will be done in background
	 * @param parameter
	 *        parameter that shall be passed to each executed task. The format
	 *        of the parameters is defined by the implementors
	 * @param monitor
	 *        a monitor, can also be null
	 * @return the execution status
	 */
	public IStatus execute(boolean background, Object parameter, IProgressMonitor monitor);

	/**
	 * Return the result of the execution. The result of the execution is not
	 * specified, could be even null
	 * 
	 * @return
	 */
	public Map<T, Object> getExecutionResult();

	/**
	 * In order to change an EMF model, all user code have to run from a
	 * writable transaction. This is a convenient method to do this: all user
	 * code shall be implemented inside the {@link Runnable#run()} method of the
	 * argument
	 * 
	 * @param runnable
	 *        the runnable to execute inside the transaction
	 * @throws ExecutionException
	 */
	public void updateModel(Runnable runnable) throws ExecutionException;

}
