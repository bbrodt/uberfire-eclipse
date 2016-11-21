package org.uberfire.client.shadowservices.impl;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.ShadowService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.shadowservices.WebappShadowService;
import org.uberfire.ext.editor.commons.service.ValidationService;

@ApplicationScoped
@ShadowService
public class ValidationShadowService extends WebappShadowService implements ValidationService {

	public static String ECLIPSE_NAME = "EclipseValidationService";
	
	public String getEclipseServiceName() {
		return ECLIPSE_NAME;
	}

	@Override
	public boolean isFileNameValid(Path path, String fileName) {
		return true;
	}

	@Override
	public boolean isFileNameValid(String fileName) {
		return true;
	}
}
