package org.uberfire.client.shadowservices;

import org.jboss.errai.ioc.support.bus.client.ServiceNotReady;
import org.jboss.errai.marshalling.client.Marshalling;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.Window;

/**
 * Base class for client-side Shadow Services. This class handles marshalling of
 * service call arguments to JSON, invokes the Eclipse browser function via a
 * native javascript call to handle the service request, and performs
 * unmarshalling of the returned object from JSON.
 * 
 * Shadow service implementations need to provide the name of the Eclipse-side
 * service function and calling arguments.
 * 
 * @author bbrodt
 *
 */
public abstract class WebappShadowService {

	/**
	 * Implementations must provide the service name. This is the name of a
	 * javascript function that was registered by the Eclipse browser.
	 * 
	 * @see org.uberfire.eclipse.browser.editors#registerServiceFunctions()
	 * 
	 * @return the name of a Shadow Service registered by the Eclipse plugin.
	 */
	public abstract String getEclipseServiceName();
	
	public Object callEclipseService(String funcName, Object... args) {
		Object object = null;
		String serviceName = getEclipseServiceName();
		// marshal the calling arguments
		String jsonArgs[] = new String[args.length];
		int i = 0;
		for (Object a : args) {
			jsonArgs[i++] = Marshalling.toJSON(a);
		}
		Window.alert("callEclipseService: "+serviceName+" "+funcName+" "+jsonArgs);

		try {
			// call the Eclipse browser function that implements this service
			Object result = callEclipseServiceJs(serviceName, funcName, jsonArgs);
			if (result!=null) {
				Window.alert("callEclipseService result: "+result);
				object = Marshalling.fromJSON(result.toString());
			}
			else
				Window.alert("callEclipseService returned null: "+serviceName+" "+funcName+" "+jsonArgs);
		}
		catch (JavaScriptException jse) {
			// During startup, the browser may not have completed loading the page
			// and the service implementation javascript function may not yet be
			// available. Throw an exception to tell Errai to try again later.
			throw new ServiceNotReady();
		}
		catch (Exception e) {
			Window.alert("Exception in call to Eclipse Shadow Service '" + serviceName + "." + funcName + ":\n"
					+ e.getClass().getName() + "\n" + e.getMessage());
		}
		if (object instanceof RuntimeException) {
			throw (RuntimeException) object;
		}
		return object;
	}

	private native Object callEclipseServiceJs( final String serviceName, final String funcName, final String... args ) /*-{
   		return window[serviceName](funcName, args);
	}-*/;
}
