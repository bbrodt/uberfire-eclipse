/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package org.uberfire.eclipse.browser.shadowservices;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.function.Predicate;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;

import org.drools.workbench.models.datamodel.oracle.PackageDataModelOracle;
import org.drools.workbench.models.datamodel.oracle.ProjectDataModelOracle;
import org.guvnor.common.services.backend.file.FileDiscoveryService;
import org.guvnor.common.services.backend.file.FileDiscoveryServiceImpl;
import org.guvnor.common.services.backend.metadata.MetadataServerSideService;
import org.guvnor.common.services.backend.metadata.MetadataServiceImpl;
import org.guvnor.common.services.backend.util.CommentedOptionFactory;
import org.guvnor.common.services.backend.util.CommentedOptionFactoryImpl;
import org.guvnor.common.services.project.backend.server.POMServiceImpl;
import org.guvnor.common.services.project.backend.server.ProjectConfigurationContentHandler;
import org.guvnor.common.services.project.backend.server.ProjectRepositoriesContentHandler;
import org.guvnor.common.services.project.backend.server.ProjectRepositoryResolverImpl;
import org.guvnor.common.services.project.backend.server.utils.POMContentHandler;
import org.guvnor.common.services.project.builder.events.InvalidateDMOProjectCacheEvent;
import org.guvnor.common.services.project.builder.service.BuildValidationHelper;
import org.guvnor.common.services.project.events.NewPackageEvent;
import org.guvnor.common.services.project.events.NewProjectEvent;
import org.guvnor.common.services.project.events.RenameProjectEvent;
import org.guvnor.common.services.project.model.Project;
import org.guvnor.common.services.project.security.ProjectAction;
import org.guvnor.common.services.project.service.POMService;
import org.guvnor.common.services.project.service.ProjectRepositoriesService;
import org.guvnor.common.services.project.service.ProjectRepositoryResolver;
import org.guvnor.common.services.shared.metadata.MetadataService;
import org.guvnor.m2repo.backend.server.M2RepoServiceImpl;
import org.guvnor.m2repo.service.M2RepoService;
import org.guvnor.structure.backend.backcompat.BackwardCompatibleUtil;
import org.guvnor.structure.backend.config.ConfigGroupMarshaller;
import org.guvnor.structure.backend.config.ConfigurationFactoryImpl;
import org.guvnor.structure.backend.config.ConfigurationServiceImpl;
import org.guvnor.structure.backend.config.DefaultPasswordServiceImpl;
import org.guvnor.structure.config.SystemRepositoryChangedEvent;
import org.guvnor.structure.organizationalunit.OrganizationalUnit;
import org.guvnor.structure.repositories.Repository;
import org.guvnor.structure.repositories.impl.git.GitRepository;
import org.guvnor.structure.security.OrganizationalUnitAction;
import org.guvnor.structure.security.RepositoryAction;
import org.guvnor.structure.server.config.ConfigurationFactory;
import org.guvnor.structure.server.config.ConfigurationService;
import org.guvnor.structure.server.config.PasswordService;
import org.jboss.errai.security.shared.api.Group;
import org.jboss.errai.security.shared.api.Role;
import org.jboss.errai.security.shared.api.identity.User;
import org.jboss.errai.security.shared.api.identity.UserImpl;
import org.kie.workbench.common.services.backend.builder.LRUBuilderCache;
import org.kie.workbench.common.services.backend.builder.LRUPomModelCache;
import org.kie.workbench.common.services.backend.dependencies.DependencyServiceImpl;
import org.kie.workbench.common.services.backend.kmodule.KModuleContentHandler;
import org.kie.workbench.common.services.backend.project.KieProjectRepositoriesServiceImpl;
import org.kie.workbench.common.services.backend.project.KieResourceResolver;
import org.kie.workbench.common.services.backend.project.ProjectImportsServiceImpl;
import org.kie.workbench.common.services.backend.project.ProjectSaver;
import org.kie.workbench.common.services.backend.whitelist.PackageNameSearchProvider;
import org.kie.workbench.common.services.backend.whitelist.PackageNameWhiteListLoader;
import org.kie.workbench.common.services.backend.whitelist.PackageNameWhiteListSaver;
import org.kie.workbench.common.services.backend.whitelist.PackageNameWhiteListServiceImpl;
import org.kie.workbench.common.services.datamodel.backend.server.DataModelServiceImpl;
import org.kie.workbench.common.services.datamodel.backend.server.builder.packages.PackageDataModelOracleBuilder;
import org.kie.workbench.common.services.datamodel.backend.server.cache.LRUDataModelOracleCache;
import org.kie.workbench.common.services.datamodel.backend.server.cache.LRUProjectDataModelOracleCache;
import org.kie.workbench.common.services.datamodel.backend.server.cache.ProjectDataModelOracleBuilderProvider;
import org.kie.workbench.common.services.datamodel.backend.server.service.DataModelService;
import org.kie.workbench.common.services.shared.dependencies.DependencyService;
import org.kie.workbench.common.services.shared.project.ProjectImportsService;
import org.kie.workbench.common.services.shared.whitelist.PackageNameWhiteListService;
import org.uberfire.backend.server.io.ConfigIOServiceProducer;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.commons.lifecycle.PriorityDisposableRegistry;
import org.uberfire.io.IOService;
import org.uberfire.io.impl.IOServiceDotFileImpl;
import org.uberfire.java.nio.file.FileSystem;
import org.uberfire.java.nio.file.FileSystemNotFoundException;
import org.uberfire.java.nio.file.api.FileSystemProviders;
import org.uberfire.java.nio.file.spi.FileSystemProvider;
import org.uberfire.java.nio.fs.jgit.JGitFileSystem;
import org.uberfire.java.nio.fs.jgit.JGitFileSystemProvider;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.rpc.impl.SessionInfoImpl;
import org.uberfire.security.ResourceType;
import org.uberfire.security.authz.AuthorizationManager;
import org.uberfire.security.authz.PermissionManager;
import org.uberfire.security.authz.PermissionTypeRegistry;
import org.uberfire.security.impl.authz.DefaultAuthorizationManager;
import org.uberfire.security.impl.authz.DefaultPermissionManager;
import org.uberfire.security.impl.authz.DefaultPermissionTypeRegistry;
import org.uberfire.security.impl.authz.DotNamedPermissionType;

