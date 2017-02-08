package org.uberfire.eclipse.browser.shadowservices;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.browser.BrowserFunction;
import org.jboss.errai.common.client.protocols.SerializationParts;
import org.jboss.errai.marshalling.client.marshallers.ObjectMarshaller;
import org.jboss.errai.marshalling.server.ServerMarshalling;
import org.uberfire.eclipse.browser.editors.BrowserProxy;
import org.uberfire.eclipse.browser.shadowservices.impl.EclipseAppConfigService;
import org.uberfire.eclipse.browser.shadowservices.impl.EclipseCopyService;
import org.uberfire.eclipse.browser.shadowservices.impl.EclipseDRLTextEditorService;
import org.uberfire.eclipse.browser.shadowservices.impl.EclipseDeleteService;
import org.uberfire.eclipse.browser.shadowservices.impl.EclipseGlobalsEditorService;
import org.uberfire.eclipse.browser.shadowservices.impl.EclipseGuidedDecisionTableEditorService;
import org.uberfire.eclipse.browser.shadowservices.impl.EclipseRenameService;
import org.uberfire.eclipse.browser.shadowservices.impl.EclipseRuleNamesService;
import org.uberfire.eclipse.browser.shadowservices.impl.EclipseVFSService;

/**
 * Base class for server-side Shadow Services. This class implements the browser
 * function that handles a Shadow Service request. The name of the javascript
 * function registered with the browser is the name of the service, e.g.
 * "EclipseVfsService()".
 * 
 * The first argument to this function call must be the name of the actual
 * service method to be invoked. Calling arguments to this method, which are
 * JSON representations of the actual service call arguments, are passed to the
 * Javascript function in a String array. These JSON arguments are materialized
 * as Java objects using the ServerMarshalling service in errai.
 * 
 * Java reflection is used to look up the service method in the service
 * implementation class by name and number and type of arguments.
 * 
 * Finally the return value from the service method, if any, is serialized as a
 * JSON string and returned to the client.
 * 
 * @author bbrodt
 *
 */
public class EclipseShadowService extends BrowserFunction {

	private static List<Class<? extends EclipseShadowService>> serviceRegistry = new ArrayList<Class<? extends EclipseShadowService>>();
	static {
		serviceRegistry.add(EclipseCopyService.class);
		serviceRegistry.add(EclipseDeleteService.class);
		serviceRegistry.add(EclipseDRLTextEditorService.class);
		serviceRegistry.add(EclipseRenameService.class);
		serviceRegistry.add(EclipseVFSService.class);
		serviceRegistry.add(EclipseGuidedDecisionTableEditorService.class);
		serviceRegistry.add(EclipseRuleNamesService.class);
		serviceRegistry.add(EclipseAppConfigService.class);
		serviceRegistry.add(EclipseGlobalsEditorService.class);
		
		ObjectMarshaller m = new ObjectMarshaller();
	}
	
	protected BrowserProxy browserProxy;
	
	public EclipseShadowService(BrowserProxy browserProxy, String name) {
		super(browserProxy.getBrowser(), name);
		this.browserProxy = browserProxy;
	}
	
	public static void createServices(BrowserProxy browserProxy) {
		for (Class<? extends EclipseShadowService> clazz : serviceRegistry) {
			Constructor<? extends EclipseShadowService> ctor;
			try {
				ctor = clazz.getConstructor(BrowserProxy.class);
				EclipseShadowService service = ctor.newInstance(browserProxy);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

        // remaining arguments should be JSON string representations of actual service call arguments
        Object jsonArgs[] = (Object[]) arguments[1];
        Object args[] = new Object[jsonArgs.length];
        for (int i=0; i<jsonArgs.length; ++i) {
//        	System.out.println("  jsonArgs["+i+"] "+jsonArgs[i].toString());
        	args[i] = fromJSON(jsonArgs[i].toString());
//        	System.out.println("  args["+i+"] type "+args[i].getClass().getSimpleName());
        }
        
        // look up service method by name and number and type of arguments using reflection
        boolean match = false;
        for (Method m : getClass().getMethods()) {
        	if (m.getName().equals(functionName) && m.getParameterCount()==jsonArgs.length) {
				// possible candidate: compare number of arguments and their
				// types to method parameters
        		match = true;
        		int i = 0;
        		for (Parameter p : m.getParameters()) {
        			Class ac = args[i]==null ? Object.class : args[i].getClass();
        			Class pc = p.getType();
        			if (!pc.isAssignableFrom(ac) && args[i]!=null) {
        				System.out.println("ac="+ac+" pc="+pc);
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
					}
        			catch (Exception e) {
						e.printStackTrace();
						result = e;
					}
        			break;
        		}
        	}
        }
        
        if (!match) {
        	System.err.println("function "+functionName+" in "+getName()+" not found!");
        }
        
        if (result!=null) {
        	// result needs to be marshalled to JSON string for return
        	result = toJSON(result);
//        	System.out.println("returns "+result);
        }
        return result;
    }
    
    private Object fromJSON(String json) {
    	String nullObject = "{\"" + SerializationParts.ENCODED_TYPE + "\":\"java.lang.Object\",\""
              + SerializationParts.QUALIFIED_VALUE + "\":null}";
    	if (nullObject.equals(json))
    		return null;
    	return ServerMarshalling.fromJSON(json);
    }
    
    private String toJSON(Object object) {
    	return ServerMarshalling.toJSON(object);
    }
}
