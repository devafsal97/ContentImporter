package com.auki.internal.processingutils;

import com.auki.internal.HttpUtils;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class ImageHandler {
    public static String imageHandler(String html, String fileName, String currentDirectory, HttpUtils httpUtils) throws IOException {

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

            imageName = fileName + imageCount + ".png";
            File outputfile = new File(currentDirectory + "/"+ fileName + "/" + imageName);
            System.out.println(outputfile.getName().toString());
            ImageIO.write(image, "png", outputfile);

            String imagePath = "/content/dam/cir2/images/" + imageName + ".png";
            StringBuffer stringBuffer = new StringBuffer(html);
            stringBuffer.replace(srcIndex + 5, imgEndIndex - 2, imagePath);
            html = String.valueOf(stringBuffer);
            httpUtils.uploadImage(outputfile.getPath(),imageName);
            index = imgEndIndex;
            imageCount = imageCount + 1;
        }
        return html;
    }
}
