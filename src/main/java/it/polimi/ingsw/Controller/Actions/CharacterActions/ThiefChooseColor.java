package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Exceptions.WrongStateException;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.Thief;
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
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he is the roundOwner
        super.canPerform(game, rules);

        // Checks if the game is set to the correct state
        if (!game.getGameState().equals(GameState.THIEF_CHOOSE_COLOR)) {
            throw new WrongStateException("state you access by activating the thief card.");
        }

        // Simple check to see if we have an active card
        Optional<CharacterCard> card = game.getActiveCharacter(Thief.class);
        if (card.isEmpty()) {
            throw new GameException("There isn't any active card present.");
        }

        // We check if any of the cards on the table are of the THIEF type
        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof Thief)) {
            throw new GameException("There isn't any character card of the type thief on the table.");
        }

        // Checking if the activated card is of the THIEF type
        if (!(card.get() instanceof Thief)) {
            throw new GameException("The card that has been activated in this turn is not of the thief type.");
        }

        // Is this necessary?
        if (chosenColor != Color.BLUE && chosenColor != Color.RED && chosenColor != Color.GREEN && chosenColor != Color.YELLOW && chosenColor != Color.PINK) {
            throw new GameException("Invalid color selected.");
        }
    }

    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        // TODO TESTING
        if (game.getActiveCharacter(Thief.class).isPresent()) {
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
            Thief thief = (Thief) game.getActiveCharacter(Thief.class).get();
            game.deactivateCharacterCard(game.getCharacterCards().indexOf(thief), rules);
        }
    }
}
