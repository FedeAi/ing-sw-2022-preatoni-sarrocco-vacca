package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;

public class HeraldCard extends CharacterCard{
    private boolean isActive;

    public HeraldCard(String imagePath){
        super(imagePath);
        isActive = false;
    }

    @Override
    public void activate(Rules rules, Game game) {
        game.setGameState(GameState.HERALD_ACTIVE);
        isActive = true;
    }

    @Override
    public void deactivate(Rules rules) {
        isActive = false;
    }

    @Override
    public boolean isActive() {
        return false;
    }
}
