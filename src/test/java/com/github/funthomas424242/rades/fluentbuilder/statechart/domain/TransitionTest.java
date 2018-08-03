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

import com.github.funthomas424242.rades.fluentbuilder.infrastructure.streaming.Counter;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatursAccessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransitionTest {


    @Test
    public void createValidInstanz() {

        final Transition transition = Transition.of("Empty", "Not Empty", "enqueue", Integer.class, String.class);

        assertNotNull(transition);
        assertEquals("Empty", transition.startState.stateName);
        assertEquals("Not Empty", transition.targetState.stateName);
        assertEquals("enqueue", transition.transitionName);

        final ParameterSignatursAccessor signatur = new ParameterSignatursAccessor(transition.parameterSignatur);
        assertEquals(2, signatur.getParameterList().size());

        final Counter counter = new Counter();
        signatur.getParameterList().stream().forEachOrdered(parameterSignatur -> {
            counter.value++;
            if (counter.value == 1) {
                assertEquals(Integer.class.getName(), parameterSignatur.getParameterTypAsTypeName().toString());
            }
            if (counter.value == 2) {
                assertEquals(String.class.getName(), parameterSignatur.getParameterTypAsTypeName().toString());
            }
        });

    }

}
