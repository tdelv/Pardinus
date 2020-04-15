package kodkod.engine.bddlab;

import java.util.Iterator;

/**
 * Interface that all bdd-based solvers should implement. Provides
 * methods for constructing a bdd for the given formula, getting solutions,
 * and checking satisfiability.
 * @param <B> The bdd class the solver uses.
 * @author Mark Lavrentyev.
 */
public interface BDDSolver<B> extends Iterator<BDDSolution> {

    /**
     * Constructs the bdd from the solver's formula, which is part of the
     * boolean translation.
     * @return true if the resulting bdd is satisfiable, and false when unsat.
     */
    boolean construct();

    /**
     * Closes the solver and frees all memory. Any other method calls
     * after this may have unspecified behavior.
     */
    void done();

    /**
     * Checks whether the given bdd is satisfiable.
     * @return true if there is some satisfying assignment for this bdd.
     */
    boolean isSat(B bdd);
}
