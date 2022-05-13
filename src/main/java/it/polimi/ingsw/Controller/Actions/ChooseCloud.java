package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Exceptions.WrongStateException;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Optional;

public class ChooseCloud extends Performable {
    private final int choice;

    ChooseCloud(String player, int choice) {
        super(player);
        this.choice = choice;
    }

    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);

        if (!game.getGameState().equals(GameState.ACTION_CHOOSE_CLOUD)) {
            throw new WrongStateException("final step of the action phase");
        }
        // A cloud for every player
        int numCloud = game.numPlayers();
        List<Cloud> clouds = game.getClouds();

        if (choice >= numCloud || choice < 0 || clouds.get(choice).isEmpty()) { //right choice
            throw new GameException("Invalid cloud index selected: you can only select indexes from 0 to " + (numCloud - 1));
        }
    }

    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);

        player.getSchool().addStudentsEntry(game.pickCloud(choice));


        if (game.getNextPlayerActionPhase().isEmpty()) { //if end Turn
            game.refillClouds(); //refill of clouds
        }

        // END OF PLAYER TURN -> deactivate Character effects if there is an active card TODO not here
        game.getActiveCharacter().ifPresent(characterCard ->
                characterCard.deactivate(rules, game));

    }

    @Override
    public String getNickNamePlayer() {
        return myNickName;
    }

    @Override
    public GameState nextState(Game game, Rules rules){
        Optional<String> nextActionPlayer = game.getNextPlayerActionPhase();
        if (nextActionPlayer.isEmpty()) { //if end Turn
            return GameState.PLANNING_CHOOSE_CARD;
        } else {    // next player turn -> move students
            return GameState.ACTION_MOVE_STUDENTS;
        }
    }

    @Override
    public Player nextPlayer(Game game, Rules rules){
        Optional<String> nextActionPlayer = game.getNextPlayerActionPhase();
        Player nextPlayer;

        if (nextActionPlayer.isEmpty()) { //if end Turn
            nextPlayer = game.getOrderedPlanningPlayers().get(0);
        } else {
            Optional<Player> nextPlayerAction = game.getPlayerByNickname(nextActionPlayer.get());
            nextPlayer = nextPlayerAction.get();
        }
        return nextPlayer;
    }
}
