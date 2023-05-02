package org.example;

//import org.apache.jena.base.Sys;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.vocabulary.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RDFExample {

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
            model.write(System.out);
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
            job.addProperty(RDF.type, model.createResource(nsJob + "Job"));


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

//        try {
//            out = new FileOutputStream(destination);
//            Resource job2 = model.getResource("http://example.org/job#2");
//            model.removeAll(null, null, job2);
//            model.removeAll(job2, null, null);
//
//            model.remove(model.listStatements(job2, null, (RDFNode) null));
//            model.remove(model.listStatements(null, null, (RDFNode) job2));
//            model.write(System.out);
//            model.write(out, "RDF/XML-ABBREV");
//            out.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        try {
//            out = new FileOutputStream(destination);
//            Resource job2 = model.getResource("http://example.org/job#2");
//
//            // Remove all statements related to job#2
//            StmtIterator iter = model.listStatements(job2, null, (RDFNode)null);
//            List<Statement> stmts = iter.toList();
//            model.remove(stmts);
//            iter = model.listStatements(null, null, job2);
//            stmts = iter.toList();
//            model.remove(stmts);
//            model.removeAll(job2, null, (RDFNode)null);
//            model.remove(job2, model.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#li"), (RDFNode) null);
//
//            model.write(System.out);
//            model.write(out, "RDF/XML-ABBREV");
//            out.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


//        try {
//            out = new FileOutputStream(destination);
////            String jobUri = "http://example.org/job#2";
//            String jobUri = "http://example.org/job#2";
//            String sparql = "PREFIX job: <http://example.org/job#>\n"
//                    + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
//                    + "DELETE {\n" +
//                    "  ?s ?p ?o .\n" +
//                    "}\n" +
//                    "WHERE {\n" +
//                    "  ?s ?p ?o .\n" +
//                    "  FILTER ( ?s = job:Job && ?s = <http://example.org/job#2> )\n" +
//                    "}";
//            UpdateAction.parseExecute(sparql, model);
//            model.write(System.out);
//            model.write(out, "RDF/XML-ABBREV");
//            out.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


