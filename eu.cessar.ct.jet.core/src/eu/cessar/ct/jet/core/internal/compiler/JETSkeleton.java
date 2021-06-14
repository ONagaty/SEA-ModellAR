package eu.cessar.ct.jet.core.internal.compiler;

import java.util.List;

import org.eclipse.jdt.core.jdom.IDOMMethod;

import eu.cessar.ct.jet.core.JETCoreUtils;
import eu.cessar.req.Requirement;

/**
 * Custom {@link JETSkeleton} implementation that makes all JET classes to inherit from {@link IJetGenerator} interface.
 */
@Requirement(
	reqID = "206782")
@SuppressWarnings({"deprecation", "nls"})
class JETSkeleton extends org.eclipse.emf.codegen.jet.JETSkeleton
{
	@SuppressWarnings("hiding")
	protected final String STRING_BUFFER_DECLARATION = "     final StringBuffer stringBuffer = new StringBuffer();"
		+ NL + "     final StringBuffer stringFormatter = stringBuffer;" + NL;
	private final String DEFAULT_INDENTATION = "\t\t";
	protected final String STRING_BUFFER_RETURN = "    return stringFormatter.toString();" + NL;

	@Override
	public void setBody(List<String> lines)
	{
		IDOMMethod method = getLastMethod();
		if (method != null)
		{
			StringBuffer body = new StringBuffer();
			body.append(NL + "  {" + NL);
			body.append(STRING_BUFFER_DECLARATION);
			for (String line: lines)
			{
				body.append(JETCoreUtils.convertLineFeed(line));
				body.append(NL);
			}
			body.append(STRING_BUFFER_RETURN);
			body.append("	}" + NL);

			method.setBody(body.toString());
		}
	}
}
