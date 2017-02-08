package org.uberfire.client.shadowservices.impl;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.drools.workbench.screens.drltext.model.DrlModelContent;
import org.drools.workbench.screens.drltext.service.DRLTextEditorService;
import org.jboss.errai.bus.server.annotations.ShadowService;
import org.uberfire.backend.vfs.Path;

/**
 * Client-side Shadow Service implementation of the DRLTextEditorService.
 * 
 * @author bbrodt
 *
 */
@ApplicationScoped
@ShadowService
public class DRLTextEditorShadowService
	extends BaseEditorShadowService<String>
	implements DRLTextEditorService {

	public String getEclipseServiceName() {
		return "EclipseDRLTextEditorService";
	}

	@Override
	public DrlModelContent loadContent(Path path) {
		return (DrlModelContent) callEclipseService("loadContent", path);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> loadClassFields(Path path, String fullyQualifiedClassName) {
    	return (List<String>) callEclipseService("loadClassFields", path, fullyQualifiedClassName);
	}

	@Override
	public String assertPackageName(String drl, Path resource) {
		return (String) callEclipseService("assertPackageName", drl, resource);
	}

}
