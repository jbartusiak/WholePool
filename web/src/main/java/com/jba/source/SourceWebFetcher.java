package com.jba.source;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class SourceWebFetcher extends SingleSourceFetch {
    @Override
    public String getResultsForQuery(String from, String to) {
        return null;
    }

    @Override
    public String search(String from, String to, String dateOfDeparture, String dateOfArrival) {
        return null;
    }
}
