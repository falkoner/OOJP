/**
 * @author UCSD MOOC development team and YOU
 * <p>
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between
 */
package roadgraph;

import geography.GeographicPoint;
import util.GraphLoader;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author UCSD MOOC development team and YOU
 *         A class which represents a graph of geographic locations
 *         Nodes in the graph are intersections while edges are segments of the roads between them
 */
public class MapGraph {
    private HashSet<MapVertex> vertices; // need separate set since there are vertices without edges
    private HashMap<MapVertex, List<MapEdge>> edges; // HashMap to speed up search for edges from a vertex
    private int numVertices;
    private int numEdges;


    /**
     * Create a new empty MapGraph
     */
    public MapGraph() {
        vertices = new HashSet<>();
        edges = new HashMap<>();
        numEdges = 0;
        numVertices = 0;
    }

    /**
     * Get the number of vertices (road intersections) in the graph
     *
     * @return The number of vertices in the graph.
     */
    public int getNumVertices() {
        return this.numVertices;
    }

    /**
     * Return the intersections, which are the vertices in this graph.
     *
     * @return The vertices in this graph as GeographicPoints
     */
    public Set<GeographicPoint> getVertices() {
        return this.vertices.stream()
                .map(mapVertex -> (GeographicPoint) mapVertex)
                .collect(Collectors.toSet());
    }

    /**
     * Get the number of road segments in the graph
     *
     * @return The number of edges in the graph.
     */
    public int getNumEdges() {
        return this.numEdges;
    }

    /**
     * Add a node corresponding to an intersection at a Geographic Point
     * If the location is already in the graph or null, this method does
     * not change the graph.
     *
     * @param location The location of the intersection
     * @return true if a node was added, false if it was not (the node
     * was already in the graph, or the parameter is null).
     */
    public boolean addVertex(GeographicPoint location) {
        if (location == null) return false;

        MapVertex newVertex = new MapVertex(location);
        if (this.vertices.contains(newVertex)) return false;
        this.vertices.add(newVertex);
        this.numVertices++;

        return true;
    }

