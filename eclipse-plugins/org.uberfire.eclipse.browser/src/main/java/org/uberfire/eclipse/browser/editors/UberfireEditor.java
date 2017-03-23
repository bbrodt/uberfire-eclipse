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
import org.uberfire.client.annotations.WorkbenchEditor;

/**
 * The main editor class. This is used by all UF file types (e.g. ".drl", ".gdst", etc.)
 * It instantiates a Browser control which is used to render the web app client editor.
 * The uberfire-eclipse-webapp client determines which editors are available (see the UberfireShowcaseClient.gwt.xml)
 */
public class UberfireEditor extends EditorPart {

	// HTML used to contain the client editor. This is a borderless, margin-less window;
	// all editor artifacts are rendered by the client editor itself.
    private final static String INDEX_HTML = "/git/uberfire-eclipse/uberfire-eclipse-webapp/target/uberfire-eclipse-webapp-1.0.0-SNAPSHOT/index.html";

    // Initialize the Errai marshalling component
	static {
		try {
			MappingContextSingleton.get();

		}
		catch( Exception e) {
			e.printStackTrace();
		}
	
	}
    
    // a Browser proxy class
    BrowserProxy browser;
    // TODO: figure out how to determine if the web app editor is "dirty"
    boolean dirty = false;

    public UberfireEditor() {
        super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    public void dispose() {
        super.dispose();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor monitor) {
        Action saveAction = browser.getSaveAction();
        if (saveAction!=null) {
            saveAction.run();
            setDirty(false);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    @Override
    public void doSaveAs() {
        doSave(null);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
     */
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#isDirty()
     */
    @Override
    public boolean isDirty() {
        return dirty;
    }

    /**
     * TODO: this needs to be called somehow by the web app editor so that the
     * eclipse workbench knows if this editor window is "dirty".
     * 
     * @param value - true if the editor is dirty, false if not
     */
    protected void setDirty(boolean value) {
        dirty = value;
        firePropertyChange(PROP_DIRTY);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     * 
     * Construct the Browser proxy and load the index.html file, passing the editor file path
     * and WorkbenchEditor identifier (e.g. "GuidedDecisionTableEditor") as query parameters
     */
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

    /**
     * Returns the editor file name as a URI
     * @return URI
     */
    protected String getFileUri() {
        IPathEditorInput fie = (IPathEditorInput) getEditorInput();
        URI uri = fie.getPath().toFile().toURI();
        return uri.toString();
    }
    
	/**
	 * Extracts the WorkbenchEditor identifier portion from the plugin.xml
	 * "org.eclipse.ui.editors" extension point ID that corresponds to the filename
	 * extension of the file being edited.
	 * For example, if the editor file has the extension "gdst", the corresponding
	 * extension point ID is "org.uberfire.eclipse.editors.GuidedDecisionTableEditor"
	 * and this function returns "GuidedDecisionTableEditor". This is passed to the
	 * web app as a query parameter in the URL, where it is parsed out by the
	 * HomePerspective. It is then used to launch the correct editor app with a
	 * PlaceManager request.
	 * 
	 * @return editor ID
	 */
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
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#isSaveOnCloseNeeded()
     */
    @Override
    public boolean isSaveOnCloseNeeded() {
        return super.isSaveOnCloseNeeded();
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
	}

    /**
     * Returns the Browser widget
     * 
     * @return a Browser widget
     */
    public Browser getBrowser() {
        return browser.getBrowser();
    }
}
