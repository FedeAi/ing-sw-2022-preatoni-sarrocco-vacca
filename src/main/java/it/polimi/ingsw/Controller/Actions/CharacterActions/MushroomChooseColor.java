package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.MushroomCharacter;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class MushroomChooseColor extends Performable {
    private final Color student;

    public MushroomChooseColor(String player, Color student) {
        super(player);
        this.student = student;
    }

    @Override
    public void canPerform(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        if (!super.canPerform(game, rules)) {
            return false;
        }

        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.MUSHROOM_CHOOSE_COLOR)) {
            return false;
        }

        // there is no an active card
        Optional<CharacterCard> card = game.getActiveCharacter();
        if (card.isEmpty())
            return false;

        // the active card is not the right one
        if (!(card.get() instanceof MushroomCharacter)) {
            return false;
        }

        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof MushroomCharacter)) {
            return false;
        }

        return true;
    }

    @Override
    public void performMove(Game game, Rules rules) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if (player_opt.isEmpty())    // if there is no Player with that nick
            return;
        Optional<CharacterCard> mushRoomCard = game.getCharacterCards().stream().filter(characterCard -> characterCard instanceof MushroomCharacter).findFirst();
        mushRoomCard.ifPresent(characterCard -> ((MushroomCharacter) characterCard).setStudent(this.student));
    }

}
