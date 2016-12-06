package org.uberfire.eclipse.browser.shadowservices.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.drools.workbench.models.datamodel.workitems.PortableWorkDefinition;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.model.GuidedDecisionTableEditorContent;
import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableEditorService;
import org.eclipse.swt.browser.Browser;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.kie.workbench.common.services.datamodel.model.PackageDataModelOracleBaselinePayload;
import org.uberfire.backend.vfs.Path;

public class EclipseGuidedDecisionTableEditorService extends BaseEditorProviderShadowService implements GuidedDecisionTableEditorService {

	public static final String NAME = "EclipseGuidedDecisionTableEditorService";
	
	public EclipseGuidedDecisionTableEditorService(Browser browser) {
		super(browser, NAME);
	}

	@Override
	public String toSource(Path path, GuidedDecisionTable52 model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ValidationMessage> validate(Path path, GuidedDecisionTable52 content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path create(Path context, String fileName, GuidedDecisionTable52 content, String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GuidedDecisionTable52 load(Path path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path save(Path path, GuidedDecisionTable52 content, Metadata metadata, String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Path path, String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Path copy(Path path, String newName, String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path copy(Path path, String newName, Path targetDirectory, String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path rename(Path path, String newName, String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GuidedDecisionTableEditorContent loadContent(Path path) {
		GuidedDecisionTable52 model = new GuidedDecisionTable52();
		Set<PortableWorkDefinition> workDefinitions = new HashSet<PortableWorkDefinition>();
		Overview overview = getOverview(path);
		PackageDataModelOracleBaselinePayload dataModel = new PackageDataModelOracleBaselinePayload();
		GuidedDecisionTableEditorContent content = new GuidedDecisionTableEditorContent(model, workDefinitions, overview, dataModel);
		return content;
	}

	@Override
	public PackageDataModelOracleBaselinePayload loadDataModel(Path arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
