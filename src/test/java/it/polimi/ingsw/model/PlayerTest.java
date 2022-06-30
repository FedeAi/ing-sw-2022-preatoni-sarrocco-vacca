package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.AssistantCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    public void HandTest() {
        String path = "";

        Player player = new Player(0, path);
        int i = 1;
        for (AssistantCard c : player.getCards()) {
            Assertions.assertEquals(i, c.getValue(), "Check if hand is properly initialized");
            i++;
        }
    }
}