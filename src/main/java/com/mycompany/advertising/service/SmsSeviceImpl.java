package com.mycompany.advertising.service;

import com.mycompany.advertising.service.api.SmsService;
import com.mycompany.advertising.service.util.FarazSmsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Amir on 9/16/2021.
 */
@Service
public class SmsSeviceImpl implements SmsService {
    private final RestTemplate restTemplate;
    private String url = "https://sms.farazsms.com/class/sms/webservice/send_url.php?from=fromnumber&to=yourtdestnumber&msg=yourmsg&uname=youruname&pass=yourpass";
    @Value("${farazsms.username}")
    private String farazsmsusername;
    @Value("${farazsms.password}")
    private String farazsmspassword;
    @Value("${farazsms.fromnumber}")
    private String farazsmsfromnumber;

    public SmsSeviceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public int sendSms(String message, String phonenumber) {
        String url = "https://sms.farazsms.com/class/sms/webservice/send_url.php?from="+farazsmsfromnumber+"&to="+phonenumber+"&msg="+message+"&uname="+farazsmsusername+"&pass="+farazsmspassword;
        FarazSmsResponse farazsmsresponse = restTemplate.getForObject(url, FarazSmsResponse.class);
        return Integer.parseInt(farazsmsresponse.getSatuse());
    }
}
