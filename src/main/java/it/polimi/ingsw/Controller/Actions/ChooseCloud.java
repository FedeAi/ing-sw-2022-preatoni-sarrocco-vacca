package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Optional;

public class ChooseCloud implements Performable {
    private String myNickName;
    private int choice;

    ChooseCloud(String player, int choice) {
        this.myNickName = player;
        this.choice = choice;
    }

    @Override
    public boolean canPerformExt(Game game) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if (player_opt.isEmpty()) {   // if there is no Player with that nick
            return false;
        }
        Player player = player_opt.get();

        if (!game.getRoundOwner().equals(player)) {   // if the player is not the round owner
            return false;
        }

        if (!game.getGameState().equals(GameState.ACTION_CHOOSE_CLOUD)) {
            return false;
        }
        // is action legal check
        int numCloud = game.numPlayers(); //clouds as many as players
        List<Cloud> clouds = game.getClouds();

        if (choice > numCloud || choice < 0 || clouds.get(choice).isEmpty()) { //right choice
            return false;
        }

        return true;

    }

    @Override
    public void performMove(Game game, Rules rules) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if (player_opt.isEmpty())    // if there is no Player with that nick
            return;
        Player player = player_opt.get();

        List<Cloud> clouds = game.getClouds();
        player.getSchool().addStudentsEntry(clouds.get(choice).pickStudents());

        Optional<String> nextPlayer = game.getNextPlayerActionPhase();

        if (nextPlayer.isEmpty()) { //if end Turn
            game.setRoundOwner(game.getOrderedPlanningPlayers().get(0)); //the next player is the first of the next turn
            game.setGameState(GameState.PLANNING_CHOOSE_CARD);
            refillClouds(game); //refill of clouds
        } else {
            Optional<Player> nextPlayerAction = game.getPlayerByNickname(nextPlayer.get()); //the next player of action phase
            if (nextPlayerAction.isPresent()) {
                game.setRoundOwner(nextPlayerAction.get());
                game.setGameState(GameState.ACTION_MOVE_STUDENTS);
            }

        }

        // END OF PLAYER TURN -> deactivate Character effects if there is an active card
        game.getActiveCharacter().ifPresent(characterCard ->
                characterCard.deactivate(rules,game));

    }

    @Override
    public String getNickNamePlayer() {
        return myNickName;
    }

    private void refillClouds(Game game) {
        int numPlayers = game.numPlayers();
        for (Cloud c : game.getClouds()) {
            // TODO this breaks the ChooseCloud tests
            //  assert !(c.isEmpty());
            c.addStudents(game.getBag().extract(Rules.getStudentsPerTurn(numPlayers)));
        }
    }

}
