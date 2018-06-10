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

import static org.junit.jupiter.api.Assertions.*;

class StatechartTest {

    @Test
    public void createQueueStatechart(){
        final State emptyState = State.of("Empty");
        final State notEmptyState = State.of("Not Empty");
        final State startState =  emptyState;
        final StatechartAccessor statechart = new StatechartBuilder()
            .addState(emptyState)
            .addState(notEmptyState)
            .addTransition( Transition.of(emptyState, notEmptyState, ParameterSignatur.of("enqueue")))
            .withStartState(startState)
            .build(StatechartAccessor.class);
        assertEquals(2,statechart.states().count());
        assertSame(startState,statechart.getStartState());
    }

}
