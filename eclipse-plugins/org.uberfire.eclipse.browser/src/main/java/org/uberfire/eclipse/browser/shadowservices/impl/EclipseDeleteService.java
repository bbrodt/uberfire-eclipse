package org.uberfire.eclipse.browser.shadowservices.impl;

import java.util.Collection;

import org.eclipse.swt.browser.Browser;
import org.uberfire.backend.vfs.Path;
import org.uberfire.eclipse.browser.shadowservices.EclipseShadowService;
import org.uberfire.ext.editor.commons.service.DeleteService;

public class EclipseDeleteService extends EclipseShadowService implements DeleteService {

	public static final String NAME = "EclipseDeleteService";
	
	public EclipseDeleteService(Browser browser) {
		super(browser, NAME);
	}

	@Override
	public void delete(Path path, String comment) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteIfExists(Collection<Path> paths, String comment) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasRestriction(Path path) {
		// TODO Auto-generated method stub
		return false;
	}

}
