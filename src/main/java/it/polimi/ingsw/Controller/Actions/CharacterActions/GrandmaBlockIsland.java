package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.Grandma;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;

import java.util.Optional;

public class GrandmaBlockIsland extends Performable {

    int islandIndex;

    public GrandmaBlockIsland(String nickName, int islandIndex) {
        super(nickName);
        this.islandIndex = islandIndex;
    }

    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he is the roundOwner
        super.canPerform(game, rules);

        // Checks if the game is set to the correct state
        if (!game.getGameState().equals(GameState.GRANDMA_BLOCK_ISLAND)) {
            throw new WrongStateException("state you access by activating the grandma card.");
        }

        // Checks if the islandIndex is correct
        if (!game.getIslandContainer().isFeasibleIndex(islandIndex)) {
            throw new InvalidIndexException("island", 0, game.getIslandContainer().size() - 1, islandIndex);
        }

        // Checks if the island isn't already blocked
        if (game.getIslandContainer().get(islandIndex).isBlocked()) {
            throw new GameException("The island has already been blocked. Please select another one.");
        }

        // We check if any of the cards on the table are of the GRANDMA type
        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof Grandma)) {
            throw new GameException("There isn't any character card of the type grandma on the table.");
        }

        // Simple check to see if we have an active card
        Optional<CharacterCard> card = game.getActiveCharacter(CharacterCard.class);
        if (card.isEmpty()) {
            throw new GameException("There isn't any active CharacterCard present.");
        }

        // Checking if the activated card is of the GRANDMA type
        if (!(card.get() instanceof Grandma)) {
            throw new GameException("The card that has been activated in this turn is not of the grandma type.");
        }

        // Now it's safe to cast the activated card
        Grandma grandma = (Grandma) card.get();

        // Verify that we have at least 1 blockingCard to use
        if (grandma.getBlockingCards() <= 0) {
            throw new GameException("The grandma character card doesn't have any blocking cards left. Please step on a blocked island in order to use it.");
        }
    }

    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        // Redundant card presence check, then we execute the action
        if (game.getActiveCharacter(Grandma.class).isPresent()) {
            Grandma grandma = (Grandma) game.getActiveCharacter(Grandma.class).get();
            grandma.moveBlockingCard();
            game.setIslandBlock(islandIndex, true);
            game.deactivateCharacterCard(game.getCharacterCards().indexOf(grandma), rules);
        }
    }
}
