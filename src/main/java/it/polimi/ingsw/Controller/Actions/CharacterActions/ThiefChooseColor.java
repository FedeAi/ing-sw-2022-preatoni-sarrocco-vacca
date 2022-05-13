package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.ThiefCharacter;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ThiefChooseColor extends Performable {

    Color chosenColor;

    public ThiefChooseColor(String nickName, Color chosenColor) {
        super(nickName);
        this.chosenColor = chosenColor;
    }

    @Override
    protected void canPerform(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name, and that he is the roundOwner
        if (!super.canPerform(game, rules)) {
            return false;
        }

        Player player = getPlayer(game);

        // Checks if the game is set to the correct state
        if (!game.getGameState().equals(GameState.THIEF_CHOOSE_COLOR)) {
            return false;
        }

        // Simple check to see if we have an active card
        Optional<CharacterCard> card = game.getActiveCharacter();
        if (card.isEmpty()) {
            return false;
        }

        // We check if any of the cards on the table are of the THIEF type
        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof ThiefCharacter)) {
            return false;
        }

        // Checking if the activated card is of the THIEF type
        if (!(card.get() instanceof ThiefCharacter)) {
            return false;
        }

        // Is this necessary?
        if (chosenColor != Color.BLUE && chosenColor != Color.RED && chosenColor != Color.GREEN && chosenColor != Color.YELLOW && chosenColor != Color.PINK) {
            return false;
        }
        return true;
    }

    @Override
    public void performMove(Game game, Rules rules) {
        // TODO TESTING
        // Redundant card presence check and general canPerform() check, then we execute the action
        if (game.getActiveCharacter().isPresent() && canPerform(game, rules)) {
            List<Player> players = game.getPlayers();
            for (Player p : players) {
                Map<Color, Integer> studentsHall = p.getSchool().getStudentsHall();
                /*
                    If we have more than 3 students of the chosenColor we remove 3 of them,
                    otherwise we reset the value to 0
                */
                if (studentsHall.getOrDefault(chosenColor, 0) > 3) {
                    // getOrDefault() just for general safety
                    for (int i = 0; i < 3; i++) {
                        studentsHall.put(chosenColor, studentsHall.getOrDefault(chosenColor, 0) - 1);
                    }
                } else {
                    studentsHall.put(chosenColor, 0);
                }
            }
            ThiefCharacter thief = (ThiefCharacter) game.getActiveCharacter().get();
            thief.deactivate(rules, game);
        }
    }
}
