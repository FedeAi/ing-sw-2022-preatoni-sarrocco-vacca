package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class ChooseCloud extends Performable {
    private final int choice;

    public ChooseCloud(String player, int choice) {
        super(player);
        this.choice = choice;
    }

    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);
        if (!game.getGameState().equals(GameState.ACTION_CHOOSE_CLOUD)) {
            throw new WrongStateException("final step of the action phase.");
        }
        // A cloud for every player
        int numCloud = game.numPlayers();
        List<Cloud> clouds = game.getClouds();

        if (choice >= numCloud || choice < 0 || clouds.get(choice).isEmpty()) {
            throw new InvalidIndexException("cloud", 0, numCloud - 1, choice);
        }
    }

    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);

        player.getSchool().addStudentsEntry(game.pickCloud(choice));


        if (nextPlayer(game, rules) == null) { //if end Turn
            game.refillClouds(); //refill of clouds
        }

    }

    @Override
    public String getNickNamePlayer() {
        return myNickName;
    }

    @Override
    public GameState nextState(Game game, Rules rules) {
        if (nextPlayer(game, rules) == null) { //if end Turn
            return GameState.PLANNING_CHOOSE_CARD;
        } else {    // next player turn -> move students
            return GameState.ACTION_MOVE_STUDENTS;
        }
    }

    /**
     *
     * @param game
     * @param rules
     * @return null if there is no next player for the action phase (end of round)
     */
    @Override
    public Player nextPlayer(Game game, Rules rules) {
        Optional<String> nextActionPlayer = game.getNextPlayerActionPhase();
        Player nextPlayer;
        int i = 1;

        while(nextActionPlayer.isPresent() && !game.getPlayerByNickname(nextActionPlayer.get()).get().isConnected()){
            i++;
            nextActionPlayer = game.getNextPlayerActionPhasePlus(i);

        }

        if (nextActionPlayer.isEmpty()) { //if end Turn
            nextPlayer = null;
        } else {
            Optional<Player> nextPlayerAction = game.getPlayerByNickname(nextActionPlayer.get());
            nextPlayer = nextPlayerAction.get();
        }
        return nextPlayer;
    }
}
