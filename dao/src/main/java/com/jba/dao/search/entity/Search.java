package com.jba.dao.search.entity;

import com.jba.dao.route.entity.Route;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "Search")
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
    @NonNull
    private String searchCriteria;

    @Column(name = "ORDER_CRITERIA")
    @NonNull
    private String orderCriteria;

}
