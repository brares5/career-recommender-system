package org.example;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

import java.io.IOException;
import java.io.InputStream;

public class RDFToJSON {

    public static void main(String[] args) {
        String source = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\tryjobs.rdf";
        String nsJob = "http://example.org/job#";
        String nsVcard = "http://www.w3.org/2001/vcard-rdf/3.0#";
        Model model = ModelFactory.createDefaultModel();
        InputStream in = null;
        try {
            in = RDFDataMgr.open(source);
            model.read(in, null);
            in.close();

            model.write(System.out, "JSONLD");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
