package com.auki.internal.processingutils;

import com.auki.internal.HttpUtils;

import java.io.File;
import java.io.IOException;

public class PdfHandler {
    public static String handlePdf(String html, String currentDirectory, String description, HttpUtils httpUtils) throws IOException {
        String fileName = "";
        int pdfIndex = 0;
        while ((pdfIndex = html.indexOf("<p>Click HERE to", pdfIndex)) != -1) {
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
                            httpUtils.uploadPdf(fileName,currentDirectory);
                            String subString = html.substring(pdfIndex,pdfEndIndex);
                            StringBuffer stringBuffer = new StringBuffer(subString);
                            stringBuffer.replace(9,13,"<a href=\"/content/dam/cir2/document/internal/" + fileName + "\">" + "<u>HERE</u></a>");
                            StringBuffer stringBuffer1 = new StringBuffer(html);
                            stringBuffer1.replace(pdfIndex,pdfEndIndex, String.valueOf(stringBuffer));
                            html = String.valueOf(stringBuffer1);
                            pdfIndex = pdfEndIndex;
                        }
                    }
                }
            }

        }
        return html;
    }
}
