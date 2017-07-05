package graph;

import org.junit.Test;
import util.GraphLoader;

import static org.junit.Assert.*;

/**
 * Created by Alex Filatau on 7/4/17.
 */
public class SpreadRunnerTest {
    @Test
    public void resetShouldAllowToReuseGraphAndSpread() throws Exception {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 5);
        graph.addEdge(3, 4);
        graph.addEdge(4, 3);
        graph.addEdge(3, 1);
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);
        SpreadRunner runner = new SpreadRunner(graph, spread);

        runner.setSpreadPoint(4);
        runner.setSensorsCentralityByCount(1); // should be 1
        runner.run();
        runner.reset();

        assertEquals(0, spread.getCurrentStepNumber());
        assertTrue(spread.getAllTriggeredVertices().isEmpty());
        assertEquals(0, runner.getSensors().size());
        assertNull(runner.getTriggeredSensor());
    }

    @Test
    public void inPredictableGraphASensorShouldPredictablyDetectSpread() throws Exception {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 5);
        graph.addEdge(3, 4);
        graph.addEdge(4, 3);
        graph.addEdge(3, 1);
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);
        SpreadRunner runner = new SpreadRunner(graph, spread);

        runner.setSpreadPoint(4);
        runner.setSensorsCentralityByCount(1); // should be 1
        runner.run();
        assertEquals(2, spread.getCurrentStepNumber());
        assertTrue(spread.isVertexTriggered(1));
    }

    @Test
    public void stepsShouldBe0IfSpreadStartsAtSensorNode() throws Exception {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 5);
        graph.addEdge(3, 4);
        graph.addEdge(4, 3);
        graph.addEdge(3, 1);
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);
        SpreadRunner runner = new SpreadRunner(graph, spread);

        runner.setSpreadPoint(1);
        runner.setSensorsCentralityByCount(1); // should be 1
        runner.run();
        assertEquals(0, spread.getCurrentStepNumber());
        assertTrue(spread.isVertexTriggered(1));
    }

    @Test
    public void shouldNotBeAbleToSetUpDifferentSensorTypes() {
        SocialGraph graph = new SocialGraph();
        GraphLoader.loadGraph(graph, "data/small_test_graph.txt");
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);
        SpreadRunner runner = new SpreadRunner(graph, spread);

        runner.setSensorsCentralityByCount(1);
        try {
            runner.setSensorsFriendsParadoxByCount(1);
        } catch (Exception e) {
            assertEquals("Sensors are already set", e.getMessage());
            return;
        }
        fail();

    }
}