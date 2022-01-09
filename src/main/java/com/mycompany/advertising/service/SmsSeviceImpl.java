package com.mycompany.advertising.service;

import com.mycompany.advertising.components.utils.SendSmsException;
import com.mycompany.advertising.service.api.SSLRESTClient;
import com.mycompany.advertising.service.api.SmsService;
import com.mycompany.advertising.service.api.TokenForChangePhoneNumberService;
import com.mycompany.advertising.service.api.UserService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Amir on 9/16/2021.
 */
@Service
public class SmsSeviceImpl implements SmsService {
    final static Logger logger = Logger.getLogger(SmsSeviceImpl.class);
    private final RestTemplate restTemplate;
    @Autowired
    SSLRESTClient<String> sslrestclient;
    @Autowired
    UserService userService;
    @Autowired
    TokenForChangePhoneNumberService tokenForChangePhoneNumberService;
    //private String url = "https://sms.farazsms.com/class/sms/webservice/send_url.php?from=fromnumber&to=yourtdestnumber&msg=yourmsg&uname=youruname&pass=yourpass";
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
    public void sendSms(String message, String phonenumber) throws SendSmsException {
        String url = "https://sms.farazsms.com/class/sms/webservice/send_url.php?from=" + farazsmsfromnumber +
                "&to=" + phonenumber + "&msg=" + message + "&uname=" + farazsmsusername + "&pass=" + farazsmspassword;
        /*Unirest.setTimeouts(0, 0);
        HttpResponse response = Unirest.post("https://api.parsaspace.com/v2/user/login/refresh")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("username", "your username")
                .field("password", "your password")
                .asString();*/
        String response;
        FarazSmsResponse farazsmsresponse = new FarazSmsResponse();
        try {
            response = sslrestclient.callWebService(url, String.class);
            Matcher matcher = Pattern.compile("^\\[\"(\\d+)\",\"([\\w|\\s]+)\"\\]$").matcher(response);
            if (matcher.matches() && matcher.groupCount() == 2) {
                farazsmsresponse.setStatus(matcher.group(1));
                farazsmsresponse.setMessage(matcher.group(2));
            } else {
                throw new SendSmsException("The regex can not find pattern in faraz sms response: " + response);
            }
            if (farazsmsresponse.getStatus().equals("0")) logger.info("vrification code sent to " + phonenumber);
            else {
                logger.warn("vrification code faild to send " + phonenumber + "\tthe error is: " + response);
                throw new SendSmsException("vrification code faild to send " + response);
            }
            return;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        throw new SendSmsException("Error in calling Faraz Sms webservice");
    }
    /*@Override
    public FarazSmsResponse sendTocken(String phonenumber) throws CreateTokenException, PhoneNumberFormatException {
        String pn = userService.getCorrectFormatPhoneNo(phonenumber);
        UserTo user = userService.getUserByPhoneNo(pn);
        if (user == null) throw new CreateTokenException("user is not exist");
        if (user.getEnabled()) throw new CreateTokenException("user is activated");
        String token = userService.getVerficationTokenByPhoneNumber(pn);
        if (token != null) throw new CreateTokenException("token is exist and sms will not send");
        token = new DecimalFormat("000000").format(new Random().nextInt(999999));
        FarazSmsResponse smsresponse = sendSms("your vrification code is: " + token, user.getUsername());
        if (smsresponse.getStatus().equals("0")) {
            userService.saveVerificationToken(user, token);
            logger.info("tocken " + token + " sent to " + user.getUsername());
        } else {
            //Amir todo
            userService.saveVerificationToken(user, token);
            logger.debug("tocken " + token + " could not send to " + user.getUsername() + " " + smsresponse.getMessage());
        }
        return smsresponse;
    }

    @Override
    public FarazSmsResponse sendTokenForEditNumber(UserTo userTo, String newphonenumber) throws PhoneNumberFormatException, CreateTokenException {
        newphonenumber = userService.getCorrectFormatPhoneNo(newphonenumber);
        if (userService.getUserStatuseByPhoneNumber(newphonenumber) != UserStatuseByPhoneNumber.NOT_EXIST)
            throw new CreateTokenException("new phone number is exist");
        if (tokenForChangePhoneNumberService.existsByNewPhoneNumber(newphonenumber))
            throw new CreateTokenException("new phone number is exist");
        if (tokenForChangePhoneNumberService.existsByUser(userTo))
            throw new CreateTokenException("you already have a change phone number task");
        TokenForChangePhoneNumberTo tokenForChangePhoneNumber = new TokenForChangePhoneNumberTo();
        tokenForChangePhoneNumber.setToken(new DecimalFormat("000000").format(new Random().nextInt(999999)););
        FarazSmsResponse smsresponse = sendSms("your vrification code is: " + tokenForChangePhoneNumber.getToken(), userTo.getUsername());
        if (smsresponse.getStatus().equals("0")) {
            tokenForChangePhoneNumberService.saveVerificationToken(userTo, tokenForChangePhoneNumber);
            logger.info("tocken " + tokenForChangePhoneNumber.getToken() + " sent to " + userTo.getUsername());
        } else {
            //Amir todo
            tokenForChangePhoneNumberService.saveVerificationToken(userTo, tokenForChangePhoneNumber);
            logger.debug("tocken " + tokenForChangePhoneNumber.getToken() + " could not send to " + userTo.getUsername() + " " + smsresponse.getMessage());
        }
    }*/
}
