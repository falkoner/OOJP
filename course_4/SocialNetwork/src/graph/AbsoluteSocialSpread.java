package graph;

import java.util.HashSet;

/**
 * Models social spread mechanism when all neighbouring friends are getting triggered
 * on next step. That's the fastest way of spreading considering one generation is equal one distance from source.
 * <p>
 * Created by Alex Filatau.
 */
public class AbsoluteSocialSpread extends SocialSpread {
    /**
     * Any social spread requires target graph to work on
     *
     * @param graph target Graph
     */
    public AbsoluteSocialSpread(SocialGraph graph) {
        super(graph);
    }

    /**
     * Process next generation of spread
     *
     * @return step number
     */
    @Override
    public boolean step() {
        if (getAllTriggeredVertices().isEmpty())
            throw new IllegalStateException("Nothing to spread - not initialized");
        if (isCompleted()) return false;

        boolean spreaded = false;
        HashSet<Integer> newlyTriggered = new HashSet<>();

        for (Integer frontierVertex : getLastTriggeredVertices()) {
            for (Integer vertex : getNonTriggeredFriends(frontierVertex)) {
                this.triggeredVertices.add(vertex);
                newlyTriggered.add(vertex);
                spreaded = true;
            }
        }

        if (spreaded) {
            this.currentStepNumber++;
            this.lastTriggeredVertices = (HashSet<Integer>) newlyTriggered.clone();
        }

        return spreaded;
    }
}
