package com.jba.service.ifs;

import com.jba.dao2.source.entity.Source;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SourceService {
    List<Source> getAllSources();
    Source getSourceByName(String name);
    Source addSource(String sourceName, String searchBaseURL, String resultsParseRules);
    Source addSource(Source source);
    Source editSource(Source source);
    Source deleteSource(Source source);
}
