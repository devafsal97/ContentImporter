package com.auki.internal;

import com.auki.internal.processingutils.CreateTemplate;
import lombok.extern.java.Log;

import java.io.File;
import java.util.ArrayList;


@Log
public class ContentImporter {

    public static ArrayList<String>  fileNameList = new ArrayList();
    public static ArrayList<String>  descriptionList = new ArrayList();

    public static void main(String[] args) {
        try{
            File jarPath=new File(ContentImporter.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String currentDirectory=jarPath.getParentFile().getAbsolutePath();
            File folder = new File("/Users/afsal/Workspace/untitledfolder");
            String cd = folder.getPath();
            RenameFiles.renameFiles(currentDirectory);
            CreateTemplate.createTemplate(currentDirectory);
        }
      catch (Exception exception){
            log.info(exception.getMessage());
        }
    }
}
