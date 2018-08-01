package com.github.funthomas424242.rades.fluentbuilder.statechart.domain;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import test.MockitoExtension;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StatechartTest {


    @Test
    public void createValidInstanz() {

        final Statechart statechart = new StatechartBuilder()
            .withId("test.Statechart1")
            .withStartState(State.of("Not Empty")).build();

        assertNotNull(statechart);
        assertEquals("test.Statechart1", statechart.id);
        assertEquals("Not Empty", statechart.startState.stateName);

    }

    @Test
    public void testSaveAsAdoc(@Mock PrintWriter writer) {

        final Statechart statechart = new StatechartBuilder()
            .withId("test.Statechart1")
            .withStartState(State.of("Not Empty")).build();

        verify(writer, times(0)).println(any(String.class));

        statechart.saveAsAdoc(writer);

        verify(writer, times(1)).println("@startuml");
        verify(writer, times(1)).println("@enduml");
    }


}