//        String id = "1";
//
//        String queryString = "PREFIX job: <http://example.org/job#>\n"
//                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
//                + "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n"
//                + "SELECT ?title ?description ?skill ?eduField ?experience ?company\n"
//                + "WHERE {\n"
//                + "  job:" + id + " job:title ?title ;\n"
//                + "      job:description ?description ;\n"
//                + "      job:skills ?skills ;\n"
//                + "      job:experience ?experience .\n"
//
//                + "}";
//
//
//        Query query = QueryFactory.create(queryString);
//
//        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
//            System.out.println("bbb");
//            ResultSet results = qexec.execSelect();
//            while (results.hasNext()) {
//                System.out.println("aaa");
//                QuerySolution soln = results.nextSolution();
//                String title = soln.getLiteral("title").toString();
//                String description = soln.get("description").toString();
//                String experience = soln.get("experience").toString();
//                System.out.println("Title: " + title);
//                System.out.println("Description: " + description);
//                System.out.println("Experience: " + experience);
//            }
//        }
//
//        queryString = "PREFIX job: <http://example.org/job#>\n" +
//                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
//                "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
//                "SELECT ?skill\n" +
//                "WHERE {\n" +
//                "  job:" + id + " job:skills ?skills .\n" +
//                "   ?skills rdf:_1|rdf:_2 ?skill .\n" +
//                "}";
//
//        Query querySkills = QueryFactory.create(queryString);
//        List<String> skills = new ArrayList<>();
//        try (QueryExecution qexecSkills = QueryExecutionFactory.create(querySkills, model)) {
//            ResultSet results = qexecSkills.execSelect();
//            while (results.hasNext()) {
//                QuerySolution soln = results.nextSolution();
//                RDFNode field = soln.get("skill");
//                skills.add(field.toString());
//            }
//        }
//        System.out.println("Skills: " + skills);
//
//
//        // Query for educational fields
//        String eduFieldQueryString = "PREFIX job: <http://example.org/job#>\n" +
//                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
//                "SELECT ?eduField\n" +
//                "WHERE {\n" +
//                "  job:" + id + " job:educational_fields ?eduFields .\n" +
//                "   ?eduFields rdf:_1|rdf:_2 ?eduField .\n" +
//                "}";
//        List<String> eduFields = new ArrayList<>();
//
//        Query eduFieldQuery = QueryFactory.create(eduFieldQueryString);
//        try (QueryExecution qexecEdu = QueryExecutionFactory.create(eduFieldQuery, model)) {
//            ResultSet results = qexecEdu.execSelect();
//            while (results.hasNext()) {
//                QuerySolution soln = results.nextSolution();
//                RDFNode eduField = soln.get("eduField");
//                eduFields.add(eduField.toString());
//            }
//        }
//
//        // Query for companies
//        String companyQueryString = "PREFIX job: <http://example.org/job#>\n" +
//                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
//                "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
//                "SELECT ?company\n" +
//                "WHERE {\n" +
//                "  job:" + id + " job:company ?companyNode .\n" +
//                "   ?companyNode vcard:Orgname ?company .\n" +
//                "}";
//        List<String> companies = new ArrayList<>();
//
//        Query companyQuery = QueryFactory.create(companyQueryString);
//        try (QueryExecution qexecCompanies = QueryExecutionFactory.create(companyQuery, model)) {
//            ResultSet results = qexecCompanies.execSelect();
//            while (results.hasNext()) {
//                QuerySolution soln = results.nextSolution();
//                RDFNode company = soln.get("company");
//                companies.add(company.toString());
//            }
//        }
//
//
//        System.out.println("Ed fields: " + eduFields);
//        System.out.println("Companies: " + companies);



    }




//    public static void main(String[] args) {
//
//        String jobURI = "http://example.org/job#3";
//        String title = "Java Developer";
//        String description = "Does code in Java";
//        String[] skills = {"Taking decisions", "Information gathering"};
//        String[] educationalFields = {"System analysis", "Basic programming", "Analysis and making decisions"};
//        int experience = 4;
//        String[] companies = {"Google", "Microsoft", "Oracle"};
//
//        // create model
//        Model model = ModelFactory.createDefaultModel();
//
//        // read existing RDF file
//        String source = "jobs2.rdf";
//        model.read(source);
//
//        // create resource for new job
//        Resource job = model.createResource(jobURI);
//
//        // set properties for new job
//        job.addProperty(model.createProperty("http://example.org/job#title"), title)
//                .addProperty(model.createProperty("http://example.org/job#description"), description)
//                .addProperty(model.createProperty("http://example.org/job#experience"), String.valueOf(experience));
//
//        // add skills to new job
//        Bag skillsBag = model.createBag();
//        for (String skill : skills) {
//            skillsBag.add(model.createTypedLiteral(skill));
//        }
//        job.addProperty(model.createProperty("http://example.org/job#skills"), skillsBag);
//
//        // add educational fields to new job
//        Seq fieldsSeq = model.createSeq();
//        for (String field : educationalFields) {
//            fieldsSeq.add(model.createTypedLiteral(field));
//        }
//        job.addProperty(model.createProperty("http://example.org/job#educational_fields"), fieldsSeq);
//
//        // add companies to new job
//        for (String company : companies) {
//            Resource org = model.createResource()
//                    .addProperty(RDF.type, VCARD.Orgname)
//                    .addProperty(VCARD.FN, company);
//            job.addProperty(model.createProperty("http://example.org/job#company"), org);
//        }
//
//        // write updated RDF file
//        String destination = "jobs2_updated.rdf";
//        model.write(System.out, "RDF/XML-ABBREV");
//        model.write(destination);
//    }

}
