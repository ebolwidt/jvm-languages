package prolog.executor;

import java.util.ArrayList;
import java.util.List;

import prolog.Execution;
import prolog.expression.Variable;

public abstract class Executor {
    protected List<Variable> bindings = new ArrayList<Variable>();

    public void addBinding(Variable variable) {
        bindings.add(variable);
    }

    public void resetBindings() {
        for (Variable variable : bindings) {
            variable.unbind();
        }
        bindings.clear();
    }

    /**
     * 
     * @param execution
     * @return false: no more results, true: one result, null: look at the stack
     */
    public abstract Boolean execute(Execution execution);
}
