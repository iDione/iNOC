package com.idione.inoc.services;

import org.javalite.activejdbc.LazyList;
import org.springframework.stereotype.Service;

import com.idione.inoc.models.Filter;
import com.idione.inoc.models.FilterKeyword;

@Service
public class KeywordMatcher {
    public Filter emailMatchesFilter(String email, int clientId) {
        email = email.toLowerCase();
        LazyList<Filter> clientFilters = Filter.where("client_id = ?", clientId);
        for (Filter clientFilter : clientFilters) {
            LazyList<FilterKeyword> keywords = clientFilter.getAll(FilterKeyword.class);
            for (FilterKeyword keyword : keywords) {
                if (email.contains(keyword.getString("keyword").toLowerCase())) {
                    return clientFilter;
                }
            }
        }
        return null;
    }
}
