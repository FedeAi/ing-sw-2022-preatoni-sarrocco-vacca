package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Exceptions.WrongStateException;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.MushroomCharacter;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class MushroomChooseColor extends Performable {
    private final Color student;

    public MushroomChooseColor(String player, Color student) {
        super(player);
        this.student = student;
    }

    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);

        if (!game.getGameState().equals(GameState.MUSHROOM_CHOOSE_COLOR)) {
            throw new WrongStateException("state you access by activating the mushroom card.");
        }

        // there is no an active card
        Optional<CharacterCard> card = game.getActiveCharacter();
        if (card.isEmpty()) {
            throw new GameException("There isn't any active card present.");
        }

        // the active card is not the right one
        if (!(card.get() instanceof MushroomCharacter)) {
            throw new GameException("The card that has been activated in this turn is not of the mushroom type.");
        }

        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof MushroomCharacter)) {
            throw new GameException("There isn't any character card of the type mushroom on the table.");
        }
    }

    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Optional<CharacterCard> mushRoomCard = game.getCharacterCards().stream().filter(characterCard -> characterCard instanceof MushroomCharacter).findFirst();
        mushRoomCard.ifPresent(characterCard -> ((MushroomCharacter) characterCard).setStudent(this.student));
    }

}
