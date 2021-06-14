package eu.cessar.ct.sdk.logging;

import java.io.File;
import java.io.OutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;

import eu.cessar.req.Requirement;

/**
 * Logger interface accessible by the application users (from JETs, validators, plugets). This interface allow the
 * clients to log several kind of information. It also allows to specify if the logging should be made in a specified
 * file.
 */
@Requirement(
	reqID = "202071")
public interface ILogger
{
	public String getName();

	// debug methods
	@Requirement(
		reqID = "201795")
	public void debug(boolean b);

	public void debug(char c);

	public void debug(long l);

	public void debug(double d);

	public void debug(String s);

	public void debug(Object o);

	// info methods
	@Requirement(
		reqID = "201797")
	public void info(boolean b);

	public void info(char c);

	public void info(long l);

	public void info(double d);

	@Requirement(
		reqID = "202069")
	public void info(String s);

	public void info(Object o);

	// warning methods
	@Requirement(
		reqID = "201799")
	public void warn(boolean b);

	public void warn(char c);

	public void warn(long l);

	public void warn(double d);

	public void warn(String s);

	public void warn(Object o);

	// error methods
	@Requirement(
		reqID = "201801")
	public void error(boolean b);

	public void error(char c);

	public void error(long l);

	public void error(double d);

	public void error(String s);

	public void error(Object o);

	public void log(Throwable t);

	public void log(IStatus status);

	// log file methods
	public void attachFile(IFile iFile, boolean append);

	public void attachFile(File file, boolean append);

	/**
	 * Attach a stream to logger using the default pattern
	 *
	 * @param stream
	 */
	public void attachStream(OutputStream stream);

	public void attachStream(OutputStream stream, String pattern);

	public void detachStream(OutputStream stream);

	public void detachFile();

	/**
	 *
	 * @param messageId
	 *        the ID of the generic message from the error catalogue
	 * @param contextMessage
	 *        an additional context-specific message, or {@code null} if no additional message should be added to the
	 *        generic message from the error catalogue
	 * @param parameters
	 *        the list of parameter values that are used to substitute the placeholders ("{0}", "{1}", etc.) in the
	 *        message text from the error catalogue
	 */
	@Requirement(
		reqID = "201805")
	public void info(String messageId, String contextMessage, Object... parameters);

	/**
	 * @param messageId
	 *        the ID of the generic message from the error catalogue
	 * @param contextMessage
	 *        an additional context-specific message, or {@code null} if no additional message should be added to the
	 *        generic message from the error catalogue
	 * @param parameters
	 *        the list of parameter values that are used to substitute the placeholders ("{0}", "{1}", etc.) in the
	 *        message text from the error catalogue
	 */
	public void warn(String messageId, String contextMessage, Object... parameters);

	/**
	 * @param messageId
	 *        the ID of the generic message from the error catalogue
	 * @param contextMessage
	 *        an additional context-specific message, or {@code null} if no additional message should be added to the
	 *        generic message from the error catalogue
	 * @param parameters
	 *        the list of parameter values that are used to substitute the placeholders ("{0}", "{1}", etc.) in the
	 *        message text from the error catalogue
	 */
	public void error(String messageId, String contextMessage, Object... parameters);

	/**
	 * @param messageId
	 *        the ID of the generic message from the error catalogue
	 * @param contextMessage
	 *        an additional context-specific message, or {@code null} if no additional message should be added to the
	 *        generic message from the error catalogue
	 * @param parameters
	 *        the list of parameter values that are used to substitute the placeholders ("{0}", "{1}", etc.) in the
	 *        message text from the error catalogue
	 */
	public void debug(String messageId, String contextMessage, Object... parameters);
}