    /**
     * Adds a directed edge to the graph from pt1 to pt2.
     * Precondition: Both GeographicPoints have already been added to the graph
     *
     * @param from     The starting point of the edge
     * @param to       The ending point of the edge
     * @param roadName The name of the road
     * @param roadType The type of the road
     * @param length   The length of the road, in km
     * @throws IllegalArgumentException If the points have not already been
     *                                  added as nodes to the graph, if any of the arguments is null,
     *                                  or if the length is less than 0.
     */
    public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
                        String roadType, double length) throws IllegalArgumentException {

        if (from == null || to == null) throw new IllegalArgumentException("From and To parameters can't be empty");

        MapVertex fromVertex = new MapVertex(from);
        MapVertex toVertex = new MapVertex(to);
        if (!this.vertices.contains(fromVertex))
            throw new IllegalArgumentException("From location is not known vertex");
        if (!this.vertices.contains(toVertex)) throw new IllegalArgumentException("To location is not known vertex");

        MapEdge edge = new MapEdge(fromVertex, toVertex, roadName, roadType, length);
        List<MapEdge> edgesList = this.edges.getOrDefault(fromVertex, new ArrayList<>());
        if (edgesList.isEmpty()) this.edges.put(fromVertex, edgesList);
        edgesList.add(edge);
        this.numEdges++;
    }

    /**
     * Find the path from start to goal using breadth first search
     *
     * @param start The starting location
     * @param goal  The goal location
     * @return The list of intersections that form the shortest (unweighted)
     * path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return bfs(start, goal, temp);
    }

    /**
     * Find the path from start to goal using breadth first search
     *
     * @param start        The starting location
     * @param goal         The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest (unweighted)
     * path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start,
                                     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {

        if (start == null || goal == null) throw new IllegalArgumentException("Start and Goal should have values");
        if (start.equals(goal)) return new LinkedList<>();

        HashMap<GeographicPoint, GeographicPoint> parentMap = new HashMap<>();

        if (performBfs(start, goal, parentMap, nodeSearched)) return backtracePath(start, goal, parentMap);
        else return new LinkedList<>();
    }

    /**
     * @param start      start location of the search
     * @param goal       final location of the search
     * @param parentMap  empty map for GeographicPoint objects representing how to get from one location to another
     * @param visualizer utility hook to visualize search process in UI client
     * @return True if path was found and False if there is no path
     */
    private boolean performBfs(GeographicPoint start, GeographicPoint goal, HashMap<GeographicPoint, GeographicPoint> parentMap,
                               Consumer<GeographicPoint> visualizer) {
        HashSet<GeographicPoint> visited = new HashSet<>();
        Queue<GeographicPoint> toExplore = new LinkedList<>();
        toExplore.add(start);

        while (!toExplore.isEmpty()) {
            GeographicPoint currentVertex = toExplore.remove();
            visualizer.accept(currentVertex);

            if (currentVertex.equals(goal)) return true;

            // one way streets with dead-ends will result in vertex not having outbound edges
            List<MapEdge> currentEdges = this.edges.getOrDefault(currentVertex, Collections.emptyList());

            for (MapEdge edge : currentEdges) {
                GeographicPoint nextVertex = edge.getTo();
                if (!visited.contains(nextVertex)) {
                    visited.add(nextVertex);
                    toExplore.add(nextVertex);
                    parentMap.put(nextVertex, currentVertex);
                }
            }
        }
        return false;
    }

    /**
     * Method to build final version of the path based on the graph traversal history
     *
     * @param start     start location of the search
     * @param goal      final location of the search
     * @param parentMap map of location GeographicPoint objects representing how to get from one location to another
     * @return list of GeographicPoint objects in sequence from start to goal aka "path"
     */
    private List<GeographicPoint> backtracePath(GeographicPoint start, GeographicPoint goal,
                                                HashMap parentMap) {
        LinkedList<GeographicPoint> path = new LinkedList<>();
        GeographicPoint currentVertex = goal;
        while (!currentVertex.equals(start)) {
            path.addFirst(currentVertex);
            currentVertex = (GeographicPoint) parentMap.get(currentVertex);
        }

        path.addFirst(start);

        return path;
    }


    /**
     * Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start The starting location
     * @param goal  The goal location
     * @return The list of intersections that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        // You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return dijkstra(start, goal, temp);
    }

    /**
     * Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start        The starting location
     * @param goal         The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(GeographicPoint start,
                                          GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
        // TODO: Implement this method in WEEK 4

        if (start == null || goal == null) throw new IllegalArgumentException("Start and Goal should have values");
        if (start.equals(goal)) return new LinkedList<>();

        HashMap<MapVertex, MapVertex> parentMap = new HashMap<>();
        if (performDijkstra(start, goal, parentMap, nodeSearched)) {
            return backtracePath(start, goal, parentMap);
        }
        else return new LinkedList<>();
    }

    private boolean performDijkstra(GeographicPoint start, GeographicPoint goal,
                                    HashMap<MapVertex, MapVertex> parentMap,
                                    Consumer<GeographicPoint> visualizer) {
        MapVertex startVertex = new MapVertex(start);
        MapVertex goalVertex = new MapVertex(goal);

        HashSet<MapVertex> visited = new HashSet<>();
        PriorityQueue<MapVertex> toExplore = new PriorityQueue<>();
        HashMap<MapVertex, Double> bestDistance = new HashMap();
        startVertex.setCalculatedDistance(0.0);
        toExplore.add(startVertex);

        while (!toExplore.isEmpty()) {
            MapVertex currentVertex = toExplore.remove();
            visualizer.accept(currentVertex);

            if (!visited.contains(currentVertex)) {
                visited.add(currentVertex);
                System.out.println("Visiting " + currentVertex);
                if (currentVertex.equals(goalVertex)) return true;

                // one way streets with dead-ends will result in vertex not having outbound edges
                List<MapEdge> currentEdges = this.edges.getOrDefault(currentVertex, Collections.emptyList());

                for (MapEdge edge : currentEdges) {
                    MapVertex nextVertex = edge.getTo();
                    if (!visited.contains(nextVertex)) {
                        double currentDistance = currentVertex.getCalculatedDistance();
                        double potentialDistance = currentDistance + edge.getLength();

                        if (bestDistance.get(nextVertex) == null || potentialDistance < bestDistance.get(nextVertex)) {
                            nextVertex.setCalculatedDistance(potentialDistance);
                            bestDistance.put(nextVertex, potentialDistance);
                            toExplore.add(nextVertex);
                            parentMap.put(nextVertex, currentVertex);
                        }

                    }
                }
            }
        }
        return false;

    }

    /**
     * Find the path from start to goal using A-Star search
     *
     * @param start The starting location
     * @param goal  The goal location
     * @return The list of intersections that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return aStarSearch(start, goal, temp);
    }

    /**
     * Find the path from start to goal using A-Star search
     *
     * @param start        The starting location
     * @param goal         The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(GeographicPoint start,
                                             GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
        // TODO: Implement this method in WEEK 4

        // Hook for visualization.  See writeup.
        //nodeSearched.accept(next.getLocation());

        return null;
    }


    public static void main(String[] args) {
//        System.out.print("Making a new map...");
//        MapGraph firstMap = new MapGraph();
//        System.out.print("DONE. \nLoading the map...");
//        GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
//        System.out.println("DONE.");

        // You can use this method for testing.

		
		/* Here are some test cases you should try before you attempt 
         * the Week 3 End of Week Quiz, EVEN IF you score 100% on the
		 * programming assignment.
		 */
        MapGraph simpleTestMap = new MapGraph();
        GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);

        GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
        GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);

        System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
        List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart, testEnd);
