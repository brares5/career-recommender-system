package com.crs.service;

import com.crs.model.Job;

import java.util.List;

public interface JobService {

    Job getJobBySubject(String subject);

    List<Job> getAllJobs();

}
