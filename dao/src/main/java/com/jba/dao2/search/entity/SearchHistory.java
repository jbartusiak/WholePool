package com.jba.dao2.search.entity;

import com.jba.dao2.user.enitity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SearchHistory")
public class SearchHistory implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_HISTORY_USER_ID")
    private User searchOwner;

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_HISTORY_SEARCH_ID")
    private Search usersSearch;

}
