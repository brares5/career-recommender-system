package org.example;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GetLargestJobID {

    static String source = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\tryjobs.rdf";
    static String destination = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\example\\blabla.rdf";

    public static void main(String[] args) {
        String nsJob = "http://example.org/job#";
        String nsVcard = "http://www.w3.org/2001/vcard-rdf/3.0#";
        String skillsNS = "http://example.org/skills#";
        String eduFieldsNS = "http://example.org/edufields#";


        Model model = ModelFactory.createDefaultModel();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = RDFDataMgr.open(destination);
            model.read(in, null);
            model.write(System.out);
            in.close();
            String queryString = "PREFIX job: <http://example.org/job#> SELECT ?jobId WHERE {?jobId a job:Job .}";

            // create a query execution and execute the query
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet results = qe.execSelect();

            // iterate over the results and find the largest job id
            int largestJobId = 0;
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String jobId = solution.get("jobId").toString();
                int jobIdNum = Integer.parseInt(jobId.split("#")[1]);
                if (jobIdNum > largestJobId) {
                    largestJobId = jobIdNum;
                }
            }

            // print the largest job id
            System.out.println("The largest job id is: job#" + largestJobId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}