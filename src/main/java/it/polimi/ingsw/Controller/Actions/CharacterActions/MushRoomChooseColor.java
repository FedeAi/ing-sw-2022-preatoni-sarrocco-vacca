package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.CharacterCard;
import it.polimi.ingsw.Model.Cards.MushRoomCharacter;
import it.polimi.ingsw.Model.Enumerations.CharacterCardState;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class MushRoomChooseColor implements Performable {
    private final String myNickName;
    private final Color student;

    MushRoomChooseColor(String player, Color student) {
        this.myNickName = player;
        this.student = student;
    }

    @Override
    public boolean canPerformExt(Game game, Rules rules) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if (player_opt.isEmpty())    // if there is no Player with that nick
            return false;
        Player player = player_opt.get();

        if (!game.getRoundOwner().equals(player)) {   // if the player is not the round owner
            return false;
        }

        if (!game.getCharacterCardState().equals(CharacterCardState.MUSHROOM_CHOOSE_COLOR)) {
            return false;
        }

        // check the card is one of the extracted once
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

    @Override
    public String getNickNamePlayer() {
        return myNickName;
    }
}
