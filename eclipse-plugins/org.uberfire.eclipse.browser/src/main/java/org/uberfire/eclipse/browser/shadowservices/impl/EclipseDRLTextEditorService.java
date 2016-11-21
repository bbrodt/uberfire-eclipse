package org.uberfire.eclipse.browser.shadowservices.impl;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.compiler.lang.descr.ImportDescr;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.eclipse.DRLInfo;
import org.drools.eclipse.DroolsEclipsePlugin;
import org.drools.workbench.models.datamodel.rule.DSLSentence;
import org.drools.workbench.screens.drltext.model.DrlModelContent;
import org.drools.workbench.screens.drltext.service.DRLTextEditorService;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.swt.browser.Browser;
import org.guvnor.common.services.backend.metadata.MetadataServiceImpl;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.security.shared.api.Group;
import org.jboss.errai.security.shared.api.Role;
import org.jboss.errai.security.shared.api.identity.User;
import org.jboss.errai.security.shared.api.identity.UserImpl;
import org.uberfire.backend.server.io.ConfigIOServiceProducer;
import org.uberfire.backend.vfs.Path;
import org.uberfire.commons.lifecycle.PriorityDisposableRegistry;
import org.uberfire.eclipse.browser.FileUtils;
import org.uberfire.eclipse.browser.JavaProjectClassLoader;
import org.uberfire.eclipse.browser.shadowservices.EclipseShadowService;
import org.uberfire.io.IOService;
import org.uberfire.io.impl.IOServiceDotFileImpl;
import org.uberfire.java.nio.file.FileSystem;
import org.uberfire.java.nio.file.api.FileSystemProviders;
import org.uberfire.java.nio.file.spi.FileSystemProvider;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.rpc.impl.SessionInfoImpl;

/**
 * Service-side Shadow Service implementation of the DRLTextEditorService.
 * 
 * @author bbrodt
 *
 */
public class EclipseDRLTextEditorService extends EclipseShadowService implements DRLTextEditorService {

	public static final String NAME = "EclipseDRLTextEditorService";
	FileSystemProvider fsProvider = null;
    FileSystem fs = null;
	IOService ioService;
	IOService configIOService;
	SessionInfo sessionInfo;
	MetadataServiceImpl metadataService;
//    private DataModelService dataModelService;
    
	public EclipseDRLTextEditorService(Browser browser) {
		super(browser, NAME);
	}

	@Override
	public List<ValidationMessage> validate(Path path, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path create(Path context, String fileName, String content, String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String load(Path path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path save(Path path, String content, Metadata metadata, String comment) {
        IFile file = FileUtils.getFile(path.toURI());
        if (FileUtils.write(file, content) < 0 )
            return null;
		return path;
	}

	@Override
	public void delete(Path path, String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Path copy(Path path, String newName, String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path copy(Path path, String newName, Path targetDirectory, String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path rename(Path path, String newName, String comment) {
		// TODO Auto-generated method stub
		return null;
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

	private Metadata getMetadata(Path path) {
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
	
	@Override
	public DrlModelContent loadContent(Path path) {
		IFile file = FileUtils.getFile(path.toURI());

		Overview overview = new Overview();
		Metadata metadata = getMetadata(path);
		overview.setMetadata(metadata);
		overview.setProjectName(file.getProject().getName());
		
        String fileContent = "";
		try {
			fileContent = FileUtils.read(file);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		List<String> fullyQualifiedClassNames = new ArrayList<String>();
		List<DSLSentence> dslConditions = new ArrayList<DSLSentence>();
        List<DSLSentence> dslActions = new ArrayList<DSLSentence>();
		try {
			DRLInfo info = DroolsEclipsePlugin.getDefault().parseResource(file, true);
			PackageDescr pd = info.getPackageDescr();
			for (ImportDescr id : pd.getImports()) {
				fullyQualifiedClassNames.add(id.getTarget());
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DrlModelContent content = new DrlModelContent(
				fileContent,
				overview,
                fullyQualifiedClassNames,
                dslConditions,
                dslActions );
		
		return content;
	}

	@Override
	public List<String> loadClassFields(Path path, String fullyQualifiedClassName) {
		// TODO Auto-generated method stub
		List<String> fields = new ArrayList<String>();
		try {
			IFile file = FileUtils.getFile(path.toURI());
			JavaProjectClassLoader cl = new JavaProjectClassLoader(file.getProject());
			Class clazz = cl.loadClass(fullyQualifiedClassName);
			for (Field f : clazz.getDeclaredFields()) {
				fields.add(f.getName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fields;
	}

	@Override
	public String assertPackageName(String drl, Path resource) {
		// TODO Auto-generated method stub
		return null;
	}

}
