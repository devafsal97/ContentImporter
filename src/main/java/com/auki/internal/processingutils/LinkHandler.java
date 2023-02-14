package com.auki.internal.processingutils;

public class LinkHandler {
    public static String linkHandler(String html){
        int aTagIndex = 0;
        while ((aTagIndex = html.indexOf("<a", aTagIndex)) != -1) {
            int aTagEndIndex = html.indexOf("\">", aTagIndex);
            StringBuffer stringBuffer = new StringBuffer(html);
            String aTagString = stringBuffer.substring(aTagIndex, aTagEndIndex);
            if (aTagString.contains("https://www.cir2.com/")) {
                String newATagString = aTagString.replace("https://www.cir2.com/", "/content/cir2-internal/");
                String aTagEdited = newATagString.replaceAll(".$", ".com");
                stringBuffer.replace(aTagIndex, aTagEndIndex, aTagEdited);
            }

            html = String.valueOf(stringBuffer);
            aTagIndex = aTagEndIndex;
        }
        return html;
    }
}
