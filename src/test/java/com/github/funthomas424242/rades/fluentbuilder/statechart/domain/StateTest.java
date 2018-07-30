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


import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignaturType;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignaturs;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatursAccessor;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.util.Stack;

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
    public void statesSindNichtGleichWennUndNurWennDerNameUnterschiedlichIst() {
        final State state1 = State.of("Mein TestState #1");
        final State state2 = State.of("Mein TestState #2");

        assertNotNull(state1);
        assertNotNull(state2);

        assertNotSame(state1, state2);
        assertNotEquals(state1, state2);

    }

    @Test
    public void statesSindGleichWennUndNurWennDerNameGleichIst() {
        final State targetState = State.of("Target State");
        final State stateNoTransactions = State.of("Mein TestState #1");
        final State stateWithTransactions = State.of("Mein TestState #1");
        stateWithTransactions.addTransitionTo(targetState, "add");

        // Beide States sind gültige Instanzen
        assertNotNull(stateNoTransactions);
        assertNotNull(stateWithTransactions);

        // Beide States sind mit sich selber gleich
        assertEquals(stateNoTransactions, stateNoTransactions);
        assertEquals(stateWithTransactions, stateWithTransactions);

        // Beide States sind auf Grund des Namen gleich, trotz verschiedener Transitionen
        // HINT: Diese fachliche Entscheidung ist bei der aktuellen Erzeugung von Statecharts relevant.
        assertNotSame(stateNoTransactions, stateWithTransactions);
        assertEquals(stateNoTransactions, stateWithTransactions);

        // Standardtest für Gleichheit
        new EqualsTester()
            .addEqualityGroup(stateNoTransactions, stateWithTransactions)
            .addEqualityGroup(targetState)
            .testEquals();

    }

    @Test
    public void hashcodeBleibtKonstantBeiNeuenTransaktionen() {
        final State targetState = State.of("Target State");
        final State state = State.of("Mein TestState");
        assertNotNull(targetState);
        assertNotNull(state);

        final int hashCodeOhneTransaktionen = state.hashCode();
        state.addTransitionTo(targetState, "top");
        final int hashCodeMitTransaktionen = state.hashCode();

        assertEquals(hashCodeOhneTransaktionen, hashCodeMitTransaktionen);
    }

    @Test
    public void hashcodeAendertSichBeiUmbenennung() {
        final State state = State.of("Target State");
        assertNotNull(state);

        final int hasCodeVorUmbenennung = state.hashCode();
        state.stateName = "Neuer Name";
        final int hashCodeNachUmbenennung = state.hashCode();

        assertNotEquals(hasCodeVorUmbenennung, hashCodeNachUmbenennung);
    }

    @Test
    public void addValidTransasitionWitManyValidParameters() {
        final State targetState = State.of("Target State");
        final State stateWithTransitions = State.of("State für Transaktionen");

        // Transaktion ohne Parameter
        final State state1 = stateWithTransitions.addTransitionTo(targetState, "run");
        assertNotNull(state1);
        assertEquals(1, state1.transitions.size());
        final Transition transition1 = state1.transitions.stream().findFirst().get();
        assertNotNull(transition1);
        assertEquals("run", transition1.transitionName);
        final ParameterSignatursAccessor signatur1 = new ParameterSignatursAccessor(transition1.parameterSignatur);
        assertEquals(0, signatur1.getParameterList().size());

        // Transaktion mit einem Parameter
        final State state2 = stateWithTransitions.addTransitionTo(targetState, "run", ParameterSignaturType.of(Integer.class));


        // Transaktion mit vielen Parametern
        final State state3 = stateWithTransitions.addTransitionTo(targetState, "enqueue", ParameterSignaturs.of(Class.class, String.class, Stack.class));
        assertNotNull(state3);
        assertEquals(3, state3.transitions.size());
        final Transition transition3 = state3.transitions.stream().filter(transition -> {
            if ("enqueue".equals(transition.transitionName)) {
                return true;
            } else {
                return false;
            }
        }).findFirst().get();
        assertNotNull(transition3);
        assertEquals("enqueue", transition3.transitionName);
        final ParameterSignatursAccessor signatur3 = new ParameterSignatursAccessor(transition3.parameterSignatur);
        assertEquals(3, signatur3.getParameterList().size());


        assertEquals(state1, state2);
        assertEquals(state1, state3);
        assertEquals(state2, state3);

    }

}