//        List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart, testEnd);
//        List<GeographicPoint> testroute3 = simpleTestMap.bfs(testStart, testEnd);

        printRouteDetails(testroute);
//        MapGraph testMap = new MapGraph();
//        GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
//
//        // A very simple test using real data
//        testStart = new GeographicPoint(32.869423, -117.220917);
//        testEnd = new GeographicPoint(32.869255, -117.216927);
//        System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
//        testroute = testMap.dijkstra(testStart, testEnd);
//        testroute2 = testMap.aStarSearch(testStart, testEnd);
//        testroute3 = testMap.bfs(testStart, testEnd);
//        printRouteDetails(testroute);
//
//        // A slightly more complex test using real data
//        testStart = new GeographicPoint(32.8674388, -117.2190213);
//        testEnd = new GeographicPoint(32.8697828, -117.2244506);
//        System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
//        testroute = testMap.dijkstra(testStart, testEnd);
//        testroute2 = testMap.aStarSearch(testStart, testEnd);
//        testroute3 = testMap.bfs(testStart, testEnd);
//        printRouteDetails(testroute);
//
//		/* Use this code in Week 3 End of Week Quiz */
//        MapGraph theMap = new MapGraph();
//        System.out.print("DONE. \nLoading the map...");
//        GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
//        System.out.println("DONE.");
//
//        GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
//        GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
//
//        List<GeographicPoint> route = theMap.dijkstra(start, end);
//        List<GeographicPoint> route2 = theMap.aStarSearch(start, end);
//        List<GeographicPoint> route3 = testMap.bfs(testStart, testEnd);
//
//        printRouteDetails(testroute);
    }

    /**
     * Utility method to pretty-print the route details generated be search functions
     *
     * @param route list of points in the path
     */
    private static void printRouteDetails(List<GeographicPoint> route) {
        int pathLength = route.size() - 2;
        if (pathLength < 0) {
            System.out.println("No route");
            return;
        } else if (pathLength == 0) {
            System.out.println("Direct route from start to goal: " + route);
            return;
        }

        System.out.println("Number of vertices between Start and Goal: " + pathLength);

        String printableRoute = route.stream()
                .map(GeographicPoint::toString)
                .collect(Collectors.joining(" -->\n"));
        System.out.println(printableRoute);
    }

}
