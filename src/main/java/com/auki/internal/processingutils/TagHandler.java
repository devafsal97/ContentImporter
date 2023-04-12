package com.auki.internal.processingutils;

public class TagHandler {
    public static String tagHandler(String html){

        int rSymbolIndex = 0;
        while ((rSymbolIndex = html.indexOf("®", rSymbolIndex)) != -1) {
            StringBuffer stringBuffer = new StringBuffer(html);
            stringBuffer.replace(rSymbolIndex,rSymbolIndex + 1,"<sup>®</sup>");
            html = String.valueOf(stringBuffer);
            rSymbolIndex = rSymbolIndex + 12;
        }
        return html;
    }
}
