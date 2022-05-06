package cn.com.devopsplus.dop.server.defect.util;

import org.springframework.stereotype.Component;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;

import java.io.File;

@Component
public class CloneUrl {
    public static boolean deleteFile(File dirFile) {
        if (!dirFile.exists()) {
            return false;
        }
        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {
            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }
        return dirFile.delete();
    }

    public boolean cloneRepo(String directory, String uri ) {
        CloneCommand cmd = Git.cloneRepository();
        File file=new File( directory );
        deleteFile(file);
        System.out.println("start clone");
        cmd.setDirectory( file );
        cmd.setURI( uri );
        //cmd.setCredentialsProvider( credentialsProvider );
        try {
            Git git = cmd.call();
            git.close();
            System.out.println("clone success");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
