package org.uberfire.client.shadowservices.impl;

import javax.enterprise.context.ApplicationScoped;

import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.model.GuidedDecisionTableEditorContent;
import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableEditorService;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.jboss.errai.bus.server.annotations.ShadowService;
import org.kie.workbench.common.services.datamodel.model.PackageDataModelOracleBaselinePayload;
import org.uberfire.backend.vfs.Path;

/**
 * Client-side Shadow Service implementation of the GuidedDecisionTableEditorShadowService.
 * 
 * @author bbrodt
 *
 */
@ApplicationScoped
@ShadowService
public class GuidedDecisionTableEditorShadowService
	extends BaseEditorShadowService<GuidedDecisionTable52>
	implements GuidedDecisionTableEditorService {

	@Override
	public String getEclipseServiceName() {
		return "EclipseGuidedDecisionTableEditorService";
	}

	@Override
	public GuidedDecisionTableEditorContent loadContent(Path path) {
//		try {
			return (GuidedDecisionTableEditorContent) callEclipseService("loadContent", path);
//		}
//		catch (Exception e) {
//			GuidedDecisionTable52 model = new GuidedDecisionTable52();
//			model.setTableName("Test");
//			model.setPackageName("com.sample");
//			model.getRowNumberCol().setWidth(-1);
//			model.getDescriptionCol().setWidth(-1);
//			model.getAuditLog();
//			Set<PortableWorkDefinition> workDefinitions = new HashSet<PortableWorkDefinition>();
//			Overview overview = getOverview(path);
//
//			PackageDataModelOracleBaselinePayload dataModel = new PackageDataModelOracleBaselinePayload();
//			dataModel.setPackageName("com.sample");
//			dataModel.setProjectName("com.sample");
//			GuidedDecisionTableEditorContent content = new GuidedDecisionTableEditorContent(model, workDefinitions, overview, dataModel);
//			return content;
//		}
	}

	@Override
	public PackageDataModelOracleBaselinePayload loadDataModel(Path path) {
		return (PackageDataModelOracleBaselinePayload) callEclipseService("loadDataModel", path);
	}

	@Override
	public Path saveAndUpdateGraphEntries(Path path, GuidedDecisionTable52 model, Metadata metadata, String comment) {
//		try {
			return (Path) callEclipseService("saveAndUpdateGraphEntries", path, model, metadata, comment);
//		}
//		catch (Exception e) {
//			return path;
//		}
	}
}
