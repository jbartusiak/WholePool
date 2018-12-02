package com.jba.source;

import com.jba.dao2.source.entity.Source;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository
@Configuration
public class SourceRepository {

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

            SingleSourceFetch ssf = factory.getBean(SingleSourceFetch.class);
            ssf.setDefinition(s);
            result.add(ssf);
        }

        return result;
    }
}
