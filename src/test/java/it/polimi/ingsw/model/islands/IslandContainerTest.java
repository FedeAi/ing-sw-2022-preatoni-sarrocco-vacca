package it.polimi.ingsw.model.islands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Method IslandContainerTest tests the IslandContainer class.
 */
class IslandContainerTest {

    /**
     * Method joinPrevIslandTest tests the joining of an island to the previous one.
     */
    @Test
    @DisplayName("Join previous island test")
    void joinPrevIslandTest() {
        Island i0 = new BaseIsland();
        Island i1 = new BaseIsland();
        Island i2 = new BaseIsland();
        Island i3 = new BaseIsland();
        Island i4 = new BaseIsland();
        IslandContainer islandContainer = new IslandContainer(new LinkedList<>(List.of(i1, i2, i3)));

        assertEquals(2, islandContainer.prevIslandIndex(0));
        assertEquals(1, islandContainer.prevIslandIndex(2));
        assertEquals(0, islandContainer.prevIslandIndex(1));
        assertEquals(i3, islandContainer.prevIsland(0));
        assertEquals(i2, islandContainer.prevIsland(2));
        assertEquals(i1, islandContainer.prevIsland(1));

        islandContainer = new IslandContainer(new LinkedList<>(List.of(i0, i1, i2, i3, i4)));
        islandContainer.joinPrevIsland(2);

        assertEquals(4, islandContainer.size());
        assertTrue(islandContainer.get(1) instanceof SuperIsland);
        assertEquals(2, ((SuperIsland) islandContainer.get(1)).islands.size());
        assertTrue(((SuperIsland) islandContainer.get(1)).islands.containsAll(List.of(i1, i2)));
    }

    /**
     * Method joinNextIslandTest tests the joining of an island to the next one.
     */
    @Test
    @DisplayName("Join next island test")
    void joinNextIslandTest() {
        Island i0 = new BaseIsland();
        Island i1 = new BaseIsland();
        Island i2 = new BaseIsland();
        Island i3 = new BaseIsland();
        Island i4 = new BaseIsland();
        IslandContainer islandContainer = new IslandContainer(new LinkedList<>(List.of(i1, i2, i3)));

        assertEquals(1, islandContainer.nextIslandIndex(0));
        assertEquals(0, islandContainer.nextIslandIndex(2));
        assertEquals(2, islandContainer.nextIslandIndex(1));
        assertEquals(i2, islandContainer.nextIsland(0));
        assertEquals(i1, islandContainer.nextIsland(2));
        assertEquals(i3, islandContainer.nextIsland(1));

        islandContainer = new IslandContainer(new LinkedList<>(List.of(i0, i1, i2, i3, i4)));
        islandContainer.joinNextIsland(2);

        assertEquals(4, islandContainer.size());
        assertTrue(islandContainer.get(2) instanceof SuperIsland);
        assertEquals(2, ((SuperIsland) islandContainer.get(2)).islands.size());
        assertTrue(((SuperIsland) islandContainer.get(2)).islands.containsAll(List.of(i2, i3)));
    }
}