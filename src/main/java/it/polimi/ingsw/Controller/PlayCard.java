package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PlayCard implements Performable{
    private final String myNickName;
    private final AssistantCard choice;

    public PlayCard(String myNickName, AssistantCard choice){
        this.myNickName = myNickName;
        this.choice = choice;
    }

    @Override
    public boolean canPerformExt(Game game) {
        Optional<Player> player = game.getPlayerByNickname(myNickName);

        if(player.isEmpty())
            return  false;

        if(!player.equals(game.getRoundOwner()))
            return false;

        if(!game.getGameState().equals(GameState.PLANNING_PHASE))
            return false;


        if(!player.get().hasCard(choice))
            return false;


        // checks if the choice has already been played, or if I have no other choices
        if (!game.getPlayedCards().contains(choice) || game.getPlayedCards().containsAll(player.get().getCards())) {
            return true;
        }else{
            return false;
        }
    }


    @Override
    public void performMove(Game game) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if(player_opt.isEmpty())
            return;
        Player player = player_opt.get();

        player.setAndRemovePlayedCard(choice);

        // set new round owner or change game phase
        int playerIndex = game.getOrderedPlanningPlayers().indexOf(player);
        if (playerIndex == game.getPlayers().size() - 1) {

            game.setGameState(GameState.ACTION_PHASE);
            setActionOrder(game);

        } else {
            game.setRoundOwner(game.getOrderedPlanningPlayers().get(playerIndex + 1));
        }
    }

    @Override
    public String getNickNamePlayer() {
        return myNickName;
    }


    /**
     * choose Player orders for action phase
     */
    private void setActionOrder(@NotNull Game game){
        // TODO TESTINGGGGG
        List<Player> planningPhasePlayers = game.getOrderedPlanningPlayers();

        // compare by cards value
        Comparator<Player> compareByCardValue = (Player p1, Player p2) -> p1.getPlayedCard().getValue() -  p2.getPlayedCard().getValue();
        planningPhasePlayers.sort(compareByCardValue);
        game.setPlayersActionPhase(planningPhasePlayers);
        game.setRoundOwner(planningPhasePlayers.get(0));

    }
}
