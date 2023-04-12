package com.auki.internal;

import lombok.extern.java.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

@Log
public class RenameFiles {
    public static void renameFiles(String currentDirectory) {
            String oldName;
            String newName;
            String fileNameWithOutExt;
            String extension = "";
            String newNameWithSpace;

            File folder = new File(currentDirectory);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                File old = listOfFiles[i];
                if (old.isFile()) {
                    oldName = old.getName();
                    int extIndex = oldName.lastIndexOf('.');
                    if (extIndex > 0) {
                        extension = oldName.substring(extIndex + 1).toString();
                        if (extension.equals("docx")) {
                            fileNameWithOutExt = oldName.replaceFirst("[.][^.]+$", "");
                            newNameWithSpace = fileNameWithOutExt.replaceAll("[^A-Za-z0-9-_ ]", "").toLowerCase();
                            newName = newNameWithSpace.replaceAll(" ", "_");
                            old.renameTo(new File(currentDirectory + "/" + newName + "." + extension));
                            log.info("file rename from " + fileNameWithOutExt + "to " + newName);
                            ContentImporter.fileNameList.add( newName + "." + extension);
                        } else if (extension.equals("pdf")) {
                            fileNameWithOutExt = oldName.replaceFirst("[.][^.]+$", "");
                            newName = fileNameWithOutExt.replaceAll("[^A-Za-z0-9-]", "_");
                            old.renameTo(new File(currentDirectory + "/" + newName + "." + extension));
                            log.info("file rename from " + fileNameWithOutExt + "to " + newName);
                        }
                    }
                }
            }

            buildFileName(ContentImporter.fileNameList);
    }

    public static void buildFileName(ArrayList<String> filelist){
        Collections.sort(filelist, (name1, name2) -> {

            int firstdashIndex = name1.indexOf("_");
            int seconddashIndex = name2.indexOf("_");

            String firstHalf1 = name1.substring(0,firstdashIndex);
            String firstHalf2 = name2.substring(0,seconddashIndex);
            return firstHalf1.compareTo(firstHalf2);
        });

        ContentImporter.fileNameList = filelist;
        System.out.println(ContentImporter.fileNameList);

    }
}
