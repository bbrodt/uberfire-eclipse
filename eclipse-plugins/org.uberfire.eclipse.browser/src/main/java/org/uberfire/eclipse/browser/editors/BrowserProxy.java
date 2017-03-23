package org.uberfire.eclipse.browser.editors;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.actions.RetargetAction;
import org.uberfire.eclipse.browser.shadowservices.EclipseShadowService;


/**
 * A proxy class for an SWT Browser widget. This acts as a bridge between the
 * EditorPart and the actual SWT widget and handles browser events.
 */
public class BrowserProxy  {

    UberfireEditor editor;
    Browser browser;
    BrowserListener browserListener;
    
    BrowserFunction vfsService;
    BrowserFunction drlTextEditorService;

    Action saveAction = null;

    /**
	 * A Browser progress listener. When the Browser widget has finished loading
	 * the URL that invokes the web app editor, it fires a Javascript function
	 * that fetches the editor's menu bar. The returned menu items are then
	 * hooked into the eclipse workbench menu bar.
	 * 
	 * TODO: insert common menu items (like Edit->Copy/Paste, File->Save, etc.)
	 * into existing eclipse workbench menus.
	 */
    private class BrowserListener implements ProgressListener {

        BrowserProxy browser;
        // flag indicating when page loading is completed, and the menuBar has been created
        boolean completed = false;
        // menuBar contributions by the WorkbenchEditor
        IMenuManager menuBar = null;
        
        public BrowserListener(BrowserProxy browser) {
            this.browser = browser;
        }
        
        @Override
        public void changed(ProgressEvent event) {
        }

        @Override
        public void completed(ProgressEvent event) {
            if (!completed) {
                try {
                    Object o = browser.evaluate(
                            "var ed = new window.uberclipse.Editor();"
                            + "return ed.getMenus(\"" + editor.getFileUri() + "\");");
      
                    if (o instanceof Object[]) {
                        buildMenuBar((Object[]) o);
                        completed = true;
                    }
                }
                catch (Exception e) {
                	// Uberfire objects may not be registered first time through
//                    e.printStackTrace();
                }
            }
        }

        /**
         * Add the web app editor's menu items to the eclipse workbench menu bar
         * 
		 * @param menuEntries - the array of menu entries returned by the web app
         */
        void buildMenuBar(Object[] menuEntries) {
            menuBar = editor.getEditorSite().getActionBars().getMenuManager();
            buildMenuBar((Object[])menuEntries, menuBar);

            IEditorActionBarContributor abc = editor.getEditorSite().getActionBarContributor();
            abc.setActiveEditor(editor);
        }
        
        /**
		 * Parse the menu entries returned from the {@see EclipseEditorBridge}.
		 * This will be an array of groups of two Objects: the first Object is
		 * the menu label the second Object is either:
		 * 1. the menu action ID String if it is an action, or
		 * 2. an array of groups of two Objects if it is a submenu
		 * 
		 * @param menuEntries - the array of menu entries returned by the web app
		 * @param menu - the eclipse menu manager
		 */
        void buildMenuBar(Object[] menuEntries, IMenuManager menu) {
            for (int i=0; i<menuEntries.length-1; i+=2) {
                Object caption = menuEntries[i];
                final Object item = menuEntries[i+1];
                if (item instanceof String) {
                    // it's a menu action
                    Action action = new UberfireEditorAction(browser, (String)caption, (String)item);

                    System.out.println("Action: "+action.getText()+" "+action.getId());
                    // search for common menu actions like File -> Save, etc.
                    if (action.getId().matches(".*[Ss]ave")) {
                        saveAction = action;
                    }
                    // ...others here?
                    else {
                        if (menu==menuBar) {
                            menu.insertAfter("edit", action);
                        }
                        else {
                            menu.add(action);
                        }
                    }
                }
                else if (item instanceof Object[]) {
                    // it's a submenu
                    MenuManager subMenu = new MenuManager((String)caption);
                    if (menu==menuBar) {
                        menu.insertAfter("edit", subMenu);
                    }
                    else {
                        menu.add(subMenu);
                    }
                    buildMenuBar((Object[])item, subMenu);
                }
            }
        }
    }
    
