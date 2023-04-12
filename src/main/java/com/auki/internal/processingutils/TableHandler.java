package com.auki.internal.processingutils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableHandler {
    public static String tableHandler(String html){

        String tableDivClassname = "<div class=\"table-responsive\">";
        String tableClassname = " class=\"table table-striped table-hover\" ";
        String theadClassname = " class=\"thead-dark\" ";
        String textAlign = " style=\"text-align: center;\"";
        String theadWithClass = "<thead class=\"thead-dark\">";




        int tableIndex1 = 0;
        while ((tableIndex1 = html.indexOf("<table>", tableIndex1)) != -1) {
            int tableEndIndex = html.indexOf("</table>", tableIndex1);
            String htmlPart = html.substring(tableIndex1,tableEndIndex);
            int trIndex = htmlPart.indexOf("<tr>");
            int trEndIndex = htmlPart.indexOf("</tr>");
            StringBuffer stringBuffer1 = new StringBuffer(htmlPart);
            if(htmlPart.contains("<thead>")){
                int theadIndex = htmlPart.indexOf("<thead>");
                stringBuffer1.insert(theadIndex + 6,theadClassname);
            }
            else{
                stringBuffer1.insert(trEndIndex + 5,"</thead>");
                stringBuffer1.insert(trIndex,theadWithClass);
            }
            htmlPart = String.valueOf(stringBuffer1);
            StringBuffer stringBuffer2 = new StringBuffer(html);
            stringBuffer2.replace(tableIndex1,tableEndIndex,htmlPart);
            html = String.valueOf(stringBuffer2);
            tableIndex1 = tableEndIndex;
        }

        int tableIndex2 = 0;
        while ((tableIndex2 = html.indexOf("<table>", tableIndex2)) != -1) {
            int tableEndIndex = html.indexOf("</table>",tableIndex2);
            int theadEndIndex = html.indexOf("</thead>",tableIndex2);
            StringBuffer stringBuffer3= new StringBuffer(html);
            stringBuffer3.insert(tableEndIndex,"</tbody>");
            stringBuffer3.insert(theadEndIndex + 8,"<tbody>");
            html = String.valueOf(stringBuffer3);
            tableIndex2 = tableEndIndex;
        }
        int tableIndex3 = 0;
        while ((tableIndex3 = html.indexOf("<table>", tableIndex3)) != -1) {
            int tableEndIndex = html.indexOf("</table>",tableIndex3);
            int trIndex = html.indexOf("<tr>",tableIndex3);
            int trEndIndex = html.indexOf("</tr>",tableIndex3);
            String subtString = html.substring(trIndex,trEndIndex);
            String ouput = subtString.replaceAll("<td>","<th>");
            String ouput1 = ouput.replaceAll("</td>","</th>");
            StringBuffer stringBuffer4 = new StringBuffer(html);
            stringBuffer4.replace(trIndex,trEndIndex,ouput1);
            html = String.valueOf(stringBuffer4);
            tableIndex3 = tableEndIndex;
        }

        int tableIndex4 = 0;
        while ((tableIndex4 = html.indexOf("<table>", tableIndex4)) != -1) {
            int tableEndIndex = html.indexOf("</table>", tableIndex4);
            StringBuffer stringBuffer5 = new StringBuffer(html);
            stringBuffer5.insert(tableIndex4 + 6, tableClassname);
            html = String.valueOf(stringBuffer5);
            tableIndex4 = tableEndIndex;
        }

        int tableIndex5 = 0;
        while ((tableIndex5 = html.indexOf("<table", tableIndex5)) != -1) {
            int tableEndIndex = html.indexOf("</table>", tableIndex5);
            StringBuffer stringBuffer6 = new StringBuffer(html);
            stringBuffer6.insert(tableEndIndex + 8,"</div>");
            stringBuffer6.insert(tableIndex5,tableDivClassname);
            html = String.valueOf(stringBuffer6);
            tableIndex5 = tableEndIndex;
        }
        int tableIndex7 = 0;
        while ((tableIndex7 = html.indexOf("<table", tableIndex7)) != -1) {
            int tableEndIndex = html.indexOf("</table>",tableIndex7);
            String substring = html.substring(tableIndex7,tableEndIndex);
            Document doc = Jsoup.parse(substring);

            // Find all <td> tags containing multiple <p> tags
            Elements tdsWithMultiplePs = doc.select("td:has(> p:nth-child(2))");

            // Add <br> tags between each <p> tag
            for (Element td : tdsWithMultiplePs) {
                Elements ps = td.select("p");
                for (int i = 1; i < ps.size(); i++) {
                    ps.get(i - 1).after("<br>");
                }
            }
            substring = doc.html();
            substring=substring.replace("<html>","");
            substring = substring.replace("<head></head>","");
            substring = substring.replace(" <body>","");
            substring = substring.replace("</table>","");
            substring = substring.replace("</body>","");
            substring = substring.replace("</html>","");
            StringBuffer stringBuffer8 = new StringBuffer(html);
            stringBuffer8.replace(tableIndex7,tableEndIndex,substring);
            html = String.valueOf(stringBuffer8);
            tableIndex7 = tableEndIndex;
        }

        int tableIndex6 = 0;
        while ((tableIndex6 = html.indexOf("<table", tableIndex6)) != -1) {
            int tableEndIndex = html.indexOf("</table>", tableIndex6);
            String subString = html.substring(tableIndex6,tableEndIndex);
            String output = subString.replaceAll("<p>","");
            String output1 = output.replaceAll("</p>","");
            StringBuffer stringBuffer7 = new StringBuffer(html);
            stringBuffer7.replace(tableIndex6,tableEndIndex,output1);
            html = String.valueOf(stringBuffer7);
            tableIndex6 = tableEndIndex;
        }
















//        int tableIndex1 = 0;
//        while ((tableIndex1 = html.indexOf("<table>", tableIndex1)) != -1) {
//            int tableEndIndex = html.indexOf("</table>", tableIndex1);
//            int trStartIndex = html.indexOf("<tr>", tableIndex1);
//            int trEndIndex = html.indexOf("</tr>", tableIndex1);
//            String htmlPart = html.substring(tableIndex1,tableEndIndex);
//            StringBuffer stringBuffer = new StringBuffer(html);
//            if(!htmlPart.contains("<thead>")){
//                stringBuffer.insert(trStartIndex, "<thead>");
//                stringBuffer.insert(trStartIndex + 6, theadClassname);
//                stringBuffer.insert(trEndIndex + 12, "</thead>");
//            }
//            else{
//                String subString = html.substring(trStartIndex,trEndIndex);
//                String replacedString = subString.replaceAll("<p>","").replaceAll("</p>","");
//                stringBuffer.replace(trStartIndex,trEndIndex,replacedString);
//                stringBuffer.insert(trStartIndex -1, theadClassname);
//            }
//            html = String.valueOf(stringBuffer);
//            StringBuffer stringBuffer2 = new StringBuffer(html);
//            int updatedTrEndIndex = trEndIndex + 27;
//            String tableHead = stringBuffer2.substring(trStartIndex, updatedTrEndIndex);
//            String tdReplaced = tableHead.replace("td", "th");
//            String pTagReplaced = tdReplaced.replace("<p><strong>", "");
//            String PTagEndReplaced = pTagReplaced.replace("</strong></p>", "");
//            stringBuffer2.replace(trStartIndex, updatedTrEndIndex, PTagEndReplaced);
//            int tHeadEndIndex = stringBuffer2.indexOf("</thead>", tableIndex1);
//            int tableEndIndex2 = stringBuffer2.indexOf("</table>", tableIndex1);
//            html = String.valueOf(stringBuffer2);
//            if(!html.contains("<tbody>")){
//                stringBuffer2.insert(tHeadEndIndex + 8, "<tbody>");
//                stringBuffer2.insert(tableEndIndex2, "</tbody>");
//            }
//            html = String.valueOf(stringBuffer2);
//            int tableTagEndIndex = html.indexOf("</table>", tableIndex1);
//            tableIndex1 = tableTagEndIndex;
//        }

//        int tableIndex2 = 0;
//        while ((tableIndex2 = html.indexOf("<table>", tableIndex2)) != -1) {
//            int tableTagEndIndex = html.indexOf("</table>", tableIndex2);
//            StringBuffer stringBuffer = new StringBuffer(html);
//            stringBuffer.insert(tableIndex2, "<div>");
//            stringBuffer.insert(tableTagEndIndex + 13, "</div>");
//            stringBuffer.insert(tableIndex2 + 4, tableDivClassname);
//            html = String.valueOf(stringBuffer);
//            tableTagEndIndex = html.indexOf("</table>", tableIndex2);
//            tableIndex2 = tableTagEndIndex;
//        }

//        int tableIndex3 = 0;
//        while ((tableIndex3 = html.indexOf("<table>", tableIndex3)) != -1) {
//            int tableTagEndIndex = html.indexOf("</table>", tableIndex3);
//            StringBuffer stringBuffer = new StringBuffer(html);
//            stringBuffer.insert(tableIndex3 + 6, tableClassname);
//            html = String.valueOf(stringBuffer);
//            tableIndex3 = tableTagEndIndex;
//        }
//        int tabbleIndex4 = 0;
//        while ((tabbleIndex4 = html.indexOf("<table", tabbleIndex4)) != -1) {
//            int tableTagEndIndex = html.indexOf("</table>", tabbleIndex4);
//            int theadIndex = html.indexOf("<thead", tabbleIndex4);
//            int theadEndIndex = html.indexOf("</thead>", tabbleIndex4);
//            String subString = html.substring(theadIndex,theadEndIndex);
//            int lastIndex = 0;
//            int count = 0;
//            while(lastIndex != -1){
//
//                lastIndex = subString.indexOf("</th>",lastIndex);
//
//                if(lastIndex != -1){
//                    count ++;
//                    lastIndex += "</th>".length();
//                }
//            }
//            if(count == 1){
//                int trIndex = html.indexOf("<tr>",tabbleIndex4);
//                StringBuffer stringBuffer = new StringBuffer(html);
//                stringBuffer.insert(trIndex + 7, textAlign);
//                html = String.valueOf(stringBuffer);
//            }
//            tabbleIndex4 = tableTagEndIndex;
//        }
//        if(html.contains("</s</thead>")){
//            html = html.replace("</s</thead>","</th></tr></thead>");
//        }
//        if(html.contains("<tbody>trong></p>")){
//            html = html.replace("<tbody>trong></p>","<tbody>");
//        }
//        if(html.contains("</t</tbody>d></tr>")){
//            html = html.replace("</t</tbody>d></tr>","</td></tr></tbody>");
//        }
//        if(html.contains("style=\"text-align: center;\"")){
//            html = html.replace("style=\"text-align: center;\"","");
//        }


        return html;
    }
}
