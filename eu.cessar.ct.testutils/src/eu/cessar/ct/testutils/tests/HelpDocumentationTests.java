package eu.cessar.ct.testutils.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.help.standalone.Infocenter;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.Bundle;

import eu.cessar.ct.testutils.CessarTestCase;

public class HelpDocumentationTests extends CessarTestCase
{

	private static final String HTML = ".html"; //$NON-NLS-1$
	private static final String MANUAL_PATH = "/help/topic/eu.cessar.ct.doc/"; //$NON-NLS-1$
	private static final String LOCALHOST = "http://127.0.0.1:"; //$NON-NLS-1$
	private static final String PORT = "9858"; //$NON-NLS-1$
	private static final String MANUAL = "manual"; //$NON-NLS-1$
	private static final String bundleName = "eu.cessar.ct.doc"; //$NON-NLS-1$
	// private static final CharSequence ERROR_MESSAGE =
	// "at org.eclipse.equinox.http.jetty.internal.HttpServerManager$InternalHttpServiceServlet.service(HttpServerManager.java:318)";
	private static final CharSequence ERROR_MESSAGE = "An error occured while processing the requested document";
	private File docFile;

	public HelpDocumentationTests()
	{
		Bundle bundle = Platform.getBundle(bundleName);
		try
		{
			docFile = FileLocator.getBundleFile(bundle);
		}
		catch (IOException e)
		{
			fail(e);
		}
		assertTrue(docFile.exists());
	}

	/**
	 * 
	 */
	public void testFileExistance()
	{
		Location installLocation = Platform.getInstallLocation();

		String[] options = new String[] {"-eclipsehome", installLocation.getURL().getPath(),
			"-noexec", "-port", PORT};
		Infocenter infocenter = new Infocenter(options);
		List<String> errorFiles = new ArrayList<String>();

		try
		{
			infocenter.start();

			try
			{
				Thread.sleep(1000);

				if (docFile.isFile())
				{
					JarFile jarFile;
					jarFile = new JarFile(docFile);
					Enumeration<JarEntry> enumeration = jarFile.entries();
					while (enumeration.hasMoreElements())
					{
						JarEntry element = enumeration.nextElement();
						if (element.getName().endsWith(HTML))
						{
							String urlAsString = resolveURL(element.getName());
							extractErrorFile(errorFiles, element.getName(), urlAsString, 0);
						}
					}
				}
				else
				{
					File manual = null;
					File[] content = docFile.listFiles();
					for (File child: content)
					{
						if (child.isDirectory() && MANUAL.equals(child.getName()))
						{
							manual = child;
							break;
						}
					}

					assertNotNull(manual);

					for (File manualFile: manual.listFiles())
					{
						if (manualFile.getName().endsWith(HTML))
						{
							if (manualFile.getName().length() > 5)
							{
								String urlAsString = resolveURL("manual/" + manualFile.getName());
								extractErrorFile(errorFiles, manualFile.getName(), urlAsString, 0);
							}
							else
							{
								errorFiles.add(manualFile.getName());
							}
						}
					}
				}
			}
			finally
			{
				infocenter.shutdown();
			}
		}
		catch (Exception e)
		{
			fail(e);
		}

		if (!errorFiles.isEmpty())
		{
			StringBuffer result = new StringBuffer();
			for (String fName: errorFiles)
			{
				result.append(fName);
				result.append(',');
			}
			fail("The fallowing files have errors:" + result.toString()); //$NON-NLS-1$
		}
	}

	private void extractErrorFile(List<String> errorFiles, String manualFileName,
		String urlAsString, int nr)
	{
		try
		{
			URL url = new URL(urlAsString);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				if (inputLine.contains(ERROR_MESSAGE))
				{
					errorFiles.add(manualFileName);
				}
			}
			in.close();
		}
		catch (Exception e)
		{
			if (e.getMessage().contains("403 for URL") && nr < 5)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e1)
				{
					fail(e1);
				}
				nr++;
				extractErrorFile(errorFiles, manualFileName, urlAsString, nr);
			}
			else
			{
				fail(e);
			}
		}
	}

	private String resolveURL(String name)
	{
		// view-source:http://127.0.0.1:1485/help/topic/eu.cessar.ct.doc/manual/Chapter_3.3.2.html
		String result = LOCALHOST + PORT + MANUAL_PATH + name;
		return result;
	}
}
