package org.example;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class GetAllJena2 {
    public static void main(String[] args) {
        // Load the RDF file
        String source = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\tryjobs.rdf";
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
                String id = resourceURI.substring(resourceURI.indexOf("#") + 1);
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


                // Print the job information
                System.out.println("Title: " + title);
                System.out.println("Description: " + description);

                List<String> skills = new ArrayList<>();
                NodeIterator skillsIter = skillsNode.asResource().listProperties().toModel().listObjects();
                while (skillsIter.hasNext()) {
                    RDFNode node = skillsIter.next();
                    if (node.isLiteral()) {
                        skills.add(node.asLiteral().getString());
                    }
                }
                System.out.println("Skills: " + skills);

                List<String> educationalFields = new ArrayList<>();
                NodeIterator educationalFieldsIter = educationalFieldsNode.asResource().listProperties().toModel().listObjects();
                while (educationalFieldsIter.hasNext()) {
                    RDFNode node = educationalFieldsIter.next();
                    if (node.isLiteral()) {
                        educationalFields.add(node.asLiteral().getString());
                    }
                }
                System.out.println("Educational Fields: " + educationalFields);

                System.out.println("Experience: " + experience);

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
                System.out.println("Companies: " + companies);
                System.out.println();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
