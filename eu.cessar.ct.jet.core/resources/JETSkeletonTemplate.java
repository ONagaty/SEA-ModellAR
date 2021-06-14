import java.io.IOException;

import eu.cessar.ct.sdk.logging.LoggerFactory;

public final class CLASS extends eu.cessar.ct.sdk.CodeGenerator
{

	/**
	 * Class used for adding pretty printers or additional formatters to the output stream. The formatter has to
	 * implement the {@link Appendable} interface and be wrapped inside a new instance of the AppendableWrapper 
	 * class. Ex: stringFormatter = new AppendableWrapper(new PrettyPrinter(stringBuffer));
	 * 
	 * Only the stringFormatter output stream is capable to be initialized with an AppendableWrapper.
	 * 
	 * @author uidg4098
	 *
	 *         %created_by: created by %
	 * 
	 *         %date_created: creation date %
	 * 
	 *         %version: version %
	 */
	class AppendableWrapper
	{
		private Appendable wrappedObject;

		/**
		 *
		 */
		public AppendableWrapper(Appendable wrappedObject)
		{
			this.wrappedObject = wrappedObject;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Appendable#append(java.lang.CharSequence)
		 */
		public Appendable append(CharSequence csq)
		{
			try
			{
				return wrappedObject.append(csq);
			}
			catch (IOException e)
			{
				LoggerFactory.getLogger().log(e);
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Appendable#append(java.lang.CharSequence, int, int)
		 */
		public Appendable append(CharSequence csq, int start, int end)
		{
			try
			{
				return wrappedObject.append(csq, start, end);
			}
			catch (IOException e)
			{
				LoggerFactory.getLogger().log(e);
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Appendable#append(char)
		 */
		public Appendable append(char c)
		{
			try
			{
				return wrappedObject.append(c);
			}
			catch (IOException e)
			{
				LoggerFactory.getLogger().log(e);
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return wrappedObject.toString();
		}

	}

	/*
	 * This field will be set during compilation by the Jet Compiler to the name of the output file. Can be altered by
	 * the implementor using setOutputFileName(String)
	 */
	private static final String DEFAULT_OUTPUT_FILE_NAME;
	/**
	 * {@link StringBuilder} containing the plain generated code.
	 */
	private final StringBuilder stringBuffer = new StringBuilder();
	/**
	 * {@link AppendableWrapper} object able to format the plain generated code.
	 */
	private AppendableWrapper stringFormatter;

	public CLASS(org.eclipse.core.resources.IProject project, org.eclipse.core.resources.IFolder outputFolder)
	{
		super(project, outputFolder, DEFAULT_OUTPUT_FILE_NAME);
		stringFormatter = new AppendableWrapper(stringBuffer);
	}

	public String generate(eu.cessar.ct.sdk.pm.IPresentationModel argument)
	{
		return generate((ecuc.PresentationModel) argument);
	}

	/*
	 * Changes of this method signature, require modifications of JETCoreConstants.METHOD_DECLARATION constant.
	 */
	public String generate(ecuc.PresentationModel argument)
	{
	}
}