package org.uberfire.shared;

import org.jboss.errai.ioc.client.container.IOC;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.mvp.impl.PathPlaceRequest;

import com.google.gwt.user.client.Window;

import jsinterop.annotations.JsType;

@JsType(namespace = "uberclipse", name = "PlaceManager")
public class EclipsePlaceManagerBridge {

    public void goTo(String uri) {
//        Window.alert("uberclipse.PlaceManager.goTo(" + uri + ")");
        getPlaceManager().goTo(createPlace(uri));
    }

    public void closePlace(String uri) {
//        Window.alert("uberclipse.PlaceManager.closePlace(" + uri + ")");
        getPlaceManager().closePlace(createPlace(uri));
    }
    
    public static PlaceManager getPlaceManager() {
        return IOC.getBeanManager().lookupBean(PlaceManager.class).getInstance();
    }
    
    public static PathPlaceRequest createPlace(String uri) {
        String filename = uri;
        int i = uri.lastIndexOf("/");
        if (i > 0)
            filename = uri.substring(i + 1);
        Path path = PathFactory.newPath(filename, uri);
        return new PathPlaceRequest(path, "TextEditor");
    }
 }
