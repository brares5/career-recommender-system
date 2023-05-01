package com.crs.service;

import com.crs.model.Job;

import java.util.Optional;

public interface JobService {

    Job getJobById(Long id);
    Optional<Job> getJobByTitle(String title);
    void createJob(Job job);
    void updateJob(Job newJob, Long id);

    void deleteJob(Long id);
}
