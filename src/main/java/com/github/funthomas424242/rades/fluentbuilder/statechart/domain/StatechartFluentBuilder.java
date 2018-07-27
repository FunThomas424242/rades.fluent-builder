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

import com.github.funthomas424242.rades.fluentbuilder.statechart.generated.AbstractStatechartFluentBuilder;
import com.github.funthomas424242.rades.fluentbuilder.statechart.generated.AbstractStatechartFluentBuilder.Zustand1;
import com.github.funthomas424242.rades.fluentbuilder.statechart.generated.AbstractStatechartFluentBuilder.Zustand2;
import com.github.funthomas424242.rades.fluentbuilder.statechart.generated.AbstractStatechartFluentBuilder.Zustand3;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatur;

//@RadesAddFluentBuilder
public class StatechartFluentBuilder implements AbstractStatechartFluentBuilder.AllStates {


    protected StatechartAccessor statechart;

    protected StatechartFluentBuilder() {
        this(new StatechartBuilder().build(StatechartAccessor.class));
    }

    protected StatechartFluentBuilder(final StatechartAccessor statechart) {
        this.statechart = statechart;
    }

    public static Zustand1 newStatechart() {
        return new StatechartFluentBuilder();
    }

    @Override
    public Statechart build() {
        return new StatechartBuilder(this.statechart.toStatechart()).build();
    }

    @Override
    public <A> A build(Class<A> accessorClass) {
        return new StatechartBuilder(this.statechart.toStatechart()).build(accessorClass);
    }

    @Override
    public Zustand2 withQualifiedClassName(final String id) {
        this.statechart = new StatechartBuilder(this.statechart.toStatechart()).withId(id).build(StatechartAccessor.class);
        return this;
    }

    @Override
    public Zustand3 withStartState(final String startStatename) {
        this.statechart = new StatechartBuilder(this.statechart.toStatechart()).withStartState(this.statechart.getState(startStatename)).build(StatechartAccessor.class);
        return this;
    }

    @Override
    public Zustand2 addState(final String stateName) {
        this.statechart.addState(stateName, State.of(stateName));
        return this;
    }

    @Override
    public Zustand3 addTransition(final String srcStateName, final String targetStateName, final String transitionName, final ParameterSignatur... parameterSignaturs) {
        this.statechart.getState(srcStateName)
            .addTransitionTo(statechart.getState(targetStateName), transitionName, parameterSignaturs);
        return this;
    }

    @Override
    public Zustand3 addEmission(final String srcStateName, final String emissionName, final ParameterSignatur returnType) {
        this.statechart.getState(srcStateName).addTransition(emissionName, returnType);
        return this;
    }

    @Override
    public Zustand3 addEmission(final String srcStateName, final String emissionName, final ParameterSignatur returnType, final ParameterSignatur... parameterSignaturs) {
        this.statechart.getState(srcStateName).addTransition(emissionName, returnType, parameterSignaturs);
        return this;
    }

}
