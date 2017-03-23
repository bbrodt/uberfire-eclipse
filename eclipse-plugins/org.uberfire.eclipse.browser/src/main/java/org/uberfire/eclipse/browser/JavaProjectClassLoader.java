package org.uberfire.eclipse.browser;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * Java class loader.
 * This class is obsolete - replaced by the DMO Provider.
 */
public class JavaProjectClassLoader {
	private IJavaProject javaProject;
	private URLClassLoader classLoader = null;

	public JavaProjectClassLoader(IJavaProject javaProject) {
		if (javaProject == null || !javaProject.exists())
			throw new IllegalArgumentException("Invalid javaProject"); //$NON-NLS-1$
		this.javaProject = javaProject;
	}
	
	public JavaProjectClassLoader(IProject project) {
		if (project==null || !project.exists())
			throw new IllegalArgumentException("Invalid javaProject"); //$NON-NLS-1$
		javaProject = JavaCore.create(project);
	}
		
	public Class<?> loadClass(String name) {
		getClassLoader();
		if (classLoader != null) {
			String[] a = name.split("\\.");
			int i = a.length;
			while (true) {
				try {
					return classLoader.loadClass(name);
				} catch (ClassNotFoundException e) {
				}
				// class not found - try inner classes by replacing last "." with a "$"
				int j=0;
				if (--i<=0)
					break;
				while (j<i) {
					if (j==0)
						name = a[j++];
					else
						name = name + "." + a[j++];
				}
				while (j<a.length)
					name = name + "$" + a[j++];
			}
		}
		return null;
	}

	private URLClassLoader getClassLoader() {
		if (classLoader==null) {
			try {
				String[] classPathEntries = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);
				List<URL> urlList = new ArrayList<URL>();
				for (int i = 0; i < classPathEntries.length; i++) {
					String entry = classPathEntries[i];
					IPath path = new Path(entry);
					URL url = path.toFile().toURI().toURL();
					urlList.add(url);
				}
				ClassLoader parentClassLoader = javaProject.getClass().getClassLoader();
				URL[] urls = (URL[]) urlList.toArray(new URL[urlList.size()]);
				classLoader = new URLClassLoader(urls, parentClassLoader);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return classLoader;
	}
}