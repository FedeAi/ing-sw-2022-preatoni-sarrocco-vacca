package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.MushRoomRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;

public class MushRoomCharacter extends CharacterCard {
    Color student;
    private GameState previousState;

    public MushRoomCharacter(String imagePath) {
        super(imagePath);
        price = 3;
        isActive = false;
    }

    @Override
    public void activate(Rules rules, Game game) {
        isActive = true;
        activated = true;
        previousState = game.getGameState();
        game.setGameState(GameState.MUSHROOM_CHOOSE_COLOR);
        rules.setDynamicRules(new MushRoomRules());
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        isActive = false;
        rules.setDynamicRules(new BaseRules());
        game.setGameState(previousState);
        setStudent(null);
    }

    public Color getStudent() {
        return student;
    }

    public void setStudent(Color student) {
        this.student = student;
    }

}
