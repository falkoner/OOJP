package graph;

import java.util.Set;

/**
 * Created by Alex Filatau
 * Interface of some generic spread simulation (infection, idea etc) through
 * a social network. Allows multiple implementation options - absolute, conditional, probabilistic etc
 */
public interface Spread {

    /**
     * Process next generation of spread
     *
     * @return True if was able to spread
     */
    boolean step();

    int getCurrentStepNumber();

    int getNumberOfTriggeredVertices();

    /**
     * Are there any adjusting vertices that might be affected by next step
     *
     * @return True if there is room to spread
     */
    boolean isSpreadable();

    /**
     * Spread is complete if all vertices are triggered
     *
     * @return True if completed
     */
    boolean isCompleted();

    /**
     * Get Set of all vertices that were affected by previous steps
     *
     * @return Set of vertices
     */
    Set<Integer> getAllTriggeredVertices();

    /**
     * Get Set of vertices that were affected by last step
     *
     * @return Set of vertices
     */
    Set<Integer> getLastTriggeredVertices();

    /**
     * Resets state of the spread to initial state
     */
    void reset();

}
