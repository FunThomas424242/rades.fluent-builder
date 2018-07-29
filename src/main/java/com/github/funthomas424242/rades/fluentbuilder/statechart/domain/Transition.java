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
import com.github.funthomas424242.rades.annotations.builder.RadesAddBuilder;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatur;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignaturs;

import javax.validation.constraints.NotNull;

@RadesAddBuilder
@RadesAddAccessor
public class Transition {

    @NotNull
    protected State startState;
    @NotNull
    protected String transitionName;
    @NotNull
    protected ParameterSignaturs parameterSignatur;

    // Entweder mit
    protected State targetState;
    // oder mit
    protected ParameterSignatur returnType;


    public static Transition of(final State startState, final State targetState, final String transitionName, final ParameterSignatur... parameterSignaturs) {
        final ParameterSignaturs parameterSignaturList = ParameterSignaturs.of(parameterSignaturs);
        return of(startState, targetState, transitionName, parameterSignaturList);
    }

    public static Transition of(final State startState, final State targetState, final String transitionName, final ParameterSignaturs parameterSignaturList) {
        return new TransitionBuilder().withStartState(startState)
            .withTargetState(targetState)
            .withTransitionName(transitionName)
            .withParameterSignatur(parameterSignaturList).build();
    }

    public static Transition of(final String startStateName, final String targetStateName, final String transitionName, final Class... parameterTyp) {
        return Transition.of(State.of(startStateName), State.of(targetStateName), transitionName, ParameterSignaturs.of(parameterTyp));
    }

}
