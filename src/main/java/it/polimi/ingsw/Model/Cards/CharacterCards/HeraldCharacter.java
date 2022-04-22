package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;

public class HeraldCharacter extends CharacterCard{
    private GameState previousState;
    public HeraldCharacter(String imagePath){
        super(imagePath);
        price = 3;
        isActive = false;
    }

    @Override
    public void activate(Rules rules, Game game) {
        previousState = game.getGameState();
        game.setGameState(GameState.HERALD_ACTIVE);
        isActive = true;

    }

    @Override
    public void deactivate(Rules rules, Game game) {
        isActive = false;
        game.setGameState(previousState);
    }


}
