package org.uberfire.client.shadowservices;

import java.util.List;

import javax.enterprise.context.Dependent;

import org.drools.workbench.screens.drltext.model.DrlModelContent;
import org.drools.workbench.screens.drltext.service.DRLTextEditorService;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.bus.server.annotations.ShadowService;
import org.uberfire.backend.vfs.Path;

@Dependent
@ShadowService
public class EclipseDRLTextEditorService implements DRLTextEditorService {

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

	@Override
	public DrlModelContent loadContent(Path path) {
		// TODO Auto-generated method stub
		return null;
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
