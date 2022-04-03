package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class PlayCard implements Performable{
    private final String myNickName;

    public PlayCard(String myNickName){
        this.myNickName = myNickName;
    }

    @Override
    public boolean canPerformExt(Game game) {
        Optional<Player> player = game.getPlayerByNickname(myNickName);

        if(player.isEmpty())
            return  false;


        if (player.equals(gameInstance.getRoundOwner()) &&
                gameInstance.getGameState().equals(GameState.PLANNING_PHASE) &&
                player.hasCard(choice)
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


    @Override
    public void performMove(Game game) {

    }

    @Override
    public String getNickNamePlayer() {
        return "0";
    }
}
