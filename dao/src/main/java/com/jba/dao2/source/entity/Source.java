package com.jba.dao2.source.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Source")
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PK_SOURCE_ID")
    private int sourceId;

    @Column(name="SOURCE_NAME")
    private String sourceName;

    @Column(name = "SOURCE_SEARCH_BASE_URL")
    private String searchBaseUrl;

    @Column(name = "SOURCE_RESULT_PARSE_RULES")
    private String resultsParseRules;

    public Source(int id){
        this.sourceId=id;
    }

    public Source(String sourceName, String searchBaseUrl, String resultsParseRules){
        this.sourceName=sourceName;
        this.searchBaseUrl=searchBaseUrl;
        this.resultsParseRules=resultsParseRules;
    }

    public static Source of(int id){
        Source source = new Source();
        source.setSourceId(id);
        return source;
    }
}
