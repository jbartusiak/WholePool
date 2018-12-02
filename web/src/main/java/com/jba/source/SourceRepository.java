package com.jba.source;

import com.jba.dao2.source.entity.Source;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Repository
public class SourceRepository {

    private Logger logger = Logger.getLogger(SourceRepository.class);

    @Value("${wholepool.rest.url.base.url}")
    protected String wholepoolRestBaseURL;

    @Autowired
    private BeanFactory factory;

    private List<SingleSourceFetch> sources;

    @Autowired
    private Deserializer deserializer;

    @Autowired
    public List<SingleSourceFetch> setSources(){
        String getSourcesQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                .addPathParam("source")
                .build();

        RestTemplate template = new RestTemplate();
        Source[] sourceList = deserializer.getResultArrayFor(template.getForObject(getSourcesQuery, String.class), Source[].class);

        List<SingleSourceFetch> result = new ArrayList<>();

        for (Source s: sourceList){
            if(!s.getSourceName().equals("localhost")) {
                SingleSourceFetch ssf;
                Properties properties = new Properties();
                try {
                    properties.load(new StringReader(s.getResultsParseRules()));

                    if (properties.getProperty("kind").equals("web"))
                        ssf = factory.getBean(SourceWebFetcher.class);
                    else if (properties.getProperty("kind").equals("rest"))
                        ssf = factory.getBean(SourceRestFetcher.class);
                    else
                        ssf = factory.getBean(SingleSourceFetch.class);
                    ssf.setDefinition(s);
                    result.add(ssf);
                } catch (IOException e) {
                    logger.error("Error parsing properties! " + e);
                }
            }
        }

        return result;
    }
}
