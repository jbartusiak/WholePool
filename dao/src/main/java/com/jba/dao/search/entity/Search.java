package com.jba.dao.search.entity;

import com.jba.dao.route.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "search")
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_SEARCH_ID")
    private long searchId;

    @ManyToOne
    @JoinColumn(name = "FK_ROUTE_ID")
    @NonNull
    private Route route;

    @Column(name = "SEARCH_CRITERIA")
    private String searchCriteria;

    @Column(name = "ORDER_CRITERIA")
    private String orderCriteria;

}
