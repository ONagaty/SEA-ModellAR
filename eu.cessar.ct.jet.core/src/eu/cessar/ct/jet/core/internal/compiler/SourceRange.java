package eu.cessar.ct.jet.core.internal.compiler;

/**
 * This class is only for internal use. It helps converting Java ranges reported
 * by java compiler into JET ranges;
 */
public class SourceRange
{
	public int startOffset = -1;
	public int endOffset = -1;

	public int startLine = -1;
	public int endLine = -1;

	private char[] chars;

	private boolean isComplete;

	public SourceRange(final int startOffset, final int endOffset, final char[] ch)
	{
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.startLine = 0;
		this.endLine = 0;
		chars = ch;
		isComplete = false;
	}

	/**
	 * Default Constructor.
	 */
	public SourceRange(final int startOffset, final int endOffset, final int startLine,
		final int endLine, final char[] ch)
	{
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.startLine = startLine;
		this.endLine = endLine;
		chars = ch;
		isComplete = true;
	}

	public boolean isComplete()
	{
		return isComplete;
	}

	public void setLines(final int stLine, final int edLine)
	{
		startLine = stLine;
		endLine = edLine;
		isComplete = true;
	}

	public int getStartLine()
	{
		return startLine;
	}

	public int getEndLine()
	{
		return endLine;
	}

	public int countLines()
	{
		return endLine - startLine;
	}

	public int getStartOffset()
	{
		return startOffset;
	}

	public int getEndOffset()
	{
		return endOffset;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (obj instanceof SourceRange)
		{
			SourceRange other = (SourceRange) obj;
			return (startOffset == other.startOffset) && (endOffset == other.endOffset)
				&& (startLine == other.startLine) && (endLine == other.endLine);
		}
		return super.equals(obj);
	}

	@SuppressWarnings("nls")
	@Override
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		result.append(String.format("Start Line : %d (offset %d),  End Line : %d (offset %d)\n",
			startLine, startOffset, endLine, endOffset));
		result.append("=====================================================================\n");
		result.append(chars);
		result.append("\n=====================================================================\n");
		return result.toString();
	}
}