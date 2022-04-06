package it.polimi.ingsw.Model.Cards;

public abstract class Card {

     private final String imagePath;

    public Card(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

}
