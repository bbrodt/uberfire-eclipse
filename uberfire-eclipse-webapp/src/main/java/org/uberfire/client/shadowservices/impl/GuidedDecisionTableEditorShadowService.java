package org.uberfire.client.shadowservices.impl;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.model.GuidedDecisionTableEditorContent;
import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableEditorService;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.bus.server.annotations.ShadowService;
import org.kie.workbench.common.services.datamodel.model.PackageDataModelOracleBaselinePayload;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.shadowservices.WebappShadowService;

/**
 * Client-side Shadow Service implementation of the GuidedDecisionTableEditorShadowService.
 * 
 * @author bbrodt
 *
 */
@ApplicationScoped
@ShadowService
public class GuidedDecisionTableEditorShadowService extends WebappShadowService
		implements GuidedDecisionTableEditorService {

	@Override
	public String getEclipseServiceName() {
		return "EclipseGuidedDecisionTableEditorService";
	}

	@Override
	public String toSource(Path path, GuidedDecisionTable52 model) {
		return (String) callEclipseService("toSource", path, model);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidationMessage> validate(Path path, GuidedDecisionTable52 content) {
		return (List<ValidationMessage>) callEclipseService("validate", path, content);
	}

	@Override
	public Path create(Path context, String fileName, GuidedDecisionTable52 content, String comment) {
		return (Path) callEclipseService("create", context, fileName, content, comment);
	}

	@Override
	public GuidedDecisionTable52 load(Path path) {
		return (GuidedDecisionTable52) callEclipseService("load", path);
	}

	@Override
	public Path save(Path path, GuidedDecisionTable52 content, Metadata metadata, String comment) {
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
	public GuidedDecisionTableEditorContent loadContent(Path path) {
		return (GuidedDecisionTableEditorContent) callEclipseService("loadContent", path);
	}

	@Override
	public PackageDataModelOracleBaselinePayload loadDataModel(Path path) {
		return (PackageDataModelOracleBaselinePayload) callEclipseService("loadDataModel", path);
	}
}
