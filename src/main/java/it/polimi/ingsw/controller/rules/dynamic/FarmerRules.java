package it.polimi.ingsw.controller.rules.dynamic;

/**
 * FarmerRules class extends the BaseRules class,
 * overriding the rules that have changed since the activation of the Farmer character card.
 */
public class FarmerRules extends BaseRules {

    /**
     * Method influence comparator checks if a player has more influence than another.
     * This is the implementation of the Farmer character card.
     *
     * @param influence1 the first player's influence.
     * @param influence2 the second player's influence.
     * @return true if the first player has more or equal influence, false otherwise.
     */
    @Override
    protected boolean influenceComparator(int influence1, int influence2) {
        return Integer.compare(influence1 , influence2) >= 0;
    }
}