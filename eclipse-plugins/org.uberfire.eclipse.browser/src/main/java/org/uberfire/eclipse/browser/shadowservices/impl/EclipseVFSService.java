package org.uberfire.eclipse.browser.shadowservices.impl;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.browser.Browser;
import org.uberfire.backend.vfs.DirectoryStream;
import org.uberfire.backend.vfs.DirectoryStream.Filter;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.VFSService;
import org.uberfire.eclipse.browser.FileUtils;
import org.uberfire.eclipse.browser.shadowservices.EclipseShadowService;
import org.uberfire.java.nio.IOException;
import org.uberfire.java.nio.file.AtomicMoveNotSupportedException;
import org.uberfire.java.nio.file.DirectoryNotEmptyException;
import org.uberfire.java.nio.file.FileAlreadyExistsException;
import org.uberfire.java.nio.file.FileSystemAlreadyExistsException;
import org.uberfire.java.nio.file.NoSuchFileException;
import org.uberfire.java.nio.file.NotDirectoryException;
import org.uberfire.java.nio.file.ProviderNotFoundException;

/**
 * Server-side Shadow Service implementation of the VFS service.
 * 
 * @author bbrodt
 *
 */
public class EclipseVFSService extends EclipseShadowService implements VFSService {

	public static final String NAME = "EclipseVFSService";

	public EclipseVFSService(Browser browser) {
		super(browser, NAME);
	}

	public String readAllString(Path path) {
		IFile file = FileUtils.getFile(path.toURI());
        String contents = null;
		try {
			contents = FileUtils.read(file);
		} catch (CoreException e) {
			e.printStackTrace();
		}
        return contents;
	}
	
	public Path write(Path path, String content) {
        Path response = path;
        try {
            IFile file = FileUtils.getFile(path.toURI());
            if (FileUtils.write(file, content) < 0 )
                response = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
		
	}

	@Override
	public Path get(String uri) {
		return FileUtils.createVfsPath(uri);
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir)
			throws IllegalArgumentException, NotDirectoryException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<Path> filter)
			throws IllegalArgumentException, NotDirectoryException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path createDirectory(Path dir)
			throws IllegalArgumentException, UnsupportedOperationException, FileAlreadyExistsException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path createDirectories(Path dir)
			throws UnsupportedOperationException, FileAlreadyExistsException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path createDirectory(Path dir, Map<String, ?> attrs)
			throws IllegalArgumentException, UnsupportedOperationException, FileAlreadyExistsException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path createDirectories(Path dir, Map<String, ?> attrs)
			throws UnsupportedOperationException, FileAlreadyExistsException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> readAttributes(Path path)
			throws UnsupportedOperationException, IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttributes(Path path, Map<String, Object> attrs)
			throws IllegalArgumentException, FileSystemAlreadyExistsException, ProviderNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Path path)
			throws IllegalArgumentException, NoSuchFileException, DirectoryNotEmptyException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deleteIfExists(Path path) throws IllegalArgumentException, DirectoryNotEmptyException, IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Path copy(Path source, Path target)
			throws UnsupportedOperationException, FileAlreadyExistsException, DirectoryNotEmptyException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path move(Path source, Path target) throws UnsupportedOperationException, FileAlreadyExistsException,
			DirectoryNotEmptyException, AtomicMoveNotSupportedException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path write(Path path, String content, Map<String, ?> attrs)
			throws IllegalArgumentException, IOException, UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRegularFile(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRegularFile(Path path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDirectory(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDirectory(Path path) {
		// TODO Auto-generated method stub
		return false;
	}
}
