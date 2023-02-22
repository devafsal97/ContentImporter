package com.auki.internal.processingutils;

import com.auki.internal.ContentImporter;
import com.auki.internal.HttpUtils;
import lombok.extern.java.Log;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

@Log
public class CreateTemplate {

    public static void createTemplate(String currentDirectory) throws IOException, ParseException {

        String title, content, description, date, fileName;
        HashMap<String, String> tagObject = new HashMap<>();
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

        String inputFilePath = "Templates/template.json" ;
        ClassLoader classLoader = ContentImporter.class.getClassLoader();
        InputStream resource = classLoader.getResourceAsStream(inputFilePath);
        org.json.simple.parser.JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(resource));
        String jsonString = obj.toString();
        JSONObject jsonObject = new JSONObject(jsonString);
        HttpUtils httpUtils = new HttpUtils();

        File folder = new File(currentDirectory);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {

            File myDataTxt = listOfFiles[i];
            String currentFileName = myDataTxt.getName();
            String extension = "jar";
            int extIndex = currentFileName.lastIndexOf('.');
            if(extIndex > 0) {
                extension =  currentFileName.substring(extIndex + 1).toString();
                if (!"jar".equals(extension) && !"pdf".equals(extension)) {

                    DocumentConverter converter = new DocumentConverter();
                    Result<String> result = converter.convertToHtml(myDataTxt);
                    String html = result.getValue();
                    int firstDashIndex = html.indexOf("–");
                    int secndDashIndex = html.indexOf("–", firstDashIndex + 1);
                    int dateComaIndex = html.indexOf(",",secndDashIndex);
                    int strongTagIndex = html.indexOf("</strong>",1);
                    String ptagString = "";
                    ptagString = html.substring(strongTagIndex + 9,strongTagIndex + 13);
                    if(!ptagString.equals("</p>")){
                        StringBuffer stringBuffer = new StringBuffer(html);
                        stringBuffer.insert(strongTagIndex + 9,"</p><p>");
                        html = String.valueOf(stringBuffer).replaceAll("<br />","");
                    }
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

                    int pIndex = html.indexOf("<p>", 1);
                    title = html.substring(11, firstDashIndex - 1).replaceAll("<sup>"," ").replaceAll("</sup>","").replaceAll("<em>","").replaceAll("</em>","");
                    description = html.substring(firstDashIndex + 2, secndDashIndex - 1);

                    String smallDate = html.substring(secndDashIndex + 2, dateComaIndex + 6);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
                    LocalDate localDate = LocalDate.parse(smallDate, formatter);
                    date = localDate.toString() + "T19:30:00.000+05:30";

                    fileName = listOfFiles[i].getName().replaceFirst("[.][^.]+$", "");
                    Files.createDirectories(Paths.get(currentDirectory + "/" + fileName));

                    html = ImageHandler.imageHandler(html,fileName,currentDirectory,httpUtils);
                    html = TableHandler.tableHandler(html);
                    html = LinkHandler.linkHandler(html,fileName);
                    html= PdfHandler.handlePdf(html,currentDirectory,description,httpUtils);

                    int dashIndex = description.indexOf("-");
                    String tagType = description.substring(0,dashIndex);
                    String[] tagArray = new String[1];
                    tagArray[0] = tagObject.get(tagType);

                    // assigning values to templates.
                    int index = html.lastIndexOf("<p>");
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

        }

    }
}
