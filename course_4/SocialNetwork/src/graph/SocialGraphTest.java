package graph;

import org.junit.Test;
import util.GraphLoader;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SocialGraphTest {
    @Test
    public void addEdgeShouldIncreaseCentrality() {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);
        assertEquals(1, graph.getCentrality(1));
    }

    @Test
    public void centralityShouldBe0WithoutEdges() {
        SocialGraph graph = new SocialGraph();
        graph.addVertex(1);
        assertEquals(0, graph.getCentrality(1));
    }

    @Test
    public void noCentralityOnMissingVertex() {
        SocialGraph graph = new SocialGraph();
        try {
            graph.getCentrality(1);
        } catch (Exception e) {
            assertEquals("Vertex [1] is not in graph", e.getMessage());
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void addingEdgeShouldNotIncreaseCentralityOfTargetVertex() {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);
        assertEquals(0, graph.getCentrality(2));
    }

    @Test
    public void vertexWithNoEdgesHas0Centrality() {
        SocialGraph graph = new SocialGraph();
        graph.addVertex(5);
        assertEquals(0, graph.getCentrality(5));
    }

    @Test
    public void shouldBeAbleToLoadGraphFromFile() {
        SocialGraph graph = new SocialGraph();
        GraphLoader.loadGraph(graph, "data/small_test_graph.txt");

        assertTrue("Should contain vertex 14", graph.containsVertex(14));
        assertEquals(2, graph.getCentrality(14));
    }

    @Test
    public void shouldBeAbleToGetTop3NodesByCentrality() {
        SocialGraph graph = new SocialGraph();
        graph.addVertex(0);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 1);
        graph.addEdge(2, 4);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);
        graph.addVertex(4);
        graph.addEdge(5, 1);

        List<Integer> result = graph.getTopVerticesByCentrality(3);
        assertEquals(3, result.size());
        assertEquals((Integer) 1, result.get(0));
        assertEquals((Integer) 2, result.get(1));
        assertEquals((Integer) 3, result.get(2));
    }

    @Test
    public void noTopNodesByCentralityOnEmptyGraph() {
        SocialGraph graph = new SocialGraph();
        assertEquals(0, graph.getTopVerticesByCentrality(1).size());
    }

    @Test
    public void manyTopNodesByCentalityOnOneVertexGraph() {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);
        List<Integer> result = graph.getTopVerticesByCentrality(10);
        assertEquals(1, result.size());
        assertEquals((Integer) 1, result.get(0));
    }

    @Test
    public void topByCentralityShouldNotDuplicateCountsOnIncrement() {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);

        List<Integer> result = graph.getTopVerticesByCentrality(10);
        assertEquals(1, result.size());
    }

    @Test
    public void toStringShouldShowCentrality() {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);

        String expected = "SocialGraph{vertices=[1, 2, 3], edges={1=[2, 3]}, centralitySortedIndex={2=[1]}}";
        assertEquals(expected, graph.toString());
    }

    @Test
    public void shouldGetExceptionOnEmptyGraph() {
        SocialGraph graph = new SocialGraph();
        try {
            graph.getRandomVertices(3);
        } catch (Exception e) {
            assertEquals("There are not enough vertices in the graph", e.getMessage());
            return;
        }
        fail();
    }

    @Test
    public void shouldBeAbleToGetRandomVertexFrom1VertexGraph() {
        SocialGraph graph = new SocialGraph();
        graph.addVertex(1);

        List<Integer> result = graph.getRandomVertices(1);
        assertEquals(1, result.size());
        assertTrue(result.contains(1));
    }

    @Test
    public void shouldHandleZeroRequestOnRandomVertex() {
        SocialGraph graph = new SocialGraph();
        graph.addVertex(1);

        List<Integer> result = graph.getRandomVertices(0);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotBeAbleToGetMoreVerticesThatThereIsInGraph() {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);
        try {
            graph.getRandomVertices(3);
        } catch (Exception e) {
            assertEquals("There are not enough vertices in the graph", e.getMessage());
            return;
        }
        fail();
    }

    @Test
    public void shouldBeAbleToGetAllVerticesAsRandomFromGraph() {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);
        graph.addEdge(5, 6);
        graph.addEdge(7, 8);
        graph.addEdge(9, 10);

        List<Integer> result = graph.getRandomVertices(10);
        assertEquals(10, result.size());
        graph.getVertices().forEach(vertex -> assertTrue(result.contains(vertex)));
    }

    @Test
    public void shouldBeAbleToGetSingleFriendOfRandomUser() {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);

        List<Integer> result = graph.getRandomFriendsOfRandomVertices(1);
        assertEquals(1, result.size());
        assertEquals((Integer) 2, result.get(0));
    }

    @Test
    public void shouldHandleVerticesWithNoEdges() {
        SocialGraph graph = new SocialGraph();
        graph.addVertex(5);
        try {
            graph.getRandomFriendsOfRandomVertices(1);
        } catch (Exception e) {
            assertEquals("There are not enough friends in the graph", e.getMessage());
            return;
        }
        fail();

    }

    @Test
    public void shouldBeAbleToGetAllFriendsOfRandomVertices() {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addVertex(0);
        graph.addVertex(5);

        List<Integer> result = graph.getRandomFriendsOfRandomVertices(3);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(2, 3, 4)));
    }

}