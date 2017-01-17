package com.idione.inoc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idione.inoc.forms.FilterForm;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.FilterKeyword;
import com.idione.inoc.models.FilterPocUser;

@Service
public class FilterService {
    public List<Filter> getFilters(int clientId) {
        return Filter.find("client_id = ?", clientId);
    }

    public FilterForm getFilter(int id) {
        Filter filter = Filter.findFirst("id = ?", id);
        if (filter != null) {
            return new FilterForm(filter);
        } else {
            return new FilterForm();
        }

    }

    public Filter saveFilter(FilterForm filterForm) {
        if (filterForm.getId() > 0) {
            Filter filter = Filter.findFirst("id = ?", filterForm.getId());
            filter.set("name", filterForm.getName(),
                       "retries", filterForm.getRetries(),
                       "time_interval", filterForm.getTimeInterval(),
                       "mailing_group_id", filterForm.getMailingGroupId()).saveIt();
            deletePocUsers(filterForm.getId());
            createPocUsers(filterForm.getId(), filterForm.getPocUserIds());
            deleteKeywords(filterForm.getId());
            createKeywords(filterForm.getId(), filterForm.getKeywords());
            
            return filter;
        } else {
            Filter filter = Filter.createIt("name", filterForm.getName(),
                                            "client_id", filterForm.getClientId(),
                                            "retries", filterForm.getRetries(),
                                            "time_interval", filterForm.getTimeInterval(),
                                            "mailing_group_id", filterForm.getMailingGroupId());
            createPocUsers(filter.getInteger("id"), filterForm.getPocUserIds());
            createKeywords(filter.getInteger("id"), filterForm.getKeywords());
            return filter;
        }
    }
    
    private void deletePocUsers(int filterId) {
        FilterPocUser.delete("filter_id = ?", filterId);
    }

    private void deleteKeywords(int filterId) {
        FilterKeyword.delete("filter_id = ?", filterId);
    }
    
    private void createPocUsers(int filterId, List<Integer> pocUserIds) {
        for(Integer pocUserId : pocUserIds) {
            FilterPocUser.createIt("filter_id", filterId, "poc_user_id", pocUserId);
        }
    }
    
    private void createKeywords(int filterId, List<String> keywords) {
        for(String keyword : keywords) {
            FilterKeyword.createIt("filter_id", filterId, "keyword", keyword);
        }
    }
}
