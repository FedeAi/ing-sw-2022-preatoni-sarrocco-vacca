package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;

public class GrandmaCharacter extends CharacterCard {

    private int blockingCards;
    private GameState previousState;

    public GrandmaCharacter(String imagePath) {
        super(imagePath);
        isActive = false;
        // Maybe this goes in the init() method?
        blockingCards = 4;
    }

    @Override
    public void init() {

    }

    @Override
    public void activate(Rules rules, Game game) {
        activated = true;
        isActive = true;
        previousState = game.getGameState();
        game.setGameState(GameState.GRANDMA_BLOCK_ISLAND);
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        isActive = false;
        game.setGameState(previousState);
    }

    public void moveBlockingCard() {
        // This should be impossible
        // Bad practice or good?
        if (blockingCards > 0) {
            blockingCards--;
        }
    }

    public int getBlockingCards() {
        return blockingCards;
    }
}
