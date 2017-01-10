package org.uberfire.client.shadowservices.impl;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.guvnor.common.services.shared.config.AppConfigService;
import org.jboss.errai.bus.server.annotations.ShadowService;
import org.uberfire.client.shadowservices.WebappShadowService;

@ApplicationScoped
@ShadowService
public class AppConfigShadowService extends WebappShadowService implements AppConfigService {

	public static String ECLIPSE_NAME = "EclipseAppConfigService";

	@Override
	public String getEclipseServiceName() {
		return ECLIPSE_NAME;
	}

	@Override
	public Map<String, String> loadPreferences() {
//		return (Map<String, String>) callEclipseService("loadPreferences");
        final Map<String, String> preferences = new HashMap<String, String>();
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
		return preferences;
	}

	@Override
	public long getTimestamp() {
		return (long) callEclipseService("getTimestamp");
	}
}
