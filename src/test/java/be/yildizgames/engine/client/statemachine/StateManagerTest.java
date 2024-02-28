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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateManagerTest {

    @Nested
    public class Constructor {

        @Test
        public void happyFlow() {
            StateManager manager = StateManager.withInitialState(new DummyState(StateId.valueOf(1)));
            assertEquals(StateId.valueOf(1), manager.getCurrentState().getStateId());
        }

    }

    @Nested
    public class RegisterState {

        @Test
        public void happyFlow() {
            StateManager manager = StateManager.withInitialState(new DummyState(StateId.valueOf(1)));
            manager.registerGameState(new DummyState(2));
            assertEquals(StateId.valueOf(1), manager.getCurrentState().getStateId());
        }

        @Test
        public void alreadyExists() {
            StateId value = StateId.valueOf(1);
            StateManager manager = StateManager.withInitialState(new DummyState(value));
            manager.registerGameState(new DummyState(value));
            assertEquals(value, manager.getCurrentState().getStateId());
        }

        @Test
        public void withNull() {
            StateManager manager = StateManager.withInitialState(new DummyState(StateId.valueOf(1)));
            Assertions.assertThrows(NullPointerException.class,() -> manager.registerGameState((State) null));
        }

    }

    @Nested
    public class RegisterStateFlow {

        @Test
        public void happyFlow() {
            StateManager manager = StateManager.withInitialState(new DummyState(StateId.valueOf(1)));
            manager.registerGameStateFlow(new StateFlow(StateId.valueOf(1), StateId.valueOf(2), StateFlowEvent.valueOf(1)));
        }

        @Test
        public void withNull() {
            StateManager manager = StateManager.withInitialState(new DummyState(StateId.valueOf(1)));
            Assertions.assertThrows(NullPointerException.class, () -> manager.registerGameStateFlow((StateFlow) null));
        }

    }

    @Nested
    public class ProcessEvent {

        @Test
        public void happyFlow() {
            var state1 = new DummyState(1);
            var state2 = new DummyState(2);
            var manager = StateManager.withInitialState(state1);
            manager.registerGameState(state2);
            manager.registerGameStateFlow(new StateFlow(state1.getStateId(), state2.getStateId(), StateFlowEvents.LOADING_COMPLETED.event));
            Assertions.assertEquals(state1, manager.getCurrentState());
            manager.processEvent(StateFlowEvents.LOADING_COMPLETED);
            Assertions.assertEquals(state2, manager.getCurrentState());
        }

        @Test
        public void any() {
            var state1 = new DummyState(1);
            var state2 = new DummyState(2);
            var state3 = new DummyState(3);
            var manager = StateManager.withInitialState(state1);
            manager.registerGameState(state2);
            manager.registerGameState(state3);
            manager.registerGameStateFlow(new StateFlow(StateIds.ANY.id, state2.getStateId(), StateFlowEvents.LOADING_COMPLETED.event));
            manager.registerGameStateFlow(new StateFlow(StateIds.ANY.id, state3.getStateId(), StateFlowEvents.OPEN_CONFIGURATION.event));
            Assertions.assertEquals(state1, manager.getCurrentState());
            manager.processEvent(StateFlowEvents.LOADING_COMPLETED);
            Assertions.assertEquals(state2, manager.getCurrentState());
            manager.processEvent(StateFlowEvents.OPEN_CONFIGURATION);
            Assertions.assertEquals(state3, manager.getCurrentState());
            manager.processEvent(StateFlowEvents.LOADING_COMPLETED);
            Assertions.assertEquals(state2, manager.getCurrentState());
        }

    }

    @Test
    public void registerGameStateFlow() {
    }

    @Test
    public void getCurrentState() {
    }
}