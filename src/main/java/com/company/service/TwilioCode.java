package com.company.service;// Install the Java helper library from twilio.com/docs/java/install

import com.company.database.Database;
import com.company.util.FileJson;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.Random;

public class TwilioCode extends Thread{
    // Find your Account SID and Auth Token at twilio.com/console
    // and set the environment variables. See http://twil.io/secure
    public static final String ACCOUNT_SID = "AC3173ff0aee4ba690c3c54b5c8786b22c";
    public static final String AUTH_TOKEN = "eaa74a622be3e433bea4e851092d8a03";

    private String phoneNumber;
    private Long idStr;

    public TwilioCode(String phoneNumber, Long  idStr) {
        this.phoneNumber = phoneNumber;
        this.idStr = idStr;
    }

    public void run() {

        Integer code = new Random().nextInt() % 1000;
        System.out.println("code = " + code);
        Database.twilioCode.put(idStr,code);
        FileJson.writeTwilioCode(Database.twilioCode);

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new PhoneNumber(phoneNumber),
                        new PhoneNumber("+17738196261"),
                        String.valueOf(code))
                .create();



        System.out.println(message.getSid());
    }
}
