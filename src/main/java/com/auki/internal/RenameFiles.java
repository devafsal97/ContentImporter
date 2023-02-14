package com.auki.internal;

import lombok.extern.java.Log;

import java.io.File;

@Log
public class RenameFiles {
    public static void RenameFiles(String currentDirectory) {
            String oldName;
            String newName;
            String fileNameWithOutExt;
            String extension = "";
            String newNameWithSpace;

            File folder = new File(currentDirectory);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                File old = listOfFiles[i];
                oldName = old.getName();
                int extIndex = oldName.lastIndexOf('.');
                if(extIndex > 0) {
                    extension = oldName.substring(extIndex + 1).toString();
                }
                fileNameWithOutExt = oldName.replaceFirst("[.][^.]+$", "");
                newNameWithSpace = fileNameWithOutExt.replaceAll("[^A-Za-z- ]","").toLowerCase();
                newName = newNameWithSpace.replaceAll(" ","_");
                old.renameTo(new File(currentDirectory + "/" + newName + "." + extension));
                log.info("file rename from " + fileNameWithOutExt + "to " + newName );
            }
    }
}
