package io.game;

import java.util.*;

public class Process {
    public enum StepType { CPU, IO }
    public enum ProcessColor {SINGLE, DOUBLE}

    private String id;
    private Set<StepType> stepsDone;
    private List<StepType> requiredSteps;
    private ProcessColor color;

    public Process(String id, ProcessColor color) {
        this.id = id;
        this.color = color;
        this.stepsDone = new HashSet<>();
        this.requiredSteps = new ArrayList<>();

        switch (color) {
            case SINGLE:
                requiredSteps.add(StepType.CPU);
                break;
            case DOUBLE:
                requiredSteps.add(StepType.CPU);
                requiredSteps.add(StepType.IO);
                break;
        }
    }

    public String getId() {
        return id;
    }

    public ProcessColor getColor() {
        return color;
    }

    public boolean isStepDone(StepType step) {
        return Collections.frequency(new ArrayList<>(stepsDone), step) >=
            Collections.frequency(requiredSteps, step);
    }

    public boolean completeStep(StepType step) {
        int needed = Collections.frequency(requiredSteps, step);
        int done = Collections.frequency(new ArrayList<>(stepsDone), step);
        if (done < needed) {
            stepsDone.add(step);
            return true;
        }
        return false;
    }

    public boolean isCompleted() {
        for (StepType step : requiredSteps) {
            if (!isStepDone(step)) return false;
        }
        return true;
    }

    public int getStepsCompletedCount() {
        return stepsDone.size();
    }

    public int getTotalStepsCount() {
        return requiredSteps.size();
    }
}
