package kodkod.engine.fol2sat;

import kodkod.engine.bddlab.BDDSolver;
import kodkod.engine.bool.BooleanFormula;
import kodkod.engine.bool.BooleanVariable;
import kodkod.engine.config.Options;
import kodkod.instance.Instance;

import java.util.HashMap;

public class BooleanTranslation {
    private int numVars;
    private HashMap<BooleanVariable, Integer> variableMapping;
    final private BooleanFormula formula;
    final private BDDSolver solver;

    /**
     * Create the boolean translation object from a formula and options.
     * @requires {@code options.solverType() == Options.SolverType.BDD}
     * @param formula The boolean formula that's the translation
     * @param options
     */
    BooleanTranslation(BooleanFormula formula, Options options) {
        assert options.solverType() == Options.SolverType.BDD;

        this.formula = formula;
        this.solver = options.bddSolver().instance();
    }

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

    /**
     * Interprets the
     * @return
     */
    public final Instance interpret() {
        return null;
    }
}
