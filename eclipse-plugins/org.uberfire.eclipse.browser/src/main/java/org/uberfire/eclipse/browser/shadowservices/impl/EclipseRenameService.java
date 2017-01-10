package org.uberfire.eclipse.browser.shadowservices.impl;

import java.util.Collection;

import org.eclipse.swt.browser.Browser;
import org.uberfire.backend.vfs.Path;
import org.uberfire.eclipse.browser.editors.BrowserProxy;
import org.uberfire.eclipse.browser.shadowservices.EclipseShadowService;
import org.uberfire.ext.editor.commons.service.RenameService;

public class EclipseRenameService extends EclipseShadowService implements RenameService {

	public static final String NAME = "EclipseRenameService";
	
	public EclipseRenameService(BrowserProxy browserProxy) {
		super(browserProxy, NAME);
	}

	@Override
	public Path rename(Path path, String newName, String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void renameIfExists(Collection<Path> paths, String newName, String comment) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasRestriction(Path path) {
		// TODO Auto-generated method stub
		return false;
	}

}
