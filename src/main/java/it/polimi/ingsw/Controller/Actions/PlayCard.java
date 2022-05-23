package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Comparator;
import java.util.List;

public class PlayCard extends Performable {

    private final int choice;

    public PlayCard(String myNickName, int choice) {
        super(myNickName);
        this.choice = choice;
    }

    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);
        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.PLANNING_CHOOSE_CARD)) {
            throw new WrongStateException("planning phase.");
        }
        if (!player.hasCard(choice)) {
            throw new InvalidIndexException("card", 1, 10, choice);
        }
        // checks if the choice has already been played, or if I have no other choices
        if (!game.getPlayedCards().stream().anyMatch(c -> c.getValue() == choice) || game.getPlayedCards().containsAll(player.getCards())) {
            return;
        } else {
            throw new GameException("You cannot play a card that has already been played by an other player.");
        }
    }


    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);
        AssistantCard playedCard = player.getCards().stream().filter(c -> c.getValue() == choice).findFirst().get();
        game.playCard(player, playedCard);

    }

    @Override
    public GameState nextState(Game game, Rules rules){
        int playerIndex = game.getOrderedPlanningPlayers().indexOf(getPlayer(game));
        if (playerIndex == game.getOrderedPlanningPlayers().size() - 1 || nextPlayer(game, rules) == null) {
            return GameState.ACTION_MOVE_STUDENTS;
        }
        return game.getGameState();
    }

    /**
     * @param game
     * @param rules
     * @return null if im the last player in planning phase
     */
    @Override
    public Player nextPlayer(Game game, Rules rules){
        int playerIndex = game.getOrderedPlanningPlayers().indexOf(getPlayer(game));

        Player nextPlayer;
        do{
            if (playerIndex != game.getOrderedPlanningPlayers().size() - 1) {
                nextPlayer = game.getOrderedPlanningPlayers().get(playerIndex + 1);
                playerIndex++;
            }
            else{
                nextPlayer = null;
            }
        } while ( nextPlayer!=null && !nextPlayer.isConnected());
        return nextPlayer;
    }

}
