package com.auki.internal.processingutils;

import com.auki.internal.HttpUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PdfHandler {
    public static String handlePdf(String html, String currentDirectory, String description, HttpUtils httpUtils,String title) throws IOException {
        String fileName = "";

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyy");
        String date=currentDate.format(formatter);

        String name = title.replaceAll("[^A-Za-z0-9]","_").toLowerCase();
        String fullName = description + "-" + name + "-"+date;
        fullName = fullName.replaceAll("\\_{2,}", "_");

        int pdfIndex = 0;
        while ((pdfIndex = html.indexOf("<p>Click <u>HERE</u> to", pdfIndex)) != -1) {
            int pdfEndIndex =  html.indexOf("</p>", pdfIndex);
            File folder = new File(currentDirectory);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                File file = listOfFiles[i];
                fileName = file.getName();
                String extension;
                int extIndex = fileName.lastIndexOf('.');
                if(extIndex > 0) {
                    extension =  fileName.substring(extIndex + 1).toString();
                    if(extension.equals("pdf")){
                        if(fileName.contains(description)){
                            httpUtils.uploadPdf(fileName,currentDirectory,title,description);
                            String subString = html.substring(pdfIndex,pdfEndIndex);
                            String out1 = subString.replace("<u>","");
                            String out2 = out1.replace("</u>","");
                            StringBuffer stringBuffer = new StringBuffer(out2);
                            stringBuffer.replace(9,13,"<a href=\"/content/dam/cir2/document/internal/" + fullName + ".pdf" + "\">" + "<u>HERE</u></a>");
                            StringBuffer stringBuffer1 = new StringBuffer(html);
                            stringBuffer1.replace(pdfIndex,pdfEndIndex, String.valueOf(stringBuffer));
                            html = String.valueOf(stringBuffer1);
                            pdfIndex = pdfEndIndex;
                        }
                    }
                }
            }
        }

        int pdfIndex1 = 0;
        while ((pdfIndex1 = html.indexOf("found <u>HERE</u>.</p>", pdfIndex1)) != -1) {
            int pdf1EndIndex =  html.indexOf("</p>", pdfIndex1);
            File folder = new File(currentDirectory);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                File file = listOfFiles[i];
                fileName = file.getName();
                String extension;
                int extIndex = fileName.lastIndexOf('.');
                if(extIndex > 0) {
                    extension =  fileName.substring(extIndex + 1).toString();
                    if(extension.equals("pdf")){
                        if(fileName.contains(description)){
                            httpUtils.uploadPdf(fileName,currentDirectory,title,description);
                            String subString = html.substring(pdfIndex1,pdf1EndIndex);
                            String out1 = subString.replace("<u>","");
                            String out2 = out1.replace("</u>","");
                            StringBuffer stringBuffer = new StringBuffer(out2);
                            stringBuffer.replace(6,10,"<a href=\"/content/dam/cir2/document/internal/" + fullName + ".pdf" + "\">" + "<u>HERE</u></a>");
                            StringBuffer stringBuffer1 = new StringBuffer(html);
                            stringBuffer1.replace(pdfIndex1,pdf1EndIndex, String.valueOf(stringBuffer));
                            html = String.valueOf(stringBuffer1);
                            pdfIndex1 = pdf1EndIndex;
                        }
                    }
                }
            }
        }
        return html;
    }
}
