package org.uberfire.shared;

import java.util.ArrayList;
import java.util.List;

import org.uberfire.client.mvp.Activity;
import org.uberfire.client.mvp.WorkbenchEditorActivity;
import org.uberfire.ext.editor.commons.client.history.SaveButton;
import org.uberfire.workbench.model.menu.MenuCustom;
import org.uberfire.workbench.model.menu.MenuGroup;
import org.uberfire.workbench.model.menu.MenuItemCommand;
import org.uberfire.workbench.model.menu.MenuItemPerspective;
import org.uberfire.workbench.model.menu.MenuItemPlain;
import org.uberfire.workbench.model.menu.MenuVisitor;
import org.uberfire.workbench.model.menu.Menus;
import org.uberfire.workbench.model.menu.impl.BaseMenuCustom;

import com.google.gwt.user.client.Window;

import jsinterop.annotations.JsType;

/**
 * This class acts as a Javascript to Java bridge object. The functions in this
 * object can be called from the eclipse plugin code by calling evaluate() on
 * the SWT Browser object.
 * 
 * The @JsType annotation lets the GWT compiler know not to name-mangle this
 * class; instead, it is known by its namespace (uberclipse) and name (Editor)
 * in the javascript global namespace.
 * 
 * The class currently implements a editor menu interface (fetch menu items
 * and execute menu actions)
 * 
 * TODO: expand this class' functionality to include additional editor interfaces.
 * 
 * @see org.uberfire.eclipse.browser.editors.BrowserProxy.BrowserListener
 * @author bbrodt
 *
 */
@JsType(namespace = "uberclipse", name = "Editor")
public class EclipseEditorBridge {

    private static class MenuBuilder implements MenuVisitor {

        List<Object> items = new ArrayList<Object>();
        List<List<Object>> groupStack = new ArrayList<List<Object>>();
        int groupLevel = 0;
        
        @Override
        public boolean visitEnter(Menus menus) {
            return true;
        }

        @Override
        public void visitLeave(Menus menus) {
        }

        @Override
        public boolean visitEnter(MenuGroup menuGroup) {
            items.add(menuGroup.getCaption());
            groupStack.add(items);
            ++groupLevel;
            items = new ArrayList<Object>();
            return true;
        }

        @Override
        public void visitLeave(MenuGroup menuGroup) {
            List<Object> groupItems = items;
            items = groupStack.get(--groupLevel);
            items.add(groupItems.toArray(new String[groupItems.size()]));
        }

        @Override
        public void visit(MenuItemPlain menuItemPlain) {
            items.add(menuItemPlain.getCaption());
            items.add(menuItemPlain.getIdentifier());
        }

        @Override
        public void visit(MenuItemCommand menuItemCommand) {
            items.add(menuItemCommand.getCaption());
            items.add(menuItemCommand.getIdentifier());
        }

        @Override
        public void visit(MenuItemPerspective menuItemPerspective) {
            items.add(menuItemPerspective.getCaption());
            items.add(menuItemPerspective.getIdentifier());
        }

        @Override
        public void visit(MenuCustom<?> menuCustom) {
        	if (menuCustom instanceof SaveButton) {
        		items.add("Save");
        		items.add(getClass().getName()+"#Save");
        	}
        	else {
        		items.add(menuCustom.getCaption());
        		items.add(menuCustom.getIdentifier());
        		String className = menuCustom.getClass().getName();
        		if (menuCustom instanceof BaseMenuCustom) {
        			Object item = ((BaseMenuCustom)menuCustom).build();
        			className = item.getClass().getName();
        			if (className.contains("BaseMenuCustom")) {
        				BaseMenuCustom bmc = (BaseMenuCustom) item;
        				String ca = bmc.getCaption();
        				String cb = bmc.getContributionPoint();
        				String id = bmc.getIdentifier();
        				List ra = bmc.getResourceActions();
        				Window.alert("");
        			}
        		}
        	}
        }
        
        public Object[] getMenus() {
            return items.toArray(new Object[items.size()]);
        }
    }
    
    public Object[] getMenus(String uri) {
        WorkbenchEditorActivity editor = getEditor(uri);
        if (editor != null) {
            Menus menus = editor.getMenus();
            if (menus != null) {
                MenuBuilder visitor = new MenuBuilder();
                menus.accept(visitor);
                return visitor.getMenus();
            }
        }
        return null;
    }

    public boolean isDirty(String uri) {
        return !getEditor(uri).isDirty(); //.onMayClose();
    }

    public void executeMenuCommand(String uri, final String cmdId) {
        WorkbenchEditorActivity editor = getEditor(uri);
        if (editor != null) {
            Menus menus = editor.getMenus();
            if (menus != null) {
                menus.accept(new MenuVisitor() {

                    @Override
                    public void visitLeave(MenuGroup menuGroup) {
                    }

                    @Override
                    public void visitLeave(Menus menus) {
                    }

                    @Override
                    public boolean visitEnter(MenuGroup menuGroup) {
                        return true;
                    }

                    @Override
                    public boolean visitEnter(Menus menus) {
                        return true;
                    }

                    @Override
                    public void visit(MenuCustom<?> menuCustom) {
                        // TODO: how can I execute these?
                    }

                    @Override
                    public void visit(MenuItemPerspective menuItemPerspective) {
                    }

                    @Override
                    public void visit(MenuItemCommand menuItemCommand) {
                        if (menuItemCommand.getIdentifier().equals(cmdId)) {
                            menuItemCommand.getCommand().execute();
                        }
                    }

                    @Override
                    public void visit(MenuItemPlain menuItemPlain) {
                    }
                });
            }
        }
    }

    private WorkbenchEditorActivity getEditor(String uri) {
        Activity a = EclipsePlaceManagerBridge.getPlaceManager()
                .getActivity(EclipsePlaceManagerBridge.getPlace(uri));
        if (a instanceof WorkbenchEditorActivity) {
            return (WorkbenchEditorActivity) a;
        }
        return null;
    }
}
