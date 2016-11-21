package org.uberfire.client.shadowservices.impl;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.ShadowService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.shadowservices.WebappShadowService;
import org.uberfire.ext.editor.commons.service.CopyService;

@ApplicationScoped
@ShadowService
public class CopyShadowService extends WebappShadowService implements CopyService {

	public static String ECLIPSE_NAME = "EclipseCopyService";
	
	public String getEclipseServiceName() {
		return ECLIPSE_NAME;
	}

	@Override
	public Path copy(Path path, String newName, String comment) {
		return (Path) callEclipseService("copy", path, newName, comment);
	}

	@Override
	public Path copy(Path path, String newName, Path targetDirectory, String comment) {
		return (Path) callEclipseService("copy", path, newName, targetDirectory, comment);
	}

	@Override
	public void copyIfExists(Collection<Path> paths, String newName, String comment) {
		callEclipseService("copyIfExists", paths, newName, comment);
	}

	@Override
	public boolean hasRestriction(Path path) {
		return (boolean) callEclipseService("hasRestriction", path);
	}
}
