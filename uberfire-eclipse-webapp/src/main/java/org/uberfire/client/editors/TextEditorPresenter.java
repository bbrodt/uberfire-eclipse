package org.uberfire.client.editors;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.VFSService;
import org.uberfire.client.annotations.WorkbenchEditor;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartTitleDecoration;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.editor.commons.client.menu.BasicFileMenuBuilder;
import org.uberfire.lifecycle.OnMayClose;
import org.uberfire.lifecycle.OnOpen;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.mvp.impl.PathPlaceRequest;
import org.uberfire.shared.UfResourceType;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.Menus;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;

@WorkbenchEditor(identifier = "EclipseTextEditor", supportedTypes = { UfResourceType.class })
public class TextEditorPresenter {

    public interface View extends UberView<TextEditorPresenter> {
        IsWidget getTitleWidget();

        void setContent(String content);

        String getContent();

        boolean isDirty();

        void setDirty(boolean dirty);
    }

    protected Menus menus;

    @Inject
    protected BasicFileMenuBuilder menuBuilder;

    @Inject
    private View view;

    @Inject
    protected Caller<VFSService> vfsServices;

    @Inject
    private PlaceManager placeManager;

    private PathPlaceRequest place;
    private ObservablePath path;

    @WorkbenchPartTitle
    public String getScreenTitle() {
        return "Text Editor";
    }

    @PostConstruct
    public void setup() {
        view.init(this);
    }

    @AfterInitialization
    public void init() {
//        Window.alert("TextEditorPresenter.init()");
        buildMenus();
    }

    @OnStartup
    public void onStartup(final ObservablePath path, final PlaceRequest place) {
//        Window.alert("TextEditorPresenter.onStartup(" + path.toURI() + ")");
        this.path = path;
        this.place = (PathPlaceRequest) place;
    }

    @OnOpen
    public void onOpen() {
//        Window.alert("TextEditorPresenter.onOpen(" + path.toURI() + ")");
        load();
    }

    @OnMayClose
    public boolean onMayClose() {
//        Window.alert("TextEditorPresenter.onMayClose(" + path.toURI() + ")");
        if (view.isDirty()) {
            return false;
        }
        return true;
    }

    @WorkbenchPartTitleDecoration
    public IsWidget getTitle() {
        return view.getTitleWidget();
    }

    @WorkbenchPartView
    public IsWidget asWidget() {
        return view;
    }

    @WorkbenchMenu
    public Menus getMenus() {
        return menus;
    }

    protected void buildMenus() {
        final Command cmd = new Command() {
            @Override
            public void execute() {
                Window.alert("Uberfire Rocks!");
            }
        };
        final MenuItem item = MenuFactory
                .newSimpleItem("Say Hello")
                .respondsWith(cmd)
                .endMenu()
                .build()
                .getItems()
                .get(0);
        List<MenuItem> items = new ArrayList<MenuItem>();
        items.add(item);
        MenuItem uberfireMenu = MenuFactory
                .newTopLevelMenu("Uberfire")
                .withItems(items)
                .endMenu()
                .build()
                .getItems()
                .get(0);
        menus = menuBuilder.addSave(new Command() {
            @Override
            public void execute() {
                onSave();
            }
        }).addNewTopLevelMenu(uberfireMenu).build();
    }
    
    public void onSave() {
//        Window.alert("TextEditorPresenter.onSave()");
        if (view.isDirty()) {
            save();
        }
    }

    public void onClose() {
//        Window.alert("TextEditorPresenter.onClose()");
        if (onMayClose()) {
            close();
        }
    }

    private void load() {
        vfsServices.call(new RemoteCallback<String>() {
            @Override
            public void callback(String response) {
//                Window.alert("readAllString returned: "+response);
                if (response == null)
                    response = "empty";
                view.setContent(response);
            }
        }).readAllString(path);
    }

    private void save() {
        String content = view.getContent();
        vfsServices.call(new RemoteCallback<Path>() {
            @Override
            public void callback(final Path response) {
//                Window.alert("TextEditorPresenter.save() Response: " + response);
                view.setDirty(false);
            }
        }).write(path, content);
    }

    private void close() {
        placeManager.closePlace(place);
    }
}
