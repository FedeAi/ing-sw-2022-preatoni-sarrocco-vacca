package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.cli.CLI;
import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Client.messages.turn.GameStartedMessage;
import it.polimi.ingsw.Server.Answer.Answer;
import it.polimi.ingsw.Server.Answer.ConnectionMessage;
import it.polimi.ingsw.Server.Answer.ReqMagicianMessage;
import it.polimi.ingsw.Server.Answer.ReqPlayersMessage;

import java.beans.PropertyChangeSupport;

/**
 * ServerMessageHandler class handles the answers from the server notifying the correct part of the GUI or
 * CLI through property change listeners.
 *
 * @author Federico Sarrocco
 */
public class ServerMessageHandler {

  public static final String GAME_SETUP_LISTENER = "gameSetup";

  private final ModelView modelView;
  private final PropertyChangeSupport view = new PropertyChangeSupport(this);
  private CLI cli;
  private GUI gui;

  /**
   * Constructor of the ServerMessageHandler in case players are using the CLI.
   *
   * @param cli of type CLI - the command line interface reference.
   * @param modelView of type ModelView - the structure, stored into the client, containing simple
   *     logic of the model.
   */
  public ServerMessageHandler(CLI cli, ModelView modelView) {
    this.cli = cli;
    view.addPropertyChangeListener(cli);
    this.modelView = modelView;
  }

  /**
   * Constructor of the ServerMessageHandler in case players are using the GUI.
   *
   * @param gui of type GUI - the graphical user interface reference.
   * @param modelView of type ModelView - the structure, stored into the client, containing simple
   *     logic of the model.
   */
  public ServerMessageHandler(GUI gui, ModelView modelView) {
    this.gui = gui;
    view.addPropertyChangeListener(gui);
    this.modelView = modelView;
  }

  /**
   * Method answerHandler handles the answer received from the server. It calls the client interface
   * passing values relying on the type of answer the server has sent.
   */
  public void answerHandler() {
    Answer answer = modelView.getServerAnswer();
    if (answer instanceof ConnectionMessage) {
      ConnectionMessage connectionMessage = (ConnectionMessage) answer;
      System.out.println(connectionMessage.getMessage());
    }
  }

  public void setupMessageHandler(Answer answer){
    if(answer instanceof ReqPlayersMessage){
      view.firePropertyChange(GAME_SETUP_LISTENER, null, "ReqPlayers");  // ((ReqPlayersMessage)answer).getClass().getSimpleName() TODO this should also work
    } else if (answer instanceof ReqMagicianMessage) {
      view.firePropertyChange(GAME_SETUP_LISTENER, null, "ReqMagician");
    }
    else if (answer instanceof GameStartedMessage) {
      view.firePropertyChange("gameStarted", null, null);
    }

  }


}
