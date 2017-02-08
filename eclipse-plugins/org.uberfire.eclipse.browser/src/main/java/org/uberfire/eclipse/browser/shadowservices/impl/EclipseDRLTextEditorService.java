package org.uberfire.eclipse.browser.shadowservices.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.drools.compiler.compiler.BaseKnowledgeBuilderResultImpl;
import org.drools.compiler.lang.descr.ImportDescr;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.eclipse.DRLInfo;
import org.drools.eclipse.DroolsEclipsePlugin;
import org.drools.workbench.models.datamodel.rule.DSLSentence;
import org.drools.workbench.screens.drltext.model.DrlModelContent;
import org.drools.workbench.screens.drltext.service.DRLTextEditorService;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.browser.Browser;
import org.guvnor.common.services.shared.message.Level;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.uberfire.backend.vfs.Path;
import org.uberfire.eclipse.browser.FileUtils;
import org.uberfire.eclipse.browser.JavaProjectClassLoader;
import org.uberfire.eclipse.browser.editors.BrowserProxy;

/**
 * Service-side Shadow Service implementation of the DRLTextEditorService.
 * 
 * @author bbrodt
 *
 */
public class EclipseDRLTextEditorService extends BaseEclipseEditorService<String>
		implements DRLTextEditorService {

	public static final String NAME = "EclipseDRLTextEditorService";
	public EclipseDRLTextEditorService(BrowserProxy browserProxy) {
		super(browserProxy, NAME);
	}
	
	@Override
	public List<ValidationMessage> validate(Path path, String content) {
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		try {
	        IFile file = FileUtils.getFile(path.toURI());
			try {
			   file.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
			} catch (Exception e) {
			}
			
			DroolsEclipsePlugin.getDefault().invalidateResource(file);
			DRLInfo info = DroolsEclipsePlugin.getDefault().generateParsedResource(content, file, false, true);
			for (BaseKnowledgeBuilderResultImpl parseError : info.getParserErrors()) {
				ValidationMessage validationMessage = new ValidationMessage();
				int line = parseError.getLines().length>0 ? parseError.getLines()[0] : 1;
				int severity = IMarker.SEVERITY_INFO;
				String message = parseError.getMessage().replaceFirst(".*[lL]ine [0-9]+:[0-9]+ ", "");
				switch (parseError.getSeverity()) {
				case ERROR:
					validationMessage.setLevel(Level.ERROR);
					severity = IMarker.SEVERITY_ERROR;
					break;
				case INFO:
					validationMessage.setLevel(Level.INFO);
					severity = IMarker.SEVERITY_INFO;
					break;
				case WARNING:
					validationMessage.setLevel(Level.WARNING);
					severity = IMarker.SEVERITY_WARNING;
					break;
				default:
					break;
				
				}
				validationMessage.setLine(line);
				validationMessage.setText(message);
				validationMessage.setPath(path);
				errors.add(validationMessage);
				addMarker(file, message, line, severity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errors;
	}

	@Override
	public DrlModelContent loadContent(Path path) {
		IFile file = FileUtils.getFile(path.toURI());

		Overview overview = getOverview(path);
		
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
