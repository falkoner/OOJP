package graph;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * Created by Alex Filatau.
 */
public class AbsoluteSocialSpreadTest {
    @Test
    public void singleStepInSimpleStarGraphShouldCompleteIt() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(1, 5);
        spread.setStartingPoint(1);

        assertTrue(spread.step());
        assertEquals(1, spread.getCurrentStepNumber());
        assertTrue(spread.isCompleted());
        assertTrue(spread.getAllTriggeredVertices().containsAll(asList(1, 2, 3, 4, 5)));
    }

    @Test
    public void stepShouldNotAdvanceOnEmptyGraph() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        graph.addVertex(1);
        spread.setStartingPoint(1);

        assertFalse(spread.step());
        assertEquals(0, spread.getCurrentStepNumber());
    }

    @Test
    public void stepShouldResultInExceptionIfSpreadIsNotStarted() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        try {
            assertFalse(spread.step());
        } catch (Exception e) {
            assertEquals("Nothing to spread - not initialized", e.getMessage());
            return;
        }
        fail();
    }

    @Test
    public void stepShouldNotAdvanceOnCompletedGraph() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        graph.addEdge(1, 2);
        spread.setStartingPoint(1);
        assertTrue(spread.step());
        assertEquals(1, spread.getCurrentStepNumber());
        assertTrue(spread.isCompleted());

        assertFalse(spread.step());
        assertEquals(1, spread.getCurrentStepNumber());
    }

    @Test
    public void spreadShouldNotAdvanceOnNotConnectedVertices() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        graph.addVertex(1);
        graph.addVertex(2);

        spread.setStartingPoint(1);
        assertFalse(spread.step());
        assertFalse(spread.isVertexTriggered(2));
        assertFalse(spread.isSpreadable());
        assertFalse(spread.isCompleted());
    }

    @Test
    public void shouldReachFriendOfAFriendIn2Steps() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        spread.setStartingPoint(1);
        spread.step();
        assertFalse(spread.isVertexTriggered(3));
        spread.step();
        assertEquals(2, spread.getCurrentStepNumber());
        assertTrue(spread.isVertexTriggered(3));
    }

    @Test
    public void currentStepNumberShouldBe0AfterInitialization() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);
        assertEquals(0, spread.getCurrentStepNumber());
    }

    @Test
    public void numberOfTriggeredVerticesShouldBe0BeforeStartOfSpread() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);
        assertEquals(0, spread.getNumberOfTriggeredVertices());
    }

    @Test
    public void numberOfTriggeredVerticesShouldBeCountedCorrectly() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);
        spread.setStartingPoint(1);
        assertEquals(1, spread.getNumberOfTriggeredVertices());
        assertTrue(spread.step());
        assertEquals(3, spread.getNumberOfTriggeredVertices());
    }

    @Test
    public void isSpreadableShouldCorrectlyIdentifySpreadPotential() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);
        spread.setStartingPoint(1);
        assertTrue(spread.isSpreadable());
        spread.step();
        assertTrue(spread.isSpreadable());
        spread.step();
        assertFalse(spread.isSpreadable());
    }

    @Test
    public void getNonTriggeredFriendsShouldGiveCorrectListOfFriends() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);
        spread.setStartingPoint(1);

        assertTrue(spread.getNonTriggeredFriends(1).containsAll(asList(2, 3)));
        assertFalse(spread.getNonTriggeredFriends(1).containsAll(asList(1, 4, 5)));
    }

    @Test
    public void isVertexTriggeredShouldGiveCorrectStateOfVertex() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        spread.setStartingPoint(1);
        assertTrue(spread.isVertexTriggered(1));
        assertFalse(spread.isVertexTriggered(2));
        assertFalse(spread.isVertexTriggered(3));

        spread.step();
        assertTrue(spread.isVertexTriggered(1));
        assertTrue(spread.isVertexTriggered(2));
        assertFalse(spread.isVertexTriggered(3));
    }

    @Test
    public void spreadIsCompletedWhenSingleVertexGraphIsInitialized() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);
        graph.addVertex(1);
        spread.setStartingPoint(1);
        assertTrue(spread.isCompleted());
    }

    @Test
    public void spreadIsNotCompletedWhenStartingPointIsNotSet() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);
        graph.addVertex(1);
        assertFalse(spread.isCompleted());
    }

    @Test
    public void spreadIsCompletedWhenAdvancedToAnEndWithSteps() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);
        spread.setStartingPoint(1);
        assertFalse(spread.isCompleted());
        spread.step();
        assertFalse(spread.isCompleted());
        spread.step();
        assertTrue(spread.isCompleted());
    }

    @Test
    public void itShouldBePossibleToTrackLastAndAllTriggeredVertices() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);

        spread.setStartingPoint(1);
        assertFalse(spread.getAllTriggeredVertices().containsAll(asList(2, 3, 4, 5)));
        assertFalse(spread.getLastTriggeredVertices().containsAll(asList(2, 3, 4, 5)));
        assertTrue(spread.getAllTriggeredVertices().containsAll(asList(1)));
        assertTrue(spread.getLastTriggeredVertices().containsAll(asList(1)));

        spread.step();
        assertFalse(spread.getAllTriggeredVertices().containsAll(asList(4, 5)));
        assertFalse(spread.getLastTriggeredVertices().containsAll(asList(1, 4, 5)));
        assertTrue(spread.getAllTriggeredVertices().containsAll(asList(1, 2, 3)));
        assertTrue(spread.getLastTriggeredVertices().containsAll(asList(2, 3)));
    }

    @Test
    public void resetShouldRollBackNonCompletedSpread() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);

        spread.setStartingPoint(1);
        spread.step();
        spread.reset();

        assertFalse(spread.isCompleted());
        assertTrue(spread.getAllTriggeredVertices().isEmpty());
        assertFalse(spread.isVertexTriggered(1));
    }

    @Test
    public void resetShouldRollBackCompletedSpread() {
        SocialGraph graph = new SocialGraph();
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);

        spread.setStartingPoint(1);
        spread.step();
        spread.step();
        spread.reset();

        assertFalse(spread.isCompleted());
        assertTrue(spread.getAllTriggeredVertices().isEmpty());
        assertFalse(spread.isVertexTriggered(1));
    }

}