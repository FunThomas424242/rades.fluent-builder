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

import static com.github.funthomas424242.rades.fluentbuilder.statechart.StatechartFluentBuilder.newState;
import static com.github.funthomas424242.rades.fluentbuilder.statechart.StatechartFluentBuilder.state;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class StatechartTest {

    @Test
    public void createQueueStatechart() {
        final String id = "Statechart ID";
        final StatechartAccessor statechart = StatechartFluentBuilder.newStatechart()
            .withId(id)
            .addState(
                newState("Empty"))
            .addState(
                newState("Not Empty")
            )
            .addTransition("Empty", "Not Empty", "enqueue")
            .addTransition("Empty", "Empty", "isEmpty")

            .addTransition("Not Empty", "Not Empty", "enqueue")
            .addTransition("Not Empty", "Not Empty", "isEmpty")
            .addTransition("Not Empty", "Not Empty", "dequeue")
            .addTransition("Not Empty", "Empty", "dequeue")
            .withStartState(state(id, "Empty"))
            .build(StatechartAccessor.class);

        assertEquals(2, statechart.states().count());
        assertSame(state(id, "Empty"), statechart.getStartState());
        assertEquals(state(id, "Not Empty"), newState("Not Empty"));
        assertNotSame(state(id, "Not Empty"), newState("Not Empty"));
        assertNotEquals(state(id, "Not Empty"), state(id, "Empty"));

        // TODO Transitions - equals same, nocht nicht bedacht
    }

}
