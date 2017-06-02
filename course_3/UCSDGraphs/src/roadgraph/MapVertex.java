package roadgraph;

import geography.GeographicPoint;

public class MapVertex extends GeographicPoint{
    private double calculatedDistance;

    public MapVertex(GeographicPoint location) {
        super(location.getX(), location.getY());
        resetCalculatedValues();
    }

    public double getCalculatedDistance() {
        return this.calculatedDistance;
    }

    public double incrementCalculatedDistance(double increment) {
        this.calculatedDistance += increment;
        return getCalculatedDistance();
    }

    public void resetCalculatedValues() {
        this.calculatedDistance = 0;
    }

}
