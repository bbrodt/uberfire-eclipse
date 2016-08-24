package org.uberfire.client;

import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import org.uberfire.client.workbench.panels.impl.AbstractDockingWorkbenchPanelView;
import org.uberfire.client.workbench.part.WorkbenchPartPresenter.View;
import org.uberfire.workbench.model.PartDefinition;

import com.google.gwt.user.client.ui.IsWidget;

@Dependent
@Named("SingleWorkbenchPanelView")
public class SingleWorkbenchPanelView extends AbstractDockingWorkbenchPanelView<SingleWorkbenchPanelPresenter> {

    PartDefinition part;
    SingleWorkbenchPanelPresenter presenter;

    @Override
    public void addPart(View view) {
        part = view.getPresenter().getDefinition();
        getPartViewContainer().add(view);
    }

    @Override
    public void changeTitle(PartDefinition part, String title, IsWidget titleDecoration) {

    }

    @Override
    public boolean selectPart(PartDefinition part) {
        return true;
    }

    @Override
    public boolean removePart(PartDefinition part) {
        part = null;
        return true;
    }

    @Override
    public void setFocus(boolean hasFocus) {
    }

    @Override
    public Collection<PartDefinition> getParts() {
        ArrayList<PartDefinition> parts = new ArrayList<PartDefinition>();
        if (part != null)
            parts.add(part);
        return parts;
    }

    @Override
    public void init(SingleWorkbenchPanelPresenter presenter) {
        this.presenter = presenter;
    }
}
