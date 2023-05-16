package com.crs.model;


import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Job {
    private long id;
    private String title;
    private String comment;
    private List<String> skills;
    private List<String> educationalFields;
//    private int experience;
//    private List<String> companies;


}
