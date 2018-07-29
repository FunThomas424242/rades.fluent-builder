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

import com.github.funthomas424242.rades.annotations.accessors.RadesAddAccessor;
import com.github.funthomas424242.rades.annotations.accessors.RadesNoAccessor;
import com.github.funthomas424242.rades.annotations.builder.RadesAddBuilder;
import com.github.funthomas424242.rades.annotations.builder.RadesNoBuilder;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatur;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignaturs;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@RadesAddBuilder
@RadesAddAccessor
public class State {

    protected String stateName;

    @RadesNoBuilder
    @RadesNoAccessor
    @NotNull
    protected final Set<Transition> transitions = new HashSet<Transition>();


    public static State of(final String stateName) {
        return new StateBuilder().withStateName(stateName).build();
    }

    public Stream<Transition> transitions() {
        return this.transitions.stream();
    }


    public State addTransitionTo(final State targetState, final String transitionName) {
        final Transition transition = Transition.of(this, targetState, transitionName, ParameterSignaturs.of());
        this.transitions.add(transition);
        return this;
    }

    public State addTransitionTo(final State targetState, final String transitionName, final ParameterSignatur... parameterSignaturs) {
        final Transition transition = Transition.of(this, targetState, transitionName, parameterSignaturs);
        this.transitions.add(transition);
        return this;
    }

    public State addTransitionTo(final State targetState, final String transitionName, final ParameterSignaturs parameterList) {
        final Transition transition = Transition.of(this, targetState, transitionName, parameterList);
        this.transitions.add(transition);
        return this;
    }

    public State addTransition(final String transitionName, final ParameterSignatur returnType, final ParameterSignatur... parameterSignaturs) {
        final Transition transition = new TransitionBuilder()
            .withStartState(this)
            .withTargetState(null)
            .withTransitionName(transitionName)
            .withParameterSignatur(ParameterSignaturs.of(parameterSignaturs))
            .withReturnType(returnType)
            .build();
        this.transitions.add(transition);
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final State state = (State) o;
        return Objects.equals(stateName, state.stateName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stateName);
    }
}
