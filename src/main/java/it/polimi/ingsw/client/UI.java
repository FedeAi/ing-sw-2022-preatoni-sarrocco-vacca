package it.polimi.ingsw.client;

import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.controller.rules.Rules;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UI class defines an interface for both CLI and GUI.
 * @see PropertyChangeListener
 */
public interface UI extends PropertyChangeListener {

    /**
     * Method statePrinter prints the current game state and the current round owner.
     *
     * @param modelView .
     * @return string describing the current state
     */
    static String statePrinter(ModelView modelView) {
        GameState currentState = modelView.getGameState();
        String s = "";
        String nickname = modelView.getPlayerName();
        if (modelView.amIRoundOwner()) {
            switch (currentState) {
                case PLANNING_CHOOSE_CARD -> s = "Please select a card.";
                case ACTION_MOVE_STUDENTS -> {
                    int currentEntry = modelView.getPlayerMapSchool().get(nickname).getStudentsEntryList().size();
                    int maxStudents = Rules.getStudentsPerTurn(modelView.getPlayers().size());
                    int diff = Rules.getEntrySize(modelView.getPlayers().size()) - currentEntry;
                    s = "Please move your students. You have moved "
                            + diff + " out of " + maxStudents + " students this turn.";
                }
                case ACTION_MOVE_MOTHER ->
                        s = "Please select and island to move mother nature to. Available movement is from 1 to " +
                                modelView.getPlayedCards().get(nickname).getMaxMoves();
                case ACTION_CHOOSE_CLOUD -> s = "Please select a cloud.";
                case GRANDMA_BLOCK_ISLAND -> s = "Please select an island to block.";
                case HERALD_ACTIVE -> s = "Please select an island to calculate the influence on.";
                case JOKER_SWAP_STUDENTS ->
                        s = "Please swap a maximum of 3 students between the card and your school's entry.";
                case MINSTREL_SWAP_STUDENTS ->
                        s = "Please swap a maximum of 2 students between your school's entry and hall.";
                case MONK_MOVE_STUDENT -> s = "Please move a student from the card to an island.";
                case MUSHROOM_CHOOSE_COLOR -> s = "Please select a color to disable the influence calculation on.";
                case PRINCESS_MOVE_STUDENT ->
                        s = "Please select a student from the card to be moved to your school's hall.";
                case THIEF_CHOOSE_COLOR -> s = "Please select a color to steal a maximum of 3 from each player's hall.";
            }
        } else {
            nickname = modelView.getRoundOwner();
            s = "Player " + nickname;
            switch (currentState) {
                case PLANNING_CHOOSE_CARD -> s = s + " is selecting his card.";
                case ACTION_MOVE_STUDENTS -> s = s + " is moving his students.";
                case ACTION_MOVE_MOTHER -> s = s + " is moving mother nature.";
                case ACTION_CHOOSE_CLOUD -> s = s + " is selecting a cloud.";
                default -> s = s + " is playing.";
            }
        }

        // In case of disconnection override s
        if(modelView.getActivePlayers().size() == 1){
            List<String > notConnectedPlayers = modelView.getPlayers();
            notConnectedPlayers.remove(modelView.getPlayerName());
            s = "Waiting " + notConnectedPlayers.stream().collect(Collectors.joining(", ", "", " ")) +
                    "to reconnect";
        }
        if(!modelView.getActivePlayers().contains(modelView.getPlayerName())){
            s = "Waiting for the next round to resume the game";
        }
        return s;
    }
}
