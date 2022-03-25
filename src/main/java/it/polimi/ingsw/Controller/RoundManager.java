package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Enumerations.GameStates;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RoundManager {

    private TurnManager turnManager;
    private GameManager gameManager;
    private Game gameInstance;
    private TurnManager turnManager;

    public RoundManager(GameManager gameManager, TurnManager turnManager) {
        this.gameManager = gameManager;
        this.turnManager = turnManager;
        this.gameInstance = Game.getInstance();
    }


    private Cloud initClouds() {
        assert false;
    }

    // TODO ASK PARAMETER, player is contained in players?
    // TODO ASK SE HAS CARD é MEGLIO METTERLO QUI O IN PLAYER
    public void playCard(Player player, AssistantCard choice) {
        if (!gameInstance.getPlayers().contains(player)) {
            // Throw excp
        }
        if (player.equals(gameInstance.getRoundOwner()) &&
                gameInstance.getGameState().equals(GameStates.PLANNING_PHASE) &&
                player.hasCard(choice)
        ) {

            // checks if the choice has already been played, or if I have no other choices
            if (!gameInstance.getPlayedCards().contains(choice) ||
                    gameInstance.getPlayedCards().containsAll(player.getCards())) {
                player.setAndRemovePlayedCard(choice);

                // set new round owner or change game phase
                int playerIndex = gameInstance.getOrderedPlanningPlayers().indexOf(player);
                if (playerIndex == gameInstance.getPlayers().size() - 1) {

                    gameInstance.setGameState(GameStates.ACTION_PHASE);
                    setPlayerOrderAction();

                } else {
                    gameInstance.setRoundOwner(gameInstance.getOrderedPlanningPlayers().get(playerIndex + 1));
                }
            }

        }
    }

    private void setPlayerOrderAction(){
        // TODO TESTINGGGGG
        List<Player> planningPhasePlayers = gameInstance.getOrderedPlanningPlayers();

        // compare by cards value
        Comparator<Player> compareByCardValue = (Player p1, Player p2) -> p1.getPlayedCard().getValue() -  p2.getPlayedCard().getValue();
        planningPhasePlayers.sort(compareByCardValue);

        gameInstance.setOrderedPlayersActionPhase(planningPhasePlayers);

    }


}
