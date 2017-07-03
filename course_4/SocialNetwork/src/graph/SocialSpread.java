package graph;

import java.util.*;

/**
 * Created by Alex Filatau.
 * Abstract generic spread common for all implementation options
 */
abstract class SocialSpread implements Spread {

    protected final SocialGraph graph;
    protected int currentStepNumber;
    protected HashSet<Integer> triggeredVertices;
    protected HashSet<Integer> lastTriggeredVertices;

    /**
     * Any social spread requires target graph to work on
     *
     * @param graph target Graph
     */
    public SocialSpread(SocialGraph graph) {
        this.graph = graph;
        this.reset();
    }

    @Override
    public int getCurrentStepNumber() {
        return this.currentStepNumber;
    }

    @Override
    public int getNumberOfTriggeredVertices() {
        return this.triggeredVertices.size();
    }

    /**
     * Are there any adjusting vertices that might be affected by next step
     *
     * @return
     */
    @Override
    public boolean isSpreadable() {
        if (isCompleted()) return false;
        if (this.graph.getVertices().isEmpty()) return false;
        if (triggeredVertices.isEmpty()) return false;
        if (lastTriggeredVertices.isEmpty()) return false;
        for (Integer vertex : lastTriggeredVertices) {
            if (!getNonTriggeredFriends(vertex).isEmpty()) return true;
        }
        return false;
    }

    /**
     * Returns List of friends of specified vertex which are not marked as triggered
     *
     * @param vertex target vertex
     * @return List of vertices
     */
    public List<Integer> getNonTriggeredFriends(Integer vertex) {
        HashSet<Integer> friends = this.graph.getNeighbors(vertex);

        if (friends.isEmpty()) return Collections.emptyList();

        List<Integer> result = new LinkedList<>();
        for (Integer friend : friends) {
            if (!isVertexTriggered(friend)) result.add(friend);
        }

        return result;

    }

    /**
     * Check if specified vertex is marked as triggered
     * Throws
     *
     * @param vertex vertex to check
     * @return True if vertex marked as triggered
     */
    public boolean isVertexTriggered(int vertex) {
        if (!this.graph.containsVertex(vertex))
            throw new IllegalArgumentException(String.format("Requested vertex [%s]is not in graph", vertex));
        return this.getAllTriggeredVertices().contains(vertex);
    }

    /**
     * Spread is complete if all vertices are triggered
     *
     * @return True if completed
     */
    @Override
    public boolean isCompleted() {
        return getAllTriggeredVertices().size() == this.graph.getVertices().size();
    }

    /**
     * Get Set of all vertices that were affected by previous steps
     *
     * @return Set of vertices
     */
    @Override
    public Set<Integer> getAllTriggeredVertices() {
        return this.triggeredVertices;
    }

    /**
     * Get Set of vertices that were affected by last step
     *
     * @return Set of vertices
     */
    @Override
    public Set<Integer> getLastTriggeredVertices() {
        return this.lastTriggeredVertices;
    }

    /**
     * Resets state of the spread to initial state
     */
    @Override
    public void reset() {
        this.currentStepNumber = 0;
        this.triggeredVertices = new HashSet<>();
        this.lastTriggeredVertices = new HashSet<>();
    }

}
