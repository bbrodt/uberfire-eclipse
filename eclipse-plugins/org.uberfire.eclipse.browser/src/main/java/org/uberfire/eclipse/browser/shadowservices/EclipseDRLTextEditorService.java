package org.uberfire.eclipse.browser.shadowservices;

import org.eclipse.swt.browser.Browser;

public class EclipseDRLTextEditorService extends ShadowService {

	public static final String NAME = "EclipseDRLTextEditorService";
	
	public EclipseDRLTextEditorService(Browser browser) {
		super(browser, NAME);
	}

}
