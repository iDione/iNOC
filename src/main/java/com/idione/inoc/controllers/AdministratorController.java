package com.idione.inoc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.idione.inoc.models.Email;
import com.idione.inoc.models.Issue;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.services.InocProcessor;
import com.idione.inoc.services.IssueAssignmentService;

@RequestMapping(value = "/admin")
@Controller
public class AdministratorController extends ApplicationController {

    private InocProcessor inocProcessor;
    private IssueAssignmentService issueAssignmentService;
    @Autowired
    public void setInocProcessor(InocProcessor inocProcessor) {
        this.inocProcessor = inocProcessor;
    }

    @Autowired
    public void setIssueAssignmentService(IssueAssignmentService issueAssignmentService) {
        this.issueAssignmentService = issueAssignmentService;
    }

    @RequestMapping(value = "/run", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void runInoc() {
        inocProcessor.run();
    }

    @RequestMapping(value = "/sendUnassignedEmail", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void sendUnassignedEmail() {
        Issue issue = Issue.findById(19);
        Email email = Email.findById(47);
        email.setEmailText("this is the email text, type more if you want more heheheheh.");
        email.setEmailSubject("this is the email subject");
        issueAssignmentService.sendIssueUnassignedEmail(issue, email);
    }

    @RequestMapping(value = "/sendAssignedEmail", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void sendAssignedEmail() {
        Issue issue = Issue.findById(19);
        Email email = Email.findById(47);
        PocUser pocUser = PocUser.findById(1);
        email.setEmailText("this is the email text, type more if you want more heheheheh.");
        email.setEmailSubject("this is the email subject");
        issueAssignmentService.sendIssueAssignedEmail(issue, email, pocUser);
    }
}
