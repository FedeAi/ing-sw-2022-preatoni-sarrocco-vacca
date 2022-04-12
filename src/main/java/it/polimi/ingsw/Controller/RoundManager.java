package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Game;

public class RoundManager {

    private GameManager gameManager;
    private Game gameInstance;

    public RoundManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameInstance = gameManager.getGameInstance();
    }


    private Cloud initClouds() {
        assert false;
        return null;
    }

    public void performAction(Performable action){
        if(action.canPerformExt(gameInstance)){
            action.performMove(gameInstance, gameManager.getRules());

            // deactivate a Character card if there is one active
            gameInstance.getActiveCharacter().ifPresent(characterCard ->
                    characterCard.deactivate(gameManager.getRules(),gameInstance ));
            // WinController.check(game)
        }
    }


}
