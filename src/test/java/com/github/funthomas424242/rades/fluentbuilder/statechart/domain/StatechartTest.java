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

import com.github.funthomas424242.rades.fluentbuilder.infrastructure.io.PrintWriterFactory;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatechartTest {


    @Test
    @DisplayName("Erzeuge eine gültige Instanz eines Statecharts.")
    public void createValidInstanz() {

        final Statechart statechart = new StatechartBuilder()
            .withId("test.Statechart1")
            .withStartState(State.of("Not Empty")).build();

        assertNotNull(statechart);
        assertEquals("test.Statechart1", statechart.id);
        assertEquals("Not Empty", statechart.startState.stateName);

    }

    @Test
    @DisplayName("Erzeuge adoc für Statechart ohne Transitionen.")
    public void testSaveAsAdocPrintWriter(@Mock PrintWriterFactory writerFactory, @Mock PrintWriter printWriter) {

        final Statechart statechart = new StatechartBuilder()
            .withId("test.Statechart1")
            .withStartState(State.of("Not Empty")).build();

        when(writerFactory.createPrintWriter()).thenReturn(printWriter);
        verify(printWriter, times(0)).println(any(String.class));

        statechart.saveAsAdoc(writerFactory);

        verify(printWriter, times(1)).println("@startuml");
        verify(printWriter, times(1)).println("@enduml");
    }

    @Test
    @DisplayName("Erzeuge adoc für Statechart mit ungültiger Transition deren Startstate null ist.")
    public void testSaveAsAdocInvalidTransaction(@Mock PrintWriterFactory writerFactory, @Mock PrintWriter printWriter) {

        when(writerFactory.createPrintWriter()).thenReturn(printWriter);

        final State startState = State.of("Not Empty");
        final Statechart statechart = new StatechartBuilder()
            .withId("test.Statechart1")
            .withStartState(startState)
            .build();
        statechart.states.put("Not Empty",startState);
        final Transition transition = new Transition();
        transition.targetState = startState;
        transition.transitionName = "trap";
        startState.transitions.add(transition);

        verify(printWriter, times(0)).println(any(String.class));

        statechart.saveAsAdoc(writerFactory);

        verify(printWriter, times(4)).println(any(String.class));

    }

    @Test
    @DisplayName("Erzeuge adoc für Statechart mit gültiger Emission deren Targetstate null ist.")
    public void testSaveAsAdocValidTransaction(@Mock PrintWriterFactory writerFactory, @Mock PrintWriter printWriter) {

        when(writerFactory.createPrintWriter()).thenReturn(printWriter);

        final State startState = State.of("Not Empty");
        final Statechart statechart = new StatechartBuilder()
            .withId("test.Statechart2")
            .withStartState(startState)
            .build();
        statechart.states.put("Not Empty",startState);
        final Transition transition = new Transition();
        transition.startState = startState;
        transition.transitionName = "trap";
        startState.transitions.add(transition);

        verify(printWriter, times(0)).println(any(String.class));

        statechart.saveAsAdoc(writerFactory);

        verify(printWriter, times(4)).println(any(String.class));

    }



}
