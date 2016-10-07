package org.uberfire.client.shadowservices;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.ShadowService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.editor.commons.version.VersionService;
import org.uberfire.java.nio.base.version.VersionRecord;

@ApplicationScoped
@ShadowService
public class EclipseVersionService implements VersionService {

	@Override
	public List<VersionRecord> getVersions(Path path) {
		return new ArrayList<VersionRecord>();
	}

	@Override
	public Path getPathToPreviousVersion(String uri) {
		return null;
	}

	@Override
	public Path restore(Path path, String comment) {
		return null;
	}

}
