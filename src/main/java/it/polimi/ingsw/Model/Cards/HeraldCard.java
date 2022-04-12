package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;

public class HeraldCard extends CharacterCard{
    private GameState previousState;
    public HeraldCard(String imagePath){
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
        game.setGameState(previousState);   // store the previous state back
    }


}
