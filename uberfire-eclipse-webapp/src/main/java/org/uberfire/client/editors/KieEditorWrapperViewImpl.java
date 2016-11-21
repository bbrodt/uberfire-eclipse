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

package org.uberfire.client.editors;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.kie.workbench.common.widgets.client.source.ViewDRLSourceWidget;
import org.kie.workbench.common.widgets.metadata.client.KieEditorWrapperView;
import org.kie.workbench.common.widgets.metadata.client.widget.OverviewWidgetPresenter;
import org.uberfire.client.views.pfly.multipage.MultiPageEditorImpl;
import org.uberfire.client.views.pfly.multipage.PageImpl;
import org.uberfire.client.views.pfly.tab.TabPanelEntry;
import org.uberfire.client.workbench.widgets.multipage.MultiPageEditor;
import org.uberfire.client.workbench.widgets.multipage.MultiPageEditorView;
import org.uberfire.client.workbench.widgets.multipage.Multiple;
import org.uberfire.client.workbench.widgets.multipage.Page;
import org.uberfire.ext.editor.commons.client.BaseEditorView;
import org.uberfire.client.editors.MultiPageEditorViewImpl;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

@Alternative
public class KieEditorWrapperViewImpl
        extends MultiPageEditorImpl
        implements KieEditorWrapperView {

    protected static final int EDITOR_TAB_INDEX = 0;
    protected static final int OVERVIEW_TAB_INDEX = 1;

    private KieEditorWrapperPresenter presenter;

    @Inject @Multiple
    MultiPageEditorViewImpl view;
    
    @Override
    public void setPresenter( KieEditorWrapperPresenter presenter ) {

        this.presenter = presenter;
    }

    @Override
    public MultiPageEditor getMultiPage() {
        return this;
    }

    @Override
    public void addMainEditorPage( BaseEditorView baseView ) {
        addPage( new PageImpl( baseView,
                               CommonConstants.INSTANCE.EditTabTitle() ) {
            @Override
            public void onFocus() {
                presenter.onEditTabSelected();
            }

            @Override
            public void onLostFocus() {
                presenter.onEditTabUnselected();
            }
        } );
    }

    @Override
    public void addOverviewPage( final OverviewWidgetPresenter overviewWidget,
                                 final Command onFocus ) {
//        addPage( new PageImpl( overviewWidget,
//                               CommonConstants.INSTANCE.Overview() ) {
//            @Override
//            public void onFocus() {
//                onFocus.execute();
//                presenter.onOverviewSelected();
//            }
//
//            @Override
//            public void onLostFocus() {
//
//            }
//        } );
    }

    @Override
    public void addSourcePage( ViewDRLSourceWidget sourceWidget ) {
//        addPage( new PageImpl( sourceWidget,
//                               CommonConstants.INSTANCE.SourceTabTitle() ) {
//            @Override
//            public void onFocus() {
//                presenter.onSourceTabSelected();
//            }
//
//            @Override
//            public void onLostFocus() {
//
//            }
//        } );
    }

    @Override
    public void addImportsTab( IsWidget importsWidget ) {
        addWidget( importsWidget,
                   CommonConstants.INSTANCE.DataObjectsTabTitle() );
    }

    @Override
    public boolean isEditorTabSelected() {
        return selectedPage() == EDITOR_TAB_INDEX;
    }

    @Override
    public boolean isOverviewTabSelected() {
        return selectedPage() == OVERVIEW_TAB_INDEX;
    }

    @Override
    public int getSelectedTabIndex() {
        return selectedPage();
    }

    @Override
    public void selectOverviewTab() {
        setSelectedTab( OVERVIEW_TAB_INDEX );
    }

    @Override
    public void selectEditorTab() {
        setSelectedTab( EDITOR_TAB_INDEX );
    }

    @Override
    public void setSelectedTab( int tabIndex ) {
        selectPage( tabIndex );
    }
    
    @Override
    public void addPage( final Page page ) {
        view.addPage( page );
    }

    @Override
    public void selectPage( final int index ) {
        view.selectPage( index );
    }

    @Override
    public int selectedPage() {
        return view.selectedPage();
    }

    @Override
    public void clear() {
        view.clear();
    }

    @Override
    public MultiPageEditorView getView() {
        return view;
    }

    @Override
    public void addWidget( final IsWidget widget, final String label ) {
        view.addPage( new PageImpl( widget, label ) );
    }

    @Override
    public Widget asWidget() {
        return view;
    }
}
