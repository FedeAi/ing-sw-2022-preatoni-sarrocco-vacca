package it.polimi.ingsw.Exceptions;

/**
 * Class GameException is thrown when a wrong index is given to an action, and then it is executed.
 */
public class InvalidIndexException extends GameException {

    private int first;
    private int last;
    private int selected;

    /**
     * Constructor InvalidIndexException creates the InvalidIndexException, and sets its parameters.
     *
     * @param errorMessage the type of object of
     * @param first        the first valid index.
     * @param last         the last valid index.
     * @param selected     the selected index.
     */
    public InvalidIndexException(String errorMessage, int first, int last, int selected) {
        super(errorMessage);
        this.first = first;
        this.last = last;
        this.selected = selected;
    }

    /**
     * Method getMessage returns the properly formatted InvalidIndexException message.
     */
    @Override
    public String getMessage() {
        return "The selected " + errorMessage + " index (" + selected + ") is not valid. Please select an index between " + first + " and " + last + ".";
    }
}
