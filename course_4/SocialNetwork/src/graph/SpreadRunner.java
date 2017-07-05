package graph;

import util.GraphLoader;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Alex Filatau on 7/4/17.
 * <p>
 * Run your experiments on a SocialGraph and any Spread model using SpreadRunner.
 * <p>
 * Allows to run multiple experiments loading a graph only once and reusing it later.
 * Tracks results and reports them.
 * <p>
 * Each experiment involves:
 * - setting 1 or more starting points for the spread
 * - setting up specified number of sensors (using built in SocialGraph methods)
 * - running the spread cycle until it's completed or stuck or hit a sensor
 * <p>
 * Experiment results include then:
 * - spread state
 * - sensor ID (if hit)
 * - step count
 */
public class SpreadRunner {
    private final SocialGraph graph;
    private final SocialSpread spread;
    private HashSet<Integer> sensors;
    private HashSet<Integer> startingPoints;
    private Integer triggeredSensor;
    private String typeOfSensors;

    public SpreadRunner(SocialGraph graph, SocialSpread spread) {
        this.graph = graph;
        this.spread = spread;
        reset();
    }

    /**
     * Reset the model so it can be reused without loading new graph
     */
    public void reset() {
        this.sensors = new HashSet<>();
        this.triggeredSensor = null;
        this.startingPoints = new HashSet<>();
        this.typeOfSensors = null;
        this.spread.reset();
    }

    /**
     * Start the spread and precess it until a sensor hit, no more room to spread or spread is completed
     */
    public void run() {
        do {
//            System.out.println(String.format("Triggered: %s", this.spread.getLastTriggeredVertices()));
            for (Integer triggeredVertex : this.spread.getLastTriggeredVertices()) {
                if (this.sensors.contains(triggeredVertex)) {
                    this.triggeredSensor = triggeredVertex;
                    return;
                }
            }
        } while (this.spread.step());
    }

    /**
     * Getter for the list of sensors
     *
     * @return List of sensor vertices
     */
    public HashSet<Integer> getSensors() {
        return sensors;
    }

    /**
     * Getter for the list of starting points
     *
     * @return List of spread start vertices
     */
    public HashSet<Integer> getStartingPoints() {
        return startingPoints;
    }

    /**
     * Getter for triggered sensor
     *
     * @return vertex with triggered sensor
     */
    public Integer getTriggeredSensor() {
        return triggeredSensor;
    }

    /**
     * Getter for type of sensors setup
     *
     * @return String with type of sensors set up
     */
    public String getTypeOfSensors() {
        return typeOfSensors;
    }

    /**
     * Generates report of a finished spread including information about
     * - number of steps it took to stop the spread
     * - size of the graph
     * - type of the sensors
     * - number of sensors
     * - is spread completed
     * - is it still spreadable
     * - ID of the sensor triggered
     *
     * @return String report
     */
    public String getReport() {
        StringBuilder sb = new StringBuilder();
        if (getTriggeredSensor() != null)
            sb.append("Sensor triggered on vertex: ").append(getTriggeredSensor()).append("\n");
        else sb.append("Non sensors triggered\n");
        sb.append("Number of steps taken in the spread: ").append(this.spread.getCurrentStepNumber()).append("\n");
        sb.append("Spread fully completed: ").append(this.spread.isCompleted()).append("\n");
        sb.append("Is there still room to spread: ").append(this.spread.isSpreadable()).append("\n");
        sb.append("Size of the graph: ").append(this.graph.getVertices().size()).append("\n");
        sb.append("Type of the sensors: ").append(getTypeOfSensors()).append("\n");
        sb.append("Number of the sensors: ").append(getSensors().size()).append("\n");
        sb.append("List of the sensors: ").append(getSensors()).append("\n");
        sb.append("Number of spread starting points: ").append(getStartingPoints().size()).append("\n");
        sb.append("List of the starting points of the spread: ").append(getStartingPoints()).append("\n");

        return sb.toString();
    }

    /**
     * Set a starting point for spread to a specific vertex
     *
     * @param vertex target vertex ID
     */
    public void setSpreadPoint(int vertex) {
        this.spread.setStartingPoint(vertex);
        this.startingPoints.add(vertex);
    }

