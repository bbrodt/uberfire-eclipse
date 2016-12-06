package org.uberfire.eclipse.browser.shadowservices.impl;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.swt.browser.Browser;
import org.guvnor.common.services.backend.metadata.MetadataServiceImpl;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.jboss.errai.security.shared.api.Group;
import org.jboss.errai.security.shared.api.Role;
import org.jboss.errai.security.shared.api.identity.User;
import org.jboss.errai.security.shared.api.identity.UserImpl;
import org.uberfire.backend.server.io.ConfigIOServiceProducer;
import org.uberfire.backend.vfs.Path;
import org.uberfire.commons.lifecycle.PriorityDisposableRegistry;
import org.uberfire.eclipse.browser.FileUtils;
import org.uberfire.eclipse.browser.shadowservices.EclipseShadowService;
import org.uberfire.io.IOService;
import org.uberfire.io.impl.IOServiceDotFileImpl;
import org.uberfire.java.nio.file.FileSystem;
import org.uberfire.java.nio.file.api.FileSystemProviders;
import org.uberfire.java.nio.file.spi.FileSystemProvider;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.rpc.impl.SessionInfoImpl;

public class BaseEditorProviderShadowService extends EclipseShadowService {

	protected FileSystemProvider fsProvider = null;
	protected FileSystem fs = null;
	protected IOService ioService;
	protected IOService configIOService;
	protected SessionInfo sessionInfo;
	protected MetadataServiceImpl metadataService;

	public static Repository getRepository(Path path) {
	    Repository repository = null;
	    try{
			File f = new java.io.File(new URI(path.toURI()));
	        FileRepositoryBuilder builder = new FileRepositoryBuilder();
	        repository = builder.findGitDir(f).build();
	    }
	    catch (Exception e){
			e.printStackTrace();
	    }
	    return repository;
	}

	protected void addMarker(IFile file, String message, int lineNumber, int severity) {
		try {
			IMarker marker = file.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		} catch (CoreException e) {
		}
	}

	public URI pathToURI(Path path) {
			URI uri = null;
			try {
				Repository repository = getRepository(path);
				if (repository==null) {
					uri = new URI(path.toURI());
				}
				else {
	//				String dir = repository.getDirectory().toURI().toString().replaceAll("file:", "");
	//				uri = new URI("git", "localhost", path.getFileName(), null);
					uri = new URI(path.toURI().replace("file:", "git:"));
				}
			} catch (Exception e) {
				try {
					uri = new URI(path.toURI());
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			return uri;
		}

	public BaseEditorProviderShadowService(Browser browser, String name) {
		super(browser, name);
	}

	protected Overview getOverview(Path path) {
		IFile file = FileUtils.getFile(path.toURI());

		Overview overview = new Overview();
		Metadata metadata = getMetadata(path);
		overview.setMetadata(metadata);
		overview.setProjectName(file.getProject().getName());
		
		return overview;
	}
	
	protected Metadata getMetadata(Path path) {
			URI uri = pathToURI(path);
			if (metadataService==null) {
				try {
	
					if (fsProvider == null) {
			        	// force loading of FS providers first time
			        	fsProvider = FileSystemProviders.resolveProvider(uri);
					}	        
		
					try {
				        fs = fsProvider.getFileSystem(uri);
					} catch (Exception e) {
						e.printStackTrace();
					}
			        if (fs==null) {
			        	try {
							fs = fsProvider.newFileSystem(uri, new java.util.HashMap<String,Object>());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
	//				KieServices kieServices = KieServices.Factory.get();
	//				KieFileSystem kfs = new KieFileSystemImpl();
	//				KieBuilderImpl kieBuilder = (KieBuilderImpl) kieServices.newKieBuilder( kfs );
	//				kieBuilder.createFileSet(path.toURI().replace("file:", ""));
	//				Results r = kieBuilder.buildAll().getResults();
					
					PriorityDisposableRegistry.register("systemFS", fs);
					ioService = new IOServiceDotFileImpl();
					ConfigIOServiceProducer cfiosProducer = new ConfigIOServiceProducer();
					cfiosProducer.setup();
					configIOService = cfiosProducer.configIOService();
		            Collection<Role> roles = new ArrayList<Role>();
		            Collection<Group> groups = new ArrayList<Group>();
					User user = new UserImpl("bbrodt", roles, groups);
					sessionInfo = new SessionInfoImpl("bbrodt", user);
					metadataService = new MetadataServiceImpl(ioService, configIOService, sessionInfo);
	
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			org.uberfire.java.nio.file.Path aPath = fsProvider.getPath(uri);
			return metadataService.getMetadata(aPath);
		}

}
