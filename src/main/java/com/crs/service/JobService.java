package com.crs.service;

import com.crs.model.Job;

import java.util.List;

public interface JobService {

    Job getJobBySubject(String subject);

    List<Job> getAllJobs();
//    Long getLargestId();
    void createJob(Job job);
    void updateJob(Job newJob, Long id);

    void deleteJob(Long id);
}
