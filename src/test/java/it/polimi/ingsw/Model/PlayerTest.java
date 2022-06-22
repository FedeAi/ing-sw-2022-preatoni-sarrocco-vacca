package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Cards.AssistantCard;

import static org.junit.Assert.*;

class PlayerTest {
    @org.junit.jupiter.api.Test
    public void HandTest() {
        String path = "";

        Player player = new Player(0, path);
        int i = 1;
        for (AssistantCard c : player.getCards()) {
            assertEquals("Check if hand is properly initialized", i, c.getValue());
            i++;
        }
    }
}