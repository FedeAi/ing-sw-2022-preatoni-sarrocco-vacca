package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.cli.CLI;
import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Server.Answer.Answer;
import it.polimi.ingsw.Server.Answer.ConnectionMessage;
import it.polimi.ingsw.Server.Answer.game.BalanceMessage;
import it.polimi.ingsw.Server.GameError;

import java.beans.PropertyChangeSupport;

/**
 * ActionHandler class handles the answers from the server notifying the correct part of the GUI or
 * CLI through property change listeners.
 *
 * @author Federico Sarrocco
 */
public class ActionHandler {

  public static final String FIRST_BOARD_UPDATE = "firstBoardUpdate";
  public static final String BOARD_UPDATE = "boardUpdate";
  private static final String MAIN_SCENE_FXML = "mainScene.fxml";
  public static final String MODIFIED_TURN_NO_UPDATE = "modifiedTurnNoUpdate";
  public static final String SELECT = "select";
  private final ModelView modelView;
  private final PropertyChangeSupport view = new PropertyChangeSupport(this);
  private CLI cli;
  private GUI gui;

  /**
   * Constructor of the ActionHandler in case players are using the CLI.
   *
   * @param cli of type CLI - the command line interface reference.
   * @param modelView of type ModelView - the structure, stored into the client, containing simple
   *     logic of the model.
   */
  public ActionHandler(CLI cli, ModelView modelView) {
    this.cli = cli;
    view.addPropertyChangeListener(cli);
    this.modelView = modelView;
  }

  /**
   * Constructor of the ActionHandler in case players are using the GUI.
   *
   * @param gui of type GUI - the graphical user interface reference.
   * @param modelView of type ModelView - the structure, stored into the client, containing simple
   *     logic of the model.
   */
  public ActionHandler(GUI gui, ModelView modelView) {
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


}
