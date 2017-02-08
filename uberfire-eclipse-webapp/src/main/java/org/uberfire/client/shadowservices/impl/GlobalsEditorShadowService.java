package org.uberfire.client.shadowservices.impl;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.drools.workbench.screens.globals.model.Global;
import org.drools.workbench.screens.globals.model.GlobalsEditorContent;
import org.drools.workbench.screens.globals.model.GlobalsModel;
import org.drools.workbench.screens.globals.service.GlobalsEditorService;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.jboss.errai.bus.server.annotations.ShadowService;
import org.jboss.errai.ioc.support.bus.client.ServiceNotReady;
import org.uberfire.backend.vfs.Path;

import com.google.gwt.user.client.Window;

@ApplicationScoped
@ShadowService
public class GlobalsEditorShadowService
	extends BaseEditorShadowService<GlobalsModel>
	implements GlobalsEditorService {


	@Override
	public String getEclipseServiceName() {
		return "EclipseGlobalsEditorService";
	}

	@Override
	public GlobalsEditorContent loadContent(Path path) {
		try {
			return (GlobalsEditorContent) callEclipseService("loadContent", path);
		}
		catch (ServiceNotReady snr) {
			throw snr;
		}
		catch (Exception e) {
			Window.alert(e.getClass().getSimpleName()+" in GlobalsEditorShadowService.loadContent: "+e.getMessage());
			Overview overview = getOverview(path);
			GlobalsModel model = new GlobalsModel();
			List<String> fullyQualifiedClassNames = new ArrayList<String>();
			List<Global> globals = new ArrayList<Global>();
			globals.add(new Global("globstring", "java.lang.String"));
			model.setGlobals(globals);
			model.setPackageName("com.sample");
			
			return new GlobalsEditorContent(model, overview, fullyQualifiedClassNames);
		}
	}
}
