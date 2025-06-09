package com.nekiplay.hypixelcry.baritone;

import baritone.api.IBaritone;
import baritone.api.pathing.goals.Goal;
import baritone.api.process.ICustomGoalProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.api.utils.Helper;

public final class OnlyRenderProcess implements ICustomGoalProcess, Helper {

    private Goal goal;
    private Goal mostRecentGoal;
    private State state = State.NONE;

    public OnlyRenderProcess(IBaritone baritone) {
        super();
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void setGoal(Goal goal) {
        this.goal = goal;
        this.mostRecentGoal = goal;
        if (this.state == State.NONE) {
            this.state = State.GOAL_SET;
        }
        if (this.state == State.EXECUTING) {
            this.state = State.PATH_REQUESTED;
        }
    }

    @Override
    public void path() {
        this.state = State.PATH_REQUESTED;
    }

    @Override
    public Goal getGoal() {
        return state == State.EXECUTING ? goal : null;
    }

    @Override
    public Goal mostRecentGoal() {
        return state == State.EXECUTING ? mostRecentGoal : null;
    }

    @Override
    public boolean isActive() {
        return state != State.NONE;
    }

    @Override
    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        switch (this.state) {
            case GOAL_SET:
                return new PathingCommand(this.goal, PathingCommandType.CANCEL_AND_SET_GOAL);
            case PATH_REQUESTED:
                PathingCommand result = new PathingCommand(this.goal, PathingCommandType.REVALIDATE_GOAL_AND_PATH);
                state = State.EXECUTING;
                return result;
            case EXECUTING:
                return new PathingCommand(this.goal, PathingCommandType.SET_GOAL_AND_PAUSE);
            default: throw new IllegalStateException("Unexpected state " + this.state);
        }
    }

    @Override
    public boolean isTemporary() {
        return false;
    }

    @Override
    public void onLostControl() {
        this.state = State.NONE;
        this.goal = null;
    }

    @Override
    public String displayName0() {
        return "Custom Goal " + this.goal;
    }

    public enum State {
        NONE,
        GOAL_SET,
        PATH_REQUESTED,
        EXECUTING
    }
}