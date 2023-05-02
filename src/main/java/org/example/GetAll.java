package org.example;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.VCARD;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GetAll {
    public static void main(String[] args) {
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
            Resource job = model.createResource(nsJob + "3");
            job.addProperty(VCARD.TITLE, "Java Developer")
                    .addProperty(model.createProperty("http://example.org/job#description"), "Does code in Java");


            String[] skills = {"Taking decisions", "Information gathering"};
            Bag skillsBag = model.createBag();
            for (String skill : skills) {
                skillsBag.add(model.createTypedLiteral(skill));
            }
            job.addProperty(model.createProperty("http://example.org/job#skills"), skillsBag);

            String[] educationalFields = {"System analysis", "Basic programming", "Analysis and making decisions"};
            Seq fieldsSeq = model.createSeq();
            for (String field : educationalFields) {
                fieldsSeq.add(model.createTypedLiteral(field));
            }
            job.addProperty(model.createProperty("http://example.org/job#educational_fields"), fieldsSeq);


            job
                    .addProperty(model.createProperty(nsJob, "experience"), "4")
                    .addProperty(model.createProperty(nsJob, "company"), model.createResource(nsVcard + "Organization3")
                            .addProperty(VCARD.Orgname, "Google"))
                    .addProperty(model.createProperty(nsJob, "company"), model.createResource(nsVcard + "Organization3")
                            .addProperty(VCARD.Orgname, "Microsoft"))
                    .addProperty(model.createProperty(nsJob, "company"), model.createResource(nsVcard + "Organization3")
                            .addProperty(VCARD.Orgname, "Oracle"));
            out = new FileOutputStream(destination);
            model.write(out, "RDF/XML-ABBREV");
            out.close();
        } catch (IOException ex) {
            System.err.println("Error reading/writing RDF file: " + ex.getMessage());
        }



        // Define the SPARQL query
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                "PREFIX job: <http://example.org/job#>\n" +
                "SELECT ?subject ?title ?description ?skills ?educationalFields ?experience ?companyName\n" +
                "WHERE {\n" +
//                "  ?job rdf:type job:Job .\n" +
//                "  ?job job:id ?jobId .\n" +
//                "  ?description rdf:about ?subject .\n" +
//                "  BIND(STRAFTER(str(?subject), '#') AS ?numeric_id)\n" +
                "  ?job job:title ?title .\n" +
                "  ?job job:description ?description .\n" +
                "  ?job job:skills ?skills .\n" +
                "  ?job job:educational_fields ?educationalFields .\n" +
                "  ?job job:experience ?experience .\n" +
//                "  ?job job:company ?company .\n" +
//                "  ?company vcard:Orgname ?companyName .\n" +
                "}";


//        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
//                "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
//                "PREFIX job: <http://example.org/job#>\n" +
//                "SELECT ?jobid ?title ?description\n" +
//                "WHERE {\n" +
//                "  ?jobid rdf:type job:Job .\n" +
//                "  ?job job:title ?title .\n" +
//                "  ?job job:description ?description .\n" +
//                "  BIND(STRAFTER(STR(?job), '#') AS ?id)\n" +
//                "  FILTER(?id = '1')\n" +
//                "}";



//        String queryString = "PREFIX job: <http://example.org/job#>\n"
//                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
//                + "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n"
//                + "SELECT ?job ?title ?description ?skill ?eduField ?experience ?company\n"
//                + "WHERE {\n"
////                + "  ?job" + " rdf:type job:Job ;\n"
//                + " ?job "
//                + "      job:title ?title ;\n"
//                + "      job:description ?description ;\n"
//                + "      job:experience ?experience .\n"
//                + "}";


        // Execute the query and iterate over the results
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                System.out.println("test");
                QuerySolution soln = results.nextSolution();
//                String jobId = soln.getLiteral("jobId").getString();
                String title = soln.getLiteral("title").getString();
                String description = soln.getLiteral("description").getString();
                RDFNode skillsNode = soln.get("skills");
                RDFNode educationalFieldsNode = soln.get("educationalFields");
                int experience = soln.getLiteral("experience").getInt();
//                String companyName = soln.getLiteral("companyName").getString();
//                System.out.println(jobId);
                System.out.println(title);
                System.out.println(skillsNode);
                System.out.println(educationalFieldsNode);
//                System.out.println("Company " + companyName);
//                Job job = new Job(jobId, title, description, skillsNode, educationalFieldsNode, experience, companyName);
                // Do something with the Job object
            }
        }


    }
}
