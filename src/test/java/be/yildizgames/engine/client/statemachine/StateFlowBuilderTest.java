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

public class StateFlowBuilderTest {

    @Nested
    public class Constructor {

        @Test
        public void hapyFlow() {
            StateFlowBuilder builder = StateFlow.on(StateFlowEvent.valueOf(1));
            Assertions.assertNotNull(builder);
        }

        @Test
        public void withNull() {
            Assertions.assertThrows(NullPointerException.class, () -> StateFlow.on((StateFlowEvent)null));
        }

    }

    @Nested
    public class goFrom {

        @Test
        public void hapyFlow() {
            StateFlowBuilder builder = StateFlow.on(StateFlowEvent.valueOf(1));
            builder.goFrom(StateId.valueOf(5));
        }

        @Test
        public void withNull() {
            StateFlowBuilder builder = StateFlow.on(StateFlowEvent.valueOf(1));
            Assertions.assertThrows(NullPointerException.class, () -> builder.goFrom((StateId)null));
        }

    }

    @Nested
    public class to {

        @Test
        public void hapyFlow() {
            StateFlowBuilder builder = StateFlow.on(StateFlowEvent.valueOf(1));
            builder.goFrom(StateId.valueOf(5));
            StateFlow flow = builder.to(StateId.valueOf(6));
            Assertions.assertEquals(StateId.valueOf(5), flow.state);
            Assertions.assertEquals(StateId.valueOf(6), flow.nextState);
            Assertions.assertEquals(StateFlowEvent.valueOf(1), flow.event);
        }

        @Test
        public void withNull() {
            StateFlowBuilder builder = StateFlow.on(StateFlowEvent.valueOf(1));
            Assertions.assertThrows(NullPointerException.class, () -> builder.to((StateId)null));
        }

    }

}
