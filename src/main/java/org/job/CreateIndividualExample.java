package org.job;

import org.coode.owlapi.owlxml.renderer.OWLXMLRenderer;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLRendererException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import java.io.StringWriter;
import java.util.Set;

public class CreateIndividualExample {
    public static void main(String[] args) {
        // Load the OWL ontology
        String fileIRI = "file:///E:/CTI%20eng/an%204/licenta/career-recommender-system/src/main/java/org/job/crs.owl";
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology;
        try {
            ontology = manager.loadOntologyFromOntologyDocument(IRI.create(fileIRI));
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
            return;
        }



        // Create an instance of OWLNamedIndividual for Matthew
        OWLDataFactory dataFactory = manager.getOWLDataFactory();
        IRI matthewIRI = IRI.create("http://www.crs.com/ontologies/crs.owl#Matthew");
        OWLNamedIndividual matthew = dataFactory.getOWLNamedIndividual(matthewIRI);

        // Create OWLClassExpression instances for educational fields
        OWLClassExpression analysisAndTakingDecisions = dataFactory.getOWLClass(IRI.create("http://www.crs.com/ontologies/crs.owl#AnalysisAndTakingDecisions"));
        OWLClassExpression law = dataFactory.getOWLClass(IRI.create("http://www.crs.com/ontologies/crs.owl#Law"));
        OWLClassExpression psychology = dataFactory.getOWLClass(IRI.create("http://www.crs.com/ontologies/crs.owl#Psychology"));

        // Create OWLClassExpression instances for skills
        OWLClassExpression identificationOfObjectsActionsAndEvents = dataFactory.getOWLClass(IRI.create("http://www.crs.com/ontologies/crs.owl#IdentificationOfObjectsActionsAndEvents"));
        OWLClassExpression takingDecisionsAndSolvingProblems = dataFactory.getOWLClass(IRI.create("http://www.crs.com/ontologies/crs.owl#TakingDecisionsAndSolvingProblems"));
        OWLClassExpression verbalWrittenComprehension = dataFactory.getOWLClass(IRI.create("http://www.crs.com/ontologies/crs.owl#VerbalWrittenComprehension"));

        // Create OWLRestriction instances for educational fields
        OWLObjectProperty hasEducationalField = dataFactory.getOWLObjectProperty(IRI.create("http://www.crs.com/ontologies/crs.owl#hasEducationalField"));
        OWLClassExpression matthewEducationalFields = dataFactory.getOWLObjectIntersectionOf(analysisAndTakingDecisions, law, psychology);
        OWLClassExpression hasEducationalFieldRestriction = dataFactory.getOWLObjectSomeValuesFrom(hasEducationalField, matthewEducationalFields);

        // Create OWLRestriction instances for skills
        OWLObjectProperty hasSkill = dataFactory.getOWLObjectProperty(IRI.create("http://www.crs.com/ontologies/crs.owl#hasSkill"));
        OWLClassExpression matthewSkills = dataFactory.getOWLObjectIntersectionOf(identificationOfObjectsActionsAndEvents, takingDecisionsAndSolvingProblems, verbalWrittenComprehension);
        OWLClassExpression hasSkillRestriction = dataFactory.getOWLObjectSomeValuesFrom(hasSkill, matthewSkills);
        // Create the intersection of classes for Matthew
        OWLClassExpression matthewClassExpression = dataFactory.getOWLObjectIntersectionOf(
                dataFactory.getOWLClass(IRI.create("http://www.crs.com/ontologies/crs.owl#Job")),
                hasEducationalFieldRestriction,
                hasSkillRestriction
        );

        // Add the intersection of classes to Matthew's type
        OWLAxiom axiom = dataFactory.getOWLClassAssertionAxiom(matthewClassExpression, matthew);
        manager.addAxiom(ontology, axiom);
//
//        Reasoner hermit=new Reasoner(ontology);
//        hermit.flush();
//        System.out.println(hermit.isConsistent());

        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

        // Flush the reasoner to ensure the ontology is classified
        reasoner.flush();
        System.out.println(reasoner.isConsistent());
        // Create an instance of OWLNamedIndividual for Matthew
//        OWLDataFactory dataFactory = manager.getOWLDataFactory();
//        IRI matthewIRI = IRI.create("http://www.crs.com/ontologies/crs.owl#Matthew");
//        OWLNamedIndividual matthew = dataFactory.getOWLNamedIndividual(matthewIRI);



        OWLNamedIndividual individual = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLNamedIndividual(IRI.create("http://www.crs.com/ontologies/crs.owl#Matthew"));


//        dataFactory = manager.getOWLDataFactory();
//        matthewIRI = IRI.create("http://www.crs.com/ontologies/crs.owl#Matthew");
//        matthew = dataFactory.getOWLNamedIndividual(matthewIRI);
        NodeSet<OWLClass> types = reasoner.getTypes(individual, true);
        System.out.println(types.toString());

        // Print the classified types
        System.out.println("Classified types for Matthew:");
        for (Node<OWLClass> type : types) {
            System.out.println(type);
        }

//        for (OWLAxiom axiomm : ontology.getAxioms()) {
//            System.out.println(axiomm);
//        }

        StringWriter writer = new StringWriter();

        // Using OWLXMLRenderer
        OWLXMLRenderer owlxmlRenderer = new OWLXMLRenderer();
        try {
            owlxmlRenderer.render(ontology, writer);
            System.out.println("OWL/XML format:");
            System.out.println(writer.toString());
        } catch (OWLRendererException e) {
            e.printStackTrace();
        }



        // Get the classes the individual belongs to
//        NodeSet<OWLClass> classes = reasoner.getTypes(individual, true);

        // Iterate over the classes and print their IRIs
//        for (Node<OWLClass> classNode : classes) {
//            OWLClass owlClass = classNode.getRepresentativeElement();
//            System.out.println("Individual belongs to class: " + owlClass.getIRI());
//        }

        reasoner.dispose();

        // Save the modified ontology
//        try {
//            manager.saveOntology(ontology, IRI.create("path/to/save/modified/ontology.owl"));
//        } catch (OWLOntologyStorageException e) {
//            e.printStackTrace();
//        }
    }
}
