package com.crs_first.service;

import com.crs_first.model.Job;

import java.util.List;

public interface JobService {

    Job getJobById(Long id);

    List<Job> getAllJobs();
    Long getLargestId();
    void createJob(Job job);
    void updateJob(Job newJob, Long id);

    void deleteJob(Long id);
}
