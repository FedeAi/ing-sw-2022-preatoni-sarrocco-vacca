package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.Mushroom;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Model.Game;

import java.util.Optional;

public class MushroomRules extends BaseRules {

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
        } else {   // it should never enter this branch ( if MushRooms are active there must be a MushRoomCard
            throw new NoSuchMethodError();
        }
    }
}
