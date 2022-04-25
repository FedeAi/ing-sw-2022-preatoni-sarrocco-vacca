package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;

import java.util.Map;

public class MinstrelCharacter extends  CharacterCard{


    private GameState previousState;
    private Bag bag;
    private int swappedStudents;
    public static final int maxSwaps = 2;

    public MinstrelCharacter(String imagePath) {
        super(imagePath);
        price = 1;
        isActive = false;
    }

    @Override
    public void init() {

    }

    @Override
    public void activate(Rules rules, Game game) {
        isActive = true;
        activated = true;
        game.setGameState(GameState.PRINCESS_MOVE_STUDENT);
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        isActive = false;
        game.setGameState(previousState);
    }
    public int getSwappedStudents() {
        return swappedStudents;
    }


}
