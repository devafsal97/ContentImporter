package com.auki.internal.processingutils;

import com.auki.internal.ContentImporter;
import com.auki.internal.HttpUtils;
import lombok.extern.java.Log;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log
public class CreateTemplate {

    public static void createTemplate(String currentDirectory) throws Exception {

        String title = "", content, description = "", date, fileName,dateFromDoc = "";
        HashMap<String, String> tagObject = new HashMap<>();
        Collections.sort(ContentImporter.fileNameList);
        log.info(ContentImporter.fileNameList.toString());
        tagObject.put("CA","cir2:news/annoucements/caap");
        tagObject.put("CS","cir2:news/annoucements/cambridge-source");
        tagObject.put("CL","cir2:news/annoucements/clic");
        tagObject.put("FCCS","cir2:news/annoucements/fidelity");
        tagObject.put("HO","cir2:news/annoucements/home-offic");
        tagObject.put("PE","cir2:news/annoucements/pershing");
        tagObject.put("PM","cir2:news/annoucements/practice-management");
        tagObject.put("RC","cir2:news/annoucements/retirement-center");
        tagObject.put("WP","cir2:news/annoucements/wealthport");
        tagObject.put("WS","cir2:news/annoucements/wealth-strategies");

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(dateFormat);
        int time = 30;

        String inputFilePath = "Templates/template.json" ;
        ClassLoader classLoader = ContentImporter.class.getClassLoader();
        InputStream resource = classLoader.getResourceAsStream(inputFilePath);
        org.json.simple.parser.JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(resource));
        String jsonString = obj.toString();
        JSONObject jsonObject = new JSONObject(jsonString);
        HttpUtils httpUtils = new HttpUtils();



