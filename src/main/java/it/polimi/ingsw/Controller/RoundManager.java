package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.WinController;
import it.polimi.ingsw.Model.Game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RoundManager implements PropertyChangeListener {

    private GameManager gameManager;
    private Game gameInstance;

    public RoundManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameInstance = gameManager.getGame();
    }

    public void performAction(Performable action) {
        if (action.canPerform(gameInstance, gameManager.getRules())) {
            action.performMove(gameInstance, gameManager.getRules());

            WinController.check(gameInstance);
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
