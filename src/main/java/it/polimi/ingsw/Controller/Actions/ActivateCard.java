package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class ActivateCard extends Performable {

    private final int choice;

    public ActivateCard(String myNickname, int choice) {
        super(myNickname);
        this.choice = choice;
    }

    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);

        Player player = getPlayer(game);
        // Simple check to verify that we're in the correct state
        if (!game.getGameState().equals(GameState.ACTION_MOVE_MOTHER) && !game.getGameState().equals(GameState.ACTION_MOVE_STUDENTS)) {
            throw new WrongStateException("action phase!");
        }

        // Verify that we are under expert rules
        if (!game.isExpertMode()) {
            throw new GameException("This feature is available only in expert mode!");
        }

        if (choice < 0 || choice >= Constants.NUM_CHARACTER_CARDS) {
            throw new InvalidIndexException("character card", 0, Constants.NUM_CHARACTER_CARDS - 1, choice);
        }

        CharacterCard chosenCard = game.getCharacterCards().get(choice);
        // Verify that the player has enough money
        if (player.getBalance() < chosenCard.getPrice()) {
            throw new GameException("You don't have enough money! The selected card costs " + chosenCard.getPrice() + ".");
        }
        if (chosenCard.isActive()) {
            throw new GameException("The selected card is already active.");
        }
    }

    @Override
    public void performMove(Game game, Rules rules) {
        performMove(game, rules);
        Player player = getPlayer(game);

        CharacterCard chosenCard = game.getCharacterCards().get(choice);

        if (chosenCard.alreadyActivatedOnce()) {
            game.incrementBalance(chosenCard.getPrice());
        } else {
            game.incrementBalance(chosenCard.getPrice() - 1);
        }

        player.spendCoins(chosenCard.getPrice());

        chosenCard.activate(rules, game);
    }

    @Override
    public GameState nextState(Game game, Rules rules){
        return game.getGameState();
    }

    @Override
    public Player nextPlayer(Game game, Rules rules){
        return game.getRoundOwner();
    }
}
