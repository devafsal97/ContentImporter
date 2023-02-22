package com.auki.internal.processingutils;

import java.util.ArrayList;


public class LinkHandler {

    public static final ArrayList<String> linksInfoArray = new ArrayList<>();
    public static String linkHandler(String html, String fileName){
        int aTagIndex = 0;
        while ((aTagIndex = html.indexOf("<a", aTagIndex)) != -1) {
            int aTagEndIndex = html.indexOf("\">", aTagIndex);
            StringBuffer stringBuffer = new StringBuffer(html);
            String aTagString = stringBuffer.substring(aTagIndex, aTagEndIndex);
            if (aTagString.contains("https://www.cir2.com/")) {
                String newATagString = aTagString.replace("https://www.cir2.com/", "/content/cir2-internal/");
                int extIndex = newATagString.lastIndexOf('.');
                String extension = ".html";
                String aTagEdited = "";
                if (extIndex > 0) {
                     extension = newATagString.substring(extIndex + 1).toString();
                }
                if(extension.equals("pdf")){
                    stringBuffer.replace(aTagIndex, aTagEndIndex,newATagString );
                }
                else{
                     aTagEdited = newATagString.replaceAll(".$", ".html");
                     stringBuffer.replace(aTagIndex, aTagEndIndex, aTagEdited);
                }

                String linkInfo = aTagString.replaceAll("<a ","")+"\"" + " Changed To " + aTagEdited.replaceAll("<a ","")+"\"" + " In File " + fileName;
                linksInfoArray.add(linkInfo);
            }

            html = String.valueOf(stringBuffer);
            aTagIndex = aTagEndIndex;
        }
        return html;
    }
}
