package it.polimi.ingsw.Controller.Rules.DynamicRules;

public class KnightRules extends BaseRules{
    @Override
    public boolean influenceComparator(int influence1, int influence2) {
        return Integer.compare(influence1, influence2) >= 0;
    }
}
