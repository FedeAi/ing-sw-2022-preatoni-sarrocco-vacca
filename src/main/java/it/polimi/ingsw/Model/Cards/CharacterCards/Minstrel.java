package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;

public class Minstrel extends CharacterCard {

    private GameState previousState;
    private int swappedStudents;
    public static final int maxSwaps = 2;

    public Minstrel(String imagePath) {
        super(imagePath);
        price = 1;
        character = Character.MINSTREL;
    }

    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        previousState = game.getGameState();
        game.setGameState(GameState.MINSTREL_SWAP_STUDENTS);
        swappedStudents = 0;
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        game.setGameState(previousState);
    }

    public int getSwappedStudents() {
        return swappedStudents;
    }

    public void incrementSwapped() {
        swappedStudents++;
    }
}