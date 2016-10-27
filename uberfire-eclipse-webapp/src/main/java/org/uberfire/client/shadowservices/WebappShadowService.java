package org.uberfire.client.shadowservices;

import org.jboss.errai.ioc.support.bus.client.ServiceNotReady;
import org.jboss.errai.marshalling.client.Marshalling;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.Window;

public abstract class WebappShadowService {

	public abstract String getEclipseServiceName();
	
	public Object callEclipseService(String funcName, Object... args) {
		String serviceName = getEclipseServiceName();
		String jsonArgs[] = new String[args.length];
		int i = 0;
		for (Object a : args) {
			jsonArgs[i++] = Marshalling.toJSON(a);
		}
//		Window.alert("callEclipseService: "+serviceName+" "+funcName+" "+jsonArgs);

		try {
			Object result = callEclipseServiceJs(serviceName, funcName, jsonArgs);
			if (result!=null) {
//				Window.alert("callEclipseService result: "+result);
				return Marshalling.fromJSON(result.toString());
			}
			else
				Window.alert("callEclipseService returned null");
		}
		catch (JavaScriptException jse) {
			throw new ServiceNotReady();
		}
		catch (Exception e) {
			Window.alert("Exception in callEclipseService:\n"+e.getClass().getName()+"\n"+e.getMessage());
		}
		return null;
	}

	private native Object callEclipseServiceJs( final String serviceName, final String funcName, final String... args ) /*-{
   		return window[serviceName](funcName, args);
	}-*/;
}
