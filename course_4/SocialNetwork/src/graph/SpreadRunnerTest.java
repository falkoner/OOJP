package graph;

import org.junit.Test;

/**
 * Created by Alex Filatau on 7/4/17.
 */
public class SpreadRunnerTest {
    @Test
    public void reset() throws Exception {

    }

    @Test
    public void run() throws Exception {
        SocialGraph graph = new SocialGraph();
        graph.addEdge(1, 2);
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);
        SpreadRunner runner = new SpreadRunner(graph, spread);

        runner.setRandomSpreadPoints(1);
        runner.setSensorsRandomByCount(1);
        runner.run();
        System.out.println(runner.getReport());
    }

    @Test
    public void getReport() throws Exception {
    }

}