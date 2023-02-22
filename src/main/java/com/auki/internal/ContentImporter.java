package com.auki.internal;

import com.auki.internal.processingutils.CreateTemplate;
import lombok.extern.java.Log;

import java.io.File;


@Log
public class ContentImporter {

    public static void main(String[] args) {

        try{
            File jarPath=new File(ContentImporter.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String currentDirectory=jarPath.getParentFile().getAbsolutePath();
            RenameFiles.renameFiles(currentDirectory);
            CreateTemplate.createTemplate(currentDirectory);
        }
        catch (Exception exception){
            log.info(exception.getMessage());
        }
    }
}
