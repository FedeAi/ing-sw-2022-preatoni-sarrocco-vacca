package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.WinController;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Game;

public class RoundManager {

    private GameManager gameManager;
    private Game gameInstance;

    public RoundManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameInstance = gameManager.getGameInstance();
    }

    public void performAction(Performable action){
        if(action.canPerformExt(gameInstance,  gameManager.getRules())){
            action.performMove(gameInstance, gameManager.getRules());

            WinController.check(gameInstance);
        }
    }


}
