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
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;


import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class CreateIndividualExample {
    public static void main(String[] args) {

        ResourceLoader rs = new DefaultResourceLoader();
        Resource resource = rs.getResource("classpath:crs.owl");
        File file;
        try {
            file = resource.getFile();
            String absolutePath = file.getAbsolutePath();
            System.out.println("Absolute path: " + absolutePath);
            String encodedPath;
            encodedPath = absolutePath.replace("\\", "/").replace(" ", "%20");
            System.out.println(encodedPath);
        } catch (IOException e) {
            // Handle exception
        }        System.out.println("Error");
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


        // Create the individual declaration
        OWLDeclarationAxiom individualDeclaration = dataFactory.getOWLDeclarationAxiom(dataFactory.getOWLNamedIndividual(matthewIRI));

        // Add the individual declaration to the ontology
        manager.addAxiom(ontology, individualDeclaration);

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
//        OWLClassExpression matthewEducationalFields = dataFactory.getOWLObjectIntersectionOf(analysisAndTakingDecisions, law, psychology);
        OWLClassExpression hasEducationalFieldRestriction = dataFactory.getOWLObjectSomeValuesFrom(hasEducationalField, analysisAndTakingDecisions);
        OWLClassExpression hasEducationalFieldRestriction2 = dataFactory.getOWLObjectSomeValuesFrom(hasEducationalField, law);
        OWLClassExpression hasEducationalFieldRestriction3 = dataFactory.getOWLObjectSomeValuesFrom(hasEducationalField, psychology);

        // Create OWLRestriction instances for skills
        OWLObjectProperty hasSkill = dataFactory.getOWLObjectProperty(IRI.create("http://www.crs.com/ontologies/crs.owl#hasSkill"));
//        OWLClassExpression matthewSkills = dataFactory.getOWLObjectIntersectionOf(identificationOfObjectsActionsAndEvents, takingDecisionsAndSolvingProblems, verbalWrittenComprehension);
        OWLClassExpression hasSkillRestriction = dataFactory.getOWLObjectSomeValuesFrom(hasSkill, identificationOfObjectsActionsAndEvents);
        OWLClassExpression hasSkillRestriction2 = dataFactory.getOWLObjectSomeValuesFrom(hasSkill, takingDecisionsAndSolvingProblems);
        OWLClassExpression hasSkillRestriction3 = dataFactory.getOWLObjectSomeValuesFrom(hasSkill, verbalWrittenComprehension);


        OWLClassExpression[] hasEducationalFieldRestrictionArray = {hasEducationalFieldRestriction, hasEducationalFieldRestriction2, hasEducationalFieldRestriction3};
        OWLClassExpression[] hasSkillRestrictionArray = {hasSkillRestriction, hasSkillRestriction2, hasSkillRestriction3};

// Create a list to hold the class expressions for intersection
        Set<OWLClassExpression> classExpressions = new TreeSet<>();

// Add the Job class to the list
        classExpressions.add(dataFactory.getOWLClass(IRI.create("http://www.crs.com/ontologies/crs.owl#Job")));

// Add the educational field restrictions to the list
        classExpressions.addAll(Arrays.asList(hasEducationalFieldRestrictionArray));

// Add the skill restrictions to the list
        classExpressions.addAll(Arrays.asList(hasSkillRestrictionArray));


//        OWLClassExpression intersection = null;
//        for (OWLClassExpression classExpression : classExpressions) {
//            if (intersection == null) {
//                intersection = classExpression;
//            } else {
//                intersection = dataFactory.getOWLObjectIntersectionOf(intersection, classExpression);
//            }
//        }
//        OWLClassExpression matthewClassExpression = intersection;



        OWLClassExpression matthewClassExpression = dataFactory.getOWLObjectIntersectionOf((classExpressions));


// Create the intersection of the class expressions
//        OWLClassExpression matthewClassExpression = OWLClassExpressionUtils.createIntersection(classExpressions, dataFactory);

        // Create the intersection of classes for Matthew
//        OWLClassExpression matthewClassExpression = dataFactory.getOWLObjectIntersectionOf(
//                dataFactory.getOWLClass(IRI.create("http://www.crs.com/ontologies/crs.owl#Job")),
//                hasEducationalFieldRestriction,
//                hasEducationalFieldRestriction2,
//                hasEducationalFieldRestriction3,
//                hasSkillRestriction,
//                hasSkillRestriction2,
//                hasSkillRestriction3
//        );

        // Add the intersection of classes to Matthew's type
        OWLAxiom axiom = dataFactory.getOWLClassAssertionAxiom(matthewClassExpression, matthew);
        manager.addAxiom(ontology, axiom);
//
        Reasoner hermit=new Reasoner(ontology);
        hermit.flush();
        System.out.println(hermit.isConsistent());

//        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
//        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

        // Flush the reasoner to ensure the ontology is classified
//        reasoner.flush();
//        System.out.println(reasoner.isConsistent());
        // Create an instance of OWLNamedIndividual for Matthew
//        OWLDataFactory dataFactory = manager.getOWLDataFactory();
//        IRI matthewIRI = IRI.create("http://www.crs.com/ontologies/crs.owl#Matthew");
//        OWLNamedIndividual matthew = dataFactory.getOWLNamedIndividual(matthewIRI);



        OWLNamedIndividual individual = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLNamedIndividual(IRI.create("http://www.crs.com/ontologies/crs.owl#Matthew"));


//        dataFactory = manager.getOWLDataFactory();
//        matthewIRI = IRI.create("http://www.crs.com/ontologies/crs.owl#Matthew");
//        matthew = dataFactory.getOWLNamedIndividual(matthewIRI);
//        NodeSet<OWLClass> types = reasoner.getTypes(individual, true);
//        System.out.println(types.toString());
//
//        // Print the classified types
//        System.out.println("Classified types for Matthew:");
//        for (Node<OWLClass> type : types) {
//            System.out.println(type);
//        }

//        for (OWLAxiom axiomm : ontology.getAxioms()) {
//            System.out.println(axiomm);
//        }

//        StringWriter writer = new StringWriter();

        // Using OWLXMLRenderer
//        OWLXMLRenderer owlxmlRenderer = new OWLXMLRenderer();
//        try {
//            owlxmlRenderer.render(ontology, writer);
//            System.out.println("OWL/XML format:");
//            System.out.println(writer.toString());
//        } catch (OWLRendererException e) {
//            e.printStackTrace();
//        }



        // Get the classes the individual belongs to
        NodeSet<OWLClass> classes = hermit.getTypes(individual, true);

//         Iterate over the classes and print their IRIs
        for (Node<OWLClass> classNode : classes) {
            OWLClass owlClass = classNode.getRepresentativeElement();
            System.out.println("Individual belongs to class: " + owlClass.getIRI());
        }

//        reasoner.dispose();

        // Save the modified ontology to a file or output to the console
        try {
            manager.saveOntology(ontology, IRI.create(new File("E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\job\\newcrs.owl")));
            System.out.println("Individual declaration added to the ontology.");
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }
    }
}
