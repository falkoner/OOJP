package graph;

import org.junit.Test;
import util.GraphLoader;

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
    public void shouldBeAbleToLoadGraphFromFile() {
        SocialGraph graph = new SocialGraph();
        GraphLoader.loadGraph(graph, "data/small_test_graph.txt");

        assertTrue("Should contain vertex 14", graph.containsVertex(14));
        assertEquals(2, graph.getCentrality(14));
    }

}