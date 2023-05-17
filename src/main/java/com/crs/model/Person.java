package com.crs.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Person {
    private String name;
    private List<String> skills;
    private List<String> educationalFields;
}
