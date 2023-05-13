package org.pizza;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.vocabulary.RDF;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HermiTReasonerTest {
    public static void main(String[] args) throws OWLOntologyCreationException {
        String owlFile = "E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\pizza\\Birzaneanu_Rares_1241A_pizza-ontology.owl";
        Model baseModel = ModelFactory.createDefaultModel();
        baseModel.read(owlFile);

        String fileIRI = "file:///E:/CTI%20eng/an%204/licenta/career-recommender-system/src/main/java/org/pizza/Birzaneanu_Rares_1241A_pizza-ontology.owl";
        OWLOntologyManager m= OWLManager.createOWLOntologyManager();
        OWLOntology o=m.loadOntologyFromOntologyDocument(IRI.create(fileIRI));
        Reasoner hermit=new Reasoner(o);
        System.out.println(hermit.isConsistent());

        OWLNamedIndividual individual = o.getOWLOntologyManager().getOWLDataFactory().getOWLNamedIndividual(IRI.create("http://www.pizza.com/ontologies/pizza.owl#Example-Margherita"));

        // Get the classes the individual belongs to
        NodeSet<OWLClass> classes = hermit.getTypes(individual, true);

        // Iterate over the classes and print their IRIs
        for (Node<OWLClass> classNode : classes) {
            OWLClass owlClass = classNode.getRepresentativeElement();
            System.out.println("Individual belongs to class: " + owlClass.getIRI());
        }

    }
}
