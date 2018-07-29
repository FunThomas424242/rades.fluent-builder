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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class StateTest {


    @Test
    public void createValidInstanz() {
        final State state = State.of("Mein State");
        assertNotNull(state);
        assertNotNull(state.transitions);
        assertEquals("Mein State", state.stateName);
        assertEquals(0, state.transitions.size());
    }

    @Test
    public void statesSindGleichWennUndNurWennDerNameGleichIst(){
        final State targetState = State.of("Target State");
        final State stateNoTransactions = State.of("Mein TestState #1");
        final State stateWithTransactions = State.of("Mein TestState #1");
        stateWithTransactions.addTransitionTo(targetState,"add");

        // Beide States sind gültige Instanzen
        assertNotNull(stateNoTransactions);
        assertNotNull(stateWithTransactions);

        // Beide States sind mit sich selber gleich
        assertEquals(stateNoTransactions,stateNoTransactions);
        assertEquals(stateWithTransactions,stateWithTransactions);

        // Beide States sind auf Grund des Namen gleich, trotz verschiedener Transitionen
        // HINT: Diese fachliche Entscheidung ist bei der aktuellen Erzeugung von Statecharts relevant.
        assertNotSame(stateNoTransactions,stateWithTransactions);
        assertEquals(stateNoTransactions,stateWithTransactions);

    }

    @Test
    public void statesSindNichtGleichWennUndNurWennDerNameUnterschiedlichIst(){
        final State state1 = State.of("Mein TestState #1");
        final State state2 = State.of("Mein TestState #2");

        assertNotNull(state1);
        assertNotNull(state2);

        assertNotSame(state1,state2);
        assertNotEquals(state1,state2);

    }


}
