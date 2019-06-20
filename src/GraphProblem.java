import com.sun.javafx.event.EventDispatchTreeImpl;
import jdk.nashorn.internal.ir.WhileNode;

import java.io.File;
import java.net.URL;
import java.util.*;

class Vertex{
    String label;
    public Set<Vertex> toVertices;
    public Set<Vertex> fromVertices;

    public Set<Vertex> getToVertices() {
        return toVertices;
    }

    public void setToVertices(Set<Vertex> toVertices) {
        this.toVertices = toVertices;
    }

    public Set<Vertex> getFromVertices() {
        return fromVertices;
    }

    public void setFromVertices(Set<Vertex> fromVertices) {
        this.fromVertices = fromVertices;
    }

    public String getLabel(){
        return label;
    }

    public void addEdgeFrom(Vertex v){
        this.fromVertices.add(v);
    }

    public void addEdgeTo(Vertex v){
        this.toVertices.add(v);
    }

    Vertex(String label){
        this.toVertices = new HashSet<>();
        this.fromVertices = new HashSet<>();
        this.label  = label;
    }

    public void removeEdgeTo(Vertex toVertex) {
        this.toVertices.remove(toVertex);
    }

    public void removeEdgeFrom(Vertex fromVertex) {
        this.fromVertices.remove(fromVertex);
    }
}

class Graph{
    private Map<String,Vertex> vertices = new HashMap<>();
    private int numberOfEdges = 0;

    public Map<String, Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(Map<String, Vertex> vertices) {
        this.vertices = vertices;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    public void setNumberOfEdges(int numberOfEdges) {
        this.numberOfEdges = numberOfEdges;
    }

    public void addNewVertext(String label){
        if(!vertices.containsKey(label)){
            vertices.put(label,new Vertex(label));
        }
    }


    public void addEdge(String fromLabel,String toLabel){
        if (!vertices.containsValue(fromLabel)){
            addNewVertext(fromLabel);
        }
        Vertex fromVertex = vertices.get(fromLabel);
        if(!vertices.containsValue(toLabel)){
            addNewVertext(toLabel);
        }
        Vertex toVertex = vertices.get(toLabel);

        fromVertex.addEdgeTo(toVertex);
        toVertex.addEdgeFrom(fromVertex);
        numberOfEdges++;
    }

    public void removeEdge(String fromLabel,String toLabel){
        Vertex fromVertex = vertices.get(fromLabel);
        Vertex toVertex = vertices.get(toLabel);

        fromVertex.removeEdgeTo(toVertex);
        toVertex.removeEdgeFrom(fromVertex);
        numberOfEdges--;
    }

    public static boolean topologicalSort(Graph graph){
        List<Vertex> sortedList = new ArrayList<>();
        Set<String> inDegreeZero = new HashSet<>();

        for(Map.Entry<String,Vertex> entry: graph.getVertices().entrySet()){
            if(entry.getValue().getFromVertices().size()==0){
                inDegreeZero.add(entry.getKey());
            }
        }

        while(inDegreeZero.size()!=0){
            String outLabel = inDegreeZero.iterator().next();
            inDegreeZero.remove(outLabel);
            Vertex outVertext = graph.vertices.get(outLabel);
            sortedList.add(outVertext);

            Set<Vertex> toVertices = outVertext.getToVertices();

            Vertex trav;
            while (toVertices.size()!=0){
                trav = toVertices.iterator().next();
                graph.removeEdge(outLabel,trav.getLabel());
                if (trav.getFromVertices().size()==0){
                    inDegreeZero.add(trav.getLabel());
                }
            }
        }

        if (graph.getNumberOfEdges() >0){
            return false;
        }else {
            return true;
        }
    }
}

public class GraphProblem {
    public static void main(String[] args) throws Exception {
        URL PROBLEM_FILE_PATH = GraphProblem.class.getClassLoader().getResource("sample_problem.txt");
        Graph horizontalGraph = new Graph();
        Graph verticalGraph = new Graph();

        prepareGraphFromFile(PROBLEM_FILE_PATH,horizontalGraph,verticalGraph);

        if(Graph.topologicalSort(horizontalGraph)&&Graph.topologicalSort(verticalGraph)){
            System.out.println("Rules are consistent");

        }else {
            System.out.println("Rules are inconsistent");
        }
    }

    private static void prepareGraphFromFile(URL problem_file_path, Graph horizontalGraph, Graph verticalGraph) throws Exception {
        File file = new File("C:\\Users\\SARALA\\IdeaProjects\\BrillioInterview\\resources\\sample_problem");
        Scanner sc = new Scanner(file);

        while(sc.hasNextLine()){
            String edge = sc.nextLine();
            String tokens[] = edge.split(" ");

            String fromLabel = tokens[0];
            String toLabel = tokens[2];

            String direction = tokens[1];

            if(direction.indexOf('N')>-1){
                verticalGraph.addEdge(fromLabel,toLabel);
            }
            if (direction.indexOf('S')>-1){
                verticalGraph.addEdge(toLabel,fromLabel);
            }
            if (direction.indexOf('E')>-1){
                horizontalGraph.addEdge(fromLabel,toLabel);
            }
            if (direction.indexOf('W')>-1){
                horizontalGraph.addEdge(toLabel,fromLabel);
            }
        }
    }
}

