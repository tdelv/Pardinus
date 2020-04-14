package kodkod.engine.fol2sat.bdd;

import kodkod.engine.bool.BooleanFormula;
import kodkod.engine.bool.BooleanVariable;

import java.util.HashMap;

public class BooleanTranslation {
    private int numVars;
    private HashMap<BooleanVariable, Integer> variableMapping;
    private BooleanFormula formula;

    /**
     * Gets the number of variables in this translation.
     * @return the number of variables in the translated formula.
     */
    public int getNumVars() {
        return numVars;
    }

    /**
     * Gets the boolean formula in this translation.
     * @return the translated boolean formula.
     */
    public BooleanFormula getFormula() {
        return formula;
    }
}