    /**
     * Set specified number of random starting points for spread
     *
     * @param spreadCount number of spread starting points
     */
    public void setRandomSpreadPoints(int spreadCount) {
        this.graph.getRandomVertices(spreadCount).forEach(this::setSpreadPoint);
    }

    /**
     * Sets specified number of sensors on random vertices
     *
     * @param sensorCount number of sensors to place
     * @return List of sensors
     */
    public List<Integer> setSensorsRandomByCount(int sensorCount) {
        isSensorSet();
        List<Integer> vertices = this.graph.getRandomVertices(sensorCount);
        this.sensors.addAll(vertices);
        this.typeOfSensors = String.format("Random-%s", sensorCount);
        return vertices;
    }

    /**
     * Sets sensors randomly on graph vertices
     *
     * @param sensorPercentile number of sensors to place in % from graph size
     * @return List of sensors
     */
    public List<Integer> setSensorsRandomByPercentile(int sensorPercentile) {
        isSensorSet();
        int sensorCount = this.graph.getVertices().size() / 100 * sensorPercentile;
        List<Integer> vertices = this.graph.getRandomVertices(sensorCount);
        this.sensors.addAll(vertices);
        this.typeOfSensors = String.format("Random-%s%", sensorPercentile);
        return vertices;
    }

    /**
     * Sets sensors on vertices with most centrality
     *
     * @param sensorCount number of sensors to place
     * @return List of sensors
     */
    public List<Integer> setSensorsCentralityByCount(int sensorCount) {
        isSensorSet();
        List<Integer> vertices = this.graph.getTopVerticesByCentrality(sensorCount);
        this.sensors.addAll(vertices);
        this.typeOfSensors = String.format("Centrality-%s", sensorCount);
        return vertices;
    }

    /**
     * Sets sensors on the vertices with most centrality
     *
     * @param sensorPercentile number of sensors to place in % from graph size
     * @return List of sensors
     */
    public List<Integer> setSensorsCentralityByPercentile(int sensorPercentile) {
        isSensorSet();
        int sensorCount = this.graph.getVertices().size() / 100 * sensorPercentile;
        List<Integer> vertices = this.graph.getTopVerticesByCentrality(sensorCount);
        this.sensors.addAll(vertices);
        this.typeOfSensors = String.format("Centrality-%s%", sensorPercentile);
        return vertices;
    }

    /**
     * Sets sensors on random friends of random vertices following Friends' paradox
     *
     * @param sensorCount number of sensors to place
     * @return List of sensors
     */
    public List<Integer> setSensorsFriendsParadoxByCount(int sensorCount) {
        isSensorSet();
        List<Integer> vertices = this.graph.getRandomFriendsOfRandomVertices(sensorCount);
        this.sensors.addAll(vertices);
        this.typeOfSensors = String.format("Friends-Paradox-%s", sensorCount);
        return vertices;
    }

    /**
     * Sets sensors on random friends of random vertices following Friends' paradox
     *
     * @param sensorPercentile number of sensors to place in % from graph size
     * @return List of sensors
     */
    public List<Integer> setSensorsFriendsParadoxByPercentile(int sensorPercentile) {
        isSensorSet();
        int sensorCount = this.graph.getVertices().size() / 100 * sensorPercentile;
        List<Integer> vertices = this.graph.getRandomFriendsOfRandomVertices(sensorCount);
        this.sensors.addAll(vertices);
        this.typeOfSensors = String.format("Friends-Paradox-%s%", sensorPercentile);
        return vertices;
    }

    private void isSensorSet() {
        if (getTypeOfSensors() != null) {
            throw new IllegalStateException("Sensors are already set");
        }
    }


    /**
     * Main method to set up and run actual experiments
     *
     * @param args
     */
    public static void main(String[] args) {
        SocialGraph graph = new SocialGraph();
        GraphLoader.loadGraph(graph, "data/small_test_graph.txt");
        System.out.println(graph);
        AbsoluteSocialSpread spread = new AbsoluteSocialSpread(graph);

        SpreadRunner runner = new SpreadRunner(graph, spread);
        runner.setRandomSpreadPoints(1);
        runner.setSensorsRandomByCount(1);
        runner.run();
        System.out.println(runner.getReport());
        runner.reset();
    }
}
