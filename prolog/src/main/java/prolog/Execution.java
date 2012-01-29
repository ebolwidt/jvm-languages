package prolog;

import java.util.LinkedList;
import java.util.ListIterator;

import prolog.executor.ConjunctionExecutor;
import prolog.executor.Executor;

public class Execution {
    private LinkedList<Executor> stack = new LinkedList<Executor>();
    private PrologEngine engine;

    public Execution(PrologEngine engine) {
        this.engine = engine;
    }

    public void push(Executor executor) {
        stack.addLast(executor);
    }

    public PrologEngine getEngine() {
        return engine;
    }

    public boolean execute() {
        while (!stack.isEmpty()) {
            Executor executor = stack.getLast();
            Boolean result = executor.execute(this);
            if (result == null) {
                continue;
            }
            if (result.equals(Boolean.TRUE)) {
                if (handleConjunctions())
                    continue;
                else
                    return true;
            } else {
                Executor last = stack.removeLast();
                last.resetBindings();
            }
        }
        return false;
    }

    /**
     * Returns true if one of the conjunctions pushed more on the stack.
     */
    protected boolean handleConjunctions() {
        ListIterator<Executor> i = stack.listIterator(stack.size());
        while (i.hasPrevious()) {
            Executor e = i.previous();
            if (e instanceof ConjunctionExecutor) {
                ConjunctionExecutor ce = (ConjunctionExecutor) e;
                if (ce.pushMore(this))
                    return true;
            }
        }
        return false;
    }
}
