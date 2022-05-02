package it.polimi.ingsw.Client.messages;

import it.polimi.ingsw.Constants.ActionType;
import it.polimi.ingsw.Constants.Color;

import java.io.Serializable;

public class Action implements Serializable {
    public final ActionType action;
    public final Integer int0;
    public final Color color0;
    public final Color color1;

    public Action(ActionType action) {
        this.action = action;
        int0 = null;
        color0 = null;
        color1 = null;
    }

    public Action(ActionType action, int int0) {
        this.action = action;
        this.int0 = int0;
        color0 = null;
        color1 = null;
    }

    public Action(ActionType action, Color color0) {
        this.action = action;
        int0 = null;
        this.color0 = color0;
        color1 = null;
    }

    public Action(ActionType action, Color color0, Color color1) {
        this.action = action;
        int0 = null;
        this.color0 = color0;
        this.color1 = color1;
    }

    public Action(ActionType action, Color color0, int int0) {
        this.action = action;
        this.int0 = int0;
        this.color0 = color0;
        color1 = null;
    }
}
