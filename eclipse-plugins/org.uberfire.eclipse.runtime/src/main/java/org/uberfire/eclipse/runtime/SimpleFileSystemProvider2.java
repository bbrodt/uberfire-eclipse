package org.uberfire.eclipse.runtime;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.uberfire.java.nio.IOException;
import org.uberfire.java.nio.base.AbstractPath;
import org.uberfire.java.nio.base.FileTimeImpl;
import org.uberfire.java.nio.base.GeneralPathImpl;
import org.uberfire.java.nio.base.version.VersionAttributeView;
import org.uberfire.java.nio.base.version.VersionAttributes;
import org.uberfire.java.nio.base.version.VersionHistory;
import org.uberfire.java.nio.base.version.VersionRecord;
import org.uberfire.java.nio.file.FileSystemNotFoundException;
import org.uberfire.java.nio.file.Path;
import org.uberfire.java.nio.file.attribute.FileTime;
import org.uberfire.java.nio.fs.file.SimpleFileSystemProvider;

/**
 * A File System provider that attaches a dummy VersionAttributeView to an
 * AbstractPath so that a file's Metadata can be loaded without throwing an
 * exception.
 * 
 * @author bbrodt
 *
 */
public class SimpleFileSystemProvider2 extends SimpleFileSystemProvider {

	/**
	 * A dummy VersionAttributeView that provides empty values for a file's version
	 * attributes.
	 */
	public class VersionAttributeView2 extends VersionAttributeView<Path> {

		VersionAttributes versionAttributes;
		File file;

		public VersionAttributeView2(Path path) {
			super(path);
			file = path.toFile();
			versionAttributes = new VersionAttributes() {

				@Override
				public FileTime lastModifiedTime() {
					// TODO Auto-generated method stub
					return new FileTimeImpl(file.lastModified());
				}

				@Override
				public FileTime lastAccessTime() {
					return new FileTimeImpl(file.lastModified());
				}

				@Override
				public FileTime creationTime() {
					return new FileTimeImpl(file.lastModified());
				}

				@Override
				public boolean isRegularFile() {
					return file.isFile();
				}

				@Override
				public boolean isDirectory() {
					return file.isDirectory();
				}

				@Override
				public boolean isSymbolicLink() {
					return false;
				}

				@Override
				public boolean isOther() {
					return false;
				}

				@Override
				public long size() {
					return file.length();
				}

				@Override
				public Object fileKey() {
					try {
						return file.getCanonicalPath();
					} catch (java.io.IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return new Object();
				}

				@Override
				public VersionHistory history() {
					List<VersionRecord> records = new ArrayList<VersionRecord>();
					return new VersionHistory() {

						@Override
						public List<VersionRecord> records() {
							if (records.isEmpty()) {
								records.add(new VersionRecord() {
									@Override
									public String id() {
										return "";
									}

									@Override
									public String author() {
										return "unknown author";
									}

									@Override
									public String email() {
										return "";
									}

									@Override
									public String comment() {
										return "";
									}

									@Override
									public Date date() {
										return new Date(file.lastModified());
									}

									@Override
									public String uri() {
										return file.toURI().toString();
									}
								});
							}
							return records;
						}

					};
				}
			};
		}

		@Override
		public Class[] viewTypes() {
			return new Class[] { VersionAttributeView.class };
		}

		@Override
		public VersionAttributes readAttributes() throws IOException {
			return versionAttributes;
		}

	}
	
	@Override
	public Path getPath(URI uri) throws IllegalArgumentException, FileSystemNotFoundException, SecurityException {
		AbstractPath<?> aPath = GeneralPathImpl.create(this.getFileSystem(uri), uri.getPath(), false);
		aPath.addAttrView(new VersionAttributeView2(aPath));
		return aPath;
	}

}
