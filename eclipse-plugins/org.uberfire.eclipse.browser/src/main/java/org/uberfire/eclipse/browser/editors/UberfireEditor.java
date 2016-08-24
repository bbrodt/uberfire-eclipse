package org.uberfire.eclipse.browser.editors;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class UberfireEditor extends EditorPart {

    private final static String INDEX_HTML = "C:/Users/bbrodt/uberfire/uberfire-eclipse/target/uberfire-eclipse-1.0.0-SNAPSHOT/index.html";
    
    
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
        String saveId = "org.uberfire.workbench.model.menu.impl.MenuBuilderImpl$CurrentContext$1#Save";

        Object o = browser.executeMenuAction(saveId);
        setDirty(false);
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
            browser.setUrl(INDEX_HTML + "?path="+getFileUri());
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
