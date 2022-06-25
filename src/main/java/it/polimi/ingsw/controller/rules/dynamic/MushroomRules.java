package it.polimi.ingsw.controller.rules.dynamic;

import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Mushroom;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.model.Game;

import java.util.Optional;

/**
 * MushroomRules class extends the BaseRules class,
 * overriding the rules that have changed since the activation of the Mushroom character card.
 */
public class MushroomRules extends BaseRules {

    /**
     * Method influenceModifier will exclude from the influence computation a selected student color.
     *
     * @param game      the game model reference.
     * @param student   the selected student color.
     * @param influence the calculated influence for that color.
     * @return 0 if the color corresponds with the selected one, the input influence otherwise.
     */
    @Override
    protected int influenceModifier(Game game, Color student, int influence) {
        Optional<CharacterCard> mushCard = game.getCharacterCards().stream().filter(characterCard -> characterCard instanceof Mushroom).findFirst();
        if (mushCard.isPresent()) {
            Color excludedColor = ((Mushroom) mushCard.get()).getStudent();
            if (excludedColor != null && excludedColor.equals(student)) {
                return 0;
            } else {
                return influence;
            }
        } else {
            // This branch should never be entered! (If a Mushroom is active there must be a Mushroom Card in the Game).
            throw new NoSuchMethodError();
        }
    }
}