/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 * Copyright (c) 2019 Grégory Van den Borre
 *
 * More infos available: https://engine.yildiz-games.be
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE  SOFTWARE.
 */

package be.yildizgames.engine.client.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Grégory Van den Borre
 */
public class StateManager implements StateFlowEventProcessor {

    /**
     * The current activated game state, all other one are deactivated.
     */
    private StateId currentState = StateIds.NONE.id;

    /**
     * The list of all registered game states.
     */
    private final Map<StateId, State> states = new HashMap<>();

    /**
     * The list of all transitions between game states.
     */
    private final Map<StateId, List<StateFlow>> flows = new HashMap<>();

    private final Map<StateId, List<StateFlowExecution>> executionFlows = new HashMap<>();

    private StateManager(State initialState) {
        super();
        this.flows.put(StateIds.ANY.id, new ArrayList<>());
        this.executionFlows.put(StateIds.ANY.id, new ArrayList<>());
        this.registerInitialGameState(initialState);
    }

    public static  StateManager withInitialState(State initialState) {
        return new StateManager(initialState);
    }

    /**
     * Register a new game state.
     * @param state State to register.
     */
    public final void registerGameState(final State state) {
        Objects.requireNonNull(state);
        if(!this.states.containsKey(state.getStateId())) {
            this.states.put(state.getStateId(), state);
            this.executionFlows.put(state.getStateId(), new ArrayList<>());
            this.flows.put(state.getStateId(), new ArrayList<>());
            state.deactivate();
        }
    }

    /**
     * Register the initial game state, it will be activated directly.
     * @param state Initial state to register.
     */
    private void registerInitialGameState(final State state) {
        Objects.requireNonNull(state);
        this.states.put(state.getStateId(), state);
        this.flows.put(state.getStateId(), new ArrayList<>());
        this.executionFlows.put(state.getStateId(), new ArrayList<>());
        state.activate();
        this.currentState = state.getStateId();
    }

    @Override
    public final void processEvent(final StateFlowEvent event) {
        this.flows.get(this.currentState)
                .stream()
                .filter(f -> f.isForEvent(event))
                .findFirst()
                .ifPresentOrElse(this::setCurrentStateFromFlow, () -> getFromAny(event));
        this.executionFlows.get(this.currentState)
                .stream()
                .filter(f -> f.isForEvent(event))
                .findFirst()
                .ifPresentOrElse(e -> e.function.execute(), () -> getFromAny(event));
    }

    @Override
    public final void processEvent(StateFlowEvents events) {
        processEvent(events.event);
    }

    public final void registerGameStateFlow(final StateFlow flow) {
        Objects.requireNonNull(flow);
        if(!flow.state.equals(StateIds.ANY.id)) {
            this.flows.get(flow.state).add(flow);
        }
    }

    public final void registerGameStateFlow(final StateFlowExecution flow) {
        Objects.requireNonNull(flow);
        this.executionFlows.get(flow.state).add(flow);
    }

    public final State getCurrentState() {
        return this.states.get(this.currentState);
    }


    private void setCurrentStateFromFlow(final StateFlow f) {
        this.setCurrentState(f.nextState);
    }

    private void setCurrentState(final StateId id) {
        Optional.ofNullable(currentState).ifPresent(c -> states.get(this.currentState).deactivate());
        if(!this.states.containsKey(id)) {
            throw new IllegalArgumentException("No state associated with " + id.value);
        }
        this.states.get(id).activate();
        this.currentState = id;
    }

    private void getFromAny(final StateFlowEvent event) {
        this.flows.get(StateIds.ANY.id)
                .stream()
                .filter(f -> f.isForEvent(event))
                .findFirst()
                .ifPresent(this::setCurrentStateFromFlow);
        this.executionFlows.get(StateIds.ANY.id)
                .stream()
                .filter(f -> f.isForEvent(event))
                .findFirst()
                .ifPresent(e -> e.function.execute());
    }
}