        File folder = new File(currentDirectory);
        for (int i = 0; i < ContentImporter.fileNameList.size(); i++) {

            File myDataTxt = new File(currentDirectory + "/" + ContentImporter.fileNameList.get(i));
            String currentFileName = myDataTxt.getName();
            String extension = "jar";
            int extIndex = currentFileName.lastIndexOf('.');
            if(extIndex > 0) {
                extension =  currentFileName.substring(extIndex + 1).toString();
                if (!"jar".equals(extension) && !"pdf".equals(extension)) {

                    DocumentConverter converter = new DocumentConverter().addStyleMap("u => u");
                    Result<String> result = converter.convertToHtml(myDataTxt);
                    String html = result.getValue();
                    int pTagIndex = html.indexOf("</p>");

                    FileInputStream fis = new FileInputStream(myDataTxt);
                    XWPFDocument docx = new XWPFDocument(fis);
                    XWPFParagraph firstParagraph = docx.getParagraphs().get(0);
                    String firstLine = firstParagraph.getText();
                    fis.close();

                    if(!firstLine.equals("")){
                        Pattern pattern = Pattern.compile("[A-Z]{1,4}-\\d{1,2}-\\d{1,3}");
                        Matcher matcher = pattern.matcher(firstLine);
                        if (matcher.find()) {
                            description = matcher.group();
                        }
                        String regexPattern = "(\\b(January|February|March|April|May|June|July|August|September|October|November|December)\\s\\d{1,2},\\s\\d{4}\\b)";
                        Pattern datePattern = Pattern.compile(regexPattern);
                        Matcher dateMatcher = datePattern.matcher(firstLine);

                        if (dateMatcher.find()) {
                            dateFromDoc = dateMatcher.group(1);
                        }
                        int descriptionIndex = firstLine.indexOf(description);
                        StringBuffer stringBuffer1 = new StringBuffer(firstLine);
                        stringBuffer1 = stringBuffer1.replace(descriptionIndex,firstLine.length(),"");
                        firstLine = String.valueOf(stringBuffer1);
//                    firstLine = firstLine.replace(description,"");
//                    firstLine = firstLine.replace(dateFromDoc,"");
                        firstLine = firstLine.replaceAll("\\<.*?\\>", "");
                        firstLine = firstLine.replace("–","").trim();
                        title = firstLine;
                    }



                    if(title.equals("")){
                        String headString = html.substring(0,pTagIndex);
                        Pattern pattern1 = Pattern.compile("[A-Z]{1,4}-\\d{1,2}-\\d{1,3}");
                        Matcher matcher1 = pattern1.matcher(headString);
                        if (matcher1.find()) {
                            description = matcher1.group();
                        }
                        String regexPattern1 = "(\\b(January|February|March|April|May|June|July|August|September|October|November|December)\\s\\d{1,2},\\s\\d{4}\\b)";
                        Pattern datePattern1 = Pattern.compile(regexPattern1);
                        Matcher dateMatcher1 = datePattern1.matcher(headString);

                        if (dateMatcher1.find()) {
                            dateFromDoc = dateMatcher1.group(1);
                        }
                        headString = headString.replace(description,"");
                        headString = headString.replace(dateFromDoc,"");
                        headString = headString.replaceAll("\\<.*?\\>", "");
                        headString = headString.replace("–","").trim();
                        title = headString;
                    }

                    html = html.replaceAll("<br />","");
                    int h1TagIndex = 0;

                    while ((h1TagIndex = html.indexOf("<h1>", h1TagIndex)) != -1) {
                        int h1TagEndIndex = html.indexOf("</h1>", h1TagIndex);
                        String subString = html.substring(h1TagIndex,h1TagEndIndex);
                        if(subString.contains("<a")){
                            int aIndex = subString.indexOf("<a");
                            int aEndIndex = subString.indexOf("</a>",aIndex);
                            String newSubString = subString.substring(aIndex,aEndIndex + 4);
                            String replacedString = subString.replace(newSubString,"");
                            StringBuffer stringBuffer = new StringBuffer(html);
                            stringBuffer.replace(h1TagIndex,h1TagEndIndex,replacedString);
                            html = String.valueOf(stringBuffer);
                        }
                        h1TagIndex = h1TagEndIndex;
                    }
                    html = html.replaceAll("<h1>","<b>").replaceAll("</h1>","</b>");


                    date = formattedDate + "T" + "18:"+time+"+05:30";
                    time = time+1;

                    fileName =myDataTxt.getName().replaceFirst("[.][^.]+$", "");
                    Files.createDirectories(Paths.get(currentDirectory + "/" + fileName));

                    html = ImageHandler.imageHandler(html,fileName,currentDirectory,httpUtils,title,description);
                    html = TableHandler.tableHandler(html);
                    html = LinkHandler.linkHandler(html,fileName);
                    html = PdfHandler.handlePdf(html,currentDirectory,description,httpUtils,title);
                    html = FontHandler.fontHandler(html,currentDirectory,fileName);
                    html = TagHandler.tagHandler(html);



                    int dashIndex = description.indexOf("-");
                    String tagType = description.substring(0,dashIndex);
                    String[] tagArray = new String[1];
                    tagArray[0] = tagObject.get(tagType);

                    // assigning values to templates.
                    int pIndex = html.indexOf("<p>", 1);
                    int index = html.lastIndexOf("<p");
                    StringBuffer stringBuffer = new StringBuffer(html);
                    int length = html.length();
                    stringBuffer.replace(index,length,"");
                    content = String.valueOf(stringBuffer);
                    content = content.substring(pIndex);


                    jsonObject.getJSONObject("jcr:content").remove("jcr:title");
                    jsonObject.getJSONObject("jcr:content").put("jcr:title", title);
                    jsonObject.getJSONObject("jcr:content").remove("cq:tags");
                    jsonObject.getJSONObject("jcr:content").put("cq:tags",tagArray);
                    jsonObject.getJSONObject("jcr:content").remove("originalPublishDate");
                    jsonObject.getJSONObject("jcr:content").put("originalPublishDate", date);
                    jsonObject.getJSONObject("jcr:content").getJSONObject("root").getJSONObject("responsivegrid").getJSONObject("responsivegrid").getJSONObject("responsivegrid").getJSONObject("text").remove("text");
                    jsonObject.getJSONObject("jcr:content").getJSONObject("root").getJSONObject("responsivegrid").getJSONObject("responsivegrid").getJSONObject("responsivegrid").getJSONObject("text").put("text", content);
                    jsonObject.getJSONObject("jcr:content").getJSONObject("landingTile").remove("description");
                    jsonObject.getJSONObject("jcr:content").getJSONObject("landingTile").put("description", description);
                    jsonObject.getJSONObject("jcr:content").getJSONObject("landingTile").remove("eyebrow");
                    jsonObject.getJSONObject("jcr:content").getJSONObject("landingTile").put("eyebrow",tagArray);

                    //saving created templates.
                    try (PrintWriter out = new PrintWriter(new FileWriter(currentDirectory + "/" + fileName + "/" + "templateedited.json"))) {
                        out.write(jsonObject.toString());
                        log.info("template created and saved for file: " + fileName);
                    } catch (Exception e) {
                        log.severe(e.getMessage());
                    }
                    //calling createpage method.
                   httpUtils.createPage(title,fileName,currentDirectory, description);

                }
            }
            title = "";
            dateFromDoc = "";
            description = "";

        }

    }
}
