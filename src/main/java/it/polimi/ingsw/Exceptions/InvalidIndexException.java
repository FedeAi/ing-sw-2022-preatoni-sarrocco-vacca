package it.polimi.ingsw.Exceptions;

public class InvalidIndexException extends GameException {

    private int first;
    private int last;
    private int selected;

    public InvalidIndexException(String errorMessage, int first, int last, int selected) {
        super(errorMessage);
        this.first = first;
        this.last = last;
        this.selected = selected;
    }

    @Override
    public String getMessage() {
        return "The selected " + errorMessage + " index (" + selected + ") is not valid. Please select an index between " + first + " and " + last + ".";
    }
}
