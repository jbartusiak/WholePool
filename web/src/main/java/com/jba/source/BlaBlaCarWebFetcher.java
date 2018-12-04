package com.jba.source;

import com.jaunt.JauntException;
import com.jaunt.UserAgent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlaBlaCarWebFetcher extends SingleSourceFetch {
    @Autowired
    UserAgent agent;

    @Override
    public String getResultsForQuery(String from, String to) {
        return null;
    }

    @Override
    public void doImplementationSpecificInitialization() {

    }

    @Override
    public void search(String from, String to, String dateOfDeparture, String dateOfArrival) {
        String searchString = searchBaseUrl+"";
        try {
            searchString = searchString.replace(DEPARTURE_PLACEHOLDER, URLEncoder.encode(from, StandardCharsets.UTF_8.toString()));
            searchString = searchString.replace(ARRIVAL_PLACEHOLDER, URLEncoder.encode(to, StandardCharsets.UTF_8.toString()));
            searchString = searchString.replace(DATE_OF_DEPARTURE_PLACEHOLDER, dateOfDeparture);
        }
        catch (Exception e){

        }
        try {
            agent.visit(searchString);
            parse(agent.doc.innerHTML(), from, to);
        }
        catch (JauntException e){
            e.printStackTrace();
        }
    }

    @Override
    public void parse(String input, String from, String to) {
        Document doc = Jsoup.parse(input);

        Elements elements= doc.getElementsByClass("tripsList");

        Elements ul = elements.get(0).getElementsByTag("ul");

        Elements li = ul.get(0).getElementsByTag("li");

        li.forEach(element ->{
            if(element.hasAttr("itemtype")){
                parseAsIndividual(element.toString(), from, to);
            }
        });

        System.out.println(li.toString());
    }

    public void parseAsIndividual(String input, String from, String to){
        Document doc = Jsoup.parse(input);

        Elements elements= doc.getElementsByTag("meta");
        String rideId="";
        String rideFrom="";
        String rideTo="";
        String startDate="";
        String endDate="";
        String price="";

        List<Element> elementsArray = elements.stream().collect(Collectors.toList());
        for(Element element: elementsArray){
            String attribute = element.attr("itemprop");
            switch (attribute){
                case "url":{
                    rideId=element.attr("content");
                    break;
                }
                case "name":{
                    String fromTo = element.attr("content");
                    fromTo = fromTo.substring(fromTo.lastIndexOf("/"));
                    String fromToArray[] = fromTo.split(" → ");
                    rideFrom=fromToArray[0];
                    rideTo=fromToArray[1];
                    break;
                }
                case "startDate":{
                    startDate = element.attr("content");
                    startDate = startDate.substring(startDate.lastIndexOf("/"));
                    break;
                }
                case "endDate":{
                    endDate = element.attr("content");
                    endDate = endDate.substring(endDate.lastIndexOf("/"));
                }
                default:{
                    break;
                }
            }
        }

        System.out.println(rideId+rideFrom+rideTo+startDate+endDate+price);

    }

    @Bean
    UserAgent getUserAgent(){
        return new UserAgent();
    }
}
