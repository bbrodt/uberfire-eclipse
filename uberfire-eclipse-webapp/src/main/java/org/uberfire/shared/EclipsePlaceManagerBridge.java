package org.uberfire.shared;

import java.util.HashMap;

import org.jboss.errai.ioc.client.container.IOC;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.mvp.impl.PathPlaceRequest;

import jsinterop.annotations.JsType;

/**
 * This class acts as a Javascript to Java bridge object. The functions in this
 * object can be called from the eclipse plugin code by calling evaluate() on
 * the SWT Browser object.
 * 
 * The @JsType annotation lets the GWT compiler know not to name-mangle this
 * class; instead, it is known by its namespace (uberclipse) and name
 * (PlaceManager) in the javascript global namespace.
 * 
 * The class currently implements a minimal subset of PlaceManager functions to
 * open and close web app editors. Note that PlaceRequests must be cached because
 * the PlaceManager needs to use the same PlaceRequest used to create an editor
 * to also close it.
 * 
 * TODO: expand this class' functionality to include additional PlaceManager
 * interfaces.
 * 
 * @see org.uberfire.eclipse.browser.editors.BrowserProxy.BrowserListener
 * @author bbrodt
 *
 */
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
