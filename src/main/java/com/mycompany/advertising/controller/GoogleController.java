package com.mycompany.advertising.controller;

import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.model.to.AdminMessageTo;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.service.api.AdminMessageService;
import com.mycompany.advertising.service.api.AdvertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Amir on 3/7/2022.
 */
@Controller
public class GoogleController {
    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    AdminMessageService adminMessageService;
    @Autowired
    AdvertiseService advertiseService;

    @RequestMapping(value = "/sitemap/advertises/sitemap.xml", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
//, produces = {MediaType.APPLICATION_XML_VALUE}
    @ResponseBody
    public String advertisesSiteMap() {//ResponseEntity<XmlUrlSet>
        String result;
        result = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<urlset xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\" xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\">";
        List<AdvertiseTo> advertiseTos = advertiseService.getAllAdvertises();
        for (AdvertiseTo a : advertiseTos) {
            result += "<url><lastmod>" + a.getStartdate() + "</lastmod><loc>" + authenticationFacade.getDomainName() + "/showAdvertise/id=" + a.getId() + "</loc></url>";
        }
        result += "</urlset>";
        return result;//new ResponseEntity<>(xmlUrlSet, HttpStatus.OK);
    }

    @RequestMapping(value = "/sitemap/adminMessages/sitemap.xml", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
//, produces = {MediaType.APPLICATION_XML_VALUE}
    @ResponseBody
    public String adminMessagesSiteMap() {//ResponseEntity<XmlUrlSet>
        String result;
        List<AdminMessageTo> adminMessageTos = adminMessageService.getAllAdminMessage();
        result = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<urlset xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\" xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\">";
        for (AdminMessageTo a : adminMessageTos) {
            result += "<url><lastmod>" + a.getDate() + "</lastmod><loc>" + authenticationFacade.getDomainName() + "/adminMessages/" + a.getId() + "</loc></url>";
        }
        result += "</urlset>";
        return result;//new ResponseEntity<>(xmlUrlSet, HttpStatus.OK);
    }

    @RequestMapping(value = "/sitemap/all/sitemap.xml", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
//, produces = {MediaType.APPLICATION_XML_VALUE}
    @ResponseBody
    public ResponseEntity<String> allSiteMap() {//ResponseEntity<XmlUrlSet>
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<sitemapindex xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\" xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\">" +
                "<sitemap><loc>" + authenticationFacade.getDomainName() + "/sitemap/advertises/sitemap.xml</loc></sitemap>" +
                "<sitemap><loc>" + authenticationFacade.getDomainName() + "/sitemap/adminMessages/sitemap.xml</loc></sitemap></sitemapindex>";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/robots.txt", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String getRobots() {
        String result = "Sitemap: " + authenticationFacade.getDomainName() + "/sitemap/all/sitemap.xml\n" +
                "\n" +
                "User-agent: *\n" +
                "Disallow: /login\n" +
                "Disallow: /signup\n" +
                "Disallow: /addAdvertise\n" +
                "Allow: /showAdvertise/*\n" +
                "Allow: /adminMessages/*";
        return result;
        //(Arrays.asList("mysite.com", "www.mysite.com").contains(request.getServerName())) ?"robotsAllowed" : "robotsDisallowed";
    }
}
