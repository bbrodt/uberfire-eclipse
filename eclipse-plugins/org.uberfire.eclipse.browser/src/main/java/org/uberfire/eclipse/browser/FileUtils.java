package org.uberfire.eclipse.browser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.uberfire.backend.vfs.PathFactory;

/**
 * File Utility class
 */
public class FileUtils {

	/**
	 * Finds the eclipse IFile resource for a given URI string.
	 * 
	 * @param - uriString the URI for the file.
	 * @return an IFile if the file exists, null if not
	 */
	public static IFile getFile(String uriString) {
		IFile file = null;
		if (uriString != null) {
			try {
				URI uri = new URI(uriString);
				IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
				IPath location = Path.fromOSString(uri.getPath());
				file = workspaceRoot.getFileForLocation(location);
				if (file == null || !file.exists())
					file = null;
			} catch (Exception e) {
			}
		}
		return file;
	}

	/**
	 * Reads the entire contents of the given IFile
	 * 
	 * @param - file an eclipse IFile resource
	 * @return contents of the file, or null if the file does not exist
	 * @throws CoreException
	 */
	public static String read(IFile file) throws CoreException {
		InputStream inputStream = file.getContents();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		try {
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				sb.append(buf, 0, numRead);
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// Nothing
			}
		}
		return sb.toString();
	}

	/**
	 * Writes the given string contents to an IFile
	 * 
	 * @param file - an eclipse IFile resource
	 * @param contents - the string contents to be written
	 * @return number of characters written
	 */
	public static int write(IFile file, String contents) {
		InputStream inputStream = new ByteArrayInputStream(contents.getBytes());
		try {
			file.setContents(inputStream, true, true, null);
		} catch (Exception e) {
			return -1;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return contents.length();
	}

	/**
	 * Creates an Uberfire VFS Path from the given URI.
	 * 
	 * @param uri - URI of a file
	 * @return Uberfire VFS Path
	 */
	public static org.uberfire.backend.vfs.Path createVfsPath(String uri) {
		String filename = uri;
		int i = uri.lastIndexOf("/");
		if (i > 0)
			filename = uri.substring(i + 1);
		org.uberfire.backend.vfs.Path path = PathFactory.newPath(filename, uri);
		return path;
	}
}