    /**
     * A menu action class for the web app editor menu items.
     */
    public static class UberfireEditorAction extends RetargetAction {
        BrowserProxy browser;
        
        public UberfireEditorAction(BrowserProxy browser, String text, String id) {
            super(id, text);
            this.browser = browser;
            setId(id);
        }
        @Override
        public boolean isEnabled() {
            return true;
        }
        @Override
        public void run() {
            browser.executeMenuAction(getId());
        }
        @Override
        public void runWithEvent(Event event) {
            run();
        }
    }
    
    /**
     * Construct the Browser Proxy class.
     * 
     * @param editor - the EditorPart
     */
    public BrowserProxy(UberfireEditor editor) {
        this.editor = editor;
    }
    
    /**
     * Called by the Uberfire EditorPart to create the SWT widget.
     * 
     * @param parent - the editor's container widget
     * @param style - SWT style bits
     */
    public void createBrowser(Composite parent, int style) {
        browser = new Browser(parent, style);

        browser.setJavascriptEnabled(true);
        browser.addKeyListener(new KeyListener() {
            
            /*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.
			 * events.KeyEvent)
			 * 
			 * TODO: this is an attempt to determine if the editor is "dirty".
			 * This obviously doesn't work for graphic editors that can become
			 * dirty from a mouseclick action. Need to figure out a way of getting
			 * events from the web app editor to let us know when the editor is dirty.
			 */
            @Override
            public void keyReleased(KeyEvent e) {
                char c = e.character;
                if (!isPrintableChar(c) &&
                    c!=SWT.DEL &&
                    c!=SWT.BS &&
                    c!=SWT.CR &&
                    c!=SWT.LF &&
                    c!=SWT.TAB &&
                    c!=SWT.SPACE)
                    return;
                boolean dirty = editor.isDirty();
                try {
                    Object o = evaluate(
                            "var ed = new window.uberclipse.Editor();"
                            + "return ed.isDirty(\""
                            + editor.getFileUri()
                            + "\");");
                    if (o instanceof Boolean)
                        dirty = ((Boolean) o).booleanValue();
                }
                catch (Exception e2) {
                	e2.printStackTrace();
                }
                editor.setDirty(dirty);
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
            }
            
            public boolean isPrintableChar( char c ) {
                Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
                return (!Character.isISOControl(c)) &&
                        block != null &&
                        block != Character.UnicodeBlock.SPECIALS;
            }
        });

        registerServiceFunctions();
        
        browserListener = new BrowserListener(this);
        browser.addProgressListener(browserListener);
    }
    
    /**
     * Register the server-side Shadow Services implemented by our eclipse plugin.
     */
    protected void registerServiceFunctions() {
    	EclipseShadowService.createServices(this);
    }
    
    /**
     * Run a menu action initiated from the eclipse workbench menu bar,
     * on the web app editor.
     *  
     * @param id - menu item ID
     * @return value returned by the web app editor
     */
    public Object executeMenuAction(String id) {
        try {
            return evaluate(
                    "var ed = new window.uberclipse.Editor();"
                    + "return ed.executeMenuCommand(\"" + editor.getFileUri() + "\","
                    + "\""+id+"\");");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object evaluate(String script) {
        return browser.evaluate(script);
    }

    /**
     * Point the SWT Browser widget at the given URL.
     * @param url
     */
    public void setUrl(String url) {
        browser.setUrl(url);
    }

    /**
     * @return the Uberfire EditorPart
     */
    public UberfireEditor getEditor() {
    	return editor;
    }
    
    /**
     * @return the SWT Browser widget
     */
    public Browser getBrowser() {
        return browser;
    }
    
    /**
     * @return the "File->Save" action.
     * TODO: implement other common menu actions for File and Edit menus.
     */
    public Action getSaveAction() {
        return saveAction;
    }
}
