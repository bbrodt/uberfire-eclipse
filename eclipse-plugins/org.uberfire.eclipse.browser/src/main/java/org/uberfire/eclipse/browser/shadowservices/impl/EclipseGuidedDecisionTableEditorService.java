package org.uberfire.eclipse.browser.shadowservices.impl;

import java.util.HashSet;
import java.util.Set;

import org.drools.workbench.models.datamodel.workitems.PortableWorkDefinition;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.model.GuidedDecisionTableEditorContent;
import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableEditorService;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.kie.workbench.common.services.datamodel.model.PackageDataModelOracleBaselinePayload;
import org.uberfire.backend.vfs.Path;
import org.uberfire.eclipse.browser.FileUtils;
import org.uberfire.eclipse.browser.editors.BrowserProxy;

/**
 * Service-side Shadow Service implementation of the GuidedDecisionTableEditorService.
 */
public class EclipseGuidedDecisionTableEditorService
	extends BaseEclipseEditorService<GuidedDecisionTable52>
	implements GuidedDecisionTableEditorService {

	public static final String NAME = "EclipseGuidedDecisionTableEditorService";
	
	public EclipseGuidedDecisionTableEditorService(BrowserProxy browserProxy) {
		super(browserProxy, NAME);
	}

	@Override
	public String toSource(Path path, GuidedDecisionTable52 model) {
		IFile file = FileUtils.getFile(path.toURI());
		try {
			return FileUtils.read(file);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public GuidedDecisionTableEditorContent loadContent(Path path) {
		IFile file = FileUtils.getFile(path.toURI());

		GuidedDecisionTable52 model = new GuidedDecisionTable52();
		model.setTableName(file.getName());
		model.setPackageName("com.sample");
		model.getRowNumberCol().setWidth(-1);
		model.getDescriptionCol().setWidth(-1);
		model.getAuditLog();
		Set<PortableWorkDefinition> workDefinitions = new HashSet<PortableWorkDefinition>();
		Overview overview = getOverview(path);
		PackageDataModelOracleBaselinePayload dataModel = new PackageDataModelOracleBaselinePayload();
		dataModel.setPackageName("com.sample");
		dataModel.setProjectName(file.getProject().getName());
		GuidedDecisionTableEditorContent content = new GuidedDecisionTableEditorContent(model, workDefinitions, overview, dataModel);
		return content;
	}

	@Override
	public PackageDataModelOracleBaselinePayload loadDataModel(Path path) {
		return null;
	}

	@Override
	public Path saveAndUpdateGraphEntries(Path resource, GuidedDecisionTable52 model, Metadata metadata, String comment) {
		return this.save(resource, model, metadata, comment);
	}

}
