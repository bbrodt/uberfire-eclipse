package org.uberfire.eclipse.browser;
import java.io.File;
import java.net.URI;

import org.uberfire.java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.jboss.errai.common.client.protocols.SerializationParts;
import org.jboss.errai.marshalling.client.api.json.EJObject;
import org.uberfire.ext.editor.commons.version.impl.PortableVersionRecord;
import org.uberfire.java.nio.base.version.VersionRecord;

/**
 * This class is obsolete - to be replaced
 */
public class GitUtils {

	public static class FileHistory {
		public String comment;
		public String committer;
		public String creator;
		public Date commitDate;
		public Date createDate;

		public FileHistory() {
			comment = "";
			committer = "";
			creator = "";
			commitDate = new Date();
			createDate = new Date();
		}
	}
	
	public static Repository getRepository(Path path) {
		
		File f = path.toFile();
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = null;
        // get a manager for the repository
        try{
            repository = builder.findGitDir(f).build();
        }
        catch (Exception e){
        }
        return repository;
	}
	
    /**
     * @param f, the most up2date file which is located in a git-repository
     * @return the history of the given file
     */
    public static List<VersionRecord> getHistory(File f) throws Exception{
        if(!f.exists())
            throw new Exception("File "+f.getAbsolutePath()+" doesn't exist.");

        List<VersionRecord> history = new ArrayList<VersionRecord>();

        FileRepositoryBuilder builder = new FileRepositoryBuilder();

        Repository repository = null;

        // get a manager for the repository
        try{
            repository = builder.findGitDir(f).build();

            // windows-workaround for paths
            String sAbsolutePath2File = f.getAbsolutePath().replaceAll("\\\\", "/");
            String sPathGitDirectory = repository.getDirectory().getParentFile().getAbsolutePath().replaceAll("\\\\", "/");

            // Get relative file path to the file inside the Git-Repository
            String sRelativePath2File = sAbsolutePath2File.replaceFirst(sPathGitDirectory, "");
            // is there still a leading slash?
            if(sRelativePath2File.startsWith("/"))
                // remove it!
                sRelativePath2File = sRelativePath2File.substring(1);

            // Get the central git object from the repository
            Git git = new Git(repository);

            // get logcommand by relative file path
            LogCommand logCommand = git.log()
                    .add(git.getRepository().resolve(Constants.HEAD))
                    .addPath(sRelativePath2File);


            // get all commits
            for (RevCommit revCommit : logCommand.call()) {
        		Date commitDate  = new Calendar.Builder().setInstant((long)revCommit.getCommitTime()*1000).build().getTime();
            	PortableVersionRecord vr = new PortableVersionRecord(
            			revCommit.getId().getName(),
            			revCommit.getAuthorIdent().getName(),
            			revCommit.getAuthorIdent().getEmailAddress(),
            			revCommit.getFullMessage(),
            			commitDate,
            			f.toURI().toString()
            			
            	);
            	history.add(vr);
            }
            
        } finally{
        	if (repository!=null)
        		repository.close();
        }

        return history;
    }

    public static VersionRecord emptyVersionRecord() {
       	return new PortableVersionRecord(
    			"", // id
    			"", // author
    			"", // email
    			"", // comment
    			new Date(), // commit date
    			"" // uri
    			
    	);
    }
}
