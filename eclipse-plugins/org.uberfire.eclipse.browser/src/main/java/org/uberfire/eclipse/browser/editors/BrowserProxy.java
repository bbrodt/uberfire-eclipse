package org.uberfire.eclipse.browser.editors;

import org.eclipse.core.resources.IFile;
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
import org.uberfire.eclipse.browser.FileUtils;


public class BrowserProxy  {

    UberfireEditor editor;
    Browser browser;
    BrowserListener browserListener;
    BrowserFunction vfsService;
    Action saveAction = null;

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
                    e.printStackTrace();
                }
            }
        }

        void buildMenuBar(Object[] menuEntries) {
            menuBar = editor.getEditorSite().getActionBars().getMenuManager();
            buildMenuBar((Object[])menuEntries, menuBar);

            IEditorActionBarContributor abc = editor.getEditorSite().getActionBarContributor();
            abc.setActiveEditor(editor);
        }
        
        void buildMenuBar(Object[] menuEntries, IMenuManager menu) {
            // Parse the menu entries returned from the {@see EclipseEditorBridge}.
            // This will be an array of groups of two Objects:
            // * the first Object is the menu label
            // * the second Object is either:
            //   * the menu action ID String if it is an action
            //   * an array of groups of two Objects if it is a submenu
            for (int i=0; i<menuEntries.length-1; i+=2) {
                Object caption = menuEntries[i];
                final Object item = menuEntries[i+1];
                if (item instanceof String) {
                    // it's a menu action
                    Action action = new UberfireEditorAction(browser, (String)caption, (String)item);

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
    
    public BrowserProxy(UberfireEditor editor) {
        this.editor = editor;
    }
    
    public void createBrowser(Composite parent, int style) {
        browser = new Browser(parent, style);

        browser.setJavascriptEnabled(true);
        browser.addKeyListener(new KeyListener() {
            
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
                            + editor.getFileUri() + "\");");
                    if (o instanceof Boolean)
                        dirty = ((Boolean) o).booleanValue();
                }
                catch (Exception e2) {
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

        vfsService = new BrowserFunction(browser, "EclipseVFSService") {
            @Override
            public Object function(Object[] arguments) {
                String function = "none";
                if (arguments.length > 0)
                    function = arguments[0].toString();
                if ("readAllString".equals(function)) {
                    String uriString = arguments[1].toString();
                    try {
                        System.out.println();
                        IFile file = FileUtils.getFile(uriString);
                        String contents = FileUtils.read(file);
                        return contents;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "";
                }
                if ("write".equals(function)) {
                    String uriString = arguments[1].toString();
                    String contents = arguments[2].toString();
                    String response = uriString;
                    try {
                        IFile file = FileUtils.getFile(uriString);
                        if (FileUtils.write(file, contents) < 0 )
                            response = "Write Error";
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return response;
                }
                return null;
            }
        };
        browserListener = new BrowserListener(this);
        browser.addProgressListener(browserListener);
    }
    
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

    public void setUrl(String url) {
        browser.setUrl(url);
    }

    public Browser getBrowser() {
        return browser;
    }
    
    public Action getSaveAction() {
        return saveAction;
    }
}
