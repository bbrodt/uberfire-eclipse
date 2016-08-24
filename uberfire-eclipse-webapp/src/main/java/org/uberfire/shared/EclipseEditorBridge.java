package org.uberfire.shared;

import java.util.ArrayList;
import java.util.List;

import org.uberfire.client.mvp.Activity;
import org.uberfire.client.mvp.WorkbenchEditorActivity;
import org.uberfire.workbench.model.menu.MenuCustom;
import org.uberfire.workbench.model.menu.MenuGroup;
import org.uberfire.workbench.model.menu.MenuItemCommand;
import org.uberfire.workbench.model.menu.MenuItemPerspective;
import org.uberfire.workbench.model.menu.MenuItemPlain;
import org.uberfire.workbench.model.menu.MenuVisitor;
import org.uberfire.workbench.model.menu.Menus;

import jsinterop.annotations.JsType;

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
        }

        @Override
        public void visit(MenuItemCommand menuItemCommand) {
            items.add(menuItemCommand.getCaption());
            items.add(menuItemCommand.getIdentifier());
        }

        @Override
        public void visit(MenuItemPerspective menuItemPerspective) {
        }

        @Override
        public void visit(MenuCustom<?> menuCustom) {
            items.add(menuCustom.getCaption());
            items.add(menuCustom.getIdentifier());
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
        return !getEditor(uri).onMayClose();
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
                .getActivity(EclipsePlaceManagerBridge.createPlace(uri));
        if (a instanceof WorkbenchEditorActivity) {
            return (WorkbenchEditorActivity) a;
        }
        return null;
    }
}
