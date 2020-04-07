/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2019 Grégory Van den Borre
 *
 *  More infos available: https://engine.yildiz-games.be
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  SOFTWARE.
 *
 */

package be.yildizgames.engine.client.statemachine;

import java.util.Objects;

/**
 * @author Grégory Van den Borre
 */
public class StateFlowBuilder {

    private final StateFlowEvent event;

    private StateId currentState;

    StateFlowBuilder(StateFlowEvent event) {
        super();
        Objects.requireNonNull(event);
        this.event = event;
    }

    public final StateFlowBuilder goFrom(StateId state) {
        Objects.requireNonNull(state);
        this.currentState = state;
        return this;
    }

    public final StateFlowBuilder goFrom(State state) {
        return goFrom(state.getStateId());
    }

    public final StateFlowBuilder goFrom(StateIds state) {
        return goFrom(state.id);
    }

    public final StateFlow to(StateId nextState) {
        Objects.requireNonNull(nextState);
        return new StateFlow(this.currentState, nextState, this.event);
    }

    public final StateFlow to(State nextState) {
        return to(nextState.getStateId());
    }

    public final StateFlow to(StateIds nextState) {
        return to(nextState.id);
    }
}
