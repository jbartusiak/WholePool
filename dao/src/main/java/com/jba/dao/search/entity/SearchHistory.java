package com.jba.dao.search.entity;

import com.jba.dao.user.enitity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "searchhistory")
public class SearchHistory {

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_HISTORY_USER_ID")
    private User searchOwner;

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_HISTORY_SEARCH_ID")
    private Search usersSearch;
    
}
