package org.uberfire.eclipse.browser.shadowservices;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.browser.Browser;
import org.uberfire.eclipse.browser.FileUtils;

public class EclipseVFSService extends ShadowService {

	public static final String NAME = "EclipseVFSService";

	public EclipseVFSService(Browser browser) {
		super(browser, NAME);
	}

	public String readAllString(String uri) {
        IFile file = FileUtils.getFile(uri);
        String contents = null;
		try {
			contents = FileUtils.read(file);
		} catch (CoreException e) {
			e.printStackTrace();
		}
        return contents;
	}
	
	public String write(String uri, String contents) {
        String response = uri;
        try {
            IFile file = FileUtils.getFile(uri);
            if (FileUtils.write(file, contents) < 0 )
                response = "Write Error";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
		
	}
}
