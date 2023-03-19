package com.mycompany.advertising.service;

import com.mycompany.advertising.service.api.StorageService;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Amir on 1/9/2022.
 */
//@Service
public class StorageServiceUuploadIr implements StorageService {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private Map<String, String> cookies = new HashMap<String, String>();
    @Value("${upload.website.username}")
    private String username;
    @Value("${upload.website.password}")
    private String password;

    @Override
    public List<URL> storeImage(MultipartFile file) throws IOException {
        int i;
        for (i = 0; i < 3; i++) {
            if (isLogin()) {
                logger.trace("logged in in Uupload.ir");
                break;
            } else {
                logger.trace("is not login in Uupload.ir");
                login();
            }
        }
        if (i >= 3) throw new IOException("fail to login");
        String rslt = uploadInputStream(file.getInputStream(), file.getOriginalFilename());
        java.util.List<URL> result = new java.util.ArrayList<>();
        result.add(new URL(rslt));
        if (file.getOriginalFilename().lastIndexOf('.') >= 0) {
            int indx = rslt.lastIndexOf('.');
            result.add(new URL(rslt.substring(0, indx) + "_thumb" + rslt.substring(indx)));
        } else result.add(new URL(rslt + "_thumb"));
        return result;
    }

    @Override
    public List<URL> getAllImagesURL() {
        return null;
    }

    @Override
    public void deleteUnusedImages() {

    }

    @Override
    public void deleteImageByUrl(URL imgURL) {

    }

    @Override
    public List<String> getAllDomainsName() {
        List<String> result = new ArrayList<>();
        result.add("UuploadIr");
        return result;
    }

    private String uploadInputStream(InputStream is, String filename) throws IOException {
        logger.debug("try to upload image to uupload.ir " + filename);
        Connection connection = readyConnection("https://uupload.ir/", Connection.Method.GET);
        Connection.Response response = myExecute(connection);
        Document document = response.parse();
        Element formelement = document.select("#upload_form1").first();//body div div form input");
        Elements inputelements = formelement.select("input");
        Map<String, String> hiddenInputs = new HashMap<String, String>();
        for (Element telement : inputelements) {
            if (telement.attr("type") != null && telement.attr("type").equals("hidden"))
                hiddenInputs.put(telement.attr("name"), telement.attr("value"));
        }
        String action = formelement.attr("action");
        connection = readyConnection(action, Connection.Method.POST);
        connection.data(hiddenInputs);
        connection.data("ittl", "0");
        connection.data("__userfile[]", filename, is);
        connection.ignoreContentType(true);
        response = myExecute(connection);
        document = response.parse();
        Element linkelement = document.select("body table tr td input").get(0);
        String rslt = linkelement.attr("value");
        logger.info("uload link: " + rslt);
        return rslt;
    }

    private void login() throws IOException {
        logger.trace("try to login in uupload.ir");
        Connection connection = readyConnection("https://my.uupload.ir/", Connection.Method.GET);
        myExecute(connection);
        connection = readyConnection("https://uupload.ir/info.php?act=login", Connection.Method.GET);
        Connection.Response response = myExecute(connection);
        Document document = response.parse();
        Elements elements = document.select("body div div form input");
        Map<String, String> hiddenInputs = new HashMap<String, String>();
        for (Element element : elements) {
            if (element.attr("type") != null && element.attr("type").equals("hidden"))
                hiddenInputs.put(element.attr("name"), element.attr("value"));
        }
        connection = readyConnection("https://uupload.ir/users.php?act=login-d", Connection.Method.GET);
        connection.data(hiddenInputs);
        connection.data("username", username, "password", password, "rememberme", "1");
        connection.method(Connection.Method.POST);
        myExecute(connection);
    }

    private boolean isLogin() throws IOException {
        Connection connection = readyConnection("https://uupload.ir/", Connection.Method.GET);
        Connection.Response response = myExecute(connection);
        Document document = response.parse();
        Element element = document.select("body div div a").get(15);
        if (element == null || element.text().equals("ورود")) {
            logger.info("is not logged in");
            return false;
        } else {
            logger.info("is logged in");
            return true;
        }
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
