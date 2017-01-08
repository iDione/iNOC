package com.idione.inoc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.idione.inoc.models.TelephoneCall;
import com.idione.inoc.services.IssueResponseService;

@RequestMapping(value = "/twilio")
@RestController
public class TwilioController {

    private IssueResponseService issueResponseService;

    @Autowired
    public void setIssueResponseService(IssueResponseService issueResponseService) {
        this.issueResponseService = issueResponseService;
    }

    @RequestMapping(value = "/userResponse", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String updateUserResponse(@RequestParam final String Digits, @RequestParam final String CallSid) {
        issueResponseService.updateIssueWithUserResponse(Integer.parseInt(Digits), CallSid);
        return "ok";
    }

    @RequestMapping(value = "/callStatus", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String updateCallStatus(@RequestParam final String CallStatus, @RequestParam final String CallSid) {
        TelephoneCall.createOrUpdate(0, CallSid, CallStatus, true);
        return "ok";
    }
}
