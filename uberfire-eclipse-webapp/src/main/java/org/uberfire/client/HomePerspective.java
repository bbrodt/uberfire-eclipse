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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.uberfire.client.annotations.Perspective;
import org.uberfire.client.annotations.WorkbenchPerspective;
import org.uberfire.client.workbench.events.PerspectiveChange;
import org.uberfire.client.workbench.panels.impl.SimpleWorkbenchPanelPresenter;
import org.uberfire.shared.EclipseEditorBridge;
import org.uberfire.shared.EclipsePlaceManagerBridge;
import org.uberfire.workbench.model.PerspectiveDefinition;
import org.uberfire.workbench.model.impl.PerspectiveDefinitionImpl;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Window;

@ApplicationScoped
@WorkbenchPerspective(identifier = "HomePerspective", isDefault = true)

public class HomePerspective {
    PerspectiveDefinition perspectiveDefinition;
    String pathParameter;
    String idParameter;
    
    @Perspective
    public PerspectiveDefinition buildPerspective() {
    	// this panel hides menus: use this when the Uberfire-eclipse editors are ready for production
//        perspectiveDefinition = new PerspectiveDefinitionImpl(SingleWorkbenchPanelPresenter.class.getName() );
    	// this panel shows client menus: use this during debugging/testing
        perspectiveDefinition = new PerspectiveDefinitionImpl(SimpleWorkbenchPanelPresenter.class.getName() );
        perspectiveDefinition.setName( "Eclipse Editor Perspective" );
        return perspectiveDefinition;
    }
    
    /**
	 * Fetch the query parameters from URL. These should be the full path to the
	 * file being edited and the WorkbenchEditor identifier string.
	 */
    @PostConstruct
    public void onPostConstruct() {
        if ( Window.Location.getParameterMap().containsKey( "path" ) ) {
            if (!Window.Location.getParameterMap().get("path").isEmpty()) {
                pathParameter = Window.Location.getParameterMap().get("path").get(0);
                idParameter = Window.Location.getParameterMap().get("id").get(0);
            }
        }
    }

    /**
	 * Once the perspective has finished loading, load the requested editor
	 * 
	 * @param e
	 */
    public void loadEditor(@Observes PerspectiveChange e) {
    	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
//		        Window.alert("HomePerspective.loadEditor() path="+pathParameter);
		        EclipsePlaceManagerBridge ec = new EclipsePlaceManagerBridge();
				ec.goTo(pathParameter, idParameter);
				try {
					// test to make sure this stuff works
					// TODO: remove this
					new EclipseEditorBridge().getMenus(pathParameter);
				}
				catch (Exception e) {
					
				}
			}
		});
    }    
    
}
