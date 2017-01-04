package org.uberfire.client.shadowservices.impl;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.ShadowService;
import org.kie.workbench.common.services.shared.rulename.RuleNamesService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.shadowservices.WebappShadowService;

@ApplicationScoped
@ShadowService
public class RuleNamesShadowService extends WebappShadowService implements RuleNamesService {

	public static final String ECLIPSE_NAME = "EclipseRuleNamesService";

	@Override
	public String getEclipseServiceName() {
		return ECLIPSE_NAME;
	}

	@Override
	public Collection<String> getRuleNames(Path path, String packageName) {
		return (Collection<String>) callEclipseService("getRuleNames", path, packageName);
	}
}
