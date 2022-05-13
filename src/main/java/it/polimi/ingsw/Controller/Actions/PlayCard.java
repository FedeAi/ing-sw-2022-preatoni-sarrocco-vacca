package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PlayCard extends Performable {

    private final AssistantCard choice;

    public PlayCard(String myNickName, AssistantCard choice) {
        super(myNickName);
        this.choice = choice;
    }

    @Override
    public void canPerform(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        if (!super.canPerform(game, rules)) {
            return false;
        }

        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.PLANNING_CHOOSE_CARD))
            return false;


        if (!player.hasCard(choice))
            return false;


        // checks if the choice has already been played, or if I have no other choices
        if (!game.getPlayedCards().contains(choice) || game.getPlayedCards().containsAll(player.getCards())) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void performMove(Game game, Rules rules) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if (player_opt.isEmpty())
            return;
        Player player = player_opt.get();

        game.playCard(player, choice);
        // TODO MOVE THIS TO ROUNDMANAGER (?) set new round owner or change game phase
        int playerIndex = game.getOrderedPlanningPlayers().indexOf(player);
        if (playerIndex == game.getPlayers().size() - 1) {

            game.setGameState(GameState.ACTION_MOVE_STUDENTS);
            setActionOrder(game);

        } else {
            game.setRoundOwner(game.getOrderedPlanningPlayers().get(playerIndex + 1));
        }
    }

    /**
     * choose Player orders for action phase
     */
    private void setActionOrder(Game game) {
        // xTODO TESTINGGGGG
        List<Player> planningPhasePlayers = game.getOrderedPlanningPlayers();

        // compare by cards value
        Comparator<Player> compareByCardValue = (Player p1, Player p2) -> p1.getPlayedCard().getValue() - p2.getPlayedCard().getValue();
        planningPhasePlayers.sort(compareByCardValue);
        game.setPlayersActionPhase(planningPhasePlayers);
        game.setRoundOwner(planningPhasePlayers.get(0));

    }
}
