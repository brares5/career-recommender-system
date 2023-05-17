package com.crs.service;

import com.crs.model.Job;
import com.crs.model.Person;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


@Service
public class PersonServiceImpl implements PersonService {
    static ResourceLoader rs = new DefaultResourceLoader();
    static String source = rs.getResource("classpath:crs.owl").getFilename();
    static JobService jobService;


    @Override
    public void createPerson(Person p) {

        Resource resource = rs.getResource("classpath:crs.owl");
        File file;
        String encodedPath = null;
        try {
            file = resource.getFile();
            String absolutePath = file.getAbsolutePath();
            System.out.println("Absolute path: " + absolutePath);
            encodedPath = absolutePath.replace("\\", "/").replace(" ", "%20");
        } catch (IOException e) {
            System.out.println("Error");
        }


        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology;
        try {
            ontology = manager.loadOntologyFromOntologyDocument(IRI.create("file:///" + encodedPath));
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
            return;
        }

        // Create an instance of OWLNamedIndividual for Matthew
        OWLDataFactory dataFactory = manager.getOWLDataFactory();
        IRI personIRI = IRI.create("http://www.crs.com/ontologies/crs.owl#" + p.getName());
        OWLNamedIndividual person = dataFactory.getOWLNamedIndividual(personIRI);


        // Create the individual declaration
        OWLDeclarationAxiom individualDeclaration = dataFactory.getOWLDeclarationAxiom(dataFactory.getOWLNamedIndividual(personIRI));

        // Add the individual declaration to the ontology
        manager.addAxiom(ontology, individualDeclaration);

        Set<OWLClassExpression> classExpressions = new TreeSet<>();
        classExpressions.add(dataFactory.getOWLClass(IRI.create("http://www.crs.com/ontologies/crs.owl#Job")));

        // Create OWLRestriction instances for educational fields
        OWLObjectProperty hasEducationalField = dataFactory.getOWLObjectProperty(IRI.create("http://www.crs.com/ontologies/crs.owl#hasEducationalField"));

        for (String edField : p.getEducationalFields()) {
            OWLClassExpression edFieldExpression = dataFactory.getOWLClass(IRI.create("http://www.crs.com/ontologies/crs.owl#" + edField));
            OWLClassExpression hasEducationalFieldRestriction = dataFactory.getOWLObjectSomeValuesFrom(hasEducationalField, edFieldExpression);
            classExpressions.add(hasEducationalFieldRestriction);
        }

        // Create OWLRestriction instances for skills
        OWLObjectProperty hasSkill = dataFactory.getOWLObjectProperty(IRI.create("http://www.crs.com/ontologies/crs.owl#hasSkill"));

        for (String skill : p.getSkills()) {
            OWLClassExpression skillExpression = dataFactory.getOWLClass(IRI.create("http://www.crs.com/ontologies/crs.owl#" + skill));
            OWLClassExpression hasSkillFieldRestriction = dataFactory.getOWLObjectSomeValuesFrom(hasSkill, skillExpression);
            classExpressions.add(hasSkillFieldRestriction);
        }

        OWLClassExpression personClassExpression = dataFactory.getOWLObjectIntersectionOf((classExpressions));


        // Add the intersection of classes to person's type
        OWLAxiom axiom = dataFactory.getOWLClassAssertionAxiom(personClassExpression, person);
        manager.addAxiom(ontology, axiom);




        // Save the modified ontology to a file or output to the console
        try {
            manager.saveOntology(ontology, IRI.create(new File("E:\\CTI eng\\an 4\\licenta\\career-recommender-system\\src\\main\\java\\org\\job\\newcrs.owl")));
            System.out.println("Individual declaration added to the ontology.");
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }



    }

    @Override
    public List<Job> classifyPerson(Person p) {
        List<Job> jobs = new ArrayList<>();


        Resource resource = rs.getResource("classpath:crs.owl");
        File file;
        String encodedPath = null;
        try {
            file = resource.getFile();
            String absolutePath = file.getAbsolutePath();
            System.out.println("Absolute path: " + absolutePath);
            encodedPath = absolutePath.replace("\\", "/").replace(" ", "%20");
        } catch (IOException e) {
            // Handle exception
            System.out.println("Error");
        }



        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology;
        try {
            ontology = manager.loadOntologyFromOntologyDocument(IRI.create("file:///" + encodedPath));
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
            return null;
        }

        Reasoner hermit=new Reasoner(ontology);
        hermit.flush();
        System.out.println(hermit.isConsistent());


        OWLNamedIndividual individual = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLNamedIndividual(IRI.create("http://www.crs.com/ontologies/crs.owl#" + p.getName()));


        // Get the classes the individual belongs to
        NodeSet<OWLClass> classes = hermit.getTypes(individual, true);

//         Iterate over the classes and print their IRIs
        for (Node<OWLClass> classNode : classes) {
            OWLClass owlClass = classNode.getRepresentativeElement();
            System.out.println("Individual belongs to class: " + owlClass.getIRI());
            String className = owlClass.getIRI().getFragment();
            System.out.println("Individual belongs to title job: " + className);
//            jobs.add(jobService.getJobBySubject(className));
            System.out.println(jobService.getJobBySubject(className).toString());
        }
        hermit.dispose();
        return null;
    }
}
