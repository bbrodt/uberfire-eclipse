package org.uberfire.eclipse.browser.shadowservices;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;

public class ShadowService extends BrowserFunction {

	public ShadowService(Browser browser, String name) {
		super(browser, name);
	}

    @Override
    public Object function(Object[] arguments) {
    	Object result = null;
        String functionName = "";
        if (arguments.length > 0)
            functionName = arguments[0].toString();
        for (Method m : getClass().getMethods()) {
        	if (m.getName().equals(functionName) && m.getParameterCount()==arguments.length-1) {
        		// possible candidate: compare arguments
        		boolean match = true;
        		int ai = 0;
        		for (Parameter p : m.getParameters()) {
        			Class ac = arguments[ai]==null ? Object.class : arguments[ai].getClass();
        			Class pc = p.getType();
        			if (!pc.isAssignableFrom(ac)) {
        				match = false;
        				break;
        			}
        			++ai;
        		}
        		if (match) {
        			try {
        				if (arguments.length==1) {
        					if (m.getReturnType().equals(Void.TYPE))
        						m.invoke(this);
        					else
        						result = m.invoke(this);
        				}
        				else {
        					Object args[] = new Object[arguments.length-1];
        					for (int i=0; i<arguments.length-1; ++i)
        						args[i] = arguments[i+1];
        					if (m.getReturnType().equals(Void.TYPE))
            					m.invoke(this, args);
        					else
        						result = m.invoke(this, args);
        				}
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
        			break;
        		}
        	}
        }
        if (result!=null) {
//        	Gson gson = new Gson();
//        	return gson.toJson(result);
        }
        return result;
    }

	public Path createPath(String uri) {
        String filename = uri;
        int i = uri.lastIndexOf("/");
        if (i > 0)
            filename = uri.substring(i + 1);
        Path path = PathFactory.newPath(filename, uri);
        return path;
	}
}
