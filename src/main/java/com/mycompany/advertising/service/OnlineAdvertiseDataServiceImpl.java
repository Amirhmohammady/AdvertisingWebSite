package com.mycompany.advertising.service;

import com.mycompany.advertising.service.api.OnlineAdvertiseDataService;
import com.mycompany.advertising.service.util.AdvertiseTypeForOnlineData;
import com.mycompany.advertising.service.util.OnlineAdvertiseData;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amir on 8/7/2022.
 */
@Service
public class OnlineAdvertiseDataServiceImpl implements OnlineAdvertiseDataService {
    private Map<String, String> cookies = new HashMap<String, String>();

    @Override
    public OnlineAdvertiseData getData(URL url) throws IOException {
        switch (url.getHost()) {
            case "t.me":
                return getTelegramData(url);
            default:
                throw new IOException("host " + url.getHost() + " is not supported");
        }
    }

    private OnlineAdvertiseData getTelegramData(URL url) throws IOException {
        OnlineAdvertiseData result = new OnlineAdvertiseData();
        Connection connection = readyConnection(url.toString(), Connection.Method.GET);
        Connection.Response response = myExecute(connection);
        Document document = response.parse();
        Elements divElements = document.select("body>div:eq(1)>div:eq(1)>div:eq(0)");
        result.setImageUrl(divElements.select("img").first().attr("src"));
        result.setTitle(divElements.select("div:eq(1)>span").first().text());
        String members = divElements.select("div:eq(2)").first().text();
        if (members.indexOf("subscriber") != -1) {
            result.setMembers(Integer.valueOf(members.replaceAll("[^\\d]", "")));
            result.setOnlineMembers(0);
            result.setType(AdvertiseTypeForOnlineData.TELEGRAM_CHANNEL);
        } else if (members.indexOf("member") != -1) {
            String subs[] = members.split("member");
            result.setMembers(Integer.valueOf(subs[0].replaceAll("[^\\d]", "")));
            result.setOnlineMembers(Integer.valueOf(subs[1].replaceAll("[^\\d]", "")));
            result.setType(AdvertiseTypeForOnlineData.TELEGRAM_GROUP);
        }
        result.setDescription(divElements.select("div:eq(3)").first().html());
        return result;
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
