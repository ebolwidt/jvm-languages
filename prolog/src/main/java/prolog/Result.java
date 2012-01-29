package prolog;

public class Result {

    private Execution execution;

    private boolean hasAnswer;

    public Result(Execution execution) {
        this.execution = execution;
        next();
    }

    public boolean hasOneAnswer() {
        if (!hasAnswer())
            return false;
        next();
        if (hasAnswer())
            return false;
        return true;
    }

    public boolean hasAnswer() {
        return hasAnswer;
    }

    public void next() {
        hasAnswer = execution.execute();
    }
}
