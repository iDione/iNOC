package com.idione.inoc.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.models.Client;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.FilterKeyword;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class KeywordMatcherTest extends AbstractIntegrationTest {

    KeywordMatcher keywordMatcher;
    Client client;
    String emailText = "The quick brown fox jumps over the lazy dog";
    Filter filter1;
    FilterKeyword filter1Keyword1;
    FilterKeyword filter1Keyword2;
    
    @Before
    public void createFilter() {
        client = Client.createIt("name", "Mickey Mouse Club House");
        filter1 = Filter.createIt("name", "Fun Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", 1, "mailing_group_id", 1);
        filter1Keyword1 = FilterKeyword.createIt("filter_id", filter1.getInteger("id"), "keyword", "goofy");
        filter1Keyword2 = FilterKeyword.createIt("filter_id", filter1.getInteger("id"), "keyword", "lazy");
    }

    @Test
    public void itReturnsTheFilterThatMatchesTheEmailForAClient() throws Exception {
        keywordMatcher = new KeywordMatcher();
        Filter machingFilter = keywordMatcher.emailMatchesFilter(emailText, client.getInteger("id"));
        assertThat(machingFilter.getInteger("id"), is(equalTo(filter1.getInteger("id"))));
    }
    
    @Test
    public void itReturnsNullIfNoFilterMatchesTheEmailForAClient() throws Exception {
        keywordMatcher = new KeywordMatcher();
        filter1Keyword2.set("keyword", "pluto").saveIt();
        Filter machingFilter = keywordMatcher.emailMatchesFilter(emailText, client.getInteger("id"));
        assertThat(machingFilter, is(equalTo(null)));
    }
    
    @Test
    public void itMatchesKeywordWithDifferentCase() throws Exception {
        keywordMatcher = new KeywordMatcher();
        filter1Keyword2.set("keyword", "LAZY").saveIt();
        Filter machingFilter = keywordMatcher.emailMatchesFilter(emailText, client.getInteger("id"));
        assertThat(machingFilter.getInteger("id"), is(equalTo(filter1.getInteger("id"))));
    }
    
    @Test
    public void itReturnsTheNextFilterThatMatchesTheEmailForAClient() throws Exception {
        keywordMatcher = new KeywordMatcher();
        filter1Keyword2.set("keyword", "pluto").saveIt();
        Filter filter2 = Filter.createIt("name", "Fun Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", 1, "mailing_group_id", 1);
        FilterKeyword.createIt("filter_id", filter2.getInteger("id"), "keyword", "dog");
        
        Filter machingFilter = keywordMatcher.emailMatchesFilter(emailText, client.getInteger("id"));
        assertThat(machingFilter.getInteger("id"), is(equalTo(filter2.getInteger("id"))));
    }
}
