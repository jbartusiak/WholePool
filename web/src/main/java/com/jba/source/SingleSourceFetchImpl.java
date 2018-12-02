package com.jba.source;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class SingleSourceFetchImpl extends SingleSourceFetch {
    public SingleSourceFetchImpl() {
    }

    @Override
    public String getResultsForQuery(String from, String to) {
        return null;
    }
}
