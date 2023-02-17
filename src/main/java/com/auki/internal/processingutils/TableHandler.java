package com.auki.internal.processingutils;

public class TableHandler {
    public static String tableHandler(String html){

        String tableDivClassname = " class=\"table-responsive\" ";
        String tableClassname = " class=\"table table-striped table-hover\" ";
        String theadClassname = " class=\"thead-dark\" ";
        String textAlign = " style=\"text-align: center;\"";

        int tableIndex1 = 0;
        while ((tableIndex1 = html.indexOf("<table>", tableIndex1)) != -1) {
            int tableEndIndex = html.indexOf("</table>", tableIndex1);
            int trStartIndex = html.indexOf("<tr>", tableIndex1);
            int trEndIndex = html.indexOf("</tr>", tableIndex1);
            String htmlPart = html.substring(tableIndex1,tableEndIndex);
            StringBuffer stringBuffer = new StringBuffer(html);
            if(!htmlPart.contains("<thead>")){
                stringBuffer.insert(trStartIndex, "<thead>");
                stringBuffer.insert(trStartIndex + 6, theadClassname);
                stringBuffer.insert(trEndIndex + 12, "</thead>");
            }
            else{
                String subString = html.substring(trStartIndex,trEndIndex);
                String replacedString = subString.replaceAll("<p>","").replaceAll("</p>","");
                stringBuffer.replace(trStartIndex,trEndIndex,replacedString);
                stringBuffer.insert(trStartIndex -1, theadClassname);
            }
            html = String.valueOf(stringBuffer);
            StringBuffer stringBuffer2 = new StringBuffer(html);
            int updatedTrEndIndex = trEndIndex + 27;
            String tableHead = stringBuffer2.substring(trStartIndex, updatedTrEndIndex);
            String tdReplaced = tableHead.replace("td", "th");
            String pTagReplaced = tdReplaced.replace("<p><strong>", "");
            String PTagEndReplaced = pTagReplaced.replace("</strong></p>", "");
            stringBuffer2.replace(trStartIndex, updatedTrEndIndex, PTagEndReplaced);
            int tHeadEndIndex = stringBuffer2.indexOf("</thead>", tableIndex1);
            int tableEndIndex2 = stringBuffer2.indexOf("</table>", tableIndex1);
            html = String.valueOf(stringBuffer2);
            if(!html.contains("<tbody>")){
                stringBuffer2.insert(tHeadEndIndex + 8, "<tbody>");
                stringBuffer2.insert(tableEndIndex2, "</tbody>");
            }
            html = String.valueOf(stringBuffer2);
            int tableTagEndIndex = html.indexOf("</table>", tableIndex1);
            tableIndex1 = tableTagEndIndex;
        }

        int tableIndex2 = 0;
        while ((tableIndex2 = html.indexOf("<table>", tableIndex2)) != -1) {
            int tableTagEndIndex = html.indexOf("</table>", tableIndex2);
            StringBuffer stringBuffer = new StringBuffer(html);
            stringBuffer.insert(tableIndex2, "<div>");
            stringBuffer.insert(tableTagEndIndex + 13, "</div>");
            stringBuffer.insert(tableIndex2 + 4, tableDivClassname);
            html = String.valueOf(stringBuffer);
            tableTagEndIndex = html.indexOf("</table>", tableIndex2);
            tableIndex2 = tableTagEndIndex;
        }

        int tableIndex3 = 0;
        while ((tableIndex3 = html.indexOf("<table>", tableIndex3)) != -1) {
            int tableTagEndIndex = html.indexOf("</table>", tableIndex3);
            StringBuffer stringBuffer = new StringBuffer(html);
            stringBuffer.insert(tableIndex3 + 6, tableClassname);
            html = String.valueOf(stringBuffer);
            tableIndex3 = tableTagEndIndex;
        }
        int tabbleIndex4 = 0;
        while ((tabbleIndex4 = html.indexOf("<table", tabbleIndex4)) != -1) {
            int tableTagEndIndex = html.indexOf("</table>", tabbleIndex4);
            int theadIndex = html.indexOf("<thead", tabbleIndex4);
            int theadEndIndex = html.indexOf("</thead>", tabbleIndex4);
            String subString = html.substring(theadIndex,theadEndIndex);
            int lastIndex = 0;
            int count = 0;

            while(lastIndex != -1){

                lastIndex = subString.indexOf("</th>",lastIndex);

                if(lastIndex != -1){
                    count ++;
                    lastIndex += "</th>".length();
                }
            }
            if(count == 1){
                int trIndex = html.indexOf("<tr>",tabbleIndex4);
                StringBuffer stringBuffer = new StringBuffer(html);
                stringBuffer.insert(trIndex + 7, textAlign);
                html = String.valueOf(stringBuffer);
            }
            tabbleIndex4 = tableTagEndIndex;
        }

        return html;
    }
}
