package tests;

//Bien lire la documentation incluse dans la distribution de JUnit 4.10.
//Document: CookBook
//Section:  Running Tests
//CLI: java -cp lib/junit.jar:runtime/JunitTest.jar org.junit.runner.JUnitCore tests.JunitTest


import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Iterator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.alg.KShortestPaths;
// import org.jgrapht.ext.DOTExporter;


public class TestJGraphT
{
    private UndirectedGraph<String, DefaultEdge> __g = null;
    
    @Before public void setUp()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing JGraphT.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
        
        // Create an undirected graph.
        
        this.__g = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";
        String v5 = "v5";
        String v6 = "v6";
        String v7 = "v7";

        // add the vertices
        this.__g.addVertex(v1);
        this.__g.addVertex(v2);
        this.__g.addVertex(v3);
        this.__g.addVertex(v4);
        this.__g.addVertex(v5);
        this.__g.addVertex(v6);
        this.__g.addVertex(v7);
        

        // add edges to create a circuit
        this.__g.addEdge(v1, v2); 
        this.__g.addEdge(v1, v2);  // Crash test... OK, it passes.
        this.__g.addEdge(v2, v3);
        this.__g.addEdge(v3, v4);
        this.__g.addEdge(v4, v1);
        this.__g.addEdge(v4, v5);
        this.__g.addEdge(v4, v6);
        this.__g.addEdge(v4, v7);
        this.__g.addEdge(v7, v1);
        
    }
    
    @Test public void testMe()
    {
        System.out.println(this.__g.toString());
        
        KShortestPaths<String, DefaultEdge> pathesFinder = new KShortestPaths<String, DefaultEdge>(this.__g, new String("v1"), 6);
        List <GraphPath<String, DefaultEdge>> pathes = pathesFinder.getPaths(new String("v7"));
        Iterator<GraphPath<String, DefaultEdge>> pathesIterator = pathes.iterator();
        System.out.println("Nombre de chemins: " + pathes.size());
        while (pathesIterator.hasNext())
        {
            // Get the start and the end of the pathes. We already know... but it's a test.
            GraphPath<String, DefaultEdge> gr = pathesIterator.next();
            String StartVertex = gr.getStartVertex();
            String EndVertex = gr.getEndVertex();
            System.out.println("From: " + StartVertex + " to " + EndVertex);
            
            // Get the list of jumps.
            List <DefaultEdge> edges = gr.getEdgeList();
            Iterator<DefaultEdge> edgesItearator = edges.iterator();
            while (edgesItearator.hasNext())
            {
                DefaultEdge edge = edgesItearator.next();
                System.out.println(edge.toString());
            }
        }

        try {
        		File tempFile = File.createTempFile("prefix", "suffix");
        		System.out.println("Fichier temporaire: " + tempFile);
        		@SuppressWarnings("unused")
        		FileWriter w = new FileWriter(tempFile);
                // DOTExporter dot = new DOTExporter();
                // dot.export(w, this.__g);
            }
        catch (IOException e)
        {
            System.out.println("ERROR while writing DOT file: " + e.getMessage());
        }
        
        // creation du graphe avec Graphviz: dot -Tpng -o/tmp/test.png /tmp/testGraph.dot
                

        
    }
    
    
}
