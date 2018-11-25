package com.jba.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.jba.dao2.entity.WPLResponse;
import com.jba.dao2.user.enitity.User;
import lombok.experimental.UtilityClass;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Deserializer {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    ObjectMapper mapper;

    public <T> T getSingleItemFor(String response, Class type){
        try {
            JsonNode node = mapper.readTree(response);
            return (T)mapper.readValue(node.get("result").toString(), type);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public <T> T[] getResultArrayFor(String response, Class type){
        try {
            JsonNode node = mapper.readTree(response);

            JsonNode resultNode = node.get("result");
            String result = mapper.writeValueAsString(resultNode);

            T[] items = (T[])mapper.readValue(result, type);
            return items;
        }
        catch (MismatchedInputException e){
            logger.info("No entries found, returning null");
            return null;
        }
        catch (Exception e){
            logger.error("Error occured while trying to parse input "+response, e);
            return null;
        }
    }
}