/**
 * Provider class for DataModelOracle. DMOs are cached by project since
 * construction/initialization is an expensive operation.
 */
public class DataModelOracleProvider {

    private static Hashtable<URI, DataModelOracleProvider> cache = new Hashtable<URI, DataModelOracleProvider>();

    protected ProjectDataModelOracle projectOracle;
    Hashtable<String, PackageDataModelOracle> packageOracles = null;
    protected FileSystemProvider fsProvider;
    protected FileSystem fs;
    protected IOService ioService;
    protected SessionInfo sessionInfo;
    protected IOService configIOService;
    protected MetadataService metadataService;
    
    private static ResourceType REPOSITORY_TYPE = Repository.RESOURCE_TYPE;
    private static ResourceType ORGUNIT_TYPE = OrganizationalUnit.RESOURCE_TYPE;
    private static ResourceType PROJECT_TYPE = Project.RESOURCE_TYPE;

    private DataModelOracleProvider( URI uri ) throws IllegalArgumentException, FileSystemNotFoundException,
            SecurityException, MalformedURLException, URISyntaxException {
        initialize( uri );
    }

    public static DataModelOracleProvider getProvider( URI uri ) {
        URI projectUri = null;
        String path = uri.getPath();
        while (path.contains( "/" )) {
            File f = new File(path+"/src");
            if (f.exists() && f.isDirectory()) {
                try {
                    projectUri = new URI(uri.getScheme(), uri.getHost(), path, uri.getFragment());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            }
            path = path.substring( 0, path.lastIndexOf( "/" ) );
        }
        
        DataModelOracleProvider provider = cache.get( projectUri );
        if (provider == null) {
            try {
                provider = new DataModelOracleProvider( uri );
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (FileSystemNotFoundException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            cache.put( projectUri, provider );
        }
        return provider;
    }

    public static void clear() {
        cache.clear();
    }
    
    public ProjectDataModelOracle getProjectDataModelOracle() {
        return projectOracle;
    }

    public FileSystemProvider getFileSystemProvider() {
        return fsProvider;
    }

    public IOService getIOService() {
        return ioService;
    }

    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }

    public IOService getConfigIOService() {
        return configIOService;
    }

    public MetadataService getMetadataService() {
        return metadataService;
    }

    
    public Collection<PackageDataModelOracle> getPackageDataModelOracles() {
        return getPackageOracles().values();
    }
    
    public PackageDataModelOracle getPackageDataModelOracle( String packageName ) {
        return getPackageOracles().get(packageName);
    }

    private Hashtable<String, PackageDataModelOracle> getPackageOracles() {
        if (packageOracles==null) {
            packageOracles = new Hashtable<String, PackageDataModelOracle>();
            for (String pkgName : projectOracle.getProjectPackageNames() ) {
                PackageDataModelOracleBuilder builder = PackageDataModelOracleBuilder.newPackageOracleBuilder(pkgName);
                PackageDataModelOracle packageOracle = builder.build();
                packageOracles.put( pkgName, packageOracle );
            }
        }
        return packageOracles;
    }
    
    private void initialize( URI uri ) throws IllegalArgumentException, FileSystemNotFoundException, SecurityException,
            URISyntaxException, MalformedURLException {

        // disable git and ssh daemons as they are not needed here
        System.setProperty( "org.uberfire.nio.git.daemon.enabled", "false" );
        System.setProperty( "org.uberfire.nio.git.ssh.enabled", "false" );
        System.setProperty( "org.uberfire.sys.repo.monitor.disabled", "true" );
        fsProvider = FileSystemProviders.resolveProvider( uri );
        fsProvider.forceAsDefault();
        try {
            fs = fsProvider.getFileSystem( uri );
        } catch (Exception e) {
        }
        if (fs == null) {
            fs = fsProvider.newFileSystem( uri, new java.util.HashMap<String, Object>() );
        }

        PriorityDisposableRegistry.register("systemFS", fs);
        ioService = new IOServiceDotFileImpl();
        // TODO: use actual user credentials here, either from user login or System.getProperty()
        Collection<Role> roles = new ArrayList<Role>();
        Collection<Group> groups = new ArrayList<Group>();
        User user = new UserImpl( "admin", roles, groups );
        sessionInfo = new SessionInfoImpl( "admin", user );
        ConfigIOServiceProducer cfiosProducer = new ConfigIOServiceProducer();
        cfiosProducer.setup();
        configIOService = cfiosProducer.configIOService();
        metadataService = new MetadataServiceImpl( ioService, configIOService, sessionInfo );

        POMContentHandler pomContentHandler = new POMContentHandler();
        M2RepoService m2RepoService = new M2RepoServiceImpl();
        POMService pomService = new POMServiceImpl( ioService, pomContentHandler, m2RepoService, metadataService );
        KModuleContentHandler moduleContentHandler = new KModuleContentHandler();
        PasswordService secureService = new DefaultPasswordServiceImpl();
        ConfigurationFactory configurationFactory = new ConfigurationFactoryImpl( secureService );

        org.guvnor.structure.repositories.Repository systemRepository = new GitRepository( "system" );
        try {
            JGitFileSystemProvider gfsp = (JGitFileSystemProvider) FileSystemProviders.resolveProvider( new URI("git://system") );
            JGitFileSystem gfs = (JGitFileSystem) gfsp.newFileSystem( new URI("git://system"), new java.util.HashMap<String, Object>() );
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ConfigGroupMarshaller marshaller = new ConfigGroupMarshaller();
        ConfigurationService configurationService = new ConfigurationServiceImpl(
                systemRepository, marshaller, user, ioService, new EventSourceMock<SystemRepositoryChangedEvent>(),
                new EventSourceMock<SystemRepositoryChangedEvent>(),
                new EventSourceMock<SystemRepositoryChangedEvent>(), fs
        );

        CommentedOptionFactory commentedOptionFactory = new CommentedOptionFactoryImpl( sessionInfo );
        BackwardCompatibleUtil backward = new BackwardCompatibleUtil( configurationFactory );
        ProjectConfigurationContentHandler projectConfigurationContentHandler = new ProjectConfigurationContentHandler();
        ProjectImportsService projectImportsService = new ProjectImportsServiceImpl(
                ioService, projectConfigurationContentHandler
        );
        Event<NewProjectEvent> newProjectEvent = new EventSourceMock<NewProjectEvent>();
        Event<NewPackageEvent> newPackageEvent = new EventSourceMock<NewPackageEvent>();
        Event<RenameProjectEvent> renameProjectEvent = new EventSourceMock<RenameProjectEvent>();
        Event<InvalidateDMOProjectCacheEvent> invalidateDMOCache = new EventSourceMock<InvalidateDMOProjectCacheEvent>();

        PermissionTypeRegistry permissionTypeRegistry = new DefaultPermissionTypeRegistry();
        DotNamedPermissionType permissionType = new DotNamedPermissionType( REPOSITORY_TYPE.getName() );
        permissionType.createPermission( REPOSITORY_TYPE, RepositoryAction.READ, true );
        permissionType.createPermission( REPOSITORY_TYPE, RepositoryAction.CREATE, true );
        permissionType.createPermission( REPOSITORY_TYPE, RepositoryAction.UPDATE, true );
        permissionType.createPermission( REPOSITORY_TYPE, RepositoryAction.DELETE, true );
        permissionTypeRegistry.register( permissionType );

        permissionType = new DotNamedPermissionType( ORGUNIT_TYPE.getName() );
        permissionType.createPermission( ORGUNIT_TYPE, OrganizationalUnitAction.CREATE, true );
        permissionType.createPermission( ORGUNIT_TYPE, OrganizationalUnitAction.UPDATE, true );
        permissionType.createPermission( ORGUNIT_TYPE, OrganizationalUnitAction.DELETE, true );
        permissionTypeRegistry.register( permissionType );

        permissionType = new DotNamedPermissionType( PROJECT_TYPE.getName() );
        permissionType.createPermission( PROJECT_TYPE, ProjectAction.CREATE, true );
        permissionType.createPermission( PROJECT_TYPE, ProjectAction.BUILD, true );
        permissionType.createPermission( PROJECT_TYPE, ProjectAction.UPDATE, true );
        permissionType.createPermission( PROJECT_TYPE, ProjectAction.DELETE, true );
        permissionTypeRegistry.register( permissionType );

        PermissionManager permissionManager = new DefaultPermissionManager();

        AuthorizationManager authorizationManager = new DefaultAuthorizationManager( permissionManager );

        ProjectRepositoryResolver repositoryResolver = new ProjectRepositoryResolverImpl( ioService, null, null );

        FileDiscoveryService fileDiscoveryService = new FileDiscoveryServiceImpl();

        HackedKieProjectServiceImpl projectService = null;
        HackedKModuleServiceImpl kModuleService = new HackedKModuleServiceImpl(
                ioService, projectService, metadataService, moduleContentHandler
        );
        KieResourceResolver resourceResolver = new KieResourceResolver(
                ioService, pomService, configurationService, commentedOptionFactory, backward, kModuleService
        );
        ProjectSaver projectSaver = null;
        projectService = new HackedKieProjectServiceImpl(
                ioService, projectSaver, pomService, configurationService, configurationFactory, newProjectEvent,
                newPackageEvent, renameProjectEvent, invalidateDMOCache, sessionInfo, authorizationManager, backward,
                commentedOptionFactory, resourceResolver, repositoryResolver
        );

        ProjectRepositoriesContentHandler contentHandler = new ProjectRepositoriesContentHandler();
        ProjectRepositoriesService projectRepositoriesService = new KieProjectRepositoriesServiceImpl(
                ioService, repositoryResolver, resourceResolver, contentHandler, commentedOptionFactory
        );

        DependencyService dependencyService = new DependencyServiceImpl();
        PackageNameSearchProvider packageNameSearchProvider = new PackageNameSearchProvider( dependencyService );
        PackageNameWhiteListLoader loader = new PackageNameWhiteListLoader( packageNameSearchProvider, ioService );
        MetadataServerSideService serverSideMetdataService = new MetadataServiceImpl(
                ioService, configIOService, sessionInfo
        );
        PackageNameWhiteListSaver saver = new PackageNameWhiteListSaver(
                ioService, serverSideMetdataService, commentedOptionFactory
        );
        PackageNameWhiteListService packageNameWhiteListService = new PackageNameWhiteListServiceImpl(
                ioService, projectService, loader, saver
        );

        projectSaver = new ProjectSaver(
                ioService, pomService, kModuleService, newProjectEvent, newPackageEvent, resourceResolver,
                projectImportsService, projectRepositoriesService, packageNameWhiteListService, commentedOptionFactory,
                sessionInfo
        );
        projectService.setProjectSaver( projectSaver );
        kModuleService.setProjectService( projectService );

        ProjectImportsService importsService = new ProjectImportsServiceImpl(
                ioService, projectConfigurationContentHandler
        );
        Instance<BuildValidationHelper> buildValidationHelperBeans = null;
        Instance<Predicate<String>> classFilterBeans = null;
        HackedLRUProjectDependenciesClassLoaderCache dependenciesClassLoaderCache = new HackedLRUProjectDependenciesClassLoaderCache();
        LRUPomModelCache pomModelCache = new LRUPomModelCache();
        LRUBuilderCache builderCache = new LRUBuilderCache(
                ioService, projectService, importsService, buildValidationHelperBeans, dependenciesClassLoaderCache,
                pomModelCache, packageNameWhiteListService, classFilterBeans
        );

        ProjectDataModelOracleBuilderProvider builderProvider = new ProjectDataModelOracleBuilderProvider(
                packageNameWhiteListService, importsService
        );

        LRUProjectDataModelOracleCache cacheProjects = new LRUProjectDataModelOracleCache(
                builderProvider, projectService, builderCache
        );

        dependenciesClassLoaderCache.setBuilderCache( builderCache );
        LRUDataModelOracleCache cachePackages = new LRUDataModelOracleCache(
                ioService, fileDiscoveryService, cacheProjects, projectService, builderCache
        );
        DataModelService dataModelService = new DataModelServiceImpl( cachePackages, cacheProjects, projectService );

        final org.uberfire.java.nio.file.Path nioPackagePath = fsProvider.getPath( uri );
        final Path packagePath = Paths.convert( nioPackagePath );

        projectOracle = dataModelService.getProjectDataModel( packagePath );
    }
}
