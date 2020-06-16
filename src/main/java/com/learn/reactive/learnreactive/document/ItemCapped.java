package com.learn.reactive.learnreactive.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCapped {


    private String id;
    private String description;
    private Double price;
}
