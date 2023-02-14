package com.auki.internal.processingutils;

import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class ImageHandler {
    public static String imageHandler(String html, String fileName, String currentDirectory) throws IOException {

        int index = 0;
        int imageCount = 1;
        String imageName;

        while ((index = html.indexOf("<img", index)) != -1) {
            int srcIndex = html.indexOf("src", index);
            int imgEndIndex = html.indexOf("/>", index);
            String src = html.substring(srcIndex + 27, imgEndIndex - 2);
            BufferedImage image = null;
            byte[] imageByte;

            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(src);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();

            imageName = fileName + imageCount;
            File outputfile = new File(currentDirectory + "/"+ fileName + "/" + imageName + ".png");
            System.out.println(outputfile.getName().toString());
            ImageIO.write(image, "png", outputfile);

            String imagePath = "/content/dam/cir2/images/" + imageName + ".png";
            StringBuffer stringBuffer = new StringBuffer(html);
            stringBuffer.replace(srcIndex + 5, imgEndIndex - 2, imagePath);
            html = String.valueOf(stringBuffer);
            index = imgEndIndex;
            imageCount = imageCount + 1;
        }
        return html;
    }
}
