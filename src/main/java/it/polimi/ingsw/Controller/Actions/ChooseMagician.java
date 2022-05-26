package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Optional;

public class ChooseMagician extends Performable {
    private final int choice;

    public ChooseMagician(String player, int choice) {
        super(player);
        this.choice = choice;
    }

    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);
        if (!game.getGameState().equals(GameState.SETUP_CHOOSE_MAGICIAN)) {
            throw new WrongStateException("You first need to set your magician");
        }
        // A magician for each player
        int numAvailableMagicians = game.getAvailableMagicians().size();

        if(getPlayer(game).getMagician() != null){
            throw new GameException("You have already choosen a magician");
        }

        if (choice >= numAvailableMagicians || choice < 0 ) {
            throw new InvalidIndexException("magician", 0, numAvailableMagicians - 1, choice);
        }

    }

    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);

        // setting the player choice
        game.chooseMagician(player, game.getAvailableMagicians().get(choice));

    }

    @Override
    public String getNickNamePlayer() {
        return myNickName;
    }

    @Override
    public GameState nextState(Game game, Rules rules) {
        if(game.getPlayers().indexOf(getPlayer(game)) == game.getPlayers().size()-1){ // last player
            return GameState.PLANNING_CHOOSE_CARD;  // last player start the game ( planning phase )
        }
        else{
            return GameState.SETUP_CHOOSE_MAGICIAN;
        }
    }

    @Override
    public Player nextPlayer(Game game, Rules rules) {
        // todo HANDLE NEXT PLAYER NOT CONNECTED -> END GAME
        int currentPlayer = game.getPlayers().indexOf(getPlayer(game));

        if(currentPlayer == game.getPlayers().size()-1){ // last player
            return null; //the first player of planning phase
        }else{//others players
            return game.getPlayers().get(currentPlayer + 1);
        }

    }
}
