package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.CharacterCard;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class ActivateCard implements Performable {

    private String nickname;
    private int choice;

    public ActivateCard(String nickname, int choice) {
        this.nickname = nickname;
        this.choice = choice;
    }

    @Override
    public boolean canPerformExt(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name
        Optional<Player> player_opt = game.getPlayerByNickname(nickname);
        if (player_opt.isEmpty()) {
            return false;
        }
        Player player = player_opt.get();

        // By design, only the current one player can play at a time, we need to verify this
        if (!game.getRoundOwner().equals(player)) {
            return false;
        }

        // Simple check to verify that we're in the correct state
        if (!game.getGameState().equals(GameState.ACTION_MOVE_MOTHER)||!game.getGameState().equals(GameState.ACTION_MOVE_STUDENTS)) {
            return false;
        }

        // Verify that we are under expert rules
        if (!game.isExpertMode()) {
            return false;
        }

        if(choice < 0 || choice > Rules.numCharacterCards){
            return false;
        }

        CharacterCard choiceCard = game.getCharacterCards().get(choice);
        // Verify that the player has enough money
        // TODO PRICE INCREMENT
        if (player.getBalance() < choiceCard.getPrice()) {
            return false;
        }
        // TODO IF THIS NEEDED?
        if (choiceCard.isActive()) {
            return false;
        }
        return true;
    }

    @Override
    public void performMove(Game game, Rules rules) {
        CharacterCard choiceCard = game.getCharacterCards().get(choice);
        choiceCard.activate(rules, game);
    }

    @Override
    public String getNickNamePlayer() {
        return null;
    }
}
