package com.jba.source;

import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Service
public class EmptyFetcher extends SingleSourceFetch {
    @Override
    public String getResultsForQuery(String from, String to) {
        throw new NotImplementedException();
    }

    @Override
    public void doImplementationSpecificInitialization() {
        throw new NotImplementedException();
    }

    @Override
    public void search(String from, String to, String dateOfDeparture, String dateOfArrival) {
        throw new NotImplementedException();
    }

    @Override
    public void parse(String input, String from, String to) {
        throw new NotImplementedException();
    }
}
