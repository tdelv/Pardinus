package kodkod.engine.fol2sat.bdd;

import kodkod.engine.bddlab.BDDSolver;
import kodkod.engine.bool.BooleanFormula;
import kodkod.engine.bool.BooleanVariable;

import java.util.HashMap;

public class BooleanTranslation {
    private int numVars;
    private HashMap<BooleanVariable, Integer> variableMapping;
    private BooleanFormula formula;
    private BDDSolver solver;

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

    /**
     * Gets the bdd variable id this boolean variable maps to.
     * @param var The variable to get the mapping for.
     * @return The id of the variable in the solver.
     */
    public int getVarMap(BooleanVariable var) {
        return variableMapping.get(var);
    }

    /**
     * Gets the current bdd solver in use for this problem.
     * @return The bdd solver in use for this problem.
     */
    public BDDSolver solver() {
        return solver;
    }
}
