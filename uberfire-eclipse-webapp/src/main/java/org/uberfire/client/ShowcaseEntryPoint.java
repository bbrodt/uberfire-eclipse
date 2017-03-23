/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.uberfire.client;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.guvnor.common.services.shared.config.AppConfigService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.kie.workbench.common.services.shared.preferences.ApplicationPreferences;
import org.uberfire.client.mvp.PlaceManager;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * GWT's Entry-point for Uberfire-showcase
 */
@EntryPoint
public class ShowcaseEntryPoint {

	@Inject
	PlaceManager placeManager;

	@Inject
	protected AppConfigService appConfigService;

	@PostConstruct
	public void startApp() {
		// declareEclipseFunctions();
		loadPreferences();
		hideLoadingPopup();
	}

	void loadPreferences() {
		Map<String, String> preferences = appConfigService.loadPreferences();
		ApplicationPreferences.setUp(preferences);
	}

	// Fade out the "Loading application" pop-up
	private void hideLoadingPopup() {
		final Element e = RootPanel.get("loading").getElement();

		new Animation() {

			@Override
			protected void onUpdate(double progress) {
				e.getStyle().setOpacity(1.0 - progress);
			}

			@Override
			protected void onComplete() {
				e.getStyle().setVisibility(Style.Visibility.HIDDEN);
			}
		}.run(500);
	}

	public static native void redirect(String url)/*-{
													$wnd.location = url;
													}-*/;
}