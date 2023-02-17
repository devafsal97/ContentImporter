package com.auki.internal;

import com.auki.internal.processingutils.LinkHandler;
import lombok.extern.java.Log;
import okhttp3.*;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Log
public class HttpUtils {
        private static String host = "localhost";
        private static String port = "4502";
        private static String username = "admin";
        private static String password = "admin";
        private static ArrayList<String> filterArray = new ArrayList();

        private static ArrayList<String> errorPagesPathArray = new ArrayList();
        private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddhhmmss");
        private static String dateAsString = simpleDateFormat.format(new Date());

        private static String packageName = "weekly-announcement-" + dateAsString;
        private static String grpName = "my_packages";

        public void createPage(String title, String fileName, String currentDirectory, String description ) throws IOException {
                String name = title.replaceAll("[^A-Za-z0-9]","-").toLowerCase();
                String operation = "import";
                String contentType = "json";
                String replace = "true";
                String replaceProperties = "true";
                String contentFile = new String(Files.readAllBytes(Paths.get(currentDirectory + "/" + fileName + "/" + "templateedited.json")));
                int dashIndex = description.indexOf("-");
                String ancmntType = description.substring(0, dashIndex);
                HashMap<String, String> AnnouncementType = new HashMap<>();
                AnnouncementType.put("CA", "caap");
                AnnouncementType.put("CS", "cambridge-source");
                AnnouncementType.put("CL", "clic");
                AnnouncementType.put("FCCS", "nfs");
                AnnouncementType.put("HO", "home-office");
                AnnouncementType.put("PE", "pershing");
                AnnouncementType.put("PM", "practice-management");
                AnnouncementType.put("RC", "retirement-center");
                AnnouncementType.put("WP", "wealthport");
                AnnouncementType.put("WS", "wealth-strategies");
                String url = "http://" + host + ":" + port + "/content/cir2-internal/news-and-events/communications-from-cambridge/bulletins/" + AnnouncementType.get(ancmntType);
                String pagePath = "/content/cir2-internal/news-and-events/communications-from-cambridge/bulletins/" + AnnouncementType.get(ancmntType) + "/" + name;
                RequestBody formBody = new FormBody.Builder()
                        .add(":name", name)
                        .add(":operation", operation)
                        .add(":contentType", contentType)
                        .add(":replace", replace)
                        .add(":replaceProperties", replaceProperties)
                        .add(":contentFile", contentFile)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization", Credentials.basic("admin", "admin"))
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .post(formBody)
                        .build();
                OkHttpClient httpClient = new OkHttpClient();
                Call call = httpClient.newCall(request);
                Response response = call.execute();
                if (response.code() == 201) {
                    log.info(String.valueOf(response));
                    log.info("page created successfuly for" + name);
                    filterArray.add(pagePath);
                } else if(response.code() == 200){
                    log.info(String.valueOf(response));
                    log.severe("page already exist" + name);
                    errorPagesPathArray.add(pagePath);
                } else {
                    errorPagesPathArray.add(pagePath);
                    log.info(String.valueOf(response));
                    log.severe(response.toString());
                }
        }

        public void createPackage() throws JSONException, IOException {
                RequestBody formBody = new FormBody.Builder()
                        .add("packageName", packageName)
                        .add("groupName", grpName)
                        .build();
                Request request = new Request.Builder()
                        .url("http://" + host + ":" + port + "/crx/packmgr/service/.json/etc/packages/" + grpName + "/" + packageName + "?cmd=create")
                        .addHeader("Authorization", Credentials.basic("admin", "admin"))
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .post(formBody)
                        .build();
                OkHttpClient httpClient = new OkHttpClient();
                Call call = httpClient.newCall(request);
                Response response = call.execute();
                if (response.code() == 200) {
                    log.info("package created successfully");
                    log.info(response.toString());
                } else {
                    log.severe(response.toString());
                }
    }

        public void createFilters() throws IOException {
            System.out.println(filterArray);
                for (int c = 0; c < filterArray.size(); c++) {
                    RequestBody formBody = new FormBody.Builder()
                            .add("jcr:primaryType", "nt:unstructured")
                            .build();
                    Request request = new Request.Builder()
                            .url("http://" + host + ":" + port + "/etc/packages/" + grpName + "/" + packageName + ".zip/jcr:content/vlt:definition/filter/f" + c)
                            .addHeader("Authorization", Credentials.basic(username, password))
                            .post(formBody)
                            .build();
                    OkHttpClient httpClient = new OkHttpClient();
                    Call call = httpClient.newCall(request);
                    Response response = call.execute();
                    if (response.code() == 201) {
                        log.info(response.toString());
                    } else {
                        System.out.println(response);
                        log.severe(response.toString());
                    }

                    RequestBody filterRoot = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("root", filterArray.get(c))
                            .build();
                    Request requestRoot = new Request.Builder()
                            .url("http://" + host + ":" + port + "/etc/packages/" + grpName + "/" + packageName + ".zip/jcr:content/vlt:definition/filter/f" + c + ".rw.html")
                            .addHeader("Authorization", Credentials.basic(username, password))
                            .post(filterRoot)
                            .build();
                    Call callRoot = httpClient.newCall(requestRoot);
                    Response responseRoot = callRoot.execute();
                    System.out.println(responseRoot);
                    if (responseRoot.code() == 200) {
                       log.info(responseRoot.toString());
                    } else {
                       log.severe(responseRoot.toString());
                    }
                }
    }
        public void buildPackage() throws IOException {
                Request request = new Request.Builder()
                        .url("http://" + host + ":" + port + "/crx/packmgr/service/.json/etc/packages/" + grpName + "/" + packageName + ".zip?cmd=build")
                        .addHeader("Authorization", Credentials.basic(username, password))
                        .post(RequestBody.create(null, new byte[0]))
                        .build();
                OkHttpClient httpClient = new OkHttpClient();
                Call call = httpClient.newCall(request);
                Response response = call.execute();
                if (response.code() == 200) {
                    log.info("package build successfully");
                    log.info(response.toString());
                } else {
                    log.info("package build failed");
                    log.severe(response.toString());
                }
                log.info("path of pages not created: " + errorPagesPathArray.toString());
                log.info("Links Updated Info" + LinkHandler.linksInfoArray);
    }

    public void uploadImage(String path, String imageName) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String userName = username;
        String passWord = password;
        String credentials = userName + ":" + passWord;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        String url = "http://localhost:4502/api/assets/cir2/images/" + imageName;
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        Request request = new Request.Builder()
                .addHeader("Authorization", "Basic " + encodedCredentials)
                .url(url)
                .post(requestBody)
                .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                log.info("Image uploaded successfully!");
                filterArray.add("/content/dam/cir2/images/" + imageName);

            } else {
                log.severe("Error: " + response.code() + " - " + response.message());
            }
    }
}
