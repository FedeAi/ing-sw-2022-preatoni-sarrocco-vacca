package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.ServerMessageHandler;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.constants.Magician;
import it.polimi.ingsw.constants.TowerColor;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.cards.characters.ReducedCharacterCard;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.islands.Island;
import it.polimi.ingsw.model.islands.SuperIsland;
import it.polimi.ingsw.model.School;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * BoardController class represents the Board scene logic.
 */
public class BoardController extends GUIController implements PropertyChangeListener {

    public static final String ENTRY_STUDENT_LISTENER = "selectSchoolStudent";
    public static final String SCHOOL_HALL_LISTENER = "selectSchoolHall";
    public static final String SELECT_ISLAND_LISTENER = "selectIsland";
    public static final String SELECT_ASSISTANT_CARD_LISTENER = "selectAssistantCard";
    public static final String CLOUD_LISTENER = "selectCloud";
    public static final String CHARACTER_LISTENER = "character";
    public static final String CHARACTER_STUDENT_LISTENER = "characterStudent";
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);    // support

    @FXML
    Pane island0, island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11;
    @FXML
    GridPane islandGridPane;

    @FXML
    StackPane schoolPane;
    // entry students (school)
    @FXML
    Pane entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9;

    // hall students (school)
    @FXML
    Pane hallG1, hallG2, hallG3, hallG4, hallG5, hallG6, hallG7, hallG8, hallG9, hallG10,
            hallR1, hallR2, hallR3, hallR4, hallR5, hallR6, hallR7, hallR8, hallR9, hallR10,
            hallY1, hallY2, hallY3, hallY4, hallY5, hallY6, hallY7, hallY8, hallY9, hallY10,
            hallP1, hallP2, hallP3, hallP4, hallP5, hallP6, hallP7, hallP8, hallP9, hallP10,
            hallB1, hallB2, hallB3, hallB4, hallB5, hallB6, hallB7, hallB8, hallB9, hallB10;

    // player profs
    @FXML
    Pane playerProf1, playerProf2, playerProf3, playerProf4, playerProf5;
    // These represent the hall's lanes
    @FXML
    Pane greenLane, redLane, yellowLane, pinkLane, blueLane;
    @FXML
    Pane cloud0, cloud1, cloud2;

    @FXML
    Pane studentCloud1, studentCloud2, studentCloud3, studentCloud4, studentCloud5, studentCloud6, studentCloud7, studentCloud8, studentCloud9, studentCloud10, studentCloud11, studentCloud12;
    // swap school buttons
    @FXML
    Pane otherPlayer1Pane, otherPlayer2Pane;
    // card container
    @FXML
    HBox cardContainer;
    @FXML
    HBox playedCardContainer;
    @FXML
    HBox characterContainer;
    @FXML
    StackPane avatar0, avatar1, avatar2;
    @FXML
    Label username0, username1, username2;
    @FXML
    StackPane roundOwner0, roundOwner1, roundOwner2;
    @FXML
    Label balance;
    @FXML
    HBox balanceContainer;
    @FXML
    Label status;
    @FXML
    HBox towers;

    @FXML
    ProgressBar progressBar;

    private final ArrayList<Image> cloudImgs = new ArrayList<>();
    private final ArrayList<Pane> cloudsPane = new ArrayList<>();
    private final List<List<Pane>> cloudStudents = new ArrayList<>();
    private final Map<Color, Pane> hallLanes = new EnumMap<>(Color.class);

    private final Map<Magician, Image> avatarImgs = new EnumMap<>(Magician.class);

    private Image motherImg, coinImg, blockImg;
    private final ArrayList<Image> islandImgs = new ArrayList<>();

    private final ArrayList<Image> cardImgs = new ArrayList<>();

    private final HashMap<Color, Image> studentImgs = new HashMap<>();
    private final HashMap<Color, Image> profsImgs = new HashMap<>();
    private final HashMap<TowerColor, Image> towerImgs = new HashMap<>();

    private final ArrayList<Pane> islandPanes = new ArrayList<>();
    private final ArrayList<Pane> studentEntryPanes = new ArrayList<>();
    private final EnumMap<Color, List<Pane>> colorToHallStudents = new EnumMap<Color, List<Pane>>(Color.class);
    private final EnumMap<Color, Pane> colorToPlayerProfs = new EnumMap<Color, Pane>(Color.class);

    private final List<Label> playerLabels = new ArrayList<>();
    private final List<StackPane> playerAvatars = new ArrayList<>();
    private final List<StackPane> playerRoundOwners = new ArrayList<>();
    private final List<Pane> switchPlayerPanes = new ArrayList<>(); // list of buttons to change school
    private final EnumMap<Character, Image> charactersImages = new EnumMap<>(Character.class);
    String playerSchool = ""; // player's school name
    boolean initCompleted = false;

    /**
     * Method initialize creates all the GUI elements list and maps and loads the assets.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        islandPanes.addAll(List.of(island0, island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11));
        cloudsPane.addAll(List.of(cloud0, cloud1, cloud2));
        studentEntryPanes.addAll(List.of(entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9));
        // fill colorToHallStudents Map
        colorToHallStudents.put(Color.GREEN, List.of(hallG1, hallG2, hallG3, hallG4, hallG5, hallG6, hallG7, hallG8, hallG9, hallG10));
        colorToHallStudents.put(Color.RED, List.of(hallR1, hallR2, hallR3, hallR4, hallR5, hallR6, hallR7, hallR8, hallR9, hallR10));
        colorToHallStudents.put(Color.YELLOW, List.of(hallY1, hallY2, hallY3, hallY4, hallY5, hallY6, hallY7, hallY8, hallY9, hallY10));
        colorToHallStudents.put(Color.PINK, List.of(hallP1, hallP2, hallP3, hallP4, hallP5, hallP6, hallP7, hallP8, hallP9, hallP10));
        colorToHallStudents.put(Color.BLUE, List.of(hallB1, hallB2, hallB3, hallB4, hallB5, hallB6, hallB7, hallB8, hallB9, hallB10));
        // Add the lane FXML Panes to the list
        hallLanes.put(Color.GREEN, greenLane);
        hallLanes.put(Color.RED, redLane);
        hallLanes.put(Color.YELLOW, yellowLane);
        hallLanes.put(Color.PINK, pinkLane);
        hallLanes.put(Color.BLUE, blueLane);
        for (Color c : Color.values()) {
            hallLanes.get(c).setOnMouseReleased((e) -> changeSupport.firePropertyChange(SCHOOL_HALL_LISTENER, null, c));
        }
        //fill cloudToStudents
        cloudStudents.add(List.of(studentCloud1, studentCloud2, studentCloud3, studentCloud4));
        cloudStudents.add(List.of(studentCloud5, studentCloud6, studentCloud7, studentCloud8));
        cloudStudents.add(List.of(studentCloud9, studentCloud10, studentCloud11, studentCloud12));

        colorToPlayerProfs.put(Color.GREEN, playerProf1);
        colorToPlayerProfs.put(Color.RED, playerProf2);
        colorToPlayerProfs.put(Color.YELLOW, playerProf3);
        colorToPlayerProfs.put(Color.PINK, playerProf4);
        colorToPlayerProfs.put(Color.BLUE, playerProf5);

        switchPlayerPanes.addAll(List.of(otherPlayer1Pane, otherPlayer2Pane));

        playerLabels.addAll(List.of(username0, username1, username2));
        playerAvatars.addAll(List.of(avatar0, avatar1, avatar2));
        playerRoundOwners.addAll(List.of(roundOwner0, roundOwner1, roundOwner2));

        loadAssets();
        status.setFont(font);
    }

    /**
     * Method init initializes all the GUI elements.
     */
    @Override
    public void init() {
        showChangeSchoolButtons();
        // Clouds are handled here because the numOfClouds is only available after the Server communicates it.
        if (gui.getModelView().getClouds().size() == 2) {
            cloudImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/cloud_2p.png")));
            cloudImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/cloud_2p.png")));
        } else {
            cloudImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/cloud_3p.png")));
            cloudImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/cloud_3p.png")));
            cloudImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/cloud_3p.png")));
        }

        blockImg = new Image(getClass().getResourceAsStream(("/graphics/characters/deny_island_icon.png")));
        if (gui.getModelView().getExpert()) {
            coinImg = new Image(getClass().getResourceAsStream("/graphics/board/coin.png"));
            updateCharacters();
            showBalance();
        }
        showClouds();
        updateIslands();
        playerSchool = gui.getModelView().getPlayerName();
        updateSchool();
        updateProfessors();
        updateHand();
        showPlayers();
        updateStatus();
        initCompleted = true;
        changeSupport.addPropertyChangeListener(new EventsToActions(gui));
    }

    /**
     * Method loadAssets loads the game resources.
     */
    private void loadAssets() {
        islandImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/island1.png")));
        islandImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/island2.png")));
        islandImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/island3.png")));

        for (Magician m : Magician.values()) {
            avatarImgs.put(m, new Image(getClass().getResourceAsStream("/graphics/avatar/" + m.toString().toLowerCase() + ".png")));
        }

        for (int i = 1; i < 11; i++) {
            cardImgs.add(new Image(getClass().getResourceAsStream("/graphics/assistants/" + String.valueOf(i) + ".png")));
        }

        for (Color color : Color.values()) {
            studentImgs.put(color, new Image(getClass().getResourceAsStream("/graphics/board/" + color.name().toLowerCase() + "Student3D.png")));
            profsImgs.put(color, new Image(getClass().getResourceAsStream("/graphics/board/" + color.name().toLowerCase() + "Prof3D.png")));
        }

        for (TowerColor color : TowerColor.values()) {
            towerImgs.put(color, new Image(getClass().getResourceAsStream("/graphics/board/" + color.name().toLowerCase() + "_tower.png")));
        }

        motherImg = new Image(getClass().getResourceAsStream("/graphics/board/mother_nature.png"));

        for (Character c : Character.values()) {
            charactersImages.put(c, new Image(getClass().getResourceAsStream("/graphics/characters/" + c.toString().toLowerCase() + ".jpg")));
        }
    }

    /**
     * Method updateIslands sets the updated islands on the GUI.
     */
    public void updateIslands() {
        List<Island> islands = gui.getModelView().getIslandContainer().getIslands();

        // empty all island panes
        for (int i = 0; i < islandPanes.size(); i++) {
            islandPanes.get(i).getChildren().clear();
            islandPanes.get(i).setPrefHeight(Control.USE_COMPUTED_SIZE);
            islandPanes.get(i).setPrefWidth(Control.USE_COMPUTED_SIZE);
        }

        int viewIslandIndex = 0;
        for (int i = 0; i < islands.size(); i++) {
            Island island = islands.get(i);
            boolean hasMother = false;
            if (i == gui.getModelView().getMotherNature()) {
                hasMother = true;
            }
            Node newIsland = buildIsland(island, i, hasMother);
            islandPanes.get(viewIslandIndex).getChildren().add(newIsland);

            if (island instanceof SuperIsland) {
                viewIslandIndex += ((SuperIsland) island).getBaseIslands().size();
            } else {
                viewIslandIndex++;
            }
        }
    }

    /**
     * Method showClouds sets all the GUI clouds.
     */
    public void showClouds() {
        int nCloud = gui.getModelView().getClouds().size();
        // empty all clouds panes
        assert nCloud <= cloudsPane.size();
        for (int i = 0; i < nCloud; i++) {
            cloudsPane.get(i).setPrefHeight(100);
            cloudsPane.get(i).setPrefWidth(100);
            ImageView backCloud = new ImageView();
            backCloud.setImage(cloudImgs.get(i));
            backCloud.setFitHeight(100);
            backCloud.setFitWidth(100);
            cloudsPane.get(i).setOnMouseEntered(this::onSelectScaleColor);
            cloudsPane.get(i).setOnMouseExited(this::onSelectScaleColor);
            backCloud.setSmooth(true);
            backCloud.setCache(true);
            int index = i;
            cloudsPane.get(i).setOnMouseReleased((e) -> {
                changeSupport.firePropertyChange(CLOUD_LISTENER, null, index);
            });
            cloudsPane.get(i).getChildren().add(0, backCloud);
        }
        updateClouds();
    }

    /**
     * Method updateHand sets the updated hand on the GUI.
     */
    private void updateHand() {
        List<AssistantCard> cards = gui.getModelView().getHand();
        cardContainer.getChildren().clear();

        for (AssistantCard assistantCard : cards) {
            // build imageview of the card
            ImageView cardImg = buildCard(63, 92, cardImgs.get(assistantCard.getValue() - 1));
            cardImg.setOnMouseEntered((evt) -> {
                cardImg.setTranslateY(-50);
                onSelectScaleColor(evt);
            });
            cardImg.setOnMouseExited((evt) -> {
                cardImg.setTranslateY(0);
                onSelectScaleColor(evt);

            });
            cardImg.setOnMouseReleased((e) -> {
                changeSupport.firePropertyChange(SELECT_ASSISTANT_CARD_LISTENER, null, assistantCard.getValue());
                e.consume();
            });
            cardContainer.getChildren().add(cardImg);
        }
    }

    /**
     * Method updatePlayedCards sets the updated playedCards on the GUI.
     */
    private void updatePlayedCards() {
        Map<String, AssistantCard> map = gui.getModelView().getPlayedCards();
        playedCardContainer.getChildren().clear();
        for (Map.Entry<String, AssistantCard> entry : map.entrySet()) {
            StackPane s = new StackPane();
            ImageView playedCard = buildCard(80, 120, cardImgs.get(entry.getValue().getValue() - 1));
            s.getChildren().add(playedCard);
            ImageView player = new ImageView();
            player.setImage(avatarImgs.get(gui.getModelView().getPlayerMapMagician().get(entry.getKey())));
            player.setFitWidth(20);
            player.setFitHeight(20);
            s.getChildren().add(player);
            playedCardContainer.getChildren().add(s);
        }
    }

    /**
     * Method showPlayers sets the player's selected magician and nickname.
     */
    private void showPlayers() {
        List<String> players = gui.getModelView().getPlayers();
        for (int i = 0; i < players.size(); i++) {
            playerLabels.get(i).setFont(font);
            playerLabels.get(i).setStyle("-fx-font-size: 24");
            playerLabels.get(i).setText(players.get(i));
        }
        updatePlayers();
    }

    /**
     * Method updatePlayers sets the updated player info on the GUI.
     */
    private void updatePlayers() {
        Map<String, Magician> map = gui.getModelView().getPlayerMapMagician();
        List<String> players = gui.getModelView().getPlayers();

        for (int i = 0; i < players.size(); i++) {
            playerAvatars.get(i).getChildren().clear();
            playerRoundOwners.get(i).getChildren().clear();
            String username = players.get(i);
            ImageView magician = new ImageView();
            magician.setImage(avatarImgs.get(map.get(username)));
            magician.setFitWidth(55);
            magician.setFitHeight(55);
            if (!gui.getModelView().getConnectedPlayers().contains(username)) {
                // Avatar to grayScale
                ColorAdjust monochrome = new ColorAdjust();
                monochrome.setSaturation(-1);
                magician.setEffect(monochrome);
                // Blocking image on it
                ImageView block = new ImageView();
                block.setImage(blockImg);
                block.setFitWidth(55);
                block.setFitHeight(55);
                block.setEffect(monochrome);
                playerAvatars.get(i).getChildren().add(0, block);
            }
            playerAvatars.get(i).getChildren().add(0, magician);
            if (username.equals(gui.getModelView().getRoundOwner())) {
                ImageView owner = new ImageView(motherImg);
                owner.setFitWidth(50);
                owner.setFitHeight(50);
                playerRoundOwners.get(i).getChildren().add(owner);
            }
        }
    }

    /**
     * Method showChangeSchoolButtons creates the GUI buttons for showing other players' schools.
     */
    private void showChangeSchoolButtons() {
        List<Button> buttons = gui.getModelView().getPlayers().stream().filter(player -> !Objects.equals(player, gui.getModelView().getPlayerName()))
                .map(p -> {
                            Button btn = new Button(p);
                            btn.setId(p);
                            btn.setFont(font);
                            btn.setStyle(" -fx-background-color: transparent; -fx-background-image: url('/graphics/perg2.png'); -fx-background-size: stretch; -fx-font-size: 24px;");
                            btn.setOnMouseEntered(this::changeSchool);
                            btn.setOnMouseExited(this::changeSchool);
                            return btn;
                        }
                ).toList();
        assert buttons.size() <= switchPlayerPanes.size();

        for (int i = 0; i < buttons.size(); i++) {
            switchPlayerPanes.get(i).getChildren().clear();
            switchPlayerPanes.get(i).getChildren().add(buttons.get(i));
        }
    }

    /**
     * Method updateClouds sets the updated clouds data on the GUI.
     */
    private void updateClouds() {
        List<Cloud> clouds = gui.getModelView().getClouds();
        for (int j = 0; j < clouds.size(); j++) {
            List<Color> students = Color.fromMapToList(clouds.get(j).getStudents());
            List<Pane> panes = cloudStudents.get(j);

            assert students.size() <= panes.size();
            for (int i = 0; i < panes.size(); i++) {
                panes.get(i).getChildren().clear(); // first remove previous items
                if (i < students.size()) {
                    Color studentC = students.get(i);
                    ImageView student = new ImageView(studentImgs.get(studentC));
                    student.fitWidthProperty().bind(panes.get(i).widthProperty());
                    student.fitHeightProperty().bind(panes.get(i).heightProperty());
                    student.setSmooth(true);
                    student.setCache(true);
                    Pane pane = panes.get(i);
                    pane.getChildren().add(student);
                }
            }
        }
    }

    /**
     * Method buildIsland creates the updated island to be set on the GUI.
     *
     * @param island    the Model's island reference.
     * @param index     the island's index.
     * @param hasMother the flag determines if motherNature is present on the island
     */
    private Node buildIsland(Island island, int index, boolean hasMother) {
        ImageView motherImgV = null;
        ImageView blockedImgV = null;
        StackPane pane = new StackPane();
        pane.setAlignment(Pos.CENTER);
        // island background
        ImageView background = new ImageView();
        background.setImage(islandImgs.get(index % islandImgs.size()));
        background.fitWidthProperty().bind(islandPanes.get(index).widthProperty());
        background.fitHeightProperty().bind(islandPanes.get(index).heightProperty());
        background.setSmooth(true);
        background.setCache(true);

        HBox hContainer = new HBox();
        hContainer.setAlignment(Pos.CENTER);
        // build students
        VBox students = new VBox();
        students.setAlignment(Pos.CENTER);

        for (Map.Entry<Color, Image> entry : studentImgs.entrySet()) {
            if (island.getStudents().getOrDefault(entry.getKey(), 0) != 0) {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                // student image
                ImageView studImgV = new ImageView(entry.getValue());
                studImgV.setFitWidth(15);
                studImgV.setPreserveRatio(true);
                hBox.getChildren().add(studImgV);
                // student label
                Label label = new Label();
                Font font = new Font("System Bold", 10);
                label.setFont(font);
                label.setText(" " + island.getStudents().getOrDefault(entry.getKey(), 0).toString());
                hBox.getChildren().add(label);
                students.getChildren().add(hBox);
            }
        }
        // Towers
        HBox tower = new HBox();
        tower.setAlignment(Pos.CENTER);
        int numTower = island.getNumTower();
        if (numTower > 0) {
            TowerColor color = gui.getModelView().getPlayerMapSchool().get(island.getOwner()).getTowerColor();
            ImageView towerImgV = new ImageView(towerImgs.get(color));
            towerImgV.setFitWidth(30);
            towerImgV.setPreserveRatio(true);
            Label label = new Label(String.valueOf(numTower));
            Font font = new Font("System Bold", 12);
            label.setFont(font);
            tower.getChildren().addAll(towerImgV, label);
        }
        if (island instanceof SuperIsland) {
            background.setScaleX(1.5);
            background.setScaleY(1.5);
        }
        // Mother nature handling
        if (hasMother) {
            motherImgV = new ImageView(this.motherImg);
            motherImgV.setFitWidth(45);
            motherImgV.setPreserveRatio(true);
        }
        VBox secondColumn = new VBox();
        secondColumn.setAlignment(Pos.CENTER);
        secondColumn.getChildren().add(tower);
        if (motherImgV != null) {
            secondColumn.getChildren().add(motherImgV);
        }
        hContainer.getChildren().add(students);
        hContainer.getChildren().add(secondColumn);

        // blocked island
        if(island.isBlocked()){
            blockedImgV = new ImageView();
            blockedImgV.setImage(blockImg);
            blockedImgV.fitWidthProperty().bind(islandPanes.get(index).widthProperty());
            blockedImgV.fitHeightProperty().bind(islandPanes.get(index).heightProperty());
            blockedImgV.setSmooth(true);
            blockedImgV.setCache(true);

        }
        // Add elements to the pane
        pane.getChildren().add(background);
        if(blockedImgV != null){
            pane.getChildren().add(blockedImgV);
        }
        pane.getChildren().add(hContainer);
        pane.setOnMouseReleased((e) -> changeSupport.firePropertyChange(SELECT_ISLAND_LISTENER, null, index));
        pane.setOnMouseEntered(this::onSelectScaleColor);
        pane.setOnMouseExited(this::onSelectScaleColor);
        return pane;
    }

    /**
     * Method updateSchool sets the updated school data on the GUI.
     */
    public void updateSchool() {
        School school = gui.getModelView().getPlayerMapSchool().get(playerSchool);
        if (school == null)
            return;
        // Entry students
        List<Color> entryStuds = school.getStudentsEntryList();
        assert entryStuds.size() <= studentEntryPanes.size();
        for (int i = 0; i < studentEntryPanes.size(); i++) {
            studentEntryPanes.get(i).getChildren().clear(); // first remove previous items
            if (i < entryStuds.size()) {
                Color studentC = entryStuds.get(i);
                ImageView student = new ImageView(studentImgs.get(studentC));
                student.fitWidthProperty().bind(studentEntryPanes.get(i).widthProperty());
                student.fitHeightProperty().bind(studentEntryPanes.get(i).heightProperty());
                student.setSmooth(true);
                student.setCache(true);
                Pane pane = studentEntryPanes.get(i);
                pane.setOnMouseEntered(this::onSelectScaleColor);
                pane.setOnMouseExited(this::onSelectScaleColor);
                pane.setOnMouseClicked((e) -> changeSupport.firePropertyChange(ENTRY_STUDENT_LISTENER, null, studentC));
                pane.getChildren().add(student);
            }
        }
        // Hall students
        // First remove all students in the lane
        colorToHallStudents.forEach((key, value) -> value.forEach(
                pane -> pane.getChildren().clear()
        ));

        Map<Color, Integer> hallStuds = school.getStudentsHall();
        for (Map.Entry<Color, Integer> entry : hallStuds.entrySet()) {
            assert entry.getValue() <= colorToHallStudents.get(entry.getKey()).size();
            if (entry.getValue() > 0) {
                // Add students to the first entry.getValue positions
                colorToHallStudents.get(entry.getKey()).subList(0, entry.getValue()).forEach(pane -> {
                    ImageView student = new ImageView(studentImgs.get(entry.getKey()));
                    student.fitWidthProperty().bind(pane.widthProperty());
                    student.fitHeightProperty().bind(pane.heightProperty());
                    student.setSmooth(true);
                    student.setCache(true);

                    pane.getChildren().add(student);
                });
            }
        }
        towers.getChildren().clear();
        Label numTowers = new Label();
        numTowers.setFont(font);
        numTowers.setText(": " + school.getNumTowers());
        numTowers.setStyle("-fx-font-size: 30");
        ImageView tower = new ImageView(towerImgs.get(school.getTowerColor()));
        tower.setCache(true);
        tower.setFitHeight(50);
        tower.setFitWidth(50);
        towers.getChildren().add(0, tower);
        towers.getChildren().add(1, numTowers);
        towers.setAlignment(Pos.CENTER);
    }

    /**
     * Method updateProfessors sets the updated professor data on the GUI.
     */
    private void updateProfessors() {
        // PROFESSORS
        List<Color> profs = getProfsFromNickname(gui.getModelView().getProfessors(), playerSchool);
        colorToPlayerProfs.forEach((key, value) -> value.getChildren().clear());
        for (Color p : profs) {
            ImageView prof = new ImageView(profsImgs.get(p));
            prof.fitWidthProperty().bind(colorToPlayerProfs.get(p).widthProperty());
            prof.fitHeightProperty().bind(colorToPlayerProfs.get(p).heightProperty());
            prof.setSmooth(true);
            prof.setCache(true);
            colorToPlayerProfs.get(p).getChildren().add(prof);
        }
    }

    /**
     * Method showBalance creates the GUI element for showing the player's balance.
     */
    private void showBalance() {
        ImageView coin = new ImageView(coinImg);
        coin.setCache(true);
        coin.setFitHeight(50);
        coin.setFitWidth(50);
        balanceContainer.getChildren().add(0, coin);
        updateBalance();
    }

    /**
     * Method updateBalance sets the updated balance data on the GUI.
     */
    private void updateBalance() {
        if (gui.getModelView().getExpert()) {
            int coins = gui.getModelView().getBalance();
            balance.setFont(balanceFont);
            balance.setText(": " + coins);
        }
    }

    /**
     * Method buildCard is used for creating the AssitantCard element on the GUI.
     *
     * @param width  the Node's width.
     * @param height the Node's height.
     * @param img    the AssistantCard's asset.
     */
    private ImageView buildCard(double width, double height, Image img) {
        //Background character
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setArcWidth(20.0);   // Corner radius
        rectangle.setArcHeight(20.0);
        rectangle.setEffect(new DropShadow(20, javafx.scene.paint.Color.rgb(0, 0, 0, 0.8)));  // Shadow

        ImageView character = new ImageView();
        character.setImage(img);
        character.setFitWidth(width);
        character.setFitHeight(height);
        character.setClip(rectangle);
        character.setCache(true);
        return character;
    }

    /**
     * Method updateCharacters sets the updated characters' data on the GUI.
     */
    private void updateCharacters() {
        characterContainer.getChildren().clear();
        List<ReducedCharacterCard> reducedCards = gui.getModelView().getCharacterCards();
        for (int i = 0; i < reducedCards.size(); i++) {
            ReducedCharacterCard c = reducedCards.get(i);
            StackPane s = new StackPane();
            s.setAlignment(Pos.CENTER);

            // Background character
            ImageView character = buildCard(80, 120, charactersImages.get(c.type));
            character.setImage(charactersImages.get(c.type));

            VBox content = new VBox();
            content.setAlignment(Pos.CENTER);
            // Students
            FlowPane students = new FlowPane();
            students.setVgap(4);
            students.setHgap(4);
            students.setAlignment(Pos.CENTER);
            c.students.forEach((color) -> {
                ImageView student = new ImageView();
                student.setImage(studentImgs.get(color));
                student.setFitWidth(22);
                student.setFitHeight(22);
                student.setOnMouseReleased((e) -> changeSupport.firePropertyChange(CHARACTER_STUDENT_LISTENER, null, color));
                students.getChildren().add(student);
            });
            content.getChildren().add(students);

            // Activated
            if (c.activatedOnce) {
                ImageView coin = new ImageView(coinImg);
                coin.setFitWidth(22);
                coin.setFitHeight(22);
                content.getChildren().add(coin);
            }

            // Blocking cards
            s.getChildren().add(character);
            s.getChildren().add(content);

            int finalI = i;
            s.setOnMouseReleased((e) -> changeSupport.firePropertyChange(CHARACTER_LISTENER, null, finalI));

            characterContainer.getChildren().add(s);

        }
    }

    private void updateProgressBar(){
        if((gui.getModelView().getConnectedPlayers().size() == 1) ||
            (!gui.getModelView().getConnectedPlayers().contains(gui.getModelView().getPlayerName())))
        {
            progressBar.setVisible(true);
            islandGridPane.setOpacity(0.5);
            schoolPane.setOpacity(0.5);
        }
        else{
            progressBar.setVisible(false);
            islandGridPane.setOpacity(1);
            schoolPane.setOpacity(1);
        }
    }

    public void updateStatus() {
        GameState currentState = gui.getModelView().getGameState();
        String s = "";
        String nickname = gui.getModelView().getPlayerName();
        if (gui.getModelView().amIRoundOwner()) {
            switch (currentState) {
                case PLANNING_CHOOSE_CARD -> s = "Please select a card.";
                case ACTION_MOVE_STUDENTS -> {
                    int currentEntry = gui.getModelView().getPlayerMapSchool().get(nickname).getStudentsEntryList().size();
                    int maxStudents = Rules.getStudentsPerTurn(gui.getModelView().getPlayers().size());
                    int diff = Rules.getEntrySize(gui.getModelView().getPlayers().size()) - currentEntry;
                    s = "Please move your students. You have moved "
                            + diff + " out of " + maxStudents + " students this turn.";
                }
                case ACTION_MOVE_MOTHER ->
                        s = "Please select and island to move mother nature to. Available movement is from 1 to " +
                                gui.getModelView().getPlayedCards().get(nickname).getMaxMoves();
                case ACTION_CHOOSE_CLOUD -> s = "Please select a cloud.";
                case GRANDMA_BLOCK_ISLAND -> s = "Please select an island to block.";
                case HERALD_ACTIVE -> s = "Please select an island to calculate the influence on.";
                case JOKER_SWAP_STUDENTS ->
                        s = "Please swap a maximum of 3 students between the card and your school's entry.";
                case MINSTREL_SWAP_STUDENTS ->
                        s = "Please swap a maximum of 2 students between your school's entry and hall.";
                case MONK_MOVE_STUDENT -> s = "Please move a student from the card to an island.";
                case MUSHROOM_CHOOSE_COLOR -> s = "Please select a color to disable the influence calculation on.";
                case PRINCESS_MOVE_STUDENT ->
                        s = "Please select a student from the card to be moved to your school's hall.";
                case THIEF_CHOOSE_COLOR -> s = "Please select a color to steal a maximum of 3 from each player's hall.";
            }
        } else {
            nickname = gui.getModelView().getRoundOwner();
            s = "Player " + nickname;
            switch (currentState) {
                case PLANNING_CHOOSE_CARD -> s = s + " is selecting his card.";
                case ACTION_MOVE_STUDENTS -> s = s + " is moving his students.";
                case ACTION_MOVE_MOTHER -> s = s + " is moving mother nature.";
                case ACTION_CHOOSE_CLOUD -> s = s + " is selecting a cloud.";
                default -> s = s + " is playing.";
            }
        }
        status.setFont(font);
        status.setText(s);
    }

    /**
     * Method getProfsFromNickname returns the professor of a given player.
     *
     * @param profs  the game professor ownership map.
     * @param player the nickname of the player.
     * @return the professor owner by the player.
     */
    private List<Color> getProfsFromNickname(Map<Color, String> profs, String player) {
        return profs.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), player)).map(Map.Entry::getKey).toList();
    }

    // CONTROLLER METHODS (methods called directly from the fxml)

    /**
     * Method changeSchool changes the current school to another player's.
     *
     * @param event the MouseEvent instance.
     */
    public void changeSchool(MouseEvent event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            // Restore player school
            btn.setStyle(" -fx-background-color: transparent; -fx-background-image: url('/graphics/perg2.png'); -fx-background-size: stretch; -fx-font-size: 24px;");
            playerSchool = gui.getModelView().getPlayerName();
        } else {
            btn.setStyle(" -fx-background-color: transparent; -fx-background-image: url('/graphics/perg_clicked.png'); -fx-background-size: stretch; -fx-font-size: 24px;");
            playerSchool = id;
        }
        updateSchool();
        updateProfessors();
    }

    /**
     * Method onSelectScaleColor changes an element's color when it is selected.
     *
     * @param evt the MouseEvent instance.
     */
    public void onSelectScaleColor(MouseEvent evt) {
        if (evt.getSource() instanceof Pane) {
            Pane pane = (Pane) evt.getSource();
            if (evt.getEventType() == MouseEvent.MOUSE_ENTERED) {
                pane.setEffect(new DropShadow(20, javafx.scene.paint.Color.rgb(59, 52, 218, 0.8)));  // Shadow
                pane.setScaleX(1.25);
                pane.setScaleY(1.25);
            } else {
                pane.setEffect(null);
                pane.setScaleX(1);
                pane.setScaleY(1);
            }

        } else if (evt.getSource() instanceof ImageView) {
            ImageView view = (ImageView) evt.getSource();
            if (evt.getEventType() == MouseEvent.MOUSE_ENTERED) {

                view.setEffect(new DropShadow(20, javafx.scene.paint.Color.rgb(59, 52, 218, 0.8)));  // Shadow
                view.setScaleX(1.25);
                view.setScaleY(1.25);
            } else {
                view.setEffect(null);
                view.setScaleX(1);
                view.setScaleY(1);
            }
        }
    }

    /**
     * Method propertyChange triggers GUI updates when model updates are received from the server.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GameState state = gui.getModelView().getGameState();
        if (state != GameState.GAME_ROOM && state != GameState.SETUP_CHOOSE_MAGICIAN && initCompleted) {
            switch (evt.getPropertyName()) {
                case ServerMessageHandler.GAME_STATE_LISTENER -> updateStatus();
                case ServerMessageHandler.BALANCE_LISTENER -> updateBalance();
                case ServerMessageHandler.CLOUDS_LISTENER -> updateClouds();
                case ServerMessageHandler.HAND_LISTENER -> updateHand();
                case ServerMessageHandler.ISLAND_LISTENER, ServerMessageHandler.MOTHER_LISTENER -> updateIslands();
                case ServerMessageHandler.SCHOOL_LISTENER -> updateSchool();
                case ServerMessageHandler.PROFS_LISTENER -> updateProfessors();
                case ServerMessageHandler.PLAYED_CARD_LISTENER -> updatePlayedCards();
                case ServerMessageHandler.CHARACTERS_LISTENER -> updateCharacters();
                case ServerMessageHandler.NEXT_ROUNDOWNER_LISTENER, ServerMessageHandler.PLAYERS_STATUS_LISTENER -> {
                    updatePlayers();
                }
            }
            updateProgressBar();
        }
    }
}