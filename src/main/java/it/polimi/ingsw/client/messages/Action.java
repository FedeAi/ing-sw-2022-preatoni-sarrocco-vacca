package it.polimi.ingsw.client.messages;

import it.polimi.ingsw.constants.ActionType;
import it.polimi.ingsw.constants.Color;

import java.io.Serializable;

public class Action implements Serializable {
    public final ActionType actionType;
    public final Integer int0;
    public final Color color0;
    public final Color color1;

    public Action(ActionType actionType) {
        this.actionType = actionType;
        int0 = null;
        color0 = null;
        color1 = null;
    }

    public Action(ActionType actionType, int int0) {
        this.actionType = actionType;
        this.int0 = int0;
        color0 = null;
        color1 = null;
    }

    public Action(ActionType actionType, Color color0) {
        this.actionType = actionType;
        int0 = null;
        this.color0 = color0;
        color1 = null;
    }

    public Action(ActionType actionType, Color color0, Color color1) {
        this.actionType = actionType;
        int0 = null;
        this.color0 = color0;
        this.color1 = color1;
    }

    public Action(ActionType actionType, Color color0, int int0) {
        this.actionType = actionType;
        this.int0 = int0;
        this.color0 = color0;
        color1 = null;
    }

}
