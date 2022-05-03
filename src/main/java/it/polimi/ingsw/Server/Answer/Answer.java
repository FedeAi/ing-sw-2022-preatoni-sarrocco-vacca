package it.polimi.ingsw.Server.Answer;

import java.io.Serializable;

/**
 * Answer class defines an interface for server answers. It's implemented by several messages, which
 * represent a specific type of communication that the server would have with the client.
 *
 * @author Alessadro Vacca
 * @see Serializable
 */
public interface Answer extends Serializable {
    /**
     * Method getMessage returns the message of this Answer object.
     *
     * @return the message (type Object) of this Answer object.
     */
    Object getMessage();
}
