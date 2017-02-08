package org.uberfire.client.shadowservices.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.guvnor.common.services.shared.file.SupportsUpdate;
import org.guvnor.common.services.shared.metadata.model.DiscussionRecord;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.guvnor.common.services.shared.validation.ValidationService;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.kie.workbench.common.services.shared.source.ViewSourceService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.impl.LockInfo;
import org.uberfire.client.shadowservices.WebappShadowService;
import org.uberfire.ext.editor.commons.service.support.SupportsCopy;
import org.uberfire.ext.editor.commons.service.support.SupportsCreate;
import org.uberfire.ext.editor.commons.service.support.SupportsDelete;
import org.uberfire.ext.editor.commons.service.support.SupportsRead;
import org.uberfire.ext.editor.commons.service.support.SupportsRename;
import org.uberfire.java.nio.base.version.VersionRecord;

public abstract class BaseEditorShadowService<T>
	extends WebappShadowService
	implements
		SupportsCopy,
		SupportsCreate<T>,
		SupportsDelete,
		SupportsRead<T>,
		SupportsRename,
		SupportsUpdate<T>,
		ValidationService<T>,
		ViewSourceService<T> {

	/**
	 * Dummy method to construct Overview and Metadata for testing
	 */
	protected Overview getOverview(Path path) {
		Overview overview = new Overview();
		Metadata metadata = new Metadata(
				path,
				path,
				"checkinComment",
				"lastContributor",
				"creator",
				new Date(), // lastModified
				new Date(), // dateCreated
				"subject",
				"type",
				"externalRelation",
				"externalSource",
				"description",
				new ArrayList<String>(), // tags
				new ArrayList<DiscussionRecord>(),
				new ArrayList<VersionRecord>(),
				new LockInfo(true, "lockedBy", path)
				);
		overview.setMetadata(metadata);
		overview.setProjectName("com.sample");

		return overview;
	}
	
	@Override
	public String toSource(Path path, T model) {
		try {
			return (String) callEclipseService("toSource", path, model);
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public List<ValidationMessage> validate(Path path, T content) {
		try {
			return (List<ValidationMessage>) callEclipseService("validate", path, content);
		} catch (Exception e) {
			return new ArrayList<ValidationMessage>();
		}
	}

	@Override
	public Path create(Path path, String fileName, T content, String comment) {
		try {
			return (Path) callEclipseService("create", path, fileName, content, comment);
		} catch (Exception e) {
			return path;
		}
	}

	@Override
	public T load(Path path) {
		try {
			return (T) callEclipseService("load", path);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Path save(Path path, T content, Metadata metadata, String comment) {
		try {
			return (Path) callEclipseService("save", path, content, metadata, comment);
		} catch (Exception e) {
			return path;
		}
	}

	@Override
	public void delete(Path path, String comment) {
		try {
			callEclipseService("delete", path, comment);
		} catch (Exception e) {
		}
	}

	@Override
	public Path copy(Path path, String newName, String comment) {
		try {
			return (Path) callEclipseService("copy", path, newName, comment);
		} catch (Exception e) {
			return path;
		}
	}

	@Override
	public Path copy(Path path, String newName, Path targetDirectory, String comment) {
		try {
			return (Path) callEclipseService("copy", path, newName, targetDirectory, comment);
		} catch (Exception e) {
			return path;
		}
	}

	@Override
	public Path rename(Path path, String newName, String comment) {
		try {
			return (Path) callEclipseService("rename", path, newName, comment);
		} catch (Exception e) {
			return path;
		}
	}

}
