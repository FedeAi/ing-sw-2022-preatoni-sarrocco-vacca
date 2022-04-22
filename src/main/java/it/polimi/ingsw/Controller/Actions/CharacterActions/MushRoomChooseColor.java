package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.JockerCharacter;
import it.polimi.ingsw.Model.Cards.CharacterCards.MushRoomCharacter;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class MushRoomChooseColor extends Performable {
    private final Color student;

    MushRoomChooseColor(String player, Color student) {
        super(player);
        this.student = student;
    }

    @Override
    public boolean canPerformExt(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        if(!super.canPerformExt(game, rules)){
            return false;
        }

        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.MUSHROOM_CHOOSE_COLOR)) {
            return false;
        }

        // there is no an active card
        Optional<CharacterCard> card = game.getActiveCharacter();
        if(card.isEmpty())
            return false;

        // the active card is not the right one
        if(!(card.get() instanceof MushRoomCharacter)){
            return false;
        }

        if(game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof MushRoomCharacter)){
            return false;
        }

        return true;
    }

    @Override
    public void performMove(Game game, Rules rules) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if (player_opt.isEmpty())    // if there is no Player with that nick
            return;
        Optional<CharacterCard> mushRoomCard = game.getCharacterCards().stream().filter(characterCard -> characterCard instanceof MushRoomCharacter).findFirst();
        mushRoomCard.ifPresent(characterCard -> ((MushRoomCharacter) characterCard).setStudent(this.student));
    }

}
