package com.idione.inoc.tasks;

import org.javalite.activejdbc.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.idione.inoc.services.InocIssueCreator;

@Component
public class IssueCreationTask {

    private InocIssueCreator inocIssueCreator;

    @Autowired
    public void setInocIssueCreator(InocIssueCreator inocIssueCreator) {
        this.inocIssueCreator = inocIssueCreator;
    }
    
    @Scheduled(fixedDelay = 60000)
    public void createIssuesForClients() {
        try {
            Base.open();
            inocIssueCreator.run();
        } finally {
            Base.close();
        }
    }
}
