package graph;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Created by Alex Filatau
 * Modification of base CapGraph class enabled to work with following social graph features:
 * - degree centralityMap of nodes (number of connections a node has)
 * - boolean state if the nodes (for state spread simulation)
 */
public class SocialGraph extends CapGraph {
    private HashMap<Integer, Integer> centralityMap = new HashMap<>();
    private TreeMap<Integer, List<Integer>> centralitySortedIndex = new TreeMap<>(Collections.reverseOrder());

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
        // store new centrality value
        int centralityValue = this.centralityMap.getOrDefault(vertex, 0);
        centralityValue++;
        this.centralityMap.put(vertex, centralityValue);

        // remove from previous centrality index if applicable
        if (centralityValue > 1) {
            List<Integer> oldVertexReferences = this.centralitySortedIndex.getOrDefault(centralityValue - 1, new ArrayList<>());
            if (oldVertexReferences.contains(vertex)) {
                oldVertexReferences.remove((Integer) vertex);
                if (oldVertexReferences.isEmpty()) {
                    this.centralitySortedIndex.remove(centralityValue - 1);
                }
            }
        }

        // add to new centrality index
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

    /**
     * Returns set of random vertices from the graph
     * or empty set
     *
     * @param numVertices how many vertices to return
     * @return set of random vertices
     */
    public List<Integer> getRandomVertices(int numVertices) {
        if (numVertices < 1) return Collections.emptyList();
        if (getVertices().size() < numVertices)
            throw new IllegalArgumentException("There are not enough vertices in the graph");

        List<Integer> result = new LinkedList<>();

        while (result.size() < numVertices) {
            Integer vertex = getRandomVertex();
            if (!result.contains(vertex)) {
                result.add(vertex);
            }
        }

        return result;
    }

    public List<Integer> getRandomFriendsOfRandomVertices(int numVertices) {
        if (numVertices < 1) return Collections.emptyList();
        if (getVertices().size() < numVertices)
            throw new IllegalArgumentException("There are not enough vertices in the graph");

        List<Integer> result = new LinkedList<>();
        Set<Integer> visitedVertices = new HashSet<>();

        while (result.size() < numVertices) {
            if (visitedVertices.size() >= getVertices().size()) {
                throw new RuntimeException("There are not enough friends in the graph");
            }
            Integer vertex = getRandomVertex();
            visitedVertices.add(vertex);
            Integer friend = getRandomFriendOfVertex(vertex);
            if (friend == null) continue;
            if (!result.contains(friend))
                result.add(friend);
        }

        return result;
    }

    private int getRandomVertex() {
        HashSet<Integer> vertices = getVertices();
        return getRandomVertexFromSet(vertices);
    }

    private Integer getRandomFriendOfVertex(int vertex) {
        HashSet<Integer> neighbors = this.getNeighbors(vertex);
        if (neighbors.isEmpty()) return null;
        return getRandomVertexFromSet(neighbors);
    }

    private int getRandomVertexFromSet(Set<Integer> set) {
        final Random rand = new Random();
        int index = rand.nextInt(set.size());
        Iterator<Integer> iter = set.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();

    }
}
