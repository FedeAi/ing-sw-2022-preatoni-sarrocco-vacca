package it.polimi.ingsw.listeners;

import it.polimi.ingsw.server.VirtualClient;

import java.beans.PropertyChangeListener;

/**
 * AbsListener class defines an interface used to communicate to View from Model.
 *
 * @see PropertyChangeListener
 */
public abstract class AbsListener implements PropertyChangeListener {

    final VirtualClient virtualClient;
    final String propertyName;

    /**
     * Constructor AbsListener creates a new AbsListener instance.
     *
     * @param client       the virtual client on Server.
     * @param propertyName the type of the listener to be set.
     */
    public AbsListener(VirtualClient client, String propertyName) {
        virtualClient = client;
        this.propertyName = propertyName;
    }

    /**
     * Method getPropertyName returns the type of listener instantiated.
     */
    public String getPropertyName() {
        return propertyName;
    }
}