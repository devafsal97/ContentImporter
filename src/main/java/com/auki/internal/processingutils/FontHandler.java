package com.auki.internal.processingutils;

import lombok.extern.java.Log;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@Log
public class FontHandler {
    private  static  String fontClass = " class=\"smaller\" ";
    public static String fontHandler(String html, String currentDirecrory, String filename) throws Exception {

        log.info(currentDirecrory + "/" + filename + ".docx");


        File inputFile = new File(currentDirecrory + "/" + filename + ".docx");
        FileInputStream fis = null;

        fis = new FileInputStream(inputFile);
        XWPFDocument document = new XWPFDocument(fis);


        XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(new File("word/media")));

        OutputStream out = new ByteArrayOutputStream();


        XHTMLConverter.getInstance().convert(document, out, options);
        String newhtml=out.toString();
        System.out.println(html);


        Document doc = Jsoup.parse(newhtml);

        Elements elements = doc.select("*");

        for (Element element : elements) {
            String style = element.attr("style");
            if (style != null && !style.isEmpty()) {
                String[] styles = style.split(";");
                StringBuilder sb = new StringBuilder();
                for (String s : styles) {
                    if (s.trim().startsWith("font-size") || s.trim().startsWith("vertical-align")) {
                        sb.append(s.trim()).append(";");
                    }
                }
                element.attr("style", sb.toString());
            }
        }

        String filteredhtml = doc.toString();

        String styleRemovedhtml = filteredhtml.replaceAll(" style=\"\"","");
        int bodyTagIndex = styleRemovedhtml.indexOf("<body>");
        String cleannhtml = styleRemovedhtml.substring(bodyTagIndex);
        cleannhtml = cleannhtml.replaceAll(">\\s+<", "><");


        int fontsizeIndex = 0;
        while ((fontsizeIndex = cleannhtml.indexOf("font-size:10.0pt;", fontsizeIndex)) != -1) {
            int pindex = cleannhtml.lastIndexOf("<p",fontsizeIndex);
            int pendindex =  cleannhtml.indexOf("</p>",pindex);
            String substring = cleannhtml.substring(pindex,pendindex);

            int vasindex = 0;
            while ((vasindex = substring.indexOf("vertical-align:super", vasindex)) != -1) {
                int spanIndex = substring.lastIndexOf("<span",vasindex);
                int spanEndindex = substring.indexOf("\">",spanIndex);
                int spanCloseIndex = substring.indexOf("</span>",vasindex);
                StringBuffer stringBuffer = new StringBuffer(substring);
                stringBuffer.replace(spanCloseIndex,spanCloseIndex + 7, "</sup>");
                stringBuffer.replace(spanIndex,spanEndindex + 2, "<sup>");
                substring = String.valueOf(stringBuffer);
                vasindex = spanCloseIndex;
            }
            substring = substring.replaceAll("<span[^>]*>(.*?)</span>","$1");
            html = AddFontClass(substring,html);
            fontsizeIndex = pendindex;

        }

        int fontsizeIndex1 = 0;
        while ((fontsizeIndex1 = cleannhtml.indexOf("font-size:9.0pt;", fontsizeIndex1)) != -1) {
            int pindex = cleannhtml.lastIndexOf("<p",fontsizeIndex1);
            int pendindex =  cleannhtml.indexOf("</p>",pindex);
            String substring = cleannhtml.substring(pindex,pendindex);

            int vasindex = 0;
            while ((vasindex = substring.indexOf("vertical-align:super", vasindex)) != -1) {
                int spanIndex = substring.lastIndexOf("<span",vasindex);
                int spanEndindex = substring.indexOf("\">",spanIndex);
                int spanCloseIndex = substring.indexOf("</span>",vasindex);
                StringBuffer stringBuffer = new StringBuffer(substring);
                stringBuffer.replace(spanCloseIndex,spanCloseIndex + 7, "</sup>");
                stringBuffer.replace(spanIndex,spanEndindex + 2, "<sup>");
                substring = String.valueOf(stringBuffer);
                vasindex = spanCloseIndex;
            }
            substring = substring.replaceAll("<span[^>]*>(.*?)</span>","$1");
            html = AddFontClass(substring,html);
            fontsizeIndex1 = pendindex;

        }
        int fontsizeIndex2 = 0;
        while ((fontsizeIndex2 = cleannhtml.indexOf("font-size:8.0pt;", fontsizeIndex2)) != -1) {
            int pindex = cleannhtml.lastIndexOf("<p",fontsizeIndex2);
            int pendindex =  cleannhtml.indexOf("</p>",pindex);
            String substring = cleannhtml.substring(pindex,pendindex);

            int vasindex = 0;
            while ((vasindex = substring.indexOf("vertical-align:super", vasindex)) != -1) {
                int spanIndex = substring.lastIndexOf("<span",vasindex);
                int spanEndindex = substring.indexOf("\">",spanIndex);
                int spanCloseIndex = substring.indexOf("</span>",vasindex);
                StringBuffer stringBuffer = new StringBuffer(substring);
                stringBuffer.replace(spanCloseIndex,spanCloseIndex + 7, "</sup>");
                stringBuffer.replace(spanIndex,spanEndindex + 2, "<sup>");
                substring = String.valueOf(stringBuffer);
                vasindex = spanCloseIndex;
            }
            substring = substring.replaceAll("<span[^>]*>(.*?)</span>","$1");
            html = AddFontClass(substring,html);
            fontsizeIndex2 = pendindex;

        }

        return html;
    }

    public static String AddFontClass(String subString, String html){
        int ptagIndex  = html.indexOf(subString);
        StringBuffer stringBuffer = new StringBuffer(html);
        stringBuffer.insert(ptagIndex + 2,fontClass);
        html = String.valueOf(stringBuffer);
        return html;
    }
}
