package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Model.Cards.CharacterCard;
import it.polimi.ingsw.Model.Cards.MushRoomCharacter;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Game;

import java.util.Optional;

public class MushRoomRules extends BaseRules{

    @Override
    protected int influenceModifier(Game game, Color student, int influence) {
        Optional<CharacterCard> mushCard = game.getCharacterCards().stream().filter(characterCard -> characterCard instanceof MushRoomCharacter).findFirst();
        if(mushCard.isPresent()){
            Color noinfluenceStudent = ((MushRoomCharacter)mushCard.get()).getStudent();
            if(noinfluenceStudent!=null && noinfluenceStudent.equals(student)){
                return 0;
            }
            else{
                return influence;
            }
        }
        else{   // it should never enter this branch ( if MushRooms are active there must be a MushRoomCard
            throw new NoSuchMethodError();
        }
    }
}
