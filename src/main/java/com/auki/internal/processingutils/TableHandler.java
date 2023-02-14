package com.auki.internal.processingutils;

public class TableHandler {
    public static String tableHandler(String html){

        String tableDivClassname = " class=\"table-responsive\" ";
        String tableClassname = " class=\"table table-striped table-hover\" ";
        String theadClassname = " class=\"thead-dark\" ";

        int tableIndex1 = 0;
        while ((tableIndex1 = html.indexOf("<table>", tableIndex1)) != -1) {
            int trStartIndex = html.indexOf("<tr>", tableIndex1);
            int trEndIndex = html.indexOf("</tr>", tableIndex1);
            StringBuffer stringBuffer = new StringBuffer(html);
            stringBuffer.insert(trStartIndex, "<thead>");
            stringBuffer.insert(trEndIndex + 12, "</thead>");
            stringBuffer.insert(trStartIndex + 6, theadClassname);
            int updatedTrEndIndex = trEndIndex + 27;
            String tableHead = stringBuffer.substring(trStartIndex, updatedTrEndIndex);
            String tdReplaced = tableHead.replace("td", "th");
            String pTagReplaced = tdReplaced.replace("<p><strong>", "");
            String PTagEndReplaced = pTagReplaced.replace("</strong></p>", "");
            stringBuffer.replace(trStartIndex, updatedTrEndIndex, PTagEndReplaced);
            int tHeadEndIndex = stringBuffer.indexOf("</thead>", tableIndex1);
            int tableEndIndex = stringBuffer.indexOf("</table>", tableIndex1);
            html = String.valueOf(stringBuffer);
            if(!html.contains("<tbody>")){
                stringBuffer.insert(tHeadEndIndex + 8, "<tbody>");
                stringBuffer.insert(tableEndIndex, "</tbody>");
            }
            html = String.valueOf(stringBuffer);
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

        return html;
    }
}
