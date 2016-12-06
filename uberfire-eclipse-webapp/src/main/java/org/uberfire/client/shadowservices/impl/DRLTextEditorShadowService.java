package org.uberfire.client.shadowservices.impl;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.drools.workbench.screens.drltext.model.DrlModelContent;
import org.drools.workbench.screens.drltext.service.DRLTextEditorService;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.bus.server.annotations.ShadowService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.shadowservices.WebappShadowService;

/**
 * Client-side Shadow Service implementation of the DRLTextEditorService.
 * 
 * @author bbrodt
 *
 */
@ApplicationScoped
@ShadowService
public class DRLTextEditorShadowService extends WebappShadowService implements DRLTextEditorService {

	public String getEclipseServiceName() {
		return "EclipseDRLTextEditorService";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidationMessage> validate(Path path, String content) {
		return (List<ValidationMessage>) callEclipseService("validate", path, content);
	}

	@Override
	public Path create(Path context, String fileName, String content, String comment) {
		return (Path) callEclipseService("create", context, fileName, content, comment);
	}

	@Override
	public String load(Path path) {
		return (String) callEclipseService("load", path);
	}

	@Override
	public Path save(Path path, String content, Metadata metadata, String comment) {
		return (Path) callEclipseService("save", path, content, metadata, comment);
	}

	@Override
	public void delete(Path path, String comment) {
		callEclipseService("delete", path, comment);
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
	public Path rename(Path path, String newName, String comment) {
		return (Path) callEclipseService("rename", path, newName, comment);
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
