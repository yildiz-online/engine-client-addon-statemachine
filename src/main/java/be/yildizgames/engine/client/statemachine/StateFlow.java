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

/**
 * To move from one state to another when an event occurs.
 * @author Grégory Van den Borre
 */
public class StateFlow {

    /**
     * Initial state.
     */
    final StateId state;

    /**
     * Final state.
     */
    final StateId nextState;

    /**
     * Event to move from initial to final.
     */
    final StateFlowEvent event;

    StateFlow(StateId state, StateId nextState, StateFlowEvent event) {
        this.state = state;
        this.nextState = nextState;
        this.event = event;
    }

    /**
     * Build a new flow.
     * @param event Event to react to.
     * @return A builder to create the flow.
     */
    public static StateFlowBuilder on(StateFlowEvent event) {
        return new StateFlowBuilder(event);
    }

    /**
     * Check if this flow will react to a given event.
     * @param e Event to check.
     * @return True if this flow will react to the provided event, false otherwise.
     */
    boolean isForEvent(StateFlowEvent e) {
        return this.event.equals(e);
    }
}
