package twilio;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.models.Client;
import com.idione.inoc.models.Email;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.Issue;
import com.idione.inoc.models.IssuePocUser;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.models.TelephoneCall;
import com.idione.inoc.services.TelephoneService;
import com.idione.inoc.test.AbstractIntegrationTest;
import com.idione.inoc.twilio.TwilioClient;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.CallCreator;
import com.twilio.type.PhoneNumber;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class TwilioClientTest extends AbstractIntegrationTest {

     private static final String TEST_ACCOUNT_SID = "AC2e9bfe4a8333addc3732731b067cb883";
     private static final String TEST_AUTH_TOKEN = "41b3e764ada99491781fbedcdb7c1a5a";
     private static final String TEST_FROM_PHONE_NUMBER = "+15005550006";
    
    TwilioClient twilioClient;
    
    IssuePocUser issuePocUser;
    String telephoneNumber = "1111111111";
    PhoneNumber to;
    PhoneNumber from;
    URI uri;
    
    @Before
    public void createFilter() {
        to = new PhoneNumber(telephoneNumber);
        from = new PhoneNumber(TwilioClient.FROM_PHONE_NUMBER);
        uri = URI.create(TwilioClient.ISSUE_URL);
        
        Client client = Client.createIt("name", "Mickey Mouse Club House");
        Email email = Email.createIt("client_id", client.getInteger("id"));
        MailingGroup mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"));
        Filter filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", 2, "mailing_group_id", mailingGroup.getInteger("id"));
        Issue issue = Issue.createIt("email_id", email.getInteger("id"), "filter_id", filter.getInteger("id"));
        PocUser pocUser = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Mickey", "last_name", "Mouse", "phone_number", telephoneNumber);
        issuePocUser = new IssuePocUser();
        issuePocUser.set("issue_id", issue.getId());
        issuePocUser.set("poc_user_id", pocUser.getId());
        issuePocUser.set("user_response", "none");
        issuePocUser.saveIt();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void itCreatesATelephoneCallRecord(@Mocked CallCreator callCreator, @Mocked Call call) {
        int beforeTelephoneCount = TelephoneCall.findAll().size();
        new Expectations() {
            {
                new CallCreator(to, from, uri); result = callCreator;
                callCreator.setStatusCallbackEvent((List<String>) any);
                callCreator.setStatusCallback(anyString);
                callCreator.create((TwilioRestClient) any); result = call;
                call.getStatus(); result = Call.Status.COMPLETED;
                call.getSid(); result = "ac319ghnsl0";
            }
        };
        twilioClient = new TwilioClient();
        twilioClient.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), telephoneNumber);
        int afterTelephoneCount = TelephoneCall.findAll().size();
        assertThat(afterTelephoneCount, is(equalTo(beforeTelephoneCount + 1)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void itUpdatesTelephoneCallRecordWithFinalStatus(@Mocked CallCreator callCreator, @Mocked Call call) {
        new Expectations() {
            {
                new CallCreator(to, from, uri); result = callCreator;
                callCreator.setStatusCallbackEvent((List<String>) any);
                callCreator.setStatusCallback(anyString);
                callCreator.create((TwilioRestClient) any); result = call;
                call.getStatus(); result = Call.Status.FAILED;
                call.getSid(); result = "ac319ghnsl0";
            }
        };
        twilioClient = new TwilioClient();
        twilioClient.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), telephoneNumber);
        TelephoneCall telephoneCall = TelephoneCall.findFirst("external_call_id = ?", "ac319ghnsl0");
        assertThat(telephoneCall.get("call_status"), is(equalTo(Call.Status.FAILED.toString())));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void itSetStatusUpdatesForCallCompletion(@Mocked CallCreator callCreator, @Mocked Call call) {
        new Expectations() {
            {
                new CallCreator(to, from, uri); result = callCreator;
                callCreator.setStatusCallbackEvent((List<String>) any);
                callCreator.setStatusCallback(anyString);
                callCreator.create((TwilioRestClient) any); result = call;
                call.getStatus(); result = Call.Status.COMPLETED;
                call.getSid(); result = "ac319ghnsl0";
            }
        };
        twilioClient = new TwilioClient();
        twilioClient.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), telephoneNumber);
        
        new Verifications() {
            {
                callCreator.setStatusCallbackEvent(TelephoneService.callCompletedStatuses); times = 1;
                callCreator.setStatusCallback(TwilioClient.ISSUE_CALL_STATUS_CALLBACK_URL); times = 1;
                
            }
        };
    }
}
