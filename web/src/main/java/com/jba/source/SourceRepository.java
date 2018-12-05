package com.jba.source;

import com.jba.dao2.source.entity.Source;
import com.jba.source.exception.MissingPropertiesException;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

@Repository
@EnableAsync
public class SourceRepository{

    private Logger logger = Logger.getLogger(SourceRepository.class);

    @Value("${wholepool.rest.url.base.url}")
    protected String wholepoolRestBaseURL;

    @Autowired
    private BeanFactory factory;

    private List<SingleSourceFetch> sources;

    @Autowired
    private Deserializer deserializer;

    @Autowired
    public void setSources(){
        String getSourcesQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                .addPathParam("source")
                .build();

        RestTemplate template = new RestTemplate();
        Source[] sourceList = deserializer.getResultArrayFor(template.getForObject(getSourcesQuery, String.class), Source[].class);

        List<SingleSourceFetch> result = new ArrayList<>();

        for (Source s: sourceList){
            switch (s.getSourceName()){
                case "dosiadam.pl":{
                    try {
                        DosiadamRestFetcher fetcher = factory.getBean(DosiadamRestFetcher.class);
                        fetcher.setDefinition(s);
                        result.add(fetcher);
                        break;
                    }
                    catch (MissingPropertiesException e){
                        break;
                    }
                }
                case "blablacar.pl":{
                    try{
                        BlaBlaCarWebFetcher fetcher = factory.getBean(BlaBlaCarWebFetcher.class);
                        fetcher.setDefinition(s);
                        result.add(fetcher);
                        break;
                    }
                    catch (MissingPropertiesException e){
                        break;
                    }
                }
                default:{
                    break;
                }
            }
        }

        sources= result;
    }

    @Async("sourceTaskExecutor")
    public void searchInSources(String from, String to, String dateOfDeparture, String dateOfArival){
        for(SingleSourceFetch s:sources){
            try {
                s.search(from, to, dateOfDeparture, dateOfArival);
                //s.saveToDB(from,to);
            }
            catch (NotImplementedException e){
                logger.error("Fetcher for "+s.definition.getSourceName()+" not implemented.");
            }
        }
    }
}
