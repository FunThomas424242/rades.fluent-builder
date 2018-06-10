package com.github.funthomas424242.rades.fluentbuilder.statechart;

/*-
 * #%L
 * rades.fluent-builder
 * %%
 * Copyright (C) 2018 PIUG
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.junit.jupiter.api.Test;

import static com.github.funthomas424242.rades.fluentbuilder.statechart.GeneratedAbstractStatechart.newState;
import static com.github.funthomas424242.rades.fluentbuilder.statechart.GeneratedAbstractStatechart.state;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class StatechartTest {

    @Test
    public void createQueueStatechart() {
        final String id = "Statechart ID";
        final StatechartAccessor statechart = new StatechartBuilder()
            .withId(id)
            .register()
            .withStartState(
                newState(id, "Empty"))
            .addState(
                newState(id, "Not Empty")
                    .addTransitionTo(state(id, "Not Empty"), "enqueue")
                    .addTransitionTo(state(id, "Not Empty"), "isEmpty")
                    .addTransitionTo(state(id, "Not Empty"), "dequeue")
                    .addTransitionTo(state(id, "Empty"), "dequeue")
            )
            .atState("Empty")
            .addTransitionTo(newState(id, "Not Empty"), "enqueue")
            .addTransitionTo(state(id, "Empty"), "isEmpty")


            .build(StatechartAccessor.class);

        assertEquals(2, statechart.states().count());
        assertSame(startState, statechart.getStartState());
    }

}
