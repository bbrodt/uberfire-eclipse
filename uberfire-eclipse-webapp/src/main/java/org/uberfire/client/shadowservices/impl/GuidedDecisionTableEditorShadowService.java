package org.uberfire.client.shadowservices.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.drools.workbench.models.datamodel.workitems.PortableWorkDefinition;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.model.GuidedDecisionTableEditorContent;
import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableEditorService;
import org.guvnor.common.services.shared.metadata.model.DiscussionRecord;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.bus.server.annotations.ShadowService;
import org.kie.workbench.common.services.datamodel.model.PackageDataModelOracleBaselinePayload;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.impl.LockInfo;
import org.uberfire.client.shadowservices.WebappShadowService;
import org.uberfire.java.nio.base.version.VersionRecord;

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
//		return path;
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
		try {
			return (GuidedDecisionTableEditorContent) callEclipseService("loadContent", path);
//			GuidedDecisionTable52 model = new GuidedDecisionTable52();
//			model.setTableName("Test");
//			model.setPackageName("com.sample");
//			model.getRowNumberCol().setWidth(-1);
//			model.getDescriptionCol().setWidth(-1);
//			model.getAuditLog();
//			Set<PortableWorkDefinition> workDefinitions = new HashSet<PortableWorkDefinition>();
//			Overview overview = new Overview();
//			Metadata metadata = new Metadata(
//					path,
//					path,
//					"checkinComment",
//					"lastContributor",
//					"creator",
//					new Date(), // lastModified
//					new Date(), // dateCreated
//					"subject",
//					"type",
//					"externalRelation",
//					"externalSource",
//					"description",
//					new ArrayList<String>(), // tags
//					new ArrayList<DiscussionRecord>(),
//					new ArrayList<VersionRecord>(),
//					new LockInfo(true, "lockedBy", path)
//					);
//			overview.setMetadata(metadata);
//			overview.setProjectName("com.sample");
//
//			PackageDataModelOracleBaselinePayload dataModel = new PackageDataModelOracleBaselinePayload();
//			dataModel.setPackageName("com.sample");
//			dataModel.setProjectName("com.sample");
//			GuidedDecisionTableEditorContent content = new GuidedDecisionTableEditorContent(model, workDefinitions, overview, dataModel);
//			return content;
		}
		catch (Exception e) {
			return null;
		}
	}

	@Override
	public PackageDataModelOracleBaselinePayload loadDataModel(Path path) {
		return (PackageDataModelOracleBaselinePayload) callEclipseService("loadDataModel", path);
	}

	@Override
	public Path saveAndUpdateGraphEntries(Path path, GuidedDecisionTable52 model, Metadata metadata, String comment) {
		return (Path) callEclipseService("saveAndUpdateGraphEntries", path, model, metadata, comment);
//		return path;
	}
}
