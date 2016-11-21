package org.uberfire.client.shadowservices.impl;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.ShadowService;
import org.uberfire.backend.vfs.DirectoryStream;
import org.uberfire.backend.vfs.DirectoryStream.Filter;
import org.uberfire.client.shadowservices.WebappShadowService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.VFSService;
import org.uberfire.java.nio.IOException;
import org.uberfire.java.nio.file.AtomicMoveNotSupportedException;
import org.uberfire.java.nio.file.DirectoryNotEmptyException;
import org.uberfire.java.nio.file.FileAlreadyExistsException;
import org.uberfire.java.nio.file.FileSystemAlreadyExistsException;
import org.uberfire.java.nio.file.NoSuchFileException;
import org.uberfire.java.nio.file.NotDirectoryException;
import org.uberfire.java.nio.file.ProviderNotFoundException;

/**
 * Client-side Shadow Service implementation of the VFS service.
 * 
 * @author bbrodt
 *
 */
@ApplicationScoped
@ShadowService
public class VFSShadowService extends WebappShadowService implements VFSService {

	public static String ECLIPSE_NAME = "EclipseVFSService";
	
	public String getEclipseServiceName() {
		return ECLIPSE_NAME;
	}

	@Override
	public Path get(String uri) {
		return (Path) callEclipseService("get", uri);
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir)
			throws IllegalArgumentException, NotDirectoryException, IOException {
		return (DirectoryStream<Path>) callEclipseService("newDirectoryStream", dir);
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<Path> filter)
			throws IllegalArgumentException, NotDirectoryException, IOException {
		return (DirectoryStream<Path>) callEclipseService("newDirectoryStream", dir, filter);
	}

	@Override
	public Path createDirectory(Path dir)
			throws IllegalArgumentException, UnsupportedOperationException, FileAlreadyExistsException, IOException {
		return (Path) callEclipseService("createDirectory", dir);
	}

	@Override
	public Path createDirectories(Path dir)
			throws UnsupportedOperationException, FileAlreadyExistsException, IOException {
		return (Path) callEclipseService("createDirectories", dir);
	}

	@Override
	public Path createDirectory(Path dir, Map<String, ?> attrs)
			throws IllegalArgumentException, UnsupportedOperationException, FileAlreadyExistsException, IOException {
		return (Path) callEclipseService("createDirectory", dir, attrs);
	}

	@Override
	public Path createDirectories(Path dir, Map<String, ?> attrs)
			throws UnsupportedOperationException, FileAlreadyExistsException, IOException {
		return (Path) callEclipseService("createDirectories", dir, attrs);
	}

	@Override
	public Map<String, Object> readAttributes(Path path)
			throws UnsupportedOperationException, IllegalArgumentException, IOException {
		return (Map<String, Object>) callEclipseService("readAttributes", path);
	}

	@Override
	public void setAttributes(Path path, Map<String, Object> attrs)
			throws IllegalArgumentException, FileSystemAlreadyExistsException, ProviderNotFoundException {
		callEclipseService("setAttributes", path, attrs);
	}

	@Override
	public void delete(Path path)
			throws IllegalArgumentException, NoSuchFileException, DirectoryNotEmptyException, IOException {
		callEclipseService("delete", path);
	}

	@Override
	public boolean deleteIfExists(Path path) throws IllegalArgumentException, DirectoryNotEmptyException, IOException {
		return (boolean) callEclipseService("deleteIfExists", path);
	}

	@Override
	public Path copy(Path source, Path target)
			throws UnsupportedOperationException, FileAlreadyExistsException, DirectoryNotEmptyException, IOException {
		return (Path) callEclipseService("copy", source, target);
	}

	@Override
	public Path move(Path source, Path target) throws UnsupportedOperationException, FileAlreadyExistsException,
			DirectoryNotEmptyException, AtomicMoveNotSupportedException, IOException {
		return (Path) callEclipseService("move", source, target);
	}

	@Override
	public String readAllString(Path path) throws IllegalArgumentException, NoSuchFileException, IOException {
		return (String) callEclipseService("readAllString", path);
	}

	@Override
	public Path write(Path path, String content)
			throws IllegalArgumentException, IOException, UnsupportedOperationException {
		return (Path) callEclipseService("write", path, content);
	}

	@Override
	public Path write(Path path, String content, Map<String, ?> attrs)
			throws IllegalArgumentException, IOException, UnsupportedOperationException {
		return (Path) callEclipseService("write", path, content, attrs);
	}

	@Override
	public boolean isRegularFile(String uri) {
		return (boolean) callEclipseService("isRegularFile", uri);
	}

	@Override
	public boolean isRegularFile(Path path) {
		return (boolean) callEclipseService("isRegularFile", path);
	}

	@Override
	public boolean isDirectory(String uri) {
		return (boolean) callEclipseService("isDirectory", uri);
	}

	@Override
	public boolean isDirectory(Path path) {
		return (boolean) callEclipseService("isDirectory", path);
	}

}
