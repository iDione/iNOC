package com.idione.inoc.tasks;

import org.javalite.activejdbc.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.idione.inoc.processors.InocIssueAssigner;

@Component
public class IssueAssignmentTask {

    private InocIssueAssigner inocIssueAssigner;

    @Autowired
    public void setInocIssueAssigner(InocIssueAssigner inocIssueAssigner) {
        this.inocIssueAssigner = inocIssueAssigner;
    }
    
    @Scheduled(fixedDelay = 60000)
    public void assignIssues() {
        try {
            Base.open();
            inocIssueAssigner.run();
        } finally {
            Base.close();
        }
    }
}
