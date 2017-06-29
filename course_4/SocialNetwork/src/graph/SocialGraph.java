package graph;

import java.util.HashMap;

/**
 * Created by Alex Filatau
 * Modification of base CapGraph class enabled to work with following social graph features:
 * - degree centrality of nodes (number of connections a node has)
 * - boolean state if the nodes (for state spread simulation)
 */
public class SocialGraph extends CapGraph {
    private HashMap<Integer, Integer> centrality = new HashMap<>();

    /**
     * Add new edge to the graph
     * Keep count on degree centrality of the nodes involved
     *
     * @param from Integer value of the from vertex
     * @param to   Integer value of the to vertex
     */
    @Override
    public void addEdge(int from, int to) {
        super.addEdge(from, to);
        incrementCentrality(from);
    }

    /**
     * Increase centrality count for the vertex
     *
     * @param vertex Integer value of the target vertex
     */
    private void incrementCentrality(int vertex) {
        int centralityValue = this.centrality.getOrDefault(vertex, 0);
        centralityValue++;
        this.centrality.put(vertex, centralityValue);
    }


    /**
     * Returns degree centrality value stored for the vertex
     *
     * @param vertex Integer value of the target vertex
     * @return degree centrality
     */
    public int getCentrality(int vertex) {
        if (!this.containsVertex(vertex)) {
            throw new IllegalArgumentException(String.format("Vertex [%s] is not in graph", vertex));
        }
        return this.centrality.getOrDefault(vertex, 0);
    }
}
