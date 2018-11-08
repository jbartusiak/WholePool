package com.jba.service.entity;

import com.jba.dao2.route.entity.Route;
import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class SearchCriteria {

    @NonNull
    private Route route;

    private Date DOD;

    private Date DOA;
}
