package org.uberfire.eclipse.browser.shadowservices.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IPathEditorInput;
import org.guvnor.common.services.shared.config.AppConfigService;
import org.uberfire.eclipse.browser.FileUtils;
import org.uberfire.eclipse.browser.editors.BrowserProxy;
import org.uberfire.eclipse.browser.shadowservices.EclipseShadowService;

public class EclipseAppConfigService extends EclipseShadowService implements AppConfigService {

	public static String NAME = "EclipseAppConfigService";
	
	public EclipseAppConfigService(BrowserProxy browserProxy) {
		super(browserProxy, NAME);
	}

	@Override
	public Map<String, String> loadPreferences() {
        final Map<String, String> preferences = new HashMap<String, String>();
        IPathEditorInput fie = (IPathEditorInput) browserProxy.getEditor().getEditorInput();
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IFile file = workspace.getRoot().getFileForLocation(fie.getPath());
        IProject project = file.getProject();
        file = project.getFolder(".settings").getFile("org.uberfire.eclipse.prefs");
        if (file.exists()) {
        	try {
				String contents = FileUtils.read(file);
				String lines[] = contents.split("\n");
				for (String line : lines) {
					String words[] = line.split("=");
					if (words.length==2 && !words[0].trim().startsWith("#")) {
						preferences.put(words[0].trim(), words[1].trim());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        else {
        	preferences.put("visual-ruleflow", "true");
        	preferences.put("verifier", "true");
        	preferences.put("oryx-bpmn-editor", "true");
        	preferences.put("asset.format.enabled.formdef", "false");
        	preferences.put("asset.enabled.scorecards", "true");
        	preferences.put("drools.dateformat", "dd-MMM-yyyy");
        	preferences.put("drools.datetimeformat", "dd-MMM-yyyy hh:mm:ss");
        	preferences.put("drools.defaultlanguage", "en");
        	preferences.put("drools.defaultcountry", "US");
        	preferences.put("rule-modeller-onlyShowDSLStatements", "false");
        	preferences.put("designer.url", "http://localhost:8080");
        	preferences.put("designer.context", "designer");
        	preferences.put("designer.profile", "jbpm");
        }
		return preferences;
	}

	@Override
	public long getTimestamp() {
		return 0;
	}

}
