package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * PlayCard class represent the AssistantCard play move in the game, executed in the planning phase of the game.
 */
public class PlayCard extends Performable {

    private final int choice;

    /**
     * Constructor PlayCard creates the PlayCard instance, and sets the AssistantCard selection by index.
     *
     * @param player the nickname of the action owner.
     * @param choice the index of the AssistantCard selection.
     */
    public PlayCard(String player, int choice) {
        super(player);
        this.choice = choice;
    }

    /**
     * Method canPerform extends the Performable definition with the PlayCard specific checks.
     *
     * @param game  represents the game Model.
     * @param rules represents the current game rules.
     * @throws InvalidPlayerException if the player is not in the current game.
     * @throws RoundOwnerException    if the player is not the current round owner.
     * @throws GameException          for generic errors.
     * @see Performable#canPerform(Game, Rules)
     */
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
            throw new GameException("You cannot play a card that has already been played by another player.");
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     * @see PlayCard#canPerform(Game, Rules)
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);
        AssistantCard playedCard = player.getCards().stream().filter(c -> c.getValue() == choice).findFirst().get();
        game.playCard(player, playedCard);
    }

    /**
     * Method nextState determines the next game state after a PlayCard action is executed.
     * Only when all the players have played a card the game can proceed to the next state.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    @Override
    public GameState nextState(Game game, Rules rules) {
        int playerIndex = game.getOrderedPlanningPlayers().indexOf(getPlayer(game));
        if (playerIndex == game.getOrderedPlanningPlayers().size() - 1 || nextPlayer(game, rules) == null) {
            return GameState.ACTION_MOVE_STUDENTS;
        }
        return game.getGameState();
    }

    /**
     * Method nextPlayer determines the next player after a PlayCard action is executed.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     * @return the next player for the planning phase, null if the owner is the last player.
     */
    @Override
    public Player nextPlayer(Game game, Rules rules) {
        int playerIndex = game.getOrderedPlanningPlayers().indexOf(getPlayer(game));

        Player nextPlayer;
        do {
            if (playerIndex != game.getOrderedPlanningPlayers().size() - 1) {
                nextPlayer = game.getOrderedPlanningPlayers().get(playerIndex + 1);
                playerIndex++;
            } else {
                nextPlayer = null;
            }
        } while (nextPlayer != null && !nextPlayer.isActive());
        return nextPlayer;
    }
}