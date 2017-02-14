package org.uberfire.eclipse.browser.shadowservices;

import org.guvnor.common.services.shared.metadata.MetadataService;
import org.kie.workbench.common.services.backend.kmodule.KModuleContentHandler;
import org.kie.workbench.common.services.backend.kmodule.KModuleServiceImpl;
import org.kie.workbench.common.services.backend.project.KieProjectServiceImpl;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.uberfire.io.IOService;

class HackedKModuleServiceImpl extends KModuleServiceImpl {
    public HackedKModuleServiceImpl(
            IOService ioService, KieProjectServiceImpl projectService, MetadataService metadataService,
            KModuleContentHandler moduleContentHandler
    ) {
        super( ioService, projectService, metadataService, moduleContentHandler );
    }

    @Override
    public void setProjectService( KieProjectService projectService ) {
        super.setProjectService( projectService );
    }
}