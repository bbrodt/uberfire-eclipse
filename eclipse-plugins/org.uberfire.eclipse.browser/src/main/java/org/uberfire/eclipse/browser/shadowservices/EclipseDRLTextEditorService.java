package org.uberfire.eclipse.browser.shadowservices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.drools.workbench.models.datamodel.rule.DSLSentence;
import org.drools.workbench.screens.drltext.model.DrlModelContent;
import org.drools.workbench.screens.drltext.service.DRLTextEditorService;
import org.eclipse.swt.browser.Browser;
import org.guvnor.common.services.shared.metadata.model.DiscussionRecord;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.backend.vfs.impl.LockInfo;
import org.uberfire.java.nio.base.version.VersionRecord;

public class EclipseDRLTextEditorService extends ShadowService implements DRLTextEditorService {

	public static final String NAME = "EclipseDRLTextEditorService";
	
	public EclipseDRLTextEditorService(Browser browser) {
		super(browser, NAME);
	}

	@Override
	public List<ValidationMessage> validate(Path path, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path create(Path context, String fileName, String content, String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String load(Path path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path save(Path path, String content, Metadata metadata, String comment) {
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

	public DrlModelContent loadContent(String uri) {
        return loadContent(createPath(uri));
	}

	@Override
	public DrlModelContent loadContent(Path path) {
		Overview overview = new Overview();
		Metadata metadata = new Metadata(path, path,
				"checkinComment", "lastContributor", "creator",
				new Date(), new Date(),
				"subject","type","externalRelation", "externalSource", "description",
				new ArrayList<String>(), new ArrayList<DiscussionRecord>(),
				new ArrayList<VersionRecord>(),
				new LockInfo(false, "", null));
		overview.setMetadata(metadata);
		overview.setProjectName("projectName");
		DrlModelContent content = new DrlModelContent(
				"package org.drools;",
				overview,
				new ArrayList<String>(),
				new ArrayList<DSLSentence>(),
				new ArrayList<DSLSentence>()
				);
		return content;
	}

	@Override
	public List<String> loadClassFields(Path path, String fullyQualifiedClassName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String assertPackageName(String drl, Path resource) {
		// TODO Auto-generated method stub
		return null;
	}

}
