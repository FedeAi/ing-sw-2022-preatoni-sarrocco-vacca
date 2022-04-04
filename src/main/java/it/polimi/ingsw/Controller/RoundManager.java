package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Game;

public class RoundManager {

    private GameManager gameManager;
    private Game gameInstance;

    public RoundManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameInstance = Game.getInstance();
    }


    private Cloud initClouds() {
        assert false;
        return null;
    }

    public void performAction(Performable action){
        if(action.canPerformExt(gameInstance)){
            action.performMove(gameInstance);
        }
    }


}
