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

import com.github.funthomas424242.rades.fluentbuilder.statechart.fluentbuilders.StatechartFluentBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;


class StatechartTest {

    @Test
    public void createQueueStatechart() throws IOException {
        final String id = "com.github.funthomas424242.rades.fluentbuilder.statechart.QueueStatechart";
        final StatechartAccessor statechart = StatechartFluentBuilder.newStatechart()
            .withQualifiedClassName(id)
            .addState("Empty")
            .addState("Not Empty")
            .withStartState("Empty")
            .addTransition("Empty", "Not Empty", "enqueue")
            .addTransition("Empty", "Empty", "isEmpty")

            .addTransition("Not Empty", "Not Empty", "enqueue")
            .addTransition("Not Empty", "Not Empty", "isEmpty")
            .addTransition("Not Empty", "Not Empty", "dequeue")
            .addTransition("Not Empty", "Empty", "dequeue")

            .build(StatechartAccessor.class);

        assertEquals(2, statechart.states().count());
        assertSame(statechart.getState("Empty"), statechart.getStartState());
        assertEquals(statechart.getState("Not Empty"), State.of("Not Empty"));
        assertNotSame(statechart.getState("Not Empty"), State.of("Not Empty"));
        assertNotEquals(statechart.getState("Not Empty"), statechart.getState("Empty"));

//        final AbstractFluentBuilderGenerator generator = new AbstractFluentBuilderGenerator(statechart);
//        generator.generate("target/generated-test-sources/test-annotations/");
    }

    @Test
    public void createStatechartStatechart() throws IOException {
        final String id = "com.github.funthomas424242.rades.fluentbuilder.generated.AbstractStatechartFluentBuilder";
        final StatechartAccessor statechart = StatechartFluentBuilder.newStatechart()
            .withQualifiedClassName(id)
            .addState("Zustand1")
            .addState("Zustand2")
            .addState("Zustand3")
            .withStartState("Zustand1")
            .addTransition("Zustand1", "Zustand2", "withQualifiedName")

            .addTransition("Zustand2", "Zustand2", "addState")
            .addTransition("Zustand2", "Zustand3", "withStartState")

            .addTransition("Zustand3", "Zustand3", "addTransitionTo")
//            .addTransition("Zustand3", "Not Empty", "dequeue")
//            .addTransition("Not Empty", "Empty", "dequeue")

            .build(StatechartAccessor.class);

        assertEquals(3, statechart.states().count());

        final AbstractFluentBuilderGenerator generator = new AbstractFluentBuilderGenerator(statechart);
        generator.generate("target/generated-test-sources/test-annotations/");
    }

}
