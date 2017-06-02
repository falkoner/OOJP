package roadgraph;


/**
 * A class which represents and edge on a map: a road or segment of a road
 * between two intersections.
 * Each MapEdge has direction, it starts at one intersections and goes to some other one.
 * Than means that for a bi-directional street between two intersections you would need two
 * MapEdge nodes to describe it.
 */
public class MapEdge {
    private final MapVertex from;
    private final MapVertex to;
    private final String roadName;
    private final String roadType;
    private final double length;

    /**
     * Create new map edge
     *  @param from location the edge starts at
     * @param to location the edge ends at
     * @param roadName name of the road this edge belongs to
     * @param roadType type of the road
     * @param length length of the road in kilometers
     */
    public MapEdge(MapVertex from, MapVertex to, String roadName, String roadType, double length) {
        if (roadName == null) throw new IllegalArgumentException("roadName should have value");
        if (roadType == null) throw new IllegalArgumentException("roadType should have value");
        if (length < 0) throw new IllegalArgumentException("length should have non-negative value");

        this.from = from;
        this.to = to;
        this.roadName = roadName;
        this.roadType = roadType;
        this.length = length;
    }

    /**
     * Get location for this edge's starting point
     *
     * @return location for this edge's starting point
     */
    public MapVertex getFrom() {
        return from;
    }

    /**
     * Get location for this edge's end point
     *
     * @return location for this edge's end point
     */
    public MapVertex getTo() {
        return to;
    }

    /**
     * Get name of the road this edge belongs to
     *
     * @return road name
     */
    public String getRoadName() {
        return roadName;
    }

    /**
     * Get type of the road this edge belongs to
     *
     * @return type of the road
     */
    public String getRoadType() {
        return roadType;
    }

    /**
     * Get length of this section of this road
     *
     * @return length of the edge in kilometers
     */
    public double getLength() {
        return length;
    }
}
