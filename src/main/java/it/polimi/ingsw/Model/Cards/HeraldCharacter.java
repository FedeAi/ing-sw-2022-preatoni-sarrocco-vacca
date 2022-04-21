package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.CharacterCardState;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;

public class HeraldCharacter extends CharacterCard{
    public HeraldCharacter(String imagePath){
        super(imagePath);
        price = 3;
        isActive = false;
    }

    @Override
    public void activate(Rules rules, Game game) {
        game.setCharacterCardState(CharacterCardState.HERALD_ACTIVE);
        isActive = true;
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        isActive = false;
        game.setCharacterCardState(CharacterCardState.NO_ACTION);
    }


}
