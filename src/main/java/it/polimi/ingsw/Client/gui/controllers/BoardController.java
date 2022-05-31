package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.ConnectionSocket;
import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Client.gui.GUIController;
import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Constants.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Constants.Exceptions.InvalidNicknameException;
import it.polimi.ingsw.Model.Islands.Island;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.List;

public class BoardController implements GUIController {

    GUI gui;

    @FXML
    GridPane grid;

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void showContainer(){
        List<Island> islands = gui.getModelView().getIslandContainer().getIslands();

        for(int i = 0; i < islands.size(); i++){
            Pair<Integer, Integer> position = computeIslandPosition(i);
            grid.add(buildIsland(islands.get(i)), position.getKey(), position.getValue());
        }
    }

    private Node buildIsland(Island island) {
        return null;
    }

    //TODO BUILD ISLAND

    private Pair<Integer, Integer> computeIslandPosition(int islandIndex){
        int x = 0;
        int y = 0;
        switch (islandIndex){
            case 0 -> {x=0;y=0;}
            case 1 -> {x=1;y=0;}
            case 2 -> {x=2;y=0;}
            case 3 -> {x=3;y=0;}
            case 4 -> {x=4;y=0;}
            case 5 -> {x=4;y=1;}
            case 6 -> {x=4;y=2;}
            case 7 -> {x=3;y=2;}
            case 8 -> {x=2;y=2;}
            case 9 -> {x=1;y=2;}
            case 10 -> {x=0;y=2;}
            case 11 -> {x=0;y=1;}
        }
        return new Pair<>(x,y);
    }

    /**
     * Method quit kills the application when the "Quit" button is pressed.
     */
    @FXML
    public void quit() {
        System.out.println("Thanks for playing! See you next time!");
        System.exit(0);
    }


}
