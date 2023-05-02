package com.crs.service;

import com.crs.model.Job;

import java.util.List;
import java.util.Optional;

public interface JobService {

    Job getJobById(Long id);

    List<Job> getAllJobs();
    Long getLargestId();
    void createJob(Job job);
    void updateJob(Job newJob, Long id);

    void deleteJob(Long id);
}
