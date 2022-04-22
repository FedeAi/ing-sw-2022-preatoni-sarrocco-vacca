package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Player;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BaseRules implements DynamicRules{

    protected boolean influenceComparator(int influence1, int influence2) {
        return Integer.compare(influence1, influence2) > 0;
    }

    @Override
    public EnumMap<Color, String> getProfessorInfluence(Game game) {
        Player roundOwner = game.getRoundOwner();
        String roundOwnerNickname = game.getRoundOwner().getNickname();
        Island motherNatureIsland = game.getIslandContainer().get( game.getMotherNature().getPosition());
        Map<Color, Integer> studentsOnIsland = motherNatureIsland.getStudents();

        EnumMap<Color,String> professorsInfluence = new EnumMap<Color, String>(Color.class);

        for(Color prof : Color.values()){
            // find who has the most students in the hall of that color
            Optional<Player> tempOwner = game.getPlayers().stream().max((p1, p2)->Integer.compare(p1.getSchool().getStudentsHall().getOrDefault(prof,0),p2.getSchool().getStudentsHall().getOrDefault(prof,0)));
            if(tempOwner.isPresent()){
                //compare against the influence comparator for turn owner
                if(influenceComparator(roundOwner.getSchool().getStudentsHall().getOrDefault(prof,0), tempOwner.get().getSchool().getStudentsHall().getOrDefault(prof,0))){
                    professorsInfluence.put(prof,roundOwnerNickname);
                }else{
                    professorsInfluence.put(prof,tempOwner.get().getNickname());
                }

            }
            else {
                professorsInfluence.put(prof,null);
            }
        }
        return professorsInfluence;
    }

    @Override
    public int computeMotherMaxMoves(AssistantCard card) {
        return card.getMaxMoves();
    }

    @Override
    public Optional<String> computeIslandInfluence(Game game, int islandIndex) {
        if (game.getIslandContainer().isFeasibleIndex(islandIndex)) { // perform only if the island index is ok
            Map<String, Integer> playerInfluence = new HashMap<>();
            Map<Color, String> professors = game.getProfessors();
            Island island = game.getIslandContainer().get(islandIndex);

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
            turnOwnerInfluenceModifier(playerInfluence,game.getRoundOwner().getNickname());
            // let's find the player who has the bigger influence
            Optional<Map.Entry<String, Integer>> maxPlayer = playerInfluence.entrySet().stream().max((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()));
            Optional<String> player = Optional.ofNullable(maxPlayer.isPresent() ? maxPlayer.get().getKey() : null);
            return player;
        }
        return Optional.empty();
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
     * @param value
     * @return
     */
    protected int towerInfluenceModifier(int value) {
        return value;
    }

    /**
     * This function is used in by knightEffect
     * @param playerInfluence
     */
    protected void turnOwnerInfluenceModifier(Map<String, Integer> playerInfluence, String roundOwner) {
        return;
    }
}
