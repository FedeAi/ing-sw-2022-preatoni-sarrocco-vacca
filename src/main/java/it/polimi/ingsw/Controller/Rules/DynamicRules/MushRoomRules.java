package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.MushroomCharacter;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Model.Game;

import java.util.Optional;

public class MushRoomRules extends BaseRules {

    @Override
    protected int influenceModifier(Game game, Color student, int influence) {
        Optional<CharacterCard> mushCard = game.getCharacterCards().stream().filter(characterCard -> characterCard instanceof MushroomCharacter).findFirst();
        if (mushCard.isPresent()) {
            Color noinfluenceStudent = ((MushroomCharacter) mushCard.get()).getStudent();
            if (noinfluenceStudent != null && noinfluenceStudent.equals(student)) {
                return 0;
            } else {
                return influence;
            }
        } else {   // it should never enter this branch ( if MushRooms are active there must be a MushRoomCard
            throw new NoSuchMethodError();
        }
    }
}
