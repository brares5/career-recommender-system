package org.example;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class GetAllJena {

    public static void main(String[] args) {
        // Load the RDF file
        String source = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\tryjobs.rdf";
        String destination = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\blabla.rdf";
        String nsJob = "http://example.org/job#";
        String nsVcard = "http://www.w3.org/2001/vcard-rdf/3.0#";
        Model model = ModelFactory.createDefaultModel();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = RDFDataMgr.open(source);
            model.read(in, null);
//            model.write(System.out);
            in.close();

            // Create a resource for the job
            Resource jobResource = model.getResource("http://example.org/job#1");

            // Get the properties of the job
            Property titleProperty = model.getProperty("http://example.org/job#title");
            Property descriptionProperty = model.getProperty("http://example.org/job#description");
            Property skillsProperty = model.getProperty("http://example.org/job#skills");
            Property educationalFieldsProperty = model.getProperty("http://example.org/job#educational_fields");
            Property experienceProperty = model.getProperty("http://example.org/job#experience");
//            Property companyProperty = model.getProperty("http://example.org/job#company");

            // Get the values of the properties for the job
            String title = jobResource.getProperty(titleProperty).getObject().toString();
            String description = jobResource.getProperty(descriptionProperty).getObject().toString();
            RDFNode skillsNode = jobResource.getProperty(skillsProperty).getObject();
            RDFNode educationalFieldsNode = jobResource.getProperty(educationalFieldsProperty).getObject();
            String experience = jobResource.getProperty(experienceProperty).getObject().toString();

            // Print the job information
            System.out.println("Title: " + title);
            System.out.println("Description: " + description);
            List<String> skills = new ArrayList<>();
            List<String> educationalFields = new ArrayList<>();
            NodeIterator skillsIter = skillsNode.asResource().listProperties().toModel().listObjects();
            NodeIterator educationalFieldsIter = educationalFieldsNode.asResource().listProperties().toModel().listObjects();

            while (skillsIter.hasNext()) {
                RDFNode node = skillsIter.next();
                if(node.isLiteral()) {
                    skills.add(node.asLiteral().getString());
                }
            }
            System.out.println(skills);

            while (educationalFieldsIter.hasNext()) {
                RDFNode node = educationalFieldsIter.next();
                if (node.isLiteral()) {
                    educationalFields.add(node.asLiteral().getString());
                }
            }
            System.out.println(educationalFields);

            System.out.println("Experience: " + experience);


            // Get the company nodes
            List<String> companies = new ArrayList<>();



            // Get the resource with the given URI
            Resource companyResource = model.getResource("http://www.w3.org/2001/vcard-rdf/3.0#Organization1");

            // Get the vcard:Orgname properties of the resource
            StmtIterator orgnameIter = companyResource.listProperties(model.getProperty("http://www.w3.org/2001/vcard-rdf/3.0#Orgname"));

            // Iterate over the properties and print their values
            while (orgnameIter.hasNext()) {
                Statement stmt = orgnameIter.next();
                companies.add(stmt.getString());
            }
            System.out.println(companies);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
