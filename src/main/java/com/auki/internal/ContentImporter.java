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
            HttpUtils httpUtils = new HttpUtils();
            RenameFiles.renameFiles(currentDirectory);
            CreateTemplate.createTemplate(currentDirectory,httpUtils);
            httpUtils.createPackage();
            httpUtils.createFilters();
            httpUtils.buildPackage();
        }
        catch (Exception exception){
            log.severe(exception.getMessage());
        }
    }
}
