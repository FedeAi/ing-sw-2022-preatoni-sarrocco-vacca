package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.MonkCharacter;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Map;
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
    public boolean canPerformExt(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name, and that he is the roundOwner
        if (!super.canPerformExt(game, rules)) {
            return false;
        }

        Player player = getPlayer(game);

        // Checks if the game is set to the correct state
        if (!game.getGameState().equals(GameState.MONK_MOVE_STUDENT)) {
            return false;
        }

        // Checks if the islandIndex is correct
        if (!game.getIslandContainer().isFeasibleIndex(islandIndex)) {
            return false;
        }

        // Simple check to see if we have an active card
        Optional<CharacterCard> card = game.getActiveCharacter();
        if (card.isEmpty()) {
            return false;
        }

        // We check if any of the cards on the table are of the MONK type
        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof MonkCharacter)) {
            return false;
        }

        // Checking if the activated card is of the MONK type
        if (!(card.get() instanceof MonkCharacter)) {
            return false;
        }

        // Now it's safe to cast the activated card
        MonkCharacter monk = (MonkCharacter) card.get();

        // Verify that the MONK card has a student of the specified COLOR
        if (monk.getStudents().getOrDefault(student, 0) <= 0) {
            return false;
        }

        return true;
    }

    @Override
    public void performMove(Game game, Rules rules) {
        // Redundant card presence check and general canPerform() check, then we execute the action
        if (game.getActiveCharacter().isPresent() && canPerformExt(game, rules)) {
            MonkCharacter monk = (MonkCharacter) game.getActiveCharacter().get();
            monk.moveStudent(student);
            // Now we add the student to the specified island
            game.getIslandContainer().addIslandStudent(islandIndex, student);
            monk.deactivate(rules, game);
        }
    }
}
