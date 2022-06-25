package it.polimi.ingsw.Server.answers.model;

/**
 * MotherMessage class is type of ModelMessage used for sending updates of motherNature's current position.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see ModelMessage
 */
public class MotherMessage implements ModelMessage {

    private final int island;

    /**
     * Constructor MotherMessage creates a new MotherMessage instance.
     *
     * @param island the current island index of mother nature.
     */
    public MotherMessage(int island) {
        this.island = island;
    }

    /**
     * Method getMessage returns the message of this Answer object.
     *
     * @return the message (type Object) of this Answer object.
     * @see ModelMessage#getMessage()
     */
    @Override
    public Integer getMessage() {
        return island;
    }
}
