package org.example;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RDFDeletionExample {
    public static void main(String[] args) {

        String source = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\tryjobs.rdf";
        String destination = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\blabla.rdf";
        String nsJob = "http://example.org/job#";
        String nsVcard = "http://www.w3.org/2001/vcard-rdf/3.0#";
//        Model model = ModelFactory.createDefaultModel();
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//            in = RDFDataMgr.open(source);
//            model.read(in, null);
//            model.write(System.out);
//            in.close();

        String jobNS = "http://example.org/job#";
        Model model = ModelFactory.createDefaultModel();
        try (InputStream in = FileManager.get().open(source)) {
            model.read(in, null);
        } catch (IOException e) {
            System.out.println("File not found: " + source);
            return;
        }
        Resource job2 = model.createResource(jobNS + "2");
        StmtIterator stmtIterator = model.listStatements(job2, null, (RDFNode) null);
        List<Statement> statementsToRemove = new ArrayList<>();
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.next();
            statementsToRemove.add(stmt);
        }
        stmtIterator = model.listStatements(null, null, job2);
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.next();
            statementsToRemove.add(stmt);
        }
        model.remove(statementsToRemove);
        try (OutputStream out = new FileOutputStream(destination)) {
            model.write(System.out, "RDF/XML-ABBREV");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + source);
        }
    }
}
