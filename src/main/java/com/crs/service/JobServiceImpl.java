package com.crs.service;

import com.crs.model.Job;
import com.crs.repository.JobRepository;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {
//    JobRepository jobRepository;
//
//    public JobServiceImpl(JobRepository jobRepository) {
//        this.jobRepository = jobRepository;
//    }

//    String source = JobServiceImpl.class.getClassLoader().getResource("jobs.rdf").getPath();



    @Override
    public Job getJobById(Long id) {
//        model.read("jobs.rdf");

        Job job = new Job();
        String source = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\tryjobs.rdf";
        Model model = ModelFactory.createDefaultModel();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = RDFDataMgr.open(source);
            model.read(in, null);
//            model.write(System.out);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String queryString = "PREFIX job: <http://example.org/job#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n"
                + "SELECT ?title ?description ?skill ?eduField ?experience ?company\n"
                + "WHERE {\n"
                + "  job:" + id + " job:title ?title ;\n"
                + "      job:description ?description ;\n"
                + "      job:skills ?skills ;\n"
                + "      job:experience ?experience .\n"

                + "}";


        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String title = soln.getLiteral("title").toString();
                String description = soln.get("description").toString();
                int experience = soln.getLiteral("experience").getInt();
                job.setTitle(title);
                job.setDescription(description);
                job.setExperience(experience);
                System.out.println("Title: " + title);
                System.out.println("Description: " + description);
                System.out.println("Experience: " + experience);
            }
        }

        queryString = "PREFIX job: <http://example.org/job#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                "SELECT ?skill\n" +
                "WHERE {\n" +
                "  job:" + id + " job:skills ?skills .\n" +
                "   ?skills rdf:_1|rdf:_2 ?skill .\n" +
                "}";

        Query querySkills = QueryFactory.create(queryString);
        List<String> skills = new ArrayList<>();
        try (QueryExecution qexecSkills = QueryExecutionFactory.create(querySkills, model)) {
            ResultSet results = qexecSkills.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode field = soln.get("skill");
                skills.add(field.toString());
            }
        }
        System.out.println("Skills: " + skills);
        job.setSkills(skills);


        // Query for educational fields
        String eduFieldQueryString = "PREFIX job: <http://example.org/job#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "SELECT ?eduField\n" +
                "WHERE {\n" +
                "  job:" + id + " job:educational_fields ?eduFields .\n" +
                "   ?eduFields rdf:_1|rdf:_2|rdf:_3 ?eduField .\n" +
                "}";
        List<String> eduFields = new ArrayList<>();

        Query eduFieldQuery = QueryFactory.create(eduFieldQueryString);
        try (QueryExecution qexecEdu = QueryExecutionFactory.create(eduFieldQuery, model)) {
            ResultSet results = qexecEdu.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode eduField = soln.get("eduField");
                eduFields.add(eduField.toString());
            }
        }

        // Query for companies
        String companyQueryString = "PREFIX job: <http://example.org/job#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                "SELECT ?company\n" +
                "WHERE {\n" +
                "  job:" + id + " job:company ?companyNode .\n" +
                "   ?companyNode vcard:Orgname ?company .\n" +
                "}";
        List<String> companies = new ArrayList<>();

        Query companyQuery = QueryFactory.create(companyQueryString);
        try (QueryExecution qexecCompanies = QueryExecutionFactory.create(companyQuery, model)) {
            ResultSet results = qexecCompanies.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode company = soln.get("company");
                companies.add(company.toString());
            }
        }

        job.setSkills(skills);
        job.setEducationalFields(eduFields);
        job.setCompanies(companies);
        return job;
    }

    @Override
    public Optional<Job> getJobByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public void createJob(Job job) {
//        if (!job.getSkills().isEmpty() && !job.getCompanies().isEmpty() && job.getTitle() != null) {
//            jobRepository.save(job);
//        }
    }

    @Override
    public void updateJob(Job newJob, Long id) {
//        Job jobToUpdate = jobRepository.findById(id)
//        .orElseThrow(() -> new RuntimeException("Exception in update, id not found"));
//        jobToUpdate.setTitle(newJob.getTitle());
//        jobToUpdate.setDescription(newJob.getDescription());
//        jobToUpdate.setSkills(newJob.getSkills());
//        jobToUpdate.setEducationalFields(newJob.getEducationalFields());
//        jobToUpdate.setExperience(newJob.getExperience());
//        jobToUpdate.setCompanies(newJob.getCompanies());
//        jobRepository.save(jobToUpdate);
    }

    @Override
    public void deleteJob(Long id) {
//        jobRepository.deleteById(id);
    }
}
