package org.uberfire.shared;

import java.util.HashMap;

import org.jboss.errai.ioc.client.container.IOC;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.mvp.impl.PathPlaceRequest;

import com.google.gwt.user.client.Window;

import jsinterop.annotations.JsType;

@JsType(namespace = "uberclipse", name = "PlaceManager")
public class EclipsePlaceManagerBridge {

	static HashMap<String,PathPlaceRequest> placeRequests = new HashMap<String,PathPlaceRequest>();
	
    public void goTo(String uri, String id) {
//        Window.alert("uberclipse.PlaceManager.goTo(" + uri + ")");
        getPlaceManager().goTo(createPlace(uri, id));
    }

    public void closePlace(String uri) {
//        Window.alert("uberclipse.PlaceManager.closePlace(" + uri + ")");
        getPlaceManager().closePlace(getPlace(uri));
        placeRequests.remove(uri);
    }
    
    public static PlaceManager getPlaceManager() {
        return IOC.getBeanManager().lookupBean(PlaceManager.class).getInstance();
    }
    
    public static PathPlaceRequest getPlace(String uri) {
    	return placeRequests.get(uri);
    }
    
    public static PathPlaceRequest createPlace(String uri, String id) {
        String filename = uri;
        int i = uri.lastIndexOf("/");
        if (i > 0)
            filename = uri.substring(i + 1);
        Path path = PathFactory.newPath(filename, uri);
        PathPlaceRequest place = new PathPlaceRequest(path, id);
        placeRequests.put(uri, place);
        return place;
    }
 }
