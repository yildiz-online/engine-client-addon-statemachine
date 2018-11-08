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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Grégory Van den Borre
 */
public class GameStateManager <T extends GameState> {

    /**
     * The current activated game state, all other one are deactivated.
     */
    private GameStateId currentState = GameStateId.NONE;

    /**
     * The list of all registered game states.
     */
    private final Map<GameStateId, T> states = new HashMap<>();

    /**
     * The list of all transitions between game states.
     */
    private final Map<GameStateId, List<GameStateFlow>> flows = new HashMap<>();

    /**
     * Register a new game state.
     * @param state State to register.
     */
    public final void registerGameState(final T state) {
        this.states.put(state.getGameStateId(), state);
        this.flows.put(state.getGameStateId(), new ArrayList<>());
        state.deactivate();
    }

    /**
     * Register the initial game state, it will be activated directly.
     * @param state Initial state to register.
     */
    public final void registerInitialGameState(final T state) {
        this.states.put(state.getGameStateId(), state);
        this.flows.put(state.getGameStateId(), new ArrayList<>());
        state.activate();
        this.currentState = state.getGameStateId();
    }

    public void processEvent(final GameStateFlowEvent event) {
        this.flows.get(this.currentState)
                .stream()
                .filter(f -> f.isForEvent(event))
                .findFirst()
                .ifPresentOrElse(this::setCurrentStateFromFlow, () -> getFromAny(event));

    }

    public final void registerGameStateFlow(final GameStateFlow flow) {
        if(!flow.state.equals(GameStateId.ANY)) {
            this.flows.get(flow.state).add(flow);
        }
    }

    public final T getCurrentState() {
        return this.states.get(this.currentState);
    }


    private void setCurrentStateFromFlow(final GameStateFlow f) {
        this.setCurrentState(f.nextState);
    }

    private void setCurrentState(final GameStateId id) {
        Optional.ofNullable(currentState).ifPresent(c -> states.get(this.currentState).deactivate());
        if(!this.states.containsKey(id)) {
            throw new IllegalArgumentException("No state associated with " + id.value);
        }
        this.states.get(id).activate();
        this.currentState = id;
    }

    private void getFromAny(final GameStateFlowEvent event) {
        this.flows.get(GameStateId.ANY)
                .stream()
                .filter(f -> f.isForEvent(event))
                .findFirst()
                .ifPresent(this::setCurrentStateFromFlow);
    }
}
