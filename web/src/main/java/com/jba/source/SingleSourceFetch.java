package com.jba.source;

import com.jba.dao2.source.entity.Source;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * Collects data from external sources, and parses them as needed.
 */
@Service
@EnableAsync
public abstract class SingleSourceFetch {

    @Value("${wholepool.rest.url.base.url}")
    protected String wholepoolRestBaseURL;

    protected boolean debug;

    protected String exampleValue;

    protected Source definition;
    protected HttpMethod method;

    public SingleSourceFetch(){
    }

    public abstract String getResultsForQuery(String from, String to);
    public void setDefinition(Source source){
        this.definition=source;
    }
}
