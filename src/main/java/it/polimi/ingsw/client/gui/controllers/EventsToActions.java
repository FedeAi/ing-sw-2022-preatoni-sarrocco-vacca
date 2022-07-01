package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.ServerMessageHandler;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.constants.GameState;
import javafx.application.Platform;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Objects;

/**
 * EventsToActions class translates JavaFx events into well formatted commands to be passed to the InputToMessage class.
 *
 * @see it.polimi.ingsw.client.InputToMessage
 */
public class EventsToActions implements PropertyChangeListener {
    PropertyChangeEvent currEvt = null;
    PropertyChangeEvent prevEvt = null;
    GUI gui;

    /**
     * Constructor EventsToActions creates a new EventsToActions instance.
     *
     * @param gui the current GUI reference.
     */
    public EventsToActions(GUI gui) {
        this.gui = gui;
    }

    /**
     * Method propertyChange is called when an action on the GUI is received.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        currEvt = evt;
        onEventReceived();
        prevEvt = evt;
    }

    /**
     * Method onEventReceived processes the GUI action into the actual CLI commands.
     */
    private void onEventReceived() {

        String action = "";
        GameState currentState = gui.getModelView().getGameState();
        if (gui.getModelView().amIRoundOwner()) {
            switch (currentState) {
                case PLANNING_CHOOSE_CARD -> {
                    if (Objects.equals(currEvt.getPropertyName(), BoardController.SELECT_ASSISTANT_CARD_LISTENER)) {
                        action = "PLAYCARD " + String.valueOf((int) currEvt.getNewValue());
                    }
                }
                case ACTION_MOVE_STUDENTS -> {
                    if (prevEvt != null) {
                        if (Objects.equals(prevEvt.getPropertyName(), BoardController.ENTRY_STUDENT_LISTENER)) {
                            if (Objects.equals(currEvt.getPropertyName(), BoardController.SELECT_ISLAND_LISTENER)) {
                                action = "STUDENTISLAND " + prevEvt.getNewValue().toString() + " " + currEvt.getNewValue().toString();
                            }
                            if (Objects.equals(currEvt.getPropertyName(), BoardController.SCHOOL_HALL_LISTENER)) {
                                action = "STUDENTHALL " + prevEvt.getNewValue().toString();
                            }
                        }
                    }
                }
                case ACTION_MOVE_MOTHER -> {
                    if (Objects.equals(currEvt.getPropertyName(), BoardController.SELECT_ISLAND_LISTENER)) {
                        int mn = gui.getModelView().getMotherNature();
                        int islands = gui.getModelView().getIslandContainer().size();
                        int movement = ((int) currEvt.getNewValue() - mn + islands) % islands;
                        System.out.println(movement);
                        action = "MOVEMOTHER " + movement;
                    }
                }
                case ACTION_CHOOSE_CLOUD -> {
                    if (Objects.equals(currEvt.getPropertyName(), BoardController.CLOUD_LISTENER)) {
                        action = "CLOUD " + currEvt.getNewValue().toString();
                    }
                }
            }

            if (Objects.equals(currEvt.getPropertyName(), BoardController.CHARACTER_LISTENER)) {
                int cardIndex = (int) currEvt.getNewValue();
                if (gui.getModelView().getCharacterCards().get(cardIndex).isActive) {
                    action = "DEACTIVATE " + cardIndex;
                } else {
                    action = "ACTIVATE " + cardIndex;
                }

            }

            if(!handleCharacterCardsEvents().equals(""))
                action = handleCharacterCardsEvents();

            if (!action.equals("")) {
                final String actionToSend = action;
                Platform.runLater(() -> {
                    gui.getListeners().firePropertyChange("action", null, actionToSend);
                });
                // Event buffer empty
                prevEvt = null;
                currEvt = null;
            }
        }
    }

    /**
     * Method handleCharacterCardsEvents processes the characterCards events.
     * @return the characterCard action's command.
     */
    private String handleCharacterCardsEvents() {
        List<Character> activeCards = gui.getModelView().getCharacterCards().stream()
                .filter(c -> c.isActive).map(c -> c.type).toList();
        if(prevEvt !=null){
            if (Objects.equals(prevEvt.getPropertyName(), BoardController.CHARACTER_STUDENT_LISTENER)
                    && Objects.equals(currEvt.getPropertyName(), BoardController.ENTRY_STUDENT_LISTENER)) {
                if (activeCards.contains(Character.JOKER)) {
                    return "JOKER " + prevEvt.getNewValue().toString() + " " + currEvt.getNewValue().toString();
                }
            }
            if (Objects.equals(prevEvt.getPropertyName(), BoardController.ENTRY_STUDENT_LISTENER)
                    && Objects.equals(currEvt.getPropertyName(), BoardController.SCHOOL_HALL_LISTENER)) {
                if (activeCards.contains(Character.MINSTREL)) {
                    return "MINSTREL " + prevEvt.getNewValue().toString() + " " + currEvt.getNewValue().toString();
                }
            }
            if (Objects.equals(prevEvt.getPropertyName(), BoardController.CHARACTER_STUDENT_LISTENER)
                    && Objects.equals(currEvt.getPropertyName(), BoardController.SELECT_ISLAND_LISTENER)) {
                if (activeCards.contains(Character.MONK)) {
                    return "MONK " + prevEvt.getNewValue().toString() + " " + currEvt.getNewValue().toString();
                }
            }
        }

        if (Objects.equals(currEvt.getPropertyName(), BoardController.CHARACTER_STUDENT_LISTENER)) {
            if (activeCards.contains(Character.MUSHROOM)) {
                return "MUSHROOM " + currEvt.getNewValue().toString();
            }
            if (activeCards.contains(Character.PRINCESS)) {
                return "PRINCESS " + currEvt.getNewValue().toString();
            }
            if (activeCards.contains(Character.THIEF)) {
                return "THIEF " + currEvt.getNewValue().toString();
            }
        }

        if (Objects.equals(currEvt.getPropertyName(), BoardController.SELECT_ISLAND_LISTENER)) {
            if (activeCards.contains(Character.GRANDMA)) {
                return "GRANDMA " + currEvt.getNewValue().toString();
            }
            if (activeCards.contains(Character.HERALD)) {
                return "HERALD " + currEvt.getNewValue().toString();
            }
        }
        return "";
    }
}