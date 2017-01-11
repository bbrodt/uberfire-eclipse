package org.uberfire.eclipse.browser.shadowservices.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.drools.workbench.models.datamodel.workitems.PortableWorkDefinition;
import org.drools.workbench.models.guided.dtable.backend.GuidedDTXMLPersistence;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.model.GuidedDecisionTableEditorContent;
import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableEditorService;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.browser.Browser;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.kie.workbench.common.services.datamodel.model.PackageDataModelOracleBaselinePayload;
import org.uberfire.backend.vfs.Path;
import org.uberfire.eclipse.browser.FileUtils;
import org.uberfire.eclipse.browser.editors.BrowserProxy;

public class EclipseGuidedDecisionTableEditorService extends BaseEditorProviderShadowService implements GuidedDecisionTableEditorService {

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
		IFile file = FileUtils.getFile(path.toURI());
		String xml = GuidedDTXMLPersistence.getInstance().marshal(content);
		FileUtils.write(file, xml);
		return path;
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
