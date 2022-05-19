package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.stream.IntStream;

/**
 * It is used when a player decide not to go on with the effect of the active character Card (per i fino a)
 */
public class DeactivateCard extends Performable {
    private final int cardIndex;
    public DeactivateCard(String player, int cardIndex) {
        super(player);
        this.cardIndex = cardIndex;
    }

    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);
        if(cardIndex < 0 || cardIndex >= game.getCharacterCards().size()){
            throw new GameException("The selected card's index is not valid, [0," + game.getCharacterCards().size() +"]");
        }
        if(!game.getCharacterCards().get(cardIndex).isActive()){
            throw new GameException(game.getCharacterCards().get(cardIndex).getClass().getSimpleName() + " is already not active");
        }
        if(!game.getCharacterCards().get(cardIndex).getActivatingPlayer().equals(game.getRoundOwner().getNickname())){
            throw new GameException("Only the player who activated the card may deactivate it");
        }
    }

    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        game.deactivateCharacterCard(cardIndex, rules);
//        IntStream.range(0, game.getCharacterCards().size()).filter(i -> game.getCharacterCards().get(i).isActive()).forEach(i->game.deactivateCharacterCard(i,rules));
    }


}
