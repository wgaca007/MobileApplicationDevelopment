package com.example.inclass13;

import java.io.Serializable;

public class City implements Serializable {
    String id, description,tripname;

    public City(String id, String description, String tripname) {
        this.id = id;
        this.description = description;
        this.tripname = tripname;
    }
}
