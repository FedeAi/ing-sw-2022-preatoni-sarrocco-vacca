package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.PostmanRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;

import java.util.Map;

public class PrincessCharacter extends CharacterCard {

    private Map<Color, Integer> students;
    private GameState previousState;
    private Bag bag;


    public PrincessCharacter(String imagePath) {
        super(imagePath);
        price = 2;
        isActive = false;
    }
    @Override
    public void init() {

        for (int i=0; i<4;i++) {
            Color student = bag.extractOne();
            this.students.put(student, this.students.getOrDefault(student, 0) + 1);
        }
    }

    @Override
    public void activate(Rules rules, Game game) {
        isActive = true;
        activated = true;
        previousState = game.getGameState();
        game.setGameState(GameState.PRINCESS_MOVE_STUDENT);

    }

    @Override
    public void deactivate(Rules rules, Game game) {
        isActive = false;
        game.setGameState(previousState);
    }

    public void moveStudent(Color Student) {
        students.remove(Student,1);
        students.put(bag.extractOne(),1);
    }

}
