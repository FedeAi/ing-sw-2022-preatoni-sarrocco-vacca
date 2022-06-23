package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Exceptions.WrongStateException;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.Princess;
import it.polimi.ingsw.Model.Cards.CharacterCards.Thief;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.School;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ThiefChooseColor class represent the Thief character card game action.
 * The action allows a player to choose a student color
 * and remove a maximum of 3 of them from all the player's school hall.
 *
 * @see Thief
 */
public class ThiefChooseColor extends Performable {

    Color chosenColor;

    /**
     * Constructor ThiefChooseColor creates the ThiefChooseColor instance,
     * and sets the student color selection.
     *
     * @param player      the nickname of the action owner.
     * @param chosenColor the student color to be removed from all the school's hall.
     */
    public ThiefChooseColor(String player, Color chosenColor) {
        super(player);
        this.chosenColor = chosenColor;
    }

    /**
     * Method canPerform extends the Performable definition with the ThiefChooseColor specific checks.
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
        // Simple check that verifies that there is a player with the specified name, and that he is the roundOwner
        super.canPerform(game, rules);

        // Checks if the game is set to the correct state
        if (!game.getGameState().equals(GameState.THIEF_CHOOSE_COLOR)) {
            throw new WrongStateException("state you access by activating the thief card.");
        }

        // We check if any of the cards on the table are of the THIEF type
        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof Thief)) {
            throw new GameException("There isn't any character card of the type thief on the table.");
        }

        // Simple check to see if we have an active card
        Optional<CharacterCard> card = game.getActiveCharacter(Thief.class);
        if (card.isEmpty()) {
            throw new GameException("There isn't any active card present.");
        }

        // Checking if the activated card is of the THIEF type
        if (!(card.get() instanceof Thief)) {
            throw new GameException("The card that has been activated in this turn is not of the thief type.");
        }

        if (chosenColor != Color.BLUE && chosenColor != Color.RED && chosenColor != Color.GREEN && chosenColor != Color.YELLOW && chosenColor != Color.PINK) {
            throw new GameException("Invalid color selected.");
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     * The thief card effect will be activated,
     * and 3 students of the selected color will be removed from the player's school hall.
     * The method also handles automatic deactivation after the action.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        if (game.getActiveCharacter(Thief.class).isPresent()) {
            List<Player> players = game.getPlayers();
            for (Player p : players) {
                School school = p.getSchool();
                /*
                    If we have more than 3 students of the chosenColor we remove 3 of them,
                    otherwise we reset the value to 0
                */
                for (int i = 0; i < 3; i++) {
                    school.removeStudentFromHall(chosenColor);
                }
            }
            Thief thief = (Thief) game.getActiveCharacter(Thief.class).get();
            game.deactivateCharacterCard(game.getCharacterCards().indexOf(thief), rules);
        }
    }
}