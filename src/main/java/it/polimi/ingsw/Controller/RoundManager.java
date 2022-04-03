package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Comparator;
import java.util.List;

public class RoundManager {

    private GameManager gameManager;
    private Game gameInstance;

    public RoundManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameInstance = Game.getInstance();
    }


    private Cloud initClouds() {
        assert false;
        return null;
    }

    // TODO ASK PARAMETER, player is contained in players?
    // TODO ASK SE HAS CARD é MEGLIO METTERLO QUI O IN PLAYER
    public void playCard(String player, AssistantCard choice) {

        if (!gameInstance.getPlayers().contains(gameInstance.getPlayerByNickname(player))) {
            // Throw excp
        }
        if (player.equals(gameInstance.getRoundOwner()) &&
                gameInstance.getGameState().equals(GameState.PLANNING_PHASE) &&
                gameInstance.getPlayerByNickname(player).hasCard();

        ) {

            // checks if the choice has already been played, or if I have no other choices
            if (!gameInstance.getPlayedCards().contains(choice) ||
                    gameInstance.getPlayedCards().containsAll(player.getCards())) {
                player.setAndRemovePlayedCard(choice);

                // set new round owner or change game phase
                int playerIndex = gameInstance.getOrderedPlanningPlayers().indexOf(player);
                if (playerIndex == gameInstance.getPlayers().size() - 1) {

                    gameInstance.setGameState(GameState.ACTION_PHASE);
                    setActionOrder();

                } else {
                    gameInstance.setRoundOwner(gameInstance.getOrderedPlanningPlayers().get(playerIndex + 1));
                }
            }

        }
    }

    /**
     * choose Player orders for action phase
     */
    private void setActionOrder(){
        // TODO TESTINGGGGG
        List<Player> planningPhasePlayers = gameInstance.getOrderedPlanningPlayers();

        // compare by cards value
        Comparator<Player> compareByCardValue = (Player p1, Player p2) -> p1.getPlayedCard().getValue() -  p2.getPlayedCard().getValue();
        planningPhasePlayers.sort(compareByCardValue);

        gameInstance.setPlayersActionPhase(planningPhasePlayers);

    }



}
