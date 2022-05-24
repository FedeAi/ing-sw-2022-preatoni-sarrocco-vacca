package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.School;

import java.util.*;
import java.util.stream.Stream;

public class BaseRules implements DynamicRules {

    protected boolean influenceComparator(int influence1, int influence2) {
        return Integer.compare(influence1, influence2) > 0;
    }

    @Override
    public EnumMap<Color, String> getProfessorInfluence(Game game) {
        Player roundOwner = game.getRoundOwner();
        String roundOwnerNickname = game.getRoundOwner().getNickname();
        Island motherNatureIsland = game.getIslandContainer().get(game.getMotherNature().getPosition());
        Map<Color, Integer> studentsOnIsland = motherNatureIsland.getStudents();

        EnumMap<Color, String> outProfessorsInfluence = new EnumMap<Color, String>(Color.class);
        EnumMap<Color, String> initialProfessorInfluence = game.getProfessors();

        for (Color prof : Color.values()) {
            // find who has the most students in the hall of that color
            Stream<Player> playersWithStudentsInHall = game.getPlayers().stream().filter(player -> player.getSchool().getStudentsHall().get(prof) != null);
            Optional<Player> tempOwner = playersWithStudentsInHall.max((p1, p2) -> Integer.compare(p1.getSchool().getStudentsHall().getOrDefault(prof, 0), p2.getSchool().getStudentsHall().getOrDefault(prof, 0)));
            if (tempOwner.isPresent()) {
                //compare against the influence comparator for current owner
                String currentOwner = initialProfessorInfluence.get(prof);
                if (currentOwner == null) {
                    outProfessorsInfluence.put(prof, tempOwner.get().getNickname());
                } else {
                    School currentOwnerSchool = game.getPlayerByNickname(currentOwner).get().getSchool();
                    if (influenceComparator(currentOwnerSchool.getStudentsHall().getOrDefault(prof, 0), tempOwner.get().getSchool().getStudentsHall().getOrDefault(prof, 0))) {
                        outProfessorsInfluence.put(prof, currentOwner);
                    } else {
                        outProfessorsInfluence.put(prof, tempOwner.get().getNickname());
                    }

                }

            } else {
                outProfessorsInfluence.put(prof, null);
            }
        }
        return outProfessorsInfluence;
    }

    @Override
    public int computeMotherMaxMoves(AssistantCard card) {
        return card.getMaxMoves();
    }

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
        if (maxInfluenceValue.isPresent()) {
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
     * This function is used by MushRoomEffect
     *
     * @param student
     * @param influence
     * @param game
     * @return
     */
    protected int influenceModifier(Game game, Color student, int influence) {
        return influence;
    }

    /**
     * This function is used by CentaurEffect
     *
     * @param value
     * @return
     */
    protected int towerInfluenceModifier(int value) {
        return value;
    }

    /**
     * This function is used in by knightEffect
     *
     * @param playerInfluence
     */
    protected void turnOwnerInfluenceModifier(Map<String, Integer> playerInfluence, String roundOwner) {
        return;
    }
}
