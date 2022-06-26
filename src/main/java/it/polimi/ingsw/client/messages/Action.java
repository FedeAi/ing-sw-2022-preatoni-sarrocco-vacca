package it.polimi.ingsw.client.messages;

import it.polimi.ingsw.constants.ActionType;
import it.polimi.ingsw.constants.Color;

import java.io.Serializable;

/**
 * Action class is the type of message used for sending actions made from the client to the server.
 * This class features different constructor based on the type of action to be formatted.
 */
public class Action implements Serializable {
    public final ActionType actionType;
    public final Integer int0;
    public final Color color0;
    public final Color color1;

    /**
     * Constructor Action creates a new Action instance.
     *
     * @param actionType identifies the type of action to be parsed by the receiver.
     */
    public Action(ActionType actionType) {
        this.actionType = actionType;
        int0 = null;
        color0 = null;
        color1 = null;
    }

    /**
     * Constructor Action creates a new Action instance.
     *
     * @param actionType identifies the type of action to be parsed by the receiver.
     * @param int0       identifies the first integer parameter of the action.
     */
    public Action(ActionType actionType, int int0) {
        this.actionType = actionType;
        this.int0 = int0;
        color0 = null;
        color1 = null;
    }

    /**
     * Constructor Action creates a new Action instance.
     *
     * @param actionType identifies the type of action to be parsed by the receiver.
     * @param color0     identifies the first color parameter of the action.
     */
    public Action(ActionType actionType, Color color0) {
        this.actionType = actionType;
        int0 = null;
        this.color0 = color0;
        color1 = null;
    }

    /**
     * Constructor Action creates a new Action instance.
     *
     * @param actionType identifies the type of action to be parsed by the receiver.
     * @param color0     identifies the first color parameter of the action.
     * @param color1     identifies the second color parameter of the action.
     */
    public Action(ActionType actionType, Color color0, Color color1) {
        this.actionType = actionType;
        int0 = null;
        this.color0 = color0;
        this.color1 = color1;
    }

    /**
     * Constructor Action creates a new Action instance.
     *
     * @param actionType identifies the type of action to be parsed by the receiver.
     * @param color0     identifies the first color parameter of the action.
     * @param int0       identifies the first integer parameter of the action.
     */
    public Action(ActionType actionType, Color color0, int int0) {
        this.actionType = actionType;
        this.int0 = int0;
        this.color0 = color0;
        color1 = null;
    }
}