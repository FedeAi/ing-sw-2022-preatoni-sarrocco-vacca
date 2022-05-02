package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Server.VirtualClient;
import java.beans.PropertyChangeListener;

/**
 * WorkerListener class defines an interface used to communicate to View from Model.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see PropertyChangeListener
 */
public abstract class AbsListener implements PropertyChangeListener {

  final VirtualClient virtualClient;

  /**
   * Constructor WorkerListener creates a new WorkerListener instance.
   *
   * @param client of type VirtualClient - the virtual client on Server.
   */
  public AbsListener(VirtualClient client) {
    virtualClient = client;
  }
}
