package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class PlanningPhase {
    private class PlayedCard {
        protected Player player;
        protected AssistantCard card;

        public PlayedCard(Player player, AssistantCard card) {
            this.player = player;
            this.card = card;
        }
    }

    private ArrayList<PlayedCard> playedCards;
    
}
