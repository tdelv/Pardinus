package kodkod.engine.bddlab;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Container for holding a variable assignment from a BDD path.
 *
 * @author Mark Lavrentyev
 */
public class BDDSolution {
    private Set<Integer> trueVars;
    private Set<Integer> falseVars;
    private Set<Integer> dontCareVars;
    private boolean usingDontCares;

    /**
     * Constructor for creating BDD solution with dont care variables.
     * @param ts The collection of variables that are true in this solution.
     * @param fs The collection of variables that are false in this solution.
     * @param dcs The collection of variables where the assignment doesn't matter.
     */
    public BDDSolution(Collection<Integer> ts, Collection<Integer> fs, Collection<Integer> dcs) {
        this.trueVars = new HashSet<>(ts);
        this.falseVars = new HashSet<>(fs);
        this.dontCareVars = new HashSet<>(dcs);
        this.usingDontCares = true;
    }

    /**
     * Constructor for creating BDD solution without accounting for dont care vars
     * (i.e. these are already assigned to either true or false). Note that all variables must
     * still be assigned.
     * @param ts The collection of true vars in the solution, potentially including don't cares.
     * @param fs The collection of false vars in the solution, potentially including don't cares.
     */
    public BDDSolution(Collection<Integer> ts, Collection<Integer> fs) {
        this.trueVars = new HashSet<>(ts);
        this.falseVars = new HashSet<>(fs);
        this.usingDontCares = false;
    }

    /**
     * Getter for true variables in this solution.
     * @return the set of true variables in this solution.
     */
    public Set<Integer> getTrueVars() {
        return trueVars;
    }

    /**
     * Getter for the false variables in this solution.
     * @return The set of false variables in this solution.
     */
    public Set<Integer> getFalseVars() {
        return falseVars;
    }

    /**
     * Getter fot dont-care variables in the solution.
     * @return The set of variables whose assignment doesn't change the truthiness of the formula
     * in this solution.
     */
    public Set<Integer> getDontCareVars() {
        return dontCareVars;
    }

    /**
     * Tells whether this solution is equal to another solution.
     * @param o The other solution.
     * @return true if the solutions are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BDDSolution)) return false;
        BDDSolution that = (BDDSolution) o;
        return usingDontCares == that.usingDontCares &&
                       trueVars.equals(that.trueVars) &&
                       falseVars.equals(that.falseVars) &&
                       Objects.equals(dontCareVars, that.dontCareVars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trueVars, falseVars, dontCareVars, usingDontCares);
    }
}
