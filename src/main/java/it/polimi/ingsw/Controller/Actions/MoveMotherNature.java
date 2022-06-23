package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.Grandma;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

/**
 * MoveMotherNature represents the action where Mother Nature is set to a new island,
 * and this causes the calculation of the new island influence for each player.
 */
public class MoveMotherNature extends Performable {

    private final int movement;

    /**
     * Constructor MoveMotherNature creates the action instance, and sets the movement index.
     *
     * @param player   the nickname of the action owner.
     * @param movement the number of islands chosen to move mother nature of.
     */
    public MoveMotherNature(String player, int movement) {
        super(player);
        this.movement = movement;
    }

    /**
     * Method canPerform extends the Performable definition with the MoveMotherNature specific checks.
     *
     * @param game  represents the game Model.
     * @param rules represents the current game rules.
     *
     * @throws InvalidPlayerException if the player is not in the current game.
     * @throws RoundOwnerException    if the player is not the current round owner.
     * @throws GameException          for generic errors.
     * @see Performable#canPerform(Game, Rules)
     */
    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);

        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.ACTION_MOVE_MOTHER)) {
            throw new WrongStateException("action phase, when you move mother nature.");
        }

        // is action legal check
        int playerCardMaxMoves = rules.getDynamicRules().computeMotherMaxMoves(player.getPlayedCard());
        if (movement < 1 || movement > playerCardMaxMoves) {
            throw new InvalidIndexException("mother nature movement", 1, playerCardMaxMoves, movement);
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     * Mother nature will be moved by a certain amount of islands, and then the new influence on it will be calculated.
     * After influence calculation, if the conditions for island merging are met, the method will merge them.
     * This method also sorts the Grandma blocking island game feature.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     * @see it.polimi.ingsw.Controller.Actions.CharacterActions.GrandmaBlockIsland
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        game.moveMotherNature(movement);
        int newMotherPosition = game.getMotherNature().getPosition();
        Island island = game.getIslandContainer().get(newMotherPosition);
        if (!island.isBlocked()) {
            // set owner ( put the Tower )
            Optional<String> islandNewOwner_opt = rules.getDynamicRules().computeIslandInfluence(game, island);
            if (islandNewOwner_opt.isPresent()) {
                String islandPrevOwner = island.getOwner();
                if (!islandNewOwner_opt.get().equals(islandPrevOwner)) {
                    game.setIslandOwner(newMotherPosition, islandNewOwner_opt.get());
                    // remove tower to the player
                    Optional<Player> islandOwnerPlayer_opt = game.getPlayerByNickname(islandNewOwner_opt.get());
                    islandOwnerPlayer_opt.ifPresent(owner -> owner.getSchool().decreaseTowers());
                    // give back the tower to the previous owner
                    Optional<Player> islandPrevPlayer_opt = game.getPlayerByNickname(islandPrevOwner);
                    islandPrevPlayer_opt.ifPresent(owner -> owner.getSchool().increaseTowers());
                }
            }
            // SuperIsland creation
            IslandContainer islandContainer = game.getIslandContainer();
            Island prevIsland = islandContainer.prevIsland(newMotherPosition);
            if (Island.checkJoin(prevIsland, game.getIslandContainer().get(newMotherPosition))) {
                game.joinPrevIsland(newMotherPosition);
                game.moveMotherNature(-1);
            }

            // SuperIsland creation
            newMotherPosition = game.getMotherNature().getPosition();
            Island nextIsland = islandContainer.nextIsland(newMotherPosition);
            if (Island.checkJoin(game.getIslandContainer().get(newMotherPosition), nextIsland)) {
                game.joinNextIsland(newMotherPosition);
                if (newMotherPosition == islandContainer.size()) {
                    game.moveMotherNature(-1);
                }
            }
        } else {
            game.setIslandBlock(newMotherPosition, false);
            Optional<CharacterCard> card = game.getCharacterCards().stream().filter(characterCard -> characterCard instanceof Grandma).findFirst();
            if (card.isEmpty()) {
                return;
            }
            Grandma grandma = (Grandma) card.get();
            grandma.addBlockingCard();
        }
    }

    /**
     * Method nextState determines the next game state after a MoveMotherNature action is executed.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     * @return the cloud selection game state.
     */
    @Override
    public GameState nextState(Game game, Rules rules) {
        return GameState.ACTION_CHOOSE_CLOUD;
    }
}