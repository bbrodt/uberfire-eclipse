package org.uberfire.eclipse.browser.editors;

import java.net.URI;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.jboss.errai.marshalling.server.MappingContextSingleton;

public class UberfireEditor extends EditorPart {

    private final static String INDEX_HTML = "/git/uberfire-eclipse/uberfire-eclipse-webapp/target/uberfire-eclipse-webapp-1.0.0-SNAPSHOT/index.html";

	static {
		try {
			MappingContextSingleton.get();

		}
		catch( Exception e) {
			e.printStackTrace();
		}
	
	}
    
    
    BrowserProxy browser;
    boolean dirty = false;

    public UberfireEditor() {
        super();
    }

    public void dispose() {
        super.dispose();
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        Action saveAction = browser.getSaveAction();
        if (saveAction!=null) {
            saveAction.run();
            setDirty(false);
        }
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void doSaveAs() {
        doSave(null);
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    protected void setDirty(boolean value) {
        dirty = value;
        firePropertyChange(PROP_DIRTY);
    }

    @Override
    public void createPartControl(final Composite parent) {
        try {
            browser = new BrowserProxy(this);
            browser.createBrowser(parent, SWT.NONE);
            String HOME = System.getProperty("user.home");
            String url = HOME + INDEX_HTML + "?path="+getFileUri()+"&id="+getEditorId();
            browser.setUrl(url);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getFileUri() {
        IPathEditorInput fie = (IPathEditorInput) getEditorInput();
        URI uri = fie.getPath().toFile().toURI();
        return uri.toString();
    }
    
    protected String getEditorId() {
    	String fileUri = getFileUri();
    	int i = fileUri.lastIndexOf(".");
    	if (i>0) {
    		String ext = fileUri.substring(i+1);
    		IExtensionRegistry reg = Platform.getExtensionRegistry();
    	    IConfigurationElement[] elements = reg.getConfigurationElementsFor("org.eclipse.ui.editors");
    	    for (IConfigurationElement e : elements) {
    	    	if (e.getName().equals("editor")) {
    	    		String id = null;
	    	    	id = e.getAttribute("id");
	    	    	if (id!=null && id.startsWith("org.uberfire.eclipse.editors.")) {
	    	    		id = id.replace("org.uberfire.eclipse.editors", "");
	    	    	}
	    	    	else
	    	    		id = null;

    	    	    if (id!=null) {
    	    	    	String extensions = e.getAttribute("extensions");
	    	    		for (String s : extensions.split(",")) {
	    	    			if (s.trim().equals(ext)) {
	    	    				// this is the one
	    	    				return id;
	    	    			}
	    	    		}
    	    	    }    	    		
    	    	}
    	    }
    	}
    	return null;
    }
    
    @Override
    public boolean isSaveOnCloseNeeded() {
        return super.isSaveOnCloseNeeded();
    }

    @Override
    public void setFocus() {
    }

    public Browser getBrowser() {
        return browser.getBrowser();
    }
}
