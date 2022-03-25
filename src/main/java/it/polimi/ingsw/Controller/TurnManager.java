package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

public class TurnManager {

    private GameManager gameManager;
    private Player turnOwner;
    private RoundManager roundManager;

    public TurnManager(GameManager gameManager){
        this.gameManager = gameManager;
        this.roundManager = new RoundManager(gameManager, this);

    }
    public Player getTurnOwner() {
        return turnOwner;
    }

    /**
     * Method that inits the {@link TurnManager TurnManager} for the starting {@link Game Game}
     */
    void initTurnManager() {
        this.turnManager = new TurnManager(gameInstance.getPlayers());
    }

}

