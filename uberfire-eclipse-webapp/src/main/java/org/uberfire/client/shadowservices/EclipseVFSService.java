package org.uberfire.client.shadowservices;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.ShadowService;
import org.uberfire.backend.vfs.DirectoryStream;
import org.uberfire.backend.vfs.DirectoryStream.Filter;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.backend.vfs.VFSService;
import org.uberfire.java.nio.IOException;
import org.uberfire.java.nio.file.AtomicMoveNotSupportedException;
import org.uberfire.java.nio.file.DirectoryNotEmptyException;
import org.uberfire.java.nio.file.FileAlreadyExistsException;
import org.uberfire.java.nio.file.FileSystemAlreadyExistsException;
import org.uberfire.java.nio.file.NoSuchFileException;
import org.uberfire.java.nio.file.NotDirectoryException;
import org.uberfire.java.nio.file.ProviderNotFoundException;

import com.google.gwt.user.client.Window;

@ApplicationScoped
@ShadowService
public class EclipseVFSService implements VFSService {

    @Override
    public Path get(String uri) {
        // TODO Auto-generated method stub
        return null;
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
    public String readAllString(Path path) throws IllegalArgumentException, NoSuchFileException, IOException {
        String response = "none";
        try {
            response = readAllString(path.toURI());
        }
        catch (Exception e) {
            response = "readAllString failed: "+e.getMessage();
            Window.alert(response);
        }
        return response;
    }

    public native static String readAllString( final String uri ) /*-{
        return "empty string"; //EclipseVFSService("readAllString", uri);
    }-*/;

    @Override
    public Path write(Path path, String content)
            throws IllegalArgumentException, IOException, UnsupportedOperationException {
        String uri = write(path.toURI(), content);
        String filename = uri;
        int i = uri.lastIndexOf("/");
        if (i>0)
            filename = uri.substring(i+1);
        return PathFactory.newPath(filename, uri);
    }

    public native static String write( final String uri, final String content ) /*-{
        return "write"; //EclipseVFSService("write", uri, content);
    }-*/;

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
