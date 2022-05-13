package it.polimi.ingsw.Exceptions;

import it.polimi.ingsw.Constants.GameState;

public class WrongStateException extends GameException {
    private GameState state;

    public WrongStateException(GameState currentState) {
        this.state = currentState;
    }

    @Override
    public String getMessage() {
        return "This action is reserved for the " + state.toString();
    }
}
