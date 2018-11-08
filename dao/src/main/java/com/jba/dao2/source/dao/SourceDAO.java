package com.jba.dao2.source.dao;

import com.jba.dao2.source.entity.Source;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@EnableTransactionManagement
public interface SourceDAO {

    @Transactional
    List<Source> getAllSources();

    @Transactional
    Source getSourceByName(String name);

    @Transactional
    void addSource(String sourceName, String searchBaseURL, String resultsParseRules);

    @Transactional
    void addSource(Source source);

    @Transactional
    Source editSource(Source source);

    @Transactional
    Source deleteSource(Source source);
    
}
