package org.uberfire.eclipse.browser.shadowservices;

import javax.enterprise.event.Event;

import org.guvnor.common.services.backend.util.CommentedOptionFactory;
import org.guvnor.common.services.project.builder.events.InvalidateDMOProjectCacheEvent;
import org.guvnor.common.services.project.events.NewPackageEvent;
import org.guvnor.common.services.project.events.NewProjectEvent;
import org.guvnor.common.services.project.events.RenameProjectEvent;
import org.guvnor.common.services.project.model.Package;
import org.guvnor.common.services.project.service.POMService;
import org.guvnor.common.services.project.service.ProjectRepositoryResolver;
import org.guvnor.structure.backend.backcompat.BackwardCompatibleUtil;
import org.guvnor.structure.server.config.ConfigurationFactory;
import org.guvnor.structure.server.config.ConfigurationService;
import org.kie.workbench.common.services.backend.project.KieProjectServiceImpl;
import org.kie.workbench.common.services.backend.project.KieResourceResolver;
import org.kie.workbench.common.services.backend.project.ProjectSaver;
import org.kie.workbench.common.services.shared.project.KieProject;
import org.uberfire.backend.vfs.Path;
import org.uberfire.io.IOService;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.security.authz.AuthorizationManager;

/**
 *
 */
class HackedKieProjectServiceImpl extends KieProjectServiceImpl {

    public HackedKieProjectServiceImpl(
            IOService ioService, ProjectSaver projectSaver, POMService pomService,
            ConfigurationService configurationService, ConfigurationFactory configurationFactory,
            Event<NewProjectEvent> newProjectEvent, Event<NewPackageEvent> newPackageEvent,
            Event<RenameProjectEvent> renameProjectEvent, Event<InvalidateDMOProjectCacheEvent> invalidateDMOCache,
            SessionInfo sessionInfo, AuthorizationManager authorizationManager, BackwardCompatibleUtil backward,
            CommentedOptionFactory commentedOptionFactory, KieResourceResolver resourceResolver,
            ProjectRepositoryResolver repositoryResolver
    ) {
        super(
                ioService, projectSaver, pomService, configurationService, configurationFactory, newProjectEvent,
                newPackageEvent, renameProjectEvent, invalidateDMOCache, sessionInfo, authorizationManager,
                backward, commentedOptionFactory, resourceResolver, repositoryResolver
        );
    }

    public KieProject resolveProject( final Path resourcePath ) {
        return super.resolveProject( resourcePath );
    }

    public Package resolvePackage( final Path resourcePath ) {
        return super.resolvePackage( resourcePath );
    }

    @Override
    public void setProjectSaver( ProjectSaver projectSaver ) {
        super.setProjectSaver( projectSaver );
    }
}