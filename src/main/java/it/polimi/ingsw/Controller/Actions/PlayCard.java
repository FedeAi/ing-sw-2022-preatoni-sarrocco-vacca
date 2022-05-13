package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Exceptions.WrongStateException;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Constants.GameState;
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
            throw new GameException("You must select a valid index. The specified index " + choice + " is not valid.");
        }
        // checks if the choice has already been played, or if I have no other choices
        if (!game.getPlayedCards().stream().anyMatch(c -> c.getValue() == choice) || game.getPlayedCards().containsAll(player.getCards())) {
            return;
        } else {
            throw new GameException("You cannot play a card that has already been played by an other player.");
        }
    }


    @Override
    public void performMove(Game game, Rules rules) {
        Player player = getPlayer(game);
        AssistantCard playedCard = player.getCards().stream().filter(c -> c.getValue() == choice).findFirst().get();
        game.playCard(player, playedCard);
        // TODO MOVE THIS TO ROUNDMANAGER (?) set new round owner or change game phase
        int playerIndex = game.getOrderedPlanningPlayers().indexOf(player);
        if (playerIndex == game.getPlayers().size() - 1) {

            game.setGameState(GameState.ACTION_MOVE_STUDENTS);
            setActionOrder(game);

        } else {
            game.setRoundOwner(game.getOrderedPlanningPlayers().get(playerIndex + 1));
        }
    }

    /**
     * choose Player orders for action phase
     */
    private void setActionOrder(Game game) {
        // xTODO TESTINGGGGG
        List<Player> planningPhasePlayers = game.getOrderedPlanningPlayers();

        // compare by cards value
        Comparator<Player> compareByCardValue = (Player p1, Player p2) -> p1.getPlayedCard().getValue() - p2.getPlayedCard().getValue();
        planningPhasePlayers.sort(compareByCardValue);
        game.setPlayersActionPhase(planningPhasePlayers);
        game.setRoundOwner(planningPhasePlayers.get(0));

    }

    @Override
    public GameState nextState(Game game, Rules rules) {
        return game.getGameState();
    }

    @Override
    public Player nextPlayer(Game game, Rules rules) {
        return game.getRoundOwner();
    }
}
