package kodkod.engine.bddlab;


import java.util.*;

/**
 * Container for holding a variable assignment from a BDD path. Really
 * represents a class of solutions, each differing only by assignments
 * to don't-care variables.
 * @author Mark Lavrentyev
 */
public class BDDSolution implements Iterator<BDDSolution> {
    private Set<Integer> trueVars;
    private Set<Integer> falseVars;
    private List<Integer> dontCareVars;
    private boolean usingDontCares;
    private long iteratorIdx;

    /**
     * Constructor for creating BDD solution with dont care variables.
     * @param ts The collection of variables that are true in this solution.
     * @param fs The collection of variables that are false in this solution.
     * @param dcs The collection of variables where the assignment doesn't matter.
     */
    public BDDSolution(Collection<Integer> ts, Collection<Integer> fs, Collection<Integer> dcs) {
        this.trueVars = new HashSet<>(ts);
        this.falseVars = new HashSet<>(fs);
        this.dontCareVars = new ArrayList<>(dcs);
        this.usingDontCares = true;
        this.iteratorIdx = 0;

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
        return new HashSet<>(dontCareVars);
    }

    /**
     * Tells whether there is another assignment of don't-care variables
     * that would yield a new solution.
     * @return true if there is another distinct assignment of don't-care vars.
     */
    @Override
    public boolean hasNext() {
        return (Math.log(iteratorIdx) / Math.log(2)) < dontCareVars.size();
    }

    /**
     * Gets the next BDDSolution that doesn't have any don't cares and has
     * an assignment to every don't care variable. The new solution is guaranteed
     * to be distinct from all previous ones.
     * @return A new bdd solution distinct from all previous ones, with all
     * don't-care variables assigned to either true or false.
     */
    @Override
    public BDDSolution next() {
        if (this.hasNext()) {
            Set<Integer> newTrueVars = new HashSet<>(this.trueVars);
            Set<Integer> newFalseVars = new HashSet<>(this.falseVars);

            for (int i = 0; i < dontCareVars.size(); i++) {
                boolean dontCareAssn = ((iteratorIdx >> i) & 1) != 0;
                if (dontCareAssn) {
                    newTrueVars.add(dontCareVars.get(i));
                } else {
                    newFalseVars.add(dontCareVars.get(i));
                }
            }

            this.iteratorIdx++;
            return new BDDSolution(newTrueVars, newFalseVars);
        } else {
            throw new NoSuchElementException("No more distinct don't-care assignments for this path");
        }
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

    /**
     * Hash code implementation for a bdd solution that uses true vars, false vars,
     * don't-care vars, and whether using don't care.
     * @return The hash code for this bdd solution.
     */
    @Override
    public int hashCode() {
        return Objects.hash(trueVars, falseVars, dontCareVars, usingDontCares);
    }
}
