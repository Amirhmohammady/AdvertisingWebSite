package com.mycompany.advertising.service;

import com.mycompany.advertising.service.api.SmsService;
import com.mycompany.advertising.service.util.FarazSmsResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Amir on 9/16/2021.
 */
@Service
public class SmsSeviceImpl implements SmsService {
    final static Logger logger = Logger.getLogger(SmsSeviceImpl.class);
    private final RestTemplate restTemplate;
    //private String url = "https://sms.farazsms.com/class/sms/webservice/send_url.php?from=fromnumber&to=yourtdestnumber&msg=yourmsg&uname=youruname&pass=yourpass";
    @Value("${farazsms.username}")
    private String farazsmsusername;
    @Value("${farazsms.password}")
    private String farazsmspassword;
    @Value("${farazsms.fromnumber}")
    private String farazsmsfromnumber;

    @Autowired
    SSLRESTClientImpl<FarazSmsResponse> sslrestclient;

    public SmsSeviceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public FarazSmsResponse sendSms(String message, String phonenumber) {
        String url = "https://sms.farazsms.com/class/sms/webservice/send_url.php?from=" + farazsmsfromnumber +
                "&to=" + phonenumber + "&msg=" + message + "&uname=" + farazsmsusername + "&pass=" + farazsmspassword;
        FarazSmsResponse farazsmsresponse;
        try {
            farazsmsresponse = sslrestclient.callWebService(url, FarazSmsResponse.class);
            if (farazsmsresponse.getSatuse().equals("0")) logger.info("vrification code sent to " + phonenumber);
            else logger.warn("vrification code faild to send " + phonenumber);
            farazsmsresponse.getSatuse();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        farazsmsresponse = new FarazSmsResponse();
        farazsmsresponse.setSatuse("-1");
        farazsmsresponse.setMessage("Error in calling Faraz Sms webservice");
        return farazsmsresponse;
    }
}
