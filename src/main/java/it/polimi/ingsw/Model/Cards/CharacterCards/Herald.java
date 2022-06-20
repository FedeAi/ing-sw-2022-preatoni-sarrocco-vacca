package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;

public class Herald extends CharacterCard {
    private GameState previousState;

    public Herald(String imagePath) {
        super(imagePath);
        price = 3;
        character = Character.HERALD;
    }

    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        previousState = game.getGameState();
        game.setGameState(GameState.HERALD_ACTIVE);
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        game.setGameState(previousState);
    }


}