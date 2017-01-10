package org.uberfire.eclipse.browser.shadowservices.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.browser.Browser;
import org.kie.workbench.common.services.shared.rulename.RuleNamesService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.eclipse.browser.editors.BrowserProxy;
import org.uberfire.eclipse.browser.shadowservices.EclipseShadowService;

public class EclipseRuleNamesService extends EclipseShadowService implements RuleNamesService {

	public static final String NAME = "EclipseRuleNamesService";

	public EclipseRuleNamesService(BrowserProxy browserProxy) {
		super(browserProxy, NAME);
	}

	@Override
	public Collection<String> getRuleNames(Path path, String packageName) {
		return new ArrayList<String>();
	}

}
