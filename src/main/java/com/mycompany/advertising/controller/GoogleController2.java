package com.mycompany.advertising.controller;

import com.mycompany.advertising.controller.common.XmlUrl;
import com.mycompany.advertising.controller.common.XmlUrlSet;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by Amir on 3/7/2022.
 */
@Controller
public class GoogleController2 {
    @RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
//, produces = {MediaType.APPLICATION_XML_VALUE}
    public
    @ResponseBody
    XmlUrlSet siteMap() {//ResponseEntity<XmlUrlSet>
        XmlUrlSet xmlUrlSet = new XmlUrlSet();
        String link = "http://www.mysite.com";
        xmlUrlSet.addUrl(new XmlUrl(link, XmlUrl.Priority.HIGH));
        xmlUrlSet.addUrl(new XmlUrl(link + "/link-1", XmlUrl.Priority.HIGH));
        xmlUrlSet.addUrl(new XmlUrl(link + "/link-2", XmlUrl.Priority.MEDIUM));
        System.out.println(xmlUrlSet);

        return xmlUrlSet;//new ResponseEntity<>(xmlUrlSet, HttpStatus.OK);
    }

    @RequestMapping(value = "/robots.txt", method = RequestMethod.GET)
    public String getRobots(HttpServletRequest request) {
        return (Arrays.asList("mysite.com", "www.mysite.com").contains(request.getServerName())) ?
                "robotsAllowed" : "robotsDisallowed";
    }
}
