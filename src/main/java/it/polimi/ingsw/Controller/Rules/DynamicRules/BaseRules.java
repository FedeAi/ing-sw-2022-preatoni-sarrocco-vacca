package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.School;

import java.util.*;
import java.util.stream.Stream;

/**
 * BaseRules class represent the normal version of the game's DynamicRules, that may get modified by CharacterCards.
 */
public class BaseRules implements DynamicRules {

    /**
     * Method influence comparator checks if a player has more influence than another.
     *
     * @param influence1 the first player's influence.
     * @param influence2 the second player's influence.
     * @return true if the first player has more influence, false otherwise.
     */
    protected boolean influenceComparator(int influence1, int influence2) {
        return Integer.compare(influence1, influence2) > 0;
    }

    /**
     * Method getProfessorInfluence checks if current round owner has the maximum influence on every color.
     *
     * @param game the current game instance
     * @return the updated map of professors - players.
     */
    @Override
    public EnumMap<Color, String> getProfessorInfluence(Game game) {
        EnumMap<Color, String> outProfessorsInfluence = new EnumMap<>(Color.class);
        EnumMap<Color, String> initialProfessorInfluence = game.getProfessors();
        for (Color prof : Color.values()) {
            // Find who has the most students in the hall of that color
            Stream<Player> playersWithStudentsInHall = game.getPlayers().stream().filter(player -> player.getSchool().getStudentsHall().get(prof) != null);
            Optional<Player> tempOwner = playersWithStudentsInHall.max((p1, p2) -> Integer.compare(p1.getSchool().getStudentsHall().getOrDefault(prof, 0), p2.getSchool().getStudentsHall().getOrDefault(prof, 0)));
            if (tempOwner.isPresent()) {
                // Compare against the influence comparator for current owner
                String currentOwner = initialProfessorInfluence.get(prof);
                if (currentOwner == null) {
                    outProfessorsInfluence.put(prof, tempOwner.get().getNickname());
                } else {
                    School currentOwnerSchool = game.getPlayerByNickname(currentOwner).get().getSchool();
                    if (influenceComparator(tempOwner.get().getSchool().getStudentsHall().getOrDefault(prof, 0), currentOwnerSchool.getStudentsHall().getOrDefault(prof, 0))) {
                        outProfessorsInfluence.put(prof, tempOwner.get().getNickname());
                    } else {
                        outProfessorsInfluence.put(prof, currentOwner);
                    }
                }
            } else {
                outProfessorsInfluence.put(prof, null);
            }
        }
        return outProfessorsInfluence;
    }

    /**
     * Method computeMotherMaxMoves returns the value of the maximum movement of MotherNature.
     *
     * @param card the AssistantCard that has been played by the current player.
     */
    @Override
    public int computeMotherMaxMoves(AssistantCard card) {
        return card.getMaxMoves();
    }

    /**
     * Method computeIslandInfluence calculates the player with the maximum influence on that island,
     * or Optional.Empty if no players have influence.
     *
     * @param game   the current game instance reference.
     * @param island the reference to the island to calculate the influence on.
     * @return The nickname of the new owner, empty otherwise.
     */
    @Override
    public Optional<String> computeIslandInfluence(Game game, Island island) {
        Map<String, Integer> playerInfluence = new HashMap<>();
        Map<Color, String> professors = game.getProfessors();

        for (Map.Entry<Color, Integer> islandStudentInflunce : island.getStudents().entrySet()) {
            Color student = islandStudentInflunce.getKey();
            int influence = influenceModifier(game, student, islandStudentInflunce.getValue()); // used to modify if character MushRoom is active
            // let's find which player has the professor of color student
            String player = professors.get(student);
            if (player != null) {
                // increment influence of player that has the professor of a specific color (student)
                playerInfluence.put(player, playerInfluence.getOrDefault(player, 0) + influence);
            }
        }
        // take in account the + island.getNumTower() given by player towers
        if (island.getNumTower() != 0) {
            playerInfluence.put(island.getOwner(), playerInfluence.getOrDefault(island.getOwner(), 0) + towerInfluenceModifier(island.getNumTower()));
        }
        turnOwnerInfluenceModifier(playerInfluence, game.getRoundOwner().getNickname());
        // let's find the player who has the bigger influence
        Optional<Integer> maxInfluenceValue = playerInfluence.entrySet().stream().max((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue())).map(Map.Entry::getValue);
        if (maxInfluenceValue.isPresent() && maxInfluenceValue.get() > 0) {
            List<String> maxPlayers = playerInfluence.entrySet().stream().filter(stringIntegerEntry -> stringIntegerEntry.getValue() == maxInfluenceValue.get()).map(Map.Entry::getKey).toList();
            if (maxPlayers.size() == 1) {
                return Optional.of(maxPlayers.get(0));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Method influenceModifier is a simple function that will be overridden by the MushroomRules.
     *
     * @return the input influence without any changes.
     * @see MushroomRules
     */
    protected int influenceModifier(Game game, Color student, int influence) {
        return influence;
    }

    /**
     * Method towerInfluenceModifier is a simple function that will be overridden by the CentaurRules.
     *
     * @return the input tower influence without any changes.
     * @see CentaurRules
     */
    protected int towerInfluenceModifier(int value) {
        return value;
    }

    /**
     * Method turnOwnerInfluenceModifier is a simple function that will be overridden by the KnightRules.
     *
     * @see KnightRules
     */
    protected void turnOwnerInfluenceModifier(Map<String, Integer> playerInfluence, String roundOwner) {
        return;
    }
}