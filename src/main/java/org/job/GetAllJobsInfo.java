package org.job;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class GetAllJobsInfo {
    public static void main(String[] args) throws OWLOntologyCreationException {
        String owlFile = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\job\\crs.owl";
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        model.read(owlFile);

        // Get the OWL classes
        OntClass analystClass = model.getOntClass("http://www.crs.com/ontologies/crs.owl#Analyst");

        if (analystClass != null) {
            // Extract the job title
            String jobTitle = analystClass.getPropertyValue(model.getProperty("http://www.crs.com/ontologies/crs.owl#job_title")).toString();

            // Extract the job comment
            String jobComment = analystClass.getPropertyValue(RDFS.comment).toString();

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
                "  crs:Analyst owl:equivalentClass/owl:intersectionOf/rdf:rest*/rdf:first ?value .\n" +
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
                "  crs:Analyst owl:equivalentClass/owl:intersectionOf/rdf:rest*/rdf:first ?value .\n" +
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
            System.out.println("Skill: " + skill);
        }

        // Close the query execution
        queryExecutionSkills.close();




//        // Extract information for Analyst class
//        String analystTitle = analystClass.getPropertyValue(model.getProperty("http://www.crs.com/ontologies/crs.owl#job_title")).asLiteral().getString();
//        String analystComment = analystClass.getPropertyValue(model.getProperty("http://www.w3.org/2000/01/rdf-schema#comment")).asLiteral().getString();
//        StmtIterator analystSkillsIterator = getProperties(analystClass, model, "hasSkill");
//        StmtIterator analystEducationalFieldsIterator = getProperties(analystClass, model, "hasEducationalField");
//        // Extract information for AutomationEngineer class (similar to Analyst class)
//        // ...
//
//        // Print the extracted information
////        System.out.println("Analyst:");
////        System.out.println("Title: " + analystTitle);
////        System.out.println("Comment: " + analystComment);
////        System.out.println("Skills:");
////        while (analystSkillsIterator.hasNext()) {
////            Resource skill = analystSkillsIterator.nextStatement().getObject().asResource();
////            String skillName = getPropertyStringValue(skill, model, "skill_name");
////            System.out.println("- Skill Name: " + skillName);
////        }
////        System.out.println("Educational Fields:");
////        while (analystEducationalFieldsIterator.hasNext()) {
////            Resource educationalField = analystEducationalFieldsIterator.nextStatement().getObject().asResource();
////            System.out.println("blabla" + educationalField);
////            String edFieldName = getPropertyStringValue(educationalField, model, "ed_field_name");
////            System.out.println("- Educational Field Name: " + edFieldName);
////        }
//
//    }
//
//    private static StmtIterator getProperties(OntClass ontClass, OntModel model, String propertyName) {
//        Property property = model.getProperty("http://www.crs.com/ontologies/crs.owl#" + propertyName);
//        return ontClass.listProperties(property);
//    }
//
//    private static String getPropertyStringValue(Resource resource, OntModel model, String propertyName) {
//        Property property = model.getProperty("http://www.crs.com/ontologies/crs.owl#" + propertyName);
//        Statement statement = resource.getProperty(property);
//        if (statement != null) {
//            RDFNode value = statement.getObject();
//            if (value.isLiteral()) {
//                return value.asLiteral().getString();
//            }
//        }
//        return null;
    }

}
