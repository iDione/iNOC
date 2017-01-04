package twilio;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.issue.services.IssueAssignmentService;
import com.idione.inoc.models.Client;
import com.idione.inoc.models.Email;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.FilterPocUser;
import com.idione.inoc.models.Issue;
import com.idione.inoc.models.IssuePocUser;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.models.TelephoneCall;
import com.idione.inoc.telephone.services.TelephoneService;
import com.idione.inoc.test.AbstractIntegrationTest;
import com.idione.inoc.twilio.TwilioClient;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class TwilioClientTest extends AbstractIntegrationTest {

    TwilioClient twilioClient;

    @Before
    public void createTwilioClient() {
        twilioClient = new TwilioClient();
    }

    public void itMakesAnIssueAcceptanceCall() {

    }
}
