package com.mycompany.advertising.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.advertising.service.api.StorageService;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Amir on 1/9/2022.
 */
@Service
public class StorageServiceUplodIr implements StorageService {
    private static final Logger logger = Logger.getLogger(StorageServiceUplodIr.class);
    private Map<String, String> cookies = new HashMap<String, String>();
    private String username = "siroos234";
    private String password = "kirekiloft112!@#A";

    @Override
    public void init() {

    }

    @Override
    public List<String> storeImage(MultipartFile file) throws IOException {
        int i;
        for (i = 0; i < 3; i++) {
            if (isLogin()) {
                logger.trace("logged in in uplod.ir");
                break;
            } else {
                logger.trace("is not login in uplod.ir");
                login();
            }
        }
        if (i >= 3) throw new IOException("fail to login");
        String rslt = uploadInputStream(file.getInputStream(), file.getOriginalFilename());
        java.util.List<String> result = new java.util.ArrayList<String>();
        result.add(rslt);
        if (file.getOriginalFilename().lastIndexOf('.') >= 0) {
            int indx = rslt.lastIndexOf('.');
            result.add(rslt.substring(0, indx) + "_t" + ".jpg");//rslt.substring(indx));
        } else result.add(rslt + "_t");
        return result;
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    private String uploadInputStream(InputStream is, String filename) throws IOException {
        logger.trace("uploadInputStream: " + filename);
        Connection connection = readyConnection("http://uplod.ir/", Connection.Method.GET);
        Connection.Response response = myExecute(connection);
        Document document = response.parse();
        Elements elements = document.select("body div div form input");
        Map<String, String> hiddenInputs = new HashMap<String, String>();
        for (Element element : elements) {
            if (element.attr("type") != null && element.attr("type").equals("hidden"))
                hiddenInputs.put(element.attr("name"), element.attr("value"));
        }
        String action = document.select("#uploadfile").first().attr("action");
        connection = readyConnection(action, Connection.Method.POST);
        connection.data(hiddenInputs);
        connection.data("file_0", filename, is);
        //if set file_public_0 to 1 file_0 will be private
        //connection.data("file_public_0", "1");
        connection.ignoreContentType(true);
        response = myExecute(connection);
        document = response.parse();
        java.util.List<Map<String, String>> mapList = new ObjectMapper().readValue(document.select("body").first().html(), java.util.List.class);
        if (!mapList.get(0).get("file_status").equals("OK")) {
            logger.warn("upload.ir returns " + mapList.get(0).get("file_status"));
            throw new IOException("can not upolad file " + mapList.get(0).get("file_status"));
        }
        String link = mapList.get(0).get("file_code");
        connection = readyConnection("http://uplod.ir/" + link + "/" + filename + ".htm", Connection.Method.GET);
        response = myExecute(connection);
        document = response.parse();
        return document.select("#img-preview").first().attr("src");
    }

    private void login() throws IOException {
        logger.trace("try to login in uplod.ir");
        Connection connection = readyConnection("http://uplod.ir/login.html", Connection.Method.GET);
        Connection.Response response = connection.execute();
        Document document = response.parse();
        Elements elements = document.select("body div div div form input");
        Map<String, String> hiddenInputs = new HashMap<String, String>();
        //logger.trace(elements);
        for (Element element : elements) {
            if (element.attr("type") != null && element.attr("type").equals("hidden"))
                hiddenInputs.put(element.attr("name"), element.attr("value"));
        }
        connection.data(hiddenInputs);
        elements = document.select("body div div div form tr td input");
        //logger.trace(elements);
        connection.data(elements.get(0).attr("name"), username, elements.get(1).attr("name"), password);
        connection.method(Connection.Method.POST);
        myExecute(connection);
    }

    private boolean isLogin() throws IOException {
        Connection connection = readyConnection("http://uplod.ir/", Connection.Method.GET);
        Connection.Response response = connection.execute();
        Document document = response.parse();
        Element element = document.select("body div div div a").get(1);
        if (element == null || element.text().equals("ثبت نام")) return false;
        else return true;
    }

    private Connection.Response myExecute(Connection connection) throws IOException {
        Connection.Response response = connection.execute();
        if (response != null && response.cookies() != null) cookies.putAll(response.cookies());
        return response;
    }

    private Connection readyConnection(String url, Connection.Method method) {
        Connection connection;
        connection = Jsoup.connect(url);
        connection.method(method);
        connection.userAgent("Mozilla");
        connection.timeout(5000);
        connection.referrer("http://google.com");
        if (cookies != null) connection.cookies(cookies);
        return connection;
    }
}
