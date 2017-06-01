package module6;

import java.util.ArrayList;

/**
 * Created by filatau on 5/14/17.
 */
public class CountryStats {

    private String name;
    private int numQuakes;
    private Float maxMagnitude;
    private Float minMagnitude;
    private Float avgMagnitude;
    private ArrayList<Float> quakes;

    public CountryStats(String name) {
        this.name = name;
        this.quakes = new ArrayList<>();
    }

    public void addQuake(float quake) {
        this.quakes.add(quake);
        recountQuakes();
    }

    private void recountQuakes() {
        this.numQuakes = quakes.size();
        if (this.numQuakes > 0) {
            this.maxMagnitude = quakes.stream().reduce(Float::max).get();
            this.minMagnitude = quakes.stream().reduce(Float::min).get();
            this.avgMagnitude = quakes.stream().reduce(Float::sum).get() / (float) this.numQuakes;
        } else {
            this.maxMagnitude = null;
            this.minMagnitude = null;
            this.avgMagnitude = null;
        }
    }

    public String getName() {
        return name;
    }

    public int getNumQuakes() {
        return numQuakes;
    }

    public float getMaxMagnitude() {
        return maxMagnitude;
    }

    public float getMinMagnitude() {
        return minMagnitude;
    }

    public float getAvgMagnitude() {
        return avgMagnitude;
    }
}
