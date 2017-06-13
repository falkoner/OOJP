/**
 *
 */
package graph;

import util.GraphLoader;

import java.util.*;

/**
 * @author Alex Filatau.
 *         <p>
 *         For the warm up assignment, you must implement your Graph in a class
 *         named CapGraph.  Here is the stub file.
 */
public class CapGraph implements Graph {

    private final HashSet<Integer> vertices = new HashSet<>();
    private final HashMap<Integer, HashSet<Integer>> edges = new HashMap<>();

    /**
     * Add new vertex to the graph
     *
     * @param num Integer value of the vertex
     */
    @Override
    public void addVertex(int num) {
        vertices.add(num);

    }

    /**
     * Add new edge to the graph
     *
     * @param from Integer value of the from vertex
     * @param to   Integer value of the to vertex
     */
    @Override
    public void addEdge(int from, int to) {
        addVertex(from);
        addVertex(to);
        HashSet<Integer> toVertices = edges.getOrDefault(from, new HashSet<>());
        toVertices.add(to);
        edges.put(from, toVertices);
    }

    /**
     * Check if vertex is in the graph
     *
     * @param vertex target vertex
     * @return True if vertex is in the graph
     */
    public boolean containsVertex(int vertex) {
        return vertices.contains(vertex);
    }


    /**
     * Transposes (reverses) all the edges in the graph
     * and returns a copy of this graph
     *
     * @return new CapGraph with transposed edges
     */
    public CapGraph getTransposedGraph() {
        CapGraph transposedGraph = new CapGraph();

        for (Integer fromVertex : getEdges().keySet()) {
            HashSet<Integer> toVertices = getNeighbors(fromVertex);
            toVertices.forEach(toVertex -> transposedGraph.addEdge(toVertex, fromVertex));
        }

        return transposedGraph;
    }

    /**
     * Getter for vertices in the graph
     *
     * @return Set of vertices
     */
    public HashSet<Integer> getVertices() {
        return vertices;
    }

    /**
     * Getter for edges in the graph
     *
     * @return HashMap of edges
     */
    public HashMap<Integer, HashSet<Integer>> getEdges() {
        return edges;
    }

    public HashSet<Integer> getNeighbors(int fromVertex) {
        return getEdges().containsKey(fromVertex) ? getEdges().get(fromVertex) : new HashSet<>();
    }

    /**
     * Egonet algorithm implementation.
     * Takes an Integer which is the vertex at the center of the desired egonet,
     * and returns that vertex's egonet as a subgraph
     *
     * @param center a vertex in center of Egonet
     * @return new Graph object representing Egonet
     */
    @Override
    public Graph getEgonet(int center) {
        CapGraph egonet = new CapGraph();

        if (!containsVertex(center)) return egonet;

        egonet.addVertex(center);
        edges.get(center).forEach(vertex -> egonet.addEdge(center, vertex));

        for (Integer egoVertex : egonet.getVertices()) {
            for (Integer vertex : edges.get(egoVertex)) {
                if (egonet.containsVertex(vertex))
                    egonet.addEdge(egoVertex, vertex);
            }
        }

        return egonet;
    }


    /**
     * Returns all of the strongly connected components in the Graph as a list of subgraphs
     *
     * @return List of strongly connected graphs
     */
    @Override
    public List<Graph> getSCCs() {
        List<Graph> result = new ArrayList<>();

        // DFS traverse graph for the first time
        HashSet<Integer> visited = new HashSet<>();
        Stack<Integer> finished = new Stack<>();

        for (Integer vertex : getVertices())
            if (!visited.contains(vertex))
                dfsVisit(this, vertex, visited, finished);

        // transpose graph edges
        CapGraph transposedGraph = getTransposedGraph();

        // DFS graph in reverse order
        visited = new HashSet<>();
        Stack<Integer> finishedForReverse = new Stack<>();

        while (!finished.empty()) {
            Integer vertex = finished.pop();
            if (!visited.contains(vertex))
                dfsVisit(transposedGraph, vertex, visited, finishedForReverse);
            if (!finishedForReverse.empty()) {
                result.add(getSubGraphForVertices(new ArrayList<>(finishedForReverse)));
                finishedForReverse.removeAllElements();
            }
        }

        return result;
    }


    /**
     * Helper method to process visiting a vertex recursively in depth first search sequence
     * Same method is called for each neighbor of the visited vertex
     *
     * @param graph graph for traversal
     * @param vertex vertex to be visited
     * @param visited set of visited vertices so far
     * @param finished stack for tracking already processed vertices
     */
    private void dfsVisit(CapGraph graph, Integer vertex, HashSet<Integer> visited, Stack<Integer> finished) {
        visited.add(vertex);
        for (Integer edgeVertex : graph.getNeighbors(vertex)) {
            if (!visited.contains(edgeVertex)) {
                dfsVisit(graph, edgeVertex, visited, finished);
            }
        }
        finished.push(vertex);
    }

    /**
     * Helper method to extract a subgraph given list of vertices
     * Only common edges are preserved
     *
     * @param vertices List of vertices to be extracted
     * @return new subgraph based on submitted vertices
     */
    private CapGraph getSubGraphForVertices(List<Integer> vertices) {
        CapGraph subGraph = new CapGraph();
        for (Integer vertex : vertices) {
            subGraph.addVertex(vertex);
            for (Integer toVertex : this.getNeighbors(vertex)) {
                if (vertices.contains(toVertex))
                    subGraph.addEdge(vertex, toVertex);
            }
        }
        return subGraph;

    }

    /** Custom string representation
     *
     * @return String version of the Graph
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CapGraph{");
        sb.append("vertices=").append(vertices);
        sb.append(", edges=").append(edges);
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Export graph in format consumable for grading.
     *
     * @return HashMap of vertices and all their connections
     */
    @Override
    public HashMap<Integer, HashSet<Integer>> exportGraph() {
        HashMap<Integer, HashSet<Integer>> fullGraph = new HashMap<>();

        for (Integer vertex : getVertices()) {
            HashSet<Integer> edges = getEdges().getOrDefault(vertex, new HashSet<>());
            fullGraph.put(vertex, edges);
        }

        return fullGraph;
    }

    public static void main(String[] args) {
        Graph graph = new CapGraph();
        GraphLoader.loadGraph(graph, "data/scc/test_4.txt");
        System.out.println("Imported graph:");
        System.out.println(graph.exportGraph());
        System.out.println();

        Graph egonet = graph.getEgonet(5);
        System.out.println("Egonet of [5]:");
        System.out.println(egonet.exportGraph());
        System.out.println();

        List<Graph> scc = graph.getSCCs();
        System.out.println("SCCs:");
        System.out.println(scc);
        scc.forEach(graph1 -> System.out.println(graph1.exportGraph()));
    }

}
