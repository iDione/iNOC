package com.idione.inoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.idione.inoc.models.TelephoneCall;

@SpringBootApplication
public class InocApplication {

	public static void main(String[] args) {
		SpringApplication.run(InocApplication.class, args);
	}

    @RequestMapping(value = "/receiveTwilioStatus", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String receiveTwilioStatus(@RequestParam final String CallStatus, @RequestParam final String CallSid) {
    	TelephoneCall.createOrUpdate(0, CallSid, CallStatus, true);
        return "ok";
    }

    @RequestMapping(value = "/receiveTwilioResponse", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String callTwilioCustom(@RequestParam final String Digits, @RequestParam final String CallSid) {
    	TelephoneCall telephoneCall = TelephoneCall.findFirst("external_call_id = ?", CallSid);
    	telephoneCall.set("user_response", Integer.parseInt(Digits));
    	telephoneCall.saveIt();
    	// update issue poc user
        return "ok";
    }
}
