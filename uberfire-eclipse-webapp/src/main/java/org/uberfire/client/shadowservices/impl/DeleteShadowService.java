package org.uberfire.client.shadowservices.impl;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.ShadowService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.shadowservices.WebappShadowService;
import org.uberfire.ext.editor.commons.service.DeleteService;

@ApplicationScoped
@ShadowService
public class DeleteShadowService extends WebappShadowService implements DeleteService {

	public static String ECLIPSE_NAME = "EclipseDeleteService";
	
	public String getEclipseServiceName() {
		return ECLIPSE_NAME;
	}

	@Override
	public void delete(Path path, String comment) {
		callEclipseService("delete", path, comment);
	}

	@Override
	public void deleteIfExists(Collection<Path> paths, String comment) {
		callEclipseService("deleteIfExists", paths, comment);
	}

	@Override
	public boolean hasRestriction(Path path) {
		return (boolean) callEclipseService("hasRestriction", path);
	}
}
