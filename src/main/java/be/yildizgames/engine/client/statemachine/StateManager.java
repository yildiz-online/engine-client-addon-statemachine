/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 * Copyright (c) 2018 Grégory Van den Borre
 *
 * More infos available: https://www.yildiz-games.be
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

import be.yildizgames.common.exception.implementation.ImplementationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Grégory Van den Borre
 */
public class StateManager<T extends State> {

    /**
     * The current activated game state, all other one are deactivated.
     */
    private StateId currentState = StateId.NONE;

    /**
     * The list of all registered game states.
     */
    private final Map<StateId, T> states = new HashMap<>();

    /**
     * The list of all transitions between game states.
     */
    private final Map<StateId, List<StateFlow>> flows = new HashMap<>();

    private StateManager(T initialState) {
        super();
        this.registerInitialGameState(initialState);
    }

    public static <T extends State> StateManager<T> withInitialState(T initialState) {
        return new StateManager<>(initialState);
    }

    /**
     * Register a new game state.
     * @param state State to register.
     */
    public void registerGameState(final T state) {
        ImplementationException.throwForNull(state);
        if(!this.states.containsKey(state.getStateId())) {
            this.states.put(state.getStateId(), state);
            this.flows.put(state.getStateId(), new ArrayList<>());
            state.deactivate();
        }
    }

    /**
     * Register the initial game state, it will be activated directly.
     * @param state Initial state to register.
     */
    private final void registerInitialGameState(final T state) {
        ImplementationException.throwForNull(state);
        this.states.put(state.getStateId(), state);
        this.flows.put(state.getStateId(), new ArrayList<>());
        state.activate();
        this.currentState = state.getStateId();
    }

    public void processEvent(final StateFlowEvent event) {
        this.flows.get(this.currentState)
                .stream()
                .filter(f -> f.isForEvent(event))
                .findFirst()
                .ifPresentOrElse(this::setCurrentStateFromFlow, () -> getFromAny(event));

    }

    public final void registerGameStateFlow(final StateFlow flow) {
        ImplementationException.throwForNull(flow);
        if(!flow.state.equals(StateId.ANY)) {
            this.flows.get(flow.state).add(flow);
        }
    }

    public final T getCurrentState() {
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
        this.flows.get(StateId.ANY)
                .stream()
                .filter(f -> f.isForEvent(event))
                .findFirst()
                .ifPresent(this::setCurrentStateFromFlow);
    }
}
