package com.idione.inoc.processors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.models.Issue;
import com.idione.inoc.services.IssueAssignmentService;
import com.idione.inoc.services.IssueService;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class InocIssueAssignerTest extends AbstractIntegrationTest {

    InocIssueAssigner inocProcessor;
    Issue issue1;
    Issue issue2;

    @Test
    public void itProcessesAllAssignableIssues(@Mocked IssueService issueService, @Mocked IssueAssignmentService issueAssignmentService) throws MessagingException, IOException {
        {
            InocIssueAssigner inocProcessor = new InocIssueAssigner(issueService, issueAssignmentService);
            issue1 = new Issue();
            issue2 = new Issue();
            final List<Issue> issues = new ArrayList<Issue>();
            issues.add(issue1);
            issues.add(issue2);
            new Expectations() {
                {
                    issueService.getAssignableIssues();
                    result = issues;
                }
            };

            inocProcessor.run();

            new Verifications() {
                {
                    issueService.getAssignableIssues();
                    times = 1;
                    issueAssignmentService.assignIssueToPOCUser(issue1);
                    times = 1;
                    issueAssignmentService.assignIssueToPOCUser(issue2);
                    times = 1;
                }
            };
        }
    }
}
