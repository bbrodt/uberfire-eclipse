package org.uberfire.eclipse.browser.shadowservices.impl;

import java.util.ArrayList;
import java.util.List;

import org.drools.workbench.screens.globals.model.Global;
import org.drools.workbench.screens.globals.model.GlobalsEditorContent;
import org.drools.workbench.screens.globals.model.GlobalsModel;
import org.drools.workbench.screens.globals.service.GlobalsEditorService;
import org.eclipse.core.resources.IFile;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.kie.workbench.common.services.datamodel.backend.server.builder.util.GlobalsParser;
import org.uberfire.backend.vfs.Path;
import org.uberfire.commons.data.Pair;
import org.uberfire.eclipse.browser.FileUtils;
import org.uberfire.eclipse.browser.editors.BrowserProxy;

/**
 * Service-side Shadow Service implementation of the GlobalsEditorService.
 */
public class EclipseGlobalsEditorService extends BaseEclipseEditorService<GlobalsModel> implements GlobalsEditorService {

	public static final String NAME = "EclipseGlobalsEditorService";
	
	public EclipseGlobalsEditorService(BrowserProxy browserProxy) {
		super(browserProxy, NAME);
	}

	@Override
	public GlobalsEditorContent loadContent(Path path) {
		IFile file = FileUtils.getFile(path.toURI());

		Overview overview = getOverview(path);
		GlobalsModel model = new GlobalsModel();
		List<String> fullyQualifiedClassNames = new ArrayList<String>();
		fullyQualifiedClassNames.add("java.lang.String");
		fullyQualifiedClassNames.add("java.lang.Integer");
		fullyQualifiedClassNames.add("java.lang.Float");
		fullyQualifiedClassNames.add("java.lang.Byte");
		fullyQualifiedClassNames.add("java.lang.Double");
		fullyQualifiedClassNames.add("java.lang.Short");
		fullyQualifiedClassNames.add("java.lang.Long");
		fullyQualifiedClassNames.add("java.lang.Boolean");

		try {
			String content = FileUtils.read(file);
			List<Global> globals = new ArrayList<Global>();
			for (Pair<String, String> p : GlobalsParser.parseGlobals(content)) {
				globals.add(new Global(p.getK1(),p.getK2()));
				
			}
			model.setGlobals(globals);
			model.setPackageName("com.sample");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new GlobalsEditorContent(model, overview, fullyQualifiedClassNames);
	}

}
