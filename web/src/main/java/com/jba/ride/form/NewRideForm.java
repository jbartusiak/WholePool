package com.jba.ride.form;

import lombok.Data;

@Data
public class NewRideForm {
    private String inputAddressFrom, inputAddressTo, fromLat, toLat,
            fromLong, toLong, inputDOD, inputHOD, inputDOA, inputHOA, inputDescription;
    private Integer inputAvailableSpots, inputTravelTime;
    private Double inputPrice;
}
