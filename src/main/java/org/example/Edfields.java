package org.example;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.VCARD;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Edfields {
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


        String queryString = "PREFIX job: <http://example.org/job#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                "SELECT ?skill\n" +
                "WHERE {\n" +
                "  job:1 job:skills ?skills .\n" +
                "   ?skills rdf:_1|rdf:_2 ?skill .\n" +
                "}";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode field = soln.get("skill");
                System.out.println(field);
            }
        }


    }
}
