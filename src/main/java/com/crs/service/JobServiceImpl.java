package com.crs.service;

import com.crs.model.Job;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

//    String source = JobServiceImpl.class.getClassLoader().getResource("jobs.rdf").getPath();

//    static String source = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\tryjobs.rdf";
    static String destination = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\tryjobs.rdf";

    static ResourceLoader rs = new DefaultResourceLoader();
    static String source = rs.getResource("classpath:crs.owl").getFilename();


    @Override
    public Job getJobBySubject(String subject) {
        String jobTitle = null;
        String jobComment = null;
        List<String> skills = new ArrayList<>();
        List<String> educationalFields = new ArrayList<>();


        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        model.read(source);

        // Get the OWL classes
        OntClass jobClass = model.getOntClass("http://www.crs.com/ontologies/crs.owl#" + subject);

        if (jobClass != null) {
            // Extract the job title
            jobTitle = jobClass.getPropertyValue(model.getProperty("http://www.crs.com/ontologies/crs.owl#job_title")).toString();

            // Extract the job comment
            jobComment = jobClass.getPropertyValue(RDFS.comment).toString();

            // Print the extracted information
            System.out.println("Job Title: " + jobTitle);
            System.out.println("Job Comment: " + jobComment);
        }


        String sparqlQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX crs: <http://www.crs.com/ontologies/crs.owl#>" +
                "SELECT ?educationalField\n" +
                "WHERE {\n" +
                "  crs:" + subject + " owl:equivalentClass/owl:intersectionOf/rdf:rest*/rdf:first ?value .\n" +
                "  ?value owl:onProperty crs:hasEducationalField .\n" +
                "  ?value owl:someValuesFrom ?titles .\n" +
                "  ?titles crs:ed_field_name ?educationalField .\n" +
                "}";

        // Execute the SPARQL query
        Query query = QueryFactory.create(sparqlQuery);
        QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
        ResultSet results = queryExecution.execSelect();

        // Process the query results
        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            RDFNode educationalFieldNode = solution.get("educationalField");
            String educationalField = educationalFieldNode.toString();
            educationalFields.add(educationalField);
            System.out.println("Educational Field: " + educationalField);
        }

        // Close the query execution
        queryExecution.close();


        String sparqlSkills = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX crs: <http://www.crs.com/ontologies/crs.owl#>\n" +
                "\n" +
                "\n" +
                "SELECT ?skill\n" +
                "WHERE {\n" +
                "  crs:" + subject + " owl:equivalentClass/owl:intersectionOf/rdf:rest*/rdf:first ?value .\n" +
                "  ?value owl:onProperty crs:hasSkill .\n" +
                "  ?value owl:someValuesFrom ?titles .\n" +
                "  ?titles crs:skill_name ?skill .\n" +
                "}\n";

        // Execute the SPARQL query
        Query querySkills = QueryFactory.create(sparqlSkills);
        QueryExecution queryExecutionSkills = QueryExecutionFactory.create(querySkills, model);
        ResultSet resultsSkills = queryExecutionSkills.execSelect();

        // Process the query results
        while (resultsSkills.hasNext()) {
            QuerySolution solution = resultsSkills.nextSolution();
            RDFNode skillNode = solution.get("skill");
            String skill = skillNode.toString();
            skills.add(skill);
            System.out.println("Skill: " + skill);
        }

        // Close the query execution
        queryExecutionSkills.close();


        return new Job(subject, jobTitle, jobComment, skills, educationalFields);
    }

    @Override
    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();

        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        model.read(source);

        String allJobSubjectsSparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX crs: <http://www.crs.com/ontologies/crs.owl#>\n" +
                "SELECT ?title\n" +
                "WHERE {\n" +
                "  ?title crs:job_title ?job .\n" +
                "}\n" +
                "ORDER BY ?title\n";

        Query query = QueryFactory.create(allJobSubjectsSparql);
        QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
        ResultSet results = queryExecution.execSelect();

        // Process the query results
        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            RDFNode jobNode = solution.get("title");
            String jobURI = jobNode.toString();
            String jobTitle = model.createResource(jobURI).getLocalName();
            jobs.add(getJobBySubject(jobTitle));
            System.out.println("Job found: " + jobTitle);
        }

        queryExecution.close();


        return jobs;
    }

    @Override
    public void createJob(Job job) {

    }

    @Override
    public void updateJob(Job newJob, Long id) {

    }

    @Override
    public void deleteJob(Long id) {

    }
}
