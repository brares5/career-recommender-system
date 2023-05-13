package org.pizza;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDF;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReasonerTest {

    public static void main(String[] args) {
        String owlFile = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\pizza\\Birzaneanu_Rares_1241A_pizza-ontology.owl";
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        try (InputStream in = new FileInputStream(owlFile)) {
            model.read(in, null);
        } catch (IOException e) {
            // Handle any exceptions
        }

        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(model);

        InfModel infModel = ModelFactory.createInfModel(reasoner, model);


//        String individualURI = "http://www.pizza.com/ontologies/pizza.owl#MyNewPizza";
        String individualURI = "http://www.pizza.com/ontologies/pizza.owl#Example-Margherita";
        Resource individual = infModel.getResource(individualURI);
        StmtIterator stmtIterator = infModel.listStatements(null, RDF.type, (RDFNode) null);
        while (stmtIterator.hasNext()) {
            Statement statement = stmtIterator.next();
            Resource subject = statement.getSubject();
            if (subject.equals(individual) && subject.isURIResource()) {
                System.out.println(individual.getURI() + " is a member of class: " + statement.getObject().asResource().getURI());
            }
        }


        String queryString = "ASK WHERE { <" + individualURI + "> a <http://www.pizza.com/ontologies/pizza.owl#LowCaloriePizza> }";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, infModel);
        boolean isLowCaloriePizza = qexec.execAsk();

        if (isLowCaloriePizza) {
            System.out.println(individual.getURI() + " is classified as a LowCaloriePizza.");
        } else {
            System.out.println(individual.getURI() + " is not classified as a LowCaloriePizza.");
        }

//        String individualURI = "http://www.pizza.com/ontologies/pizza.owl#MyNewPizza";
//        String individualURI = "http://www.pizza.com/ontologies/pizza.owl#Example-Margherita";
//        Resource individual = infModel.getResource(individualURI);
//
//        OntClass cheesyPizzaClass = model.getOntClass("http://www.pizza.com/ontologies/pizza.owl#CheesyPizza");
//        OntClass nonVegetarianPizzaClass = model.getOntClass("http://www.pizza.com/ontologies/pizza.owl#NonVegetarianPizza");
//
//        boolean isCheesyPizza = individual.hasProperty(RDF.type, cheesyPizzaClass);
//        boolean isNonVegetarianPizza = individual.hasProperty(RDF.type, nonVegetarianPizzaClass);
//
//
//        if (isCheesyPizza && isNonVegetarianPizza) {
//            System.out.println(individual.getURI() + " belongs to both CheesyPizza and NonVegetarianPizza classes.");
//        } else if (isCheesyPizza) {
//            System.out.println(individual.getURI() + " belongs to the CheesyPizza class.");
//        } else if (isNonVegetarianPizza) {
//            System.out.println(individual.getURI() + " belongs to the NonVegetarianPizza class.");
//        } else {
//            System.out.println(individual.getURI() + " does not belong to the specified classes.");
//        }



    }
}
