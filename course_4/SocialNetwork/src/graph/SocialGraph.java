package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;

/**
 * Created by Alex Filatau
 * Modification of base CapGraph class enabled to work with following social graph features:
 * - degree centralityMap of nodes (number of connections a node has)
 * - boolean state if the nodes (for state spread simulation)
 */
public class SocialGraph extends CapGraph {
    private HashMap<Integer, Integer> centralityMap = new HashMap<>();
    private TreeMap<Integer, List<Integer>> centralitySortedIndex = new TreeMap<>();

    /**
     * Add new edge to the graph
     * Keep count on degree centralityMap of the nodes involved
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
     * Increase centralityMap count for the vertex
     *
     * @param vertex Integer value of the target vertex
     */
    private void incrementCentrality(int vertex) {
        int centralityValue = this.centralityMap.getOrDefault(vertex, 0);
        centralityValue++;
        this.centralityMap.put(vertex, centralityValue);
        List<Integer> vertexReferences = this.centralitySortedIndex.getOrDefault(centralityValue, new ArrayList<>());
        vertexReferences.add(vertex);
        this.centralitySortedIndex.put(centralityValue, vertexReferences);
    }


    /**
     * Returns degree centralityMap value stored for the vertex
     *
     * @param vertex Integer value of the target vertex
     * @return degree centralityMap
     */
    public int getCentrality(int vertex) {
        if (!this.containsVertex(vertex)) {
            throw new IllegalArgumentException(String.format("Vertex [%s] is not in graph", vertex));
        }
        return this.centralityMap.getOrDefault(vertex, 0);
    }

    /**
     * Returns specified number (at most) of vertices sorted descending by centrality.
     *
     * @param numVertices number of vertices to return
     * @return List of top vertices by centrality
     */
    public List<Integer> getTopVerticesByCentrality(int numVertices) {
        List<Integer> topVertices = new ArrayList<>();
        for (Integer centrality : this.centralitySortedIndex.keySet()) {
            if (topVertices.size() >= numVertices) break;
            List<Integer> vertices = this.centralitySortedIndex.get(centrality);
            topVertices.addAll(vertices);
        }

        return topVertices.stream().limit(numVertices).collect(toList());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SocialGraph{");
        sb.append("vertices=").append(this.getVertices());
        sb.append(", edges=").append(this.getEdges());
        sb.append(", centralitySortedIndex=").append(centralitySortedIndex);
        sb.append('}');
        return sb.toString();
    }
}
