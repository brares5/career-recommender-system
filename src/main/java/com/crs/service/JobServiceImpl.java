package com.crs.service;

import com.crs.model.Job;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;
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

    static String source = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\tryjobs.rdf";
    static String destination = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\tryjobs.rdf";


    @Override
    public Job getJobById(Long id) {
//        model.read("jobs.rdf");

        Job job = new Job();
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

            if (!results.hasNext()) {
                throw new RuntimeException("Job with ID " + id + " not found");
            }

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
    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();

        String nsJob = "http://example.org/job#";
        String nsVcard = "http://www.w3.org/2001/vcard-rdf/3.0#";
        Model model = ModelFactory.createDefaultModel();
        InputStream in = null;
        try {
            in = RDFDataMgr.open(source);
            model.read(in, null);
            in.close();

            // Get all resources with the rdf:type of job
            ResIterator jobResources = model.listResourcesWithProperty(RDF.type, model.getResource(nsJob + "Job"));

            while (jobResources.hasNext()) {
                Resource jobResource = jobResources.next();

                // Get the object's id
                String resourceURI = jobResource.getURI();
                long id = -1;
                String idString = resourceURI.substring(resourceURI.indexOf("#") + 1);
                try {
                    id = Long.parseLong(idString);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid resource ID: " + idString);
                }
                System.out.println("Id: " + id);


                // Get the properties of the job
                Property titleProperty = model.getProperty(nsJob + "title");
                Property descriptionProperty = model.getProperty(nsJob + "description");
                Property skillsProperty = model.getProperty(nsJob + "skills");
                Property educationalFieldsProperty = model.getProperty(nsJob + "educational_fields");
                Property experienceProperty = model.getProperty(nsJob + "experience");
                Property companyProperty = model.getProperty(nsJob + "company");

                // Get the values of the properties for the job
                String title = jobResource.getProperty(titleProperty).getObject().toString();
                String description = jobResource.getProperty(descriptionProperty).getObject().toString();
                RDFNode skillsNode = jobResource.getProperty(skillsProperty).getObject();
                RDFNode educationalFieldsNode = jobResource.getProperty(educationalFieldsProperty).getObject();
                int experience = Integer.parseInt(jobResource.getProperty(experienceProperty).getObject().toString());


                List<String> skills = new ArrayList<>();
                NodeIterator skillsIter = skillsNode.asResource().listProperties().toModel().listObjects();
                while (skillsIter.hasNext()) {
                    RDFNode node = skillsIter.next();
                    if (node.isLiteral()) {
                        skills.add(node.asLiteral().getString());
                    }
                }

                List<String> educationalFields = new ArrayList<>();
                NodeIterator educationalFieldsIter = educationalFieldsNode.asResource().listProperties().toModel().listObjects();
                while (educationalFieldsIter.hasNext()) {
                    RDFNode node = educationalFieldsIter.next();
                    if (node.isLiteral()) {
                        educationalFields.add(node.asLiteral().getString());
                    }
                }


                // Get the company nodes
                List<String> companies = new ArrayList<>();
                StmtIterator companyIter = jobResource.listProperties(companyProperty);
                while (companyIter.hasNext()) {
                    Statement stmt = companyIter.next();
                    Resource companyResource = stmt.getResource();
                    StmtIterator orgnameIter = companyResource.listProperties(model.getProperty(nsVcard + "Orgname"));
                    while (orgnameIter.hasNext()) {
                        Statement orgnameStmt = orgnameIter.next();
                        companies.add(orgnameStmt.getString());
                    }
                }

                jobs.add(new Job(id, title, description, skills, educationalFields, experience, companies));
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jobs;
    }

    @Override
    public Long getLargestId() {
        Model model = ModelFactory.createDefaultModel();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = RDFDataMgr.open(source);
            model.read(in, null);
            model.write(System.out);
            in.close();
            String queryString = "PREFIX job: <http://example.org/job#> SELECT ?jobId WHERE {?jobId a job:Job .}";

            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet results = qe.execSelect();

            // iterate over the results and find the largest job id
            long largestJobId = 0L;
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String jobId = solution.get("jobId").toString();
                long jobIdNum = Integer.parseInt(jobId.split("#")[1]);
                if (jobIdNum > largestJobId) {
                    largestJobId = jobIdNum;
                }
            }

            return largestJobId;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void createJob(Job jobToCreate) {
        String nsJob = "http://example.org/job#";
        String nsVcard = "http://www.w3.org/2001/vcard-rdf/3.0#";
        String skillsNS = "http://example.org/skills#";
        String eduFieldsNS = "http://example.org/edufields#";


        Model model = ModelFactory.createDefaultModel();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = RDFDataMgr.open(source);
            model.read(in, null);
            model.write(System.out);
            in.close();
            long newJobId = getLargestId() + 1;
            Resource job = model.createResource(nsJob + newJobId);
            job.addProperty(model.createProperty("http://example.org/job#title"), jobToCreate.getTitle())
                    .addProperty(model.createProperty("http://example.org/job#description"), jobToCreate.getDescription());


            Resource skillsResource = model.createResource(skillsNS + "3", RDF.Bag);
            Bag skillsBag = model.createBag(String.valueOf(skillsResource));
            for (String skill : jobToCreate.getSkills()) {
                skillsBag.add(model.createTypedLiteral(skill));
            }
            job.addProperty(model.createProperty("http://example.org/job#skills"), skillsBag);

            Resource eduFieldsResource = model.createResource(eduFieldsNS + "3", RDF.Bag);
            Bag fieldsBag = model.createBag(String.valueOf(eduFieldsResource));
            for (String field : jobToCreate.getEducationalFields()) {
                fieldsBag.add(model.createTypedLiteral(field));
            }
            job.addProperty(model.createProperty("http://example.org/job#educational_fields"), fieldsBag);
            job.addProperty(RDF.type, model.createResource(nsJob + "Job"));


            job.addProperty(model.createProperty(nsJob, "experience"), "4");


            for (String company: jobToCreate.getCompanies()) {
                job.addProperty(model.createProperty(nsJob, "company"),
                        model.createResource(nsVcard + "Organization" + newJobId).addProperty(VCARD.Orgname, company));
            }

            out = new FileOutputStream(destination);
            model.write(out, "RDF/XML-ABBREV");
            out.close();
        } catch (IOException ex) {
            System.err.println("Error reading/writing RDF file: " + ex.getMessage());
        }
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

        Model model = ModelFactory.createDefaultModel();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = RDFDataMgr.open(source);
            model.read(in, null);
            model.write(System.out);
            in.close();

            out = new FileOutputStream(destination);
            Resource jobToDelete = model.getResource("http://example.org/job#" + id);
            model.removeAll(null, null, jobToDelete);
            model.removeAll(jobToDelete, null, null);

            model.remove(model.listStatements(jobToDelete, null, (RDFNode) null));
            model.remove(model.listStatements(null, null, (RDFNode) jobToDelete));



            String skillsURI = "http://example.org/skills#" + id;
            Resource skills = model.getResource(skillsURI);
            model.removeAll(skills, null, null);
            model.removeAll(null, null, skills);


            String eduFieldsURI = "http://example.org/edufields#" + id;
            Resource eduFields = model.getResource(eduFieldsURI);
            model.removeAll(eduFields, null, null);
            model.removeAll(null, null, eduFields);

            String organizationURI = "http://www.w3.org/2001/vcard-rdf/3.0#Organization" + id;
            Resource organization = model.getResource(organizationURI);
            model.removeAll(organization, null, null);
            model.removeAll(null, null, organization);

            model.write(out, "RDF/XML-ABBREV");
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
