package org.uberfire.eclipse.browser.shadowservices.impl;

import java.util.Collection;

import org.eclipse.swt.browser.Browser;
import org.uberfire.backend.vfs.Path;
import org.uberfire.eclipse.browser.FileUtils;
import org.uberfire.eclipse.browser.editors.BrowserProxy;
import org.uberfire.eclipse.browser.shadowservices.EclipseShadowService;
import org.uberfire.ext.editor.commons.backend.service.CopyServiceImpl;
import org.uberfire.ext.editor.commons.service.CopyService;

public class EclipseCopyService extends EclipseShadowService implements CopyService {

	public static final String NAME = "EclipseCopyService";

//	private CopyServiceImpl copyService;

	public EclipseCopyService(BrowserProxy browserProxy) {
		super(browserProxy, NAME);
	}

	@Override
	public Path copy(Path path, String newName, String comment) {
		String uri = path.toURI();
		int i = uri.lastIndexOf("/");
		if (i>0)
			uri = uri.substring(0,i);
		uri += "/" + newName;
		return FileUtils.createVfsPath(uri);
	}

	@Override
	public Path copy(Path path, String newName, Path targetDirectory, String comment) {
		if (targetDirectory==null)
			return copy(path, newName, comment);
		String uri = targetDirectory.toURI();
		uri += "/" + newName;
		return FileUtils.createVfsPath(uri);
	}

	@Override
	public void copyIfExists(Collection<Path> paths, String newName, String comment) {
	}

	@Override
	public boolean hasRestriction(Path path) {
		return false;
	}

}
