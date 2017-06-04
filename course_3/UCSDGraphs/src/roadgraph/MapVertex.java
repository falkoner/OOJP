package roadgraph;

import geography.GeographicPoint;

public class MapVertex extends GeographicPoint implements Comparable<MapVertex>{
    private java.lang.Double approximatePathLength;
    private java.lang.Double traveledDistance;

    public MapVertex(GeographicPoint location) {
        super(location.getX(), location.getY());
        this.traveledDistance = java.lang.Double.MAX_VALUE;
        this.approximatePathLength = java.lang.Double.MAX_VALUE;
    }

    public double getTraveledDistance() {
        return this.traveledDistance;
    }

    public void setTraveledDistance(double traveledDistance) {
        this.traveledDistance = traveledDistance;
        this.approximatePathLength = traveledDistance; // set to known minumum - allows for A*Star and Dijstra search
    }

    public java.lang.Double getApproximatePathLength() {
        return approximatePathLength;
    }

    public void setApproximatePathLength(double approximatePathLength) {
        this.approximatePathLength = approximatePathLength;
    }

    @Override
    public int compareTo(MapVertex otherVertex) {
        if (otherVertex == null) throw new NullPointerException();
        return this.approximatePathLength.compareTo(otherVertex.getApproximatePathLength());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Lat: ").append(x);
        sb.append(", Lon: ").append(y);
//        sb.append(", traveledDistance: ").append(traveledDistance); // debug code
        return sb.toString();
    }


}
