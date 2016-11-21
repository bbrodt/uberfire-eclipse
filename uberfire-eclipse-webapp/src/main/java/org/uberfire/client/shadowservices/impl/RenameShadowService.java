package org.uberfire.client.shadowservices.impl;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.ShadowService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.shadowservices.WebappShadowService;
import org.uberfire.ext.editor.commons.service.RenameService;

@ApplicationScoped
@ShadowService
public class RenameShadowService extends WebappShadowService implements RenameService {

	public static String ECLIPSE_NAME = "EclipseRenameService";
	
	public String getEclipseServiceName() {
		return ECLIPSE_NAME;
	}

	@Override
	public Path rename(Path path, String newName, String comment) {
		return (Path) callEclipseService("rename", path, newName, comment);
	}

	@Override
	public void renameIfExists(Collection<Path> paths, String newName, String comment) {
		callEclipseService("renameIfExists", paths, newName, comment);
	}

	@Override
	public boolean hasRestriction(Path path) {
		return (boolean) callEclipseService("hasRestriction", path);
	}
}
