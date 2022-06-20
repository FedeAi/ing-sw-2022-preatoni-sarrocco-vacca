package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;

public class Grandma extends CharacterCard {

    private int blockingCards;
    private GameState previousState;

    public Grandma(String imagePath) {
        super(imagePath);
        price = 2;
        blockingCards = 4;
        character = Character.GRANDMA;
    }

    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        previousState = game.getGameState();
        game.setGameState(GameState.GRANDMA_BLOCK_ISLAND);
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        game.setGameState(previousState);
    }

    public void moveBlockingCard() {
        // This should be impossible
        // Bad practice or good?
        if (blockingCards > 0) {
            blockingCards--;
        }
    }

    public void addBlockingCard() {
        if (blockingCards <= 4) {
            blockingCards++;
        }
    }

    @Override
    public int getBlockingCards() {
        return blockingCards;
    }
}
