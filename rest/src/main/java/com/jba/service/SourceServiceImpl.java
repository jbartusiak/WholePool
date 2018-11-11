package com.jba.service;

import com.jba.dao2.source.dao.SourceDAO;
import com.jba.dao2.source.entity.Source;
import com.jba.service.ifs.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceDAO sourceDAO;

    @Override
    public List<Source> getAllSources() {
        return sourceDAO.getAllSources();
    }

    @Override
    public Source getSourceByName(String name) {
        return sourceDAO.getSourceByName(name);
    }

    @Override
    public Source addSource(String sourceName, String searchBaseURL, String resultsParseRules) {
        sourceDAO.addSource(sourceName, searchBaseURL, resultsParseRules);

        return getSourceByName(sourceName);
    }

    @Override
    public Source addSource(Source source) {
        sourceDAO.addSource(source);

        return getSourceByName(source.getSourceName());
    }

    @Override
    public Source editSource(Source source) {
        return sourceDAO.editSource(source);
    }

    @Override
    public Source deleteSource(Source source) {
        return sourceDAO.deleteSource(source);
    }
}
