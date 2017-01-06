package com.idione.inoc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.idione.inoc.services.IssueResponseService;

@Controller
@RequestMapping(value = "/twilio")
public class TwilioController {

    private IssueResponseService issueResponseService;
    
    @Autowired
    public void setIssueResponseService(IssueResponseService issueResponseService) {
        this.issueResponseService = issueResponseService;
    }
    
    @RequestMapping(value = "/userResponse", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String getPocUserResponse(@RequestParam final String Digits, @RequestParam final String CallSid) {
        issueResponseService.updateIssueWithUserResponse(Integer.parseInt(Digits), CallSid);
        return "ok";
    }
}
