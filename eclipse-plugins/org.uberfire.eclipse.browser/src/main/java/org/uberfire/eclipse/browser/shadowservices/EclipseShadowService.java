package org.uberfire.eclipse.browser.shadowservices;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.jboss.errai.marshalling.server.ServerMarshalling;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;

public class EclipseShadowService extends BrowserFunction {

	public EclipseShadowService(Browser browser, String name) {
		super(browser, name);
	}

    @Override
    public Object function(Object[] arguments) {
    	Object result = null;
    	// first argument is always the function name as a string
        String functionName = "";
        if (arguments.length > 0) {
            functionName = arguments[0].toString();
//            System.out.println("call to "+getName()+"."+functionName);
        }

        // remaining arguments should be JSON string representations of actual service call parameters
        Object jsonArgs[] = (Object[]) arguments[1];
        Object args[] = new Object[jsonArgs.length];
        for (int i=0; i<jsonArgs.length; ++i) {
//        	System.out.println("  jsonArgs["+i+"] "+jsonArgs[i].toString());
        	args[i] = ServerMarshalling.fromJSON(jsonArgs[i].toString());
//        	System.out.println("  args["+i+"] type "+args[i].getClass().getSimpleName());
        }
        
        boolean match = false;
        for (Method m : getClass().getMethods()) {
        	if (m.getName().equals(functionName) && m.getParameterCount()==jsonArgs.length) {
        		// possible candidate: compare number of arguments and their types
        		match = true;
        		int i = 0;
        		for (Parameter p : m.getParameters()) {
        			Class ac = args[i]==null ? Object.class : args[i].getClass();
        			Class pc = p.getType();
        			if (!pc.isAssignableFrom(ac)) {
        				match = false;
        				break;
        			}
        			++i;
        		}
        		if (match) {
        			try {
        				if (jsonArgs.length==0) {
        					// no arguments method
        					if (m.getReturnType().equals(Void.TYPE))
        						m.invoke(this);
        					else
        						result = m.invoke(this);
        				}
        				else {
        					// one or more arguments method
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
        if (!match) {
        	System.out.println("function "+functionName+" in "+getName()+" not found!");
        }
        
        if (result!=null) {
        	// result needs to be marshalled to JSON string for return
        	result = ServerMarshalling.toJSON(result);
//        	System.out.println("returns "+result);
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
