package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.MonkCharacter;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class MonkMoveToIsland extends Performable {

    private Color student;
    private int islandIndex;

    public MonkMoveToIsland(String nickname, Color student, int islandIndex) {
        super(nickname);
        this.student = student;
        this.islandIndex = islandIndex;
    }

    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he is the roundOwner
        super.canPerform(game, rules);

        // Checks if the game is set to the correct state
        if (!game.getGameState().equals(GameState.MONK_MOVE_STUDENT)) {
            throw new WrongStateException("state you access by activating the monk card.");
        }

        // Checks if the islandIndex is correct
        if (!game.getIslandContainer().isFeasibleIndex(islandIndex)) {
            throw new InvalidIndexException("island", 0, game.getIslandContainer().size(), islandIndex);
        }

        // Simple check to see if we have an active card
        Optional<CharacterCard> card = game.getActiveCharacter();
        if (card.isEmpty()) {
            throw new GameException("There isn't any active card present.");
        }

        // We check if any of the cards on the table are of the MONK type
        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof MonkCharacter)) {
            throw new GameException("There isn't any character card of the type monk on the table.");
        }

        // Checking if the activated card is of the MONK type
        if (!(card.get() instanceof MonkCharacter)) {
            throw new GameException("The card that has been activated in this turn is not of the monk type.");
        }

        // Now it's safe to cast the activated card
        MonkCharacter monk = (MonkCharacter) card.get();

        // Verify that the MONK card has a student of the specified COLOR
        if (monk.getStudents().getOrDefault(student, 0) <= 0) {
            throw new GameException("There isn't any student of the specified color (" + student.toString() + ") on the monk card.");
        }
    }

    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        // Redundant card presence check and general canPerform() check, then we execute the action
        if (game.getActiveCharacter().isPresent()) {
            MonkCharacter monk = (MonkCharacter) game.getActiveCharacter().get();
            monk.moveStudent(student);
            // Now we add the student to the specified island
            game.addIslandStudent(islandIndex, student);
            monk.deactivate(rules, game);
        }
    }
}
