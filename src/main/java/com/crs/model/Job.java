package com.crs.model;


import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Job {
    private String subject;
    private String title;
    private String comment;
    private List<String> skills;
    private List<String> educationalFields;
//    private int experience;
//    private List<String> companies;


}
