package roadgraph;

import geography.GeographicPoint;

public class MapVertex extends GeographicPoint implements Comparable<MapVertex>{
    private java.lang.Double calculatedDistance;

    public MapVertex(GeographicPoint location) {
        super(location.getX(), location.getY());
        this.calculatedDistance = java.lang.Double.MAX_VALUE;
    }

    public double getCalculatedDistance() {
        return this.calculatedDistance;
    }

    public void setCalculatedDistance(double calculatedDistance) {
        this.calculatedDistance = calculatedDistance;
    }

    @Override
    public int compareTo(MapVertex otherVertex) {
        if (otherVertex == null) throw new NullPointerException();
        return this.calculatedDistance.compareTo(otherVertex.getCalculatedDistance());
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Lat: ").append(x);
        sb.append(", Lon: ").append(y);
        sb.append(", calculatedDistance: ").append(calculatedDistance);
        return sb.toString();
    }
}
