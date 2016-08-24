package org.uberfire.shared;

import javax.enterprise.context.ApplicationScoped;

import org.uberfire.backend.vfs.Path;
import org.uberfire.client.workbench.type.ClientResourceType;

import com.google.gwt.user.client.ui.IsWidget;

@ApplicationScoped
public class UfResourceType implements ClientResourceType {

    @Override
    public String getShortName() {
        return "uf";
    }

    @Override
    public String getDescription() {
        return "Eclipse UF file";
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public String getSuffix() {
        return "uf";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String getSimpleWildcardPattern() {
        return "*.uf";
    }

    @Override
    public boolean accept(Path path) {
        return path.getFileName().endsWith("." + getSuffix());
    }

    @Override
    public IsWidget getIcon() {
        return null;
    }

}
