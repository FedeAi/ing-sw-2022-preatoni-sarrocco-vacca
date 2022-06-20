package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.ServerMessageHandler;
import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Constants.TowerColor;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.ReducedCharacterCard;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Islands.SuperIsland;
import it.polimi.ingsw.Model.School;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.*;
import java.util.List;

public class BoardController extends GUIController implements PropertyChangeListener {

    GUI gui;

    public static final String ENTRY_STUDENT_LISTENER = "selectSchoolStudent";
    public static final String SCHOOL_HALL_LISTENER = "selectSchoolHall"; //ciao, Bella, mi senti?
    public static final String SELECT_ISLAND_LISTENER = "selectIsland"; //ciao, Bella, mi senti?
    public static final String SELECT_ASSISTANT_CARD_LISTENER = "selectAssistantCard"; //ciao, Bella, mi senti?
    public static final String CLOUD_LISTENER = "selectCloud";
    public static final String CHARACTER_LISTENER = "character";
    public static final String CHARACTER_STUDENT_LISTENER = "characterStudent";
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);    // support

    @FXML
    GridPane grid;

    @FXML
    Pane island0, island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11;

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
    @FXML
    Pane avatarPain;
    // card container
    @FXML
    HBox cardContainer;
    @FXML
    HBox playedCardContainer;
    @FXML
    HBox characterContainer;

    private final ArrayList<Image> cloudImgs = new ArrayList<>();
    private final ArrayList<Pane> cloudsPane = new ArrayList<>();
    private final List<List<Pane>> cloudStudents = new ArrayList<>();
    private final Map<Color, Pane> hallLanes = new EnumMap<>(Color.class);

    private final Map<Magician, Image> avatarImgs = new EnumMap<>(Magician.class);
    private final List<Image> avatarImgsNRO = new ArrayList<>();

    private Image motherImg;
    private final ArrayList<Image> islandImgs = new ArrayList<>();

    private final ArrayList<Image> cardImgs = new ArrayList<>();

    private final HashMap<Color, Image> studentImgs = new HashMap<>();
    private final HashMap<Color, Image> profsImgs = new HashMap<>();
    private Image coinImg;
    private final HashMap<TowerColor, Image> towerImgs = new HashMap<>();

    private final ArrayList<Pane> islandPanes = new ArrayList<>();
    private final ArrayList<Pane> studentEntryPanes = new ArrayList<>();
    private final EnumMap<Color, List<Pane>> colorToHallStudents = new EnumMap<Color, List<Pane>>(Color.class);
    private final EnumMap<Color, Pane> colorToPlayerProfs = new EnumMap<Color, Pane>(Color.class);

    private final List<Pane> switchPlayerPanes = new ArrayList<>(); // list of buttons to change school
    private final EnumMap<Character, Image> charactersImages = new EnumMap<>(Character.class);
    String playerSchool = ""; // player's school name

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        islandPanes.addAll(List.of(island0, island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11));
        //cloudsPane.addAll(List.of(cloud0, cloud2, cloud1));
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
        //cloudStudents.add(List.of(studentCloud9, studentCloud10, studentCloud11, studentCloud12));
        cloudStudents.add(List.of(studentCloud5, studentCloud6, studentCloud7, studentCloud8));
        cloudStudents.add(List.of(studentCloud9, studentCloud10, studentCloud11, studentCloud12));

        colorToPlayerProfs.put(Color.GREEN, playerProf1);
        colorToPlayerProfs.put(Color.RED, playerProf2);
        colorToPlayerProfs.put(Color.YELLOW, playerProf3);
        colorToPlayerProfs.put(Color.PINK, playerProf4);
        colorToPlayerProfs.put(Color.BLUE, playerProf5);

        switchPlayerPanes.addAll(List.of(otherPlayer1Pane, otherPlayer2Pane));
        loadAssets();
    }

    @Override
    public void init() {
        showChangeSchoolButtons();

        //need the ModelView to upload the right kind of cloud ( in initialize I can't check)
        if (gui.getModelView().getClouds().size() == 2) {

            cloudImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/cloud_2p.png")));
            cloudImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/cloud_2p.png")));
        } else {
            cloudImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/cloud_3p.png")));
            cloudImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/cloud_3p.png")));
            cloudImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/cloud_3p.png")));
        }
        if (gui.getModelView().getExpert()) {
            coinImg = new Image(getClass().getResourceAsStream("/graphics/board/coin.png"));
            updateCharacters();
        }

        showClouds(); // have to be later of the upload image cloud
        updateIslands();
        playerSchool = gui.getModelView().getPlayerName();
        updateSchool();
        updateProfessors();
        updateHand();
        //uploadAvatar(); //todo adjustment listener
        changeSupport.addPropertyChangeListener(new EventsToActions(gui));
    }

    private void loadAssets() {
        islandImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/island1.png")));
        islandImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/island2.png")));
        islandImgs.add(new Image(getClass().getResourceAsStream("/graphics/board/island3.png")));

        avatarImgs.put(Magician.KING, new Image(getClass().getResourceAsStream("/graphics/avatar/Kingavatar.png")));
        avatarImgs.put(Magician.WIZARD, new Image(getClass().getResourceAsStream("/graphics/avatar/Wizardavatar.png")));
        avatarImgs.put(Magician.WITCH, new Image(getClass().getResourceAsStream("/graphics/avatar/Witchavatar.png")));
        avatarImgs.put(Magician.SAGE, new Image(getClass().getResourceAsStream("/graphics/avatar/Sageavatar.png")));

        //if im not a round owner i have the grey version of my avatar
        /*
        FIXME the elements were added to the same list as the color images ?
        avatarImgs.add(new Image(getClass().getResourceAsStream("/graphics/avatar/KingavatarNRO.png")));
        avatarImgs.add(new Image(getClass().getResourceAsStream("/graphics/avatar/SageavatarNRO.png")));
        avatarImgs.add(new Image(getClass().getResourceAsStream("/graphics/avatar/WitchavatarNRO.png")));
        avatarImgs.add(new Image(getClass().getResourceAsStream("/graphics/avatar/WizardavatarNRO.png")));
        */

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
            charactersImages.put(c,  new Image(getClass().getResourceAsStream("/graphics/characters/" +  c.toString().toLowerCase() + ".jpg")));
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

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
            Boolean hasMother = false;
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

    public void showClouds() {

        int nCloud = gui.getModelView().getClouds().size();

        // empty all clouds panes
        assert nCloud <= cloudsPane.size();
        for (int i = 0; i < nCloud; i++) {
            cloudsPane.get(i).setPrefHeight(Control.USE_COMPUTED_SIZE);
            cloudsPane.get(i).setPrefWidth(Control.USE_COMPUTED_SIZE);
            ImageView backCloud = new ImageView();
            backCloud.setImage(cloudImgs.get(i));
            backCloud.fitWidthProperty().bind(cloudsPane.get(i).widthProperty());
            backCloud.fitHeightProperty().bind(cloudsPane.get(i).heightProperty());
            cloudsPane.get(i).setOnMouseEntered(this::OnSelectScaleColor);
            cloudsPane.get(i).setOnMouseExited(this::OnSelectScaleColor);
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

    private void updateHand() {
        List<AssistantCard> cards = gui.getModelView().getHand();
        cardContainer.getChildren().clear();


        for (AssistantCard assistantCard : cards) {
            // build imageview of the card
            ImageView cardImg = new ImageView();
            cardImg.setImage(cardImgs.get(assistantCard.getValue() - 1));
            cardImg.setFitWidth(63);
            cardImg.setFitHeight(92.25);
            cardImg.setOnMouseEntered((evt) -> {
                cardImg.setTranslateY(-50);
                OnSelectScaleColor(evt);
            });

            cardImg.setOnMouseExited((evt) -> {
                cardImg.setTranslateY(0);
                OnSelectScaleColor(evt);

            });
            cardImg.setSmooth(true);
            cardImg.setCache(true);
            cardImg.setOnMouseReleased((e) -> {
                changeSupport.firePropertyChange(SELECT_ASSISTANT_CARD_LISTENER, null, assistantCard.getValue());
                e.consume();
            });
            cardContainer.getChildren().add(cardImg);
        }
    }

    private void updatePlayedCards() {
        Map<String, AssistantCard> map = gui.getModelView().getPlayedCards();
        playedCardContainer.getChildren().clear();
        for (Map.Entry<String, AssistantCard> entry : map.entrySet()) {
            StackPane s = new StackPane();
            ImageView playedCard = new ImageView();
            playedCard.setImage(cardImgs.get(entry.getValue().getValue() - 1));
            playedCard.setFitWidth(63);
            playedCard.setFitHeight(92.25);
            s.getChildren().add(playedCard);
            ImageView player = new ImageView();
            player.setImage(avatarImgs.get(gui.getModelView().getPlayerMapMagician().get(entry.getKey())));
            player.setFitWidth(20);
            player.setFitHeight(20);
            s.getChildren().add(player);
            playedCardContainer.getChildren().add(s);
        }
    }

    private void uploadAvatar() {
        ImageView avatar = new ImageView();
        //magician selected by the player
        String MoP = gui.getModelView().getPlayerMapMagician().getOrDefault(gui.getModelView().getPlayerName(), Magician.KING).toString();

        if (gui.getModelView().amIRoundOwner()) {
            switch (MoP.toLowerCase()) {
                case "king" -> avatar.setImage(avatarImgs.get(0));
                case "wizard" -> avatar.setImage(avatarImgs.get(1));
                case "witch" -> avatar.setImage(avatarImgs.get(2));
                case "sage" -> avatar.setImage(avatarImgs.get(3));
            }
        } else {
            switch (MoP.toLowerCase()) {
                case "king" -> avatar.setImage(avatarImgsNRO.get(0));
                case "wizard" -> avatar.setImage(avatarImgsNRO.get(1));
                case "witch" -> avatar.setImage(avatarImgsNRO.get(2));
                case "sage" -> avatar.setImage(avatarImgsNRO.get(3));
            }
        }

        avatar.setFitWidth(100);
        avatar.setFitHeight(100);
        avatar.setStyle("-fx-border-radius: 20px; -fx-border-style: ridge; -fx-border-width: 2px; -fx-border-color: black;");
        avatar.setSmooth(true);
        avatar.setCache(true);

        //avatarPain.getChildren().clear();
        avatarPain.getChildren().add(avatar);
    }

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

    private Node buildIsland(Island island, int index, boolean has_mother) {
        ImageView mother = null;
        StackPane pane = new StackPane();
//        pane.setPrefWidth(200);
//        pane.setPrefHeight(200);
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
                ImageView studImg = new ImageView(entry.getValue());
                studImg.setFitWidth(15);
                studImg.setPreserveRatio(true);
                hBox.getChildren().add(studImg);
                // student label
                Label label = new Label();
                Font font = new Font("System Bold", 10);
                label.setFont(font);
                label.setText(" " + island.getStudents().getOrDefault(entry.getKey(), 0).toString());
                hBox.getChildren().add(label);

                students.getChildren().add(hBox);
            }
        }

        // tower
        HBox tower = new HBox();
        tower.setAlignment(Pos.CENTER);

        int numTower = island.getNumTower();

        if (numTower > 0) {

            TowerColor color = gui.getModelView().getPlayerMapSchool().get(island.getOwner()).getTowerColor();
            ImageView towerImg = new ImageView(towerImgs.get(color));
            towerImg.setFitWidth(30);
            towerImg.setPreserveRatio(true);

            Label label = new Label(String.valueOf(numTower));
            Font font = new Font("System Bold", 12);

            label.setFont(font);

            tower.getChildren().addAll(towerImg, label);

        }
        if (island instanceof SuperIsland) {
            background.setScaleX(1.5);
            background.setScaleY(1.5);
        }

        // mother nature
        if (has_mother) {
            mother = new ImageView(motherImg);
            mother.setFitWidth(45);
            mother.setPreserveRatio(true);
        }

        VBox secondColumn = new VBox();
        secondColumn.setAlignment(Pos.CENTER);
        secondColumn.getChildren().add(tower);
        if (mother != null)
            secondColumn.getChildren().add(mother);

        hContainer.getChildren().add(students);
        hContainer.getChildren().add(secondColumn);

        // add elements to the pane
        pane.getChildren().add(background);
        pane.getChildren().add(hContainer);
//        pane.getChildren().add(tower);

        pane.setOnMouseEntered(this::OnSelectScaleColor);
        pane.setOnMouseExited(this::OnSelectScaleColor);
        return pane;
    }

    public void updateSchool() {
        School school = gui.getModelView().getPlayerMapSchool().get(playerSchool);
        // entry students
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
                pane.setOnMouseEntered(this::OnSelectScaleColor);
                pane.setOnMouseExited(this::OnSelectScaleColor);
                pane.setOnMouseClicked((e) -> changeSupport.firePropertyChange(ENTRY_STUDENT_LISTENER, null, studentC));
                pane.getChildren().add(student);
            }
        }
        // hall students
        // first remove all students in the lane
        colorToHallStudents.forEach((key, value) -> value.forEach(
                pane -> pane.getChildren().clear()
        ));

        Map<Color, Integer> hallStuds = school.getStudentsHall();
        for (Map.Entry<Color, Integer> entry : hallStuds.entrySet()) {
            assert entry.getValue() <= colorToHallStudents.get(entry.getKey()).size();
            if (entry.getValue() > 0) {
                // add students to the first entry.getValue positions
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
    }

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

    private void updateBalance() {
//        int balance = gui.getModelView().getBalance();
//        for (int i = 0; i < balance; i++) {
//
//        }
    }
    
    private void updateCharacters() {
        characterContainer.getChildren().clear();
        List<ReducedCharacterCard> reducedCards = gui.getModelView().getCharacterCards();
        for (int i = 0; i < reducedCards.size(); i++ ) {
            ReducedCharacterCard c = reducedCards.get(i);
            StackPane s = new StackPane();
            s.setAlignment(Pos.CENTER);

            //Background character
            ImageView character = new ImageView();
            character.setImage(charactersImages.get(c.type));
            character.setFitWidth(80);
            character.setFitHeight(120);

            VBox content = new VBox();
            content.setAlignment(Pos.CENTER);
            //Students
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
            if (c.activatedOnce){
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

    private List<Color> getProfsFromNickname(Map<Color, String> profs, String player) {
        return profs.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), player)).map(Map.Entry::getKey).toList();
    }
    // CONTROLLER METHODS ( methods called directly from the fxml )

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

    public void OnSelectScaleColor(MouseEvent evt) {
        if (evt.getSource() instanceof Pane) {
            Pane pane = (Pane) evt.getSource();
            if (evt.getEventType() == MouseEvent.MOUSE_ENTERED) {
                pane.setStyle("-fx-opacity: 0.8; -fx-effect: dropshadow(three-pass-box, rgba(59,52,218,0.8), 20, 0, 0, 0);");
                pane.setScaleX(1.25);
                pane.setScaleY(1.25);
            } else {
                pane.setStyle("-fx-opacity: 1");
                pane.setScaleX(1);
                pane.setScaleY(1);
            }

        } else if (evt.getSource() instanceof ImageView) {
            ImageView view = (ImageView) evt.getSource();
            if (evt.getEventType() == MouseEvent.MOUSE_ENTERED) {
                view.setStyle("-fx-opacity: 0.8; -fx-border-radius:50%; dropshadow(three-pass-box, rgba(26,17,255,0.8), 10, 0, 0, 0);");
                view.setScaleX(1.25);
                view.setScaleY(1.25);
            } else {
                view.setStyle("-fx-opacity: 1");
                view.setScaleX(1);
                view.setScaleY(1);
            }
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GameState state = gui.getModelView().getGameState();
        if (state != GameState.GAME_ROOM && state != GameState.SETUP_CHOOSE_MAGICIAN) {
            switch (evt.getPropertyName()) {
                case ServerMessageHandler.BALANCE_LISTENER -> updateBalance();
                case ServerMessageHandler.CLOUDS_LISTENER -> updateClouds();
                case ServerMessageHandler.HAND_LISTENER -> updateHand();
                case ServerMessageHandler.ISLAND_LISTENER, ServerMessageHandler.MOTHER_LISTENER -> updateIslands();
                case ServerMessageHandler.SCHOOL_LISTENER -> updateSchool();
                case ServerMessageHandler.PROFS_LISTENER -> updateProfessors();
                case ServerMessageHandler.PLAYED_CARD_LISTENER -> updatePlayedCards();
                case ServerMessageHandler.MAGICIANS_LISTENER -> {}
                case ServerMessageHandler.CHARACTERS_LISTENER -> updateCharacters();
            }
        }
    }
}
