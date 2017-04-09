package com.idione.inoc.tasks;

import org.javalite.activejdbc.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.idione.inoc.processors.InocIssueAssigner;
import com.idione.inoc.processors.InocIssueStatusMonitor;

@Component
public class IssueStatusTask {

    private InocIssueStatusMonitor inocIssueStatusMonitor;

    @Autowired
    public void setInocIssueStatusMonitor(InocIssueStatusMonitor inocIssueStatusMonitor) {
        this.inocIssueStatusMonitor = inocIssueStatusMonitor;
    }
    
    @Scheduled(fixedDelay = 60000)
    public void checkIssueStatus() {
        try {
            Base.open();
            inocIssueStatusMonitor.run();
        } finally {
            Base.close();
        }
    }
}
