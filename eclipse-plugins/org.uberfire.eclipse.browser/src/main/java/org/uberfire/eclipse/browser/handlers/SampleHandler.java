package org.uberfire.eclipse.browser.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.uberfire.eclipse.browser.editors.UberfireEditor;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
	public SampleHandler() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPage[] pages = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages();
        for (IWorkbenchPage p : pages) {
            IEditorReference[] refs = p.findEditors(null, "uberfire.eclipse.browser.editors.UberfireEditor", IWorkbenchPage.MATCH_ID);
            for (IEditorReference r : refs) {
                IEditorPart editor = r.getEditor(false);
                if (editor instanceof UberfireEditor) {
                    Browser browser = ((UberfireEditor)editor).getBrowser();
                    IPath path = ((IPathEditorInput) editor.getEditorInput()).getPath();
                    Object o = browser.evaluate(
                          "var ed = new window.uberclipse.Editor();"
                          + "return ed.getMenus(\"" + path.toFile().toURI() + "\");");
    
                    if (o instanceof Object[]) {
                        printMenus((Object[])o);
                    }
                    
                    String saveId = "org.uberfire.workbench.model.menu.impl.MenuBuilderImpl$CurrentContext$2#Save";
                    browser.evaluate(
                            "var ed = new window.uberclipse.Editor();"
                            + "return ed.executeMenuCommand(\"" + path.toFile().toURI() + "\","
                            + "\""+saveId+"\");");
                          break;
                }
            }
        }
		
		return null;
	}

	public static void printMenus(Object[] menus) {
        printMenus((Object[])menus, 0);
	}
	
	public static void printMenus(Object[] menus, int indent) {
	    for (int i=0; i<menus.length-1; i+=2) {
            for (int in=0; in<indent; ++in)
                System.out.print("  ");
	        Object caption = menus[i];
	        Object item = menus[i+1];
	        if (item instanceof String) {
	            System.out.println(caption+": "+item);
	        }
	        else if (item instanceof Object[]) {
                System.out.println(caption+"->");
	            printMenus((Object[])item, indent+1);
	        }
	    }
	}
}
