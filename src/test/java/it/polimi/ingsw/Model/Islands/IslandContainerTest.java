package it.polimi.ingsw.Model.Islands;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IslandContainerTest {
    @Test
    void prevIslandIndex() {
        Island i1 = new BaseIsland();
        Island i2 = new BaseIsland();
        Island i3 = new BaseIsland();
        IslandContainer islandContainer = new IslandContainer(new LinkedList<>(List.of(i1, i2, i3)));

        assertEquals(2, islandContainer.prevIslandIndex(0));
        assertEquals(1, islandContainer.prevIslandIndex(2));
        assertEquals(0, islandContainer.prevIslandIndex(1));
    }

    @Test
    void nextIslandIndex() {
        Island i1 = new BaseIsland();
        Island i2 = new BaseIsland();
        Island i3 = new BaseIsland();
        IslandContainer islandContainer = new IslandContainer(new LinkedList<>(List.of(i1, i2, i3)));

        assertEquals(1, islandContainer.nextIslandIndex(0));
        assertEquals(0, islandContainer.nextIslandIndex(2));
        assertEquals(2, islandContainer.nextIslandIndex(1));
    }

    @Test
    void prevIsland() {
        Island i1 = new BaseIsland();
        Island i2 = new BaseIsland();
        Island i3 = new BaseIsland();
        IslandContainer islandContainer = new IslandContainer(new LinkedList<>(List.of(i1, i2, i3)));

        assertEquals(i3, islandContainer.prevIsland(0));
        assertEquals(i2, islandContainer.prevIsland(2));
        assertEquals(i1, islandContainer.prevIsland(1));
    }

    @Test
    void nextIsland() {
        Island i1 = new BaseIsland();
        Island i2 = new BaseIsland();
        Island i3 = new BaseIsland();
        IslandContainer islandContainer = new IslandContainer(new LinkedList<>(List.of(i1, i2, i3)));

        assertEquals(i2, islandContainer.nextIsland(0));
        assertEquals(i1, islandContainer.nextIsland(2));
        assertEquals(i3, islandContainer.nextIsland(1));
    }

    @Test
    void joinPrevIsland() {
        Island i0 = new BaseIsland();
        BaseIsland i1 = new BaseIsland();
        BaseIsland i2 = new BaseIsland();
        Island i3 = new BaseIsland();
        Island i4 = new BaseIsland();
        IslandContainer islandContainer = new IslandContainer(new LinkedList<>(List.of(i0, i1, i2, i3, i4)));
        islandContainer.joinPrevIsland(2);

        assertEquals(4, islandContainer.size());
        assertEquals(i0, islandContainer.get(0));
        assertEquals(i3, islandContainer.get(2));
        assertEquals(i4, islandContainer.get(3));
        assertTrue(islandContainer.get(1) instanceof SuperIsland);
        assertEquals(2, ((SuperIsland) islandContainer.get(1)).islands.size());
        assertTrue(((SuperIsland) islandContainer.get(1)).islands.containsAll(List.of(i1, i2)));

    }

    @Test
    void joinNextIsland() {
        Island i0 = new BaseIsland();
        Island i1 = new BaseIsland();
        BaseIsland i2 = new BaseIsland();
        BaseIsland i3 = new BaseIsland();
        Island i4 = new BaseIsland();
        IslandContainer islandContainer = new IslandContainer(new LinkedList<>(List.of(i0, i1, i2, i3, i4)));
        islandContainer.joinNextIsland(2);

        assertEquals(4, islandContainer.size());
        assertEquals(i0, islandContainer.get(0));
        assertEquals(i1, islandContainer.get(1));
        assertEquals(i4, islandContainer.get(3));
        assertTrue(islandContainer.get(2) instanceof SuperIsland);
        assertEquals(2, ((SuperIsland) islandContainer.get(2)).islands.size());
        assertTrue(((SuperIsland) islandContainer.get(2)).islands.containsAll(List.of(i2, i3)));
    }
}