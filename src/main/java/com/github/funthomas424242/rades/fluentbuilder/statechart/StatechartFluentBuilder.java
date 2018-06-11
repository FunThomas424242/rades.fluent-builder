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

import com.github.funthomas424242.rades.annotations.accessors.InvalidAccessorException;
import com.github.funthomas424242.rades.fluentbuilder.statechart.generate.AbstractStatechartFluentBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//@RadesAddFluentBuilder
public class StatechartFluentBuilder extends AbstractStatechartFluentBuilder implements AbstractStatechartFluentBuilder.AllStates {


    protected Statechart statechart;

    protected StatechartFluentBuilder() {
        this(new Statechart());
    }

    protected StatechartFluentBuilder(final Statechart statechart) {
        this.statechart = statechart;
    }

    protected static Statechart getStatechart(final String chartId) {
        if (!statecharts.containsKey(chartId)) {
            throw new IllegalStateException("chart mit id <" + chartId + " nicht gefunden.");
        }
        return statecharts.get(chartId);
    }

    public static State state(final String chartId, final String stateName) {
        final Statechart statechart = getStatechart(chartId);
        return statechart.states.stream().filter(state -> state.stateName.equals(stateName))
            .findFirst().get();
    }

    public static State newState(final String stateName) {
        return State.of(stateName);
    }

    public static Zustand1 newStatechart() {
        return new StatechartFluentBuilder();
    }

    public Statechart build() {
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final java.util.Set<ConstraintViolation<Statechart>> constraintViolations = validator.validate(this.statechart);

        if (constraintViolations.size() > 0) {
            java.util.Set<String> violationMessages = new java.util.HashSet<String>();

            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
            }

            final StringBuffer buf = new StringBuffer();
            buf.append("Statechart is not valid:\n");
            violationMessages.forEach(message -> buf.append(message + "\n"));
            throw new ValidationException(buf.toString());
        }
        final Statechart value = this.statechart;
        this.statechart = null;
        return value;
    }

    public <A> A build(Class<A> accessorClass) {
        final Statechart statechart = this.build();
        this.statechart = statechart;
        try {
            final Constructor<A> constructor = accessorClass.getDeclaredConstructor(Statechart.class);
            final A accessor = constructor.newInstance(statechart);
            this.statechart = null;
            return accessor;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            throw new InvalidAccessorException("ungültige Accessorklasse übergeben", ex);
        }
    }

    public Zustand2 withId(final String id) {
        this.statechart.id = id;
        register(this.statechart.id, this.statechart);
        return this;
    }

    public Zustand3 withStartState(final State startState) {
        this.statechart.startState = startState;
        return this;
    }

    public Zustand2 addState(final State state) {
        this.statechart.states.add(state);
        return this;
    }

    public Zustand2 addTransition(final String srcStateName, final String targetStateName, final String parameterSignatur) {
        state(this.statechart.id, srcStateName).addTransitionTo(state(this.statechart.id, targetStateName), parameterSignatur);
        return this;
    }

    public State atState(final String stateName) {
        return state(this.statechart.id, stateName);
    }



    private static Statechart register(final String chartId, final Statechart statechart) {
        final long anzahl = statecharts.values().stream().filter(statechart1 -> {
            return statechart1.id.equals(chartId);
        }).count();
        if (anzahl > 0) throw new IllegalStateException("statechart wurde bereits registriert mit id " + chartId);
        return statecharts.put(chartId, statechart);
    }


}
