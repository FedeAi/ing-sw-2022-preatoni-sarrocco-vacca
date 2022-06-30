package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.AssistantCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * PlayerTest class tests the Player class.
 *
 * @see Player
 */
class PlayerTest {

    /**
     * Method handTest checks if a player's hand is properly initialized.
     */
    @Test
    @DisplayName("Hand creation test")
    public void handTest() {
        Player player = new Player(0, "Ale");
        int i = 1;
        for (AssistantCard c : player.getCards()) {
            Assertions.assertEquals(i, c.getValue(), "Check if hand is properly initialized");
            i++;
        }
    }
}