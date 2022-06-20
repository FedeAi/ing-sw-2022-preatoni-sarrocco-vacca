package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Cards.CharacterCards.ReducedCharacterCard;
import javafx.application.Platform;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Objects;

/**
 * This class translates JavaFx events into well formatted comands to be passed to InputToMessage
 *
 * @see it.polimi.ingsw.Client.InputToMessage
 */
public class EventsToActions implements PropertyChangeListener {
    PropertyChangeEvent currEvt = null;
    PropertyChangeEvent prevEvt = null;
    GUI gui;

    public EventsToActions(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        currEvt = evt;
        onEventReceived();
        prevEvt = evt;
    }

    private void onEventReceived() {
        System.out.println(currEvt.getPropertyName());
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

            if (Objects.equals(currEvt.getPropertyName(), BoardController.CHARACTER_STUDENT_LISTENER)) {
                action = handleCharacterCardsEvents();
            }
            if (Objects.equals(currEvt.getPropertyName(), BoardController.CHARACTER_LISTENER)) {
                int cardIndex = (int) currEvt.getNewValue();
                if(gui.getModelView().getCharacterCards().get(cardIndex).isActive){
                    action = "DEACTIVATE " + cardIndex;
                }else{
                    action = "ACTIVATE " + cardIndex;
                }

            }



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

    private String handleCharacterCardsEvents(){
        List<Character> activeCards = gui.getModelView().getCharacterCards().stream()
                .filter(c -> c.isActive).map(c -> c.type).toList();
        if(Objects.equals(prevEvt.getPropertyName(), BoardController.CHARACTER_STUDENT_LISTENER)
                && Objects.equals(currEvt.getPropertyName(), BoardController.ENTRY_STUDENT_LISTENER)){
            if(activeCards.contains(Character.JOKER)){
                return "JOKER" + prevEvt.getNewValue().toString() + " " + currEvt.getNewValue().toString();
            }
        }
        return "";
    }
}


