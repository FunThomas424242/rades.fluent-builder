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

import com.github.funthomas424242.rades.annotations.accessors.RadesAddAccessor;
import com.github.funthomas424242.rades.annotations.accessors.RadesNoAccessor;
import com.github.funthomas424242.rades.annotations.builder.RadesAddBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RadesAddBuilder
@RadesAddAccessor
public class Transition {

    @NotNull
    protected State startState;
    @NotNull
    protected String transitionName;
    @NotNull
    //@RadesNoBuilder
    @RadesNoAccessor
    protected List<ParameterSignatur> parameters = new ArrayList<ParameterSignatur>();

    // Entweder mit
    protected State targetState;
    // oder mit
    protected String returnType;


    public static Transition of(final State startState, final State targetState, final String transitionName, final ParameterSignatur parameterSignatur) {
        final List<ParameterSignatur> parameterSignaturs = new ArrayList<>();
        parameterSignaturs.add(parameterSignatur);
        return new TransitionBuilder().withStartState(startState)
            .withTargetState(targetState)
            .withTransitionName(transitionName)
            .withParameters(parameterSignaturs).build();
    }

    public static Transition of(final String startStateName, final String targetStateName, final String transitionName, final String parameterSignatur) {
        return Transition.of(State.of(startStateName), State.of(targetStateName), transitionName, ParameterSignatur.of(parameterSignatur));
    }

}
