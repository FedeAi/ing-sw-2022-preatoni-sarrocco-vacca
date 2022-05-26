package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Server.VirtualClient;
import java.beans.PropertyChangeListener;

/**
 * AbsListener class defines an interface used to communicate to View from Model.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see PropertyChangeListener
 */
public abstract class AbsListener implements PropertyChangeListener {

  final VirtualClient virtualClient;
  final String propertyName;


  /**
   * Constructor AbsListener creates a new AbsListener instance.
   *
   * @param client       of type VirtualClient - the virtual client on Server.
   * @param propertyName
   */
  public AbsListener(VirtualClient client, String propertyName) {
    virtualClient = client;
    this.propertyName = propertyName;
  }

  public String getPropertyName(){
    return propertyName;
  }
}
