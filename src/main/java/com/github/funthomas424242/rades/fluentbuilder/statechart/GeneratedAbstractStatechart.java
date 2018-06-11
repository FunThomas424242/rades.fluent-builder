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

import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractStatechart {

    protected static final Map<String, Statechart> statecharts = new HashMap<>();

    public static interface Zustand1 {
        Zustand2 withId(final String chartId);
    }

    public static interface Zustand2 {
        Zustand2 addState(final State state);

        Zustand2 addTransition(final String srcStateName, final String targetStateName, final String parameterSignatur);

        Zustand3 withStartState(final State startState);
    }

    public static interface Zustand3 {
        <A> A build(Class<A> accessorClass);

        Statechart build();
    }

    public static interface AllStates extends Zustand1, Zustand2, Zustand3 {}

    public static Statechart register(final String chartId, final Statechart statechart) {
        final long anzahl = statecharts.values().stream().filter(statechart1 -> {
            return statechart1.id.equals(chartId);
        }).count();
        if (anzahl > 0) throw new IllegalStateException("statechart wurde bereits registriert mit id " + chartId);
        return statecharts.put(chartId, statechart);
    }

    public static State state(final String chartId, final String stateName) {
        final Statechart statechart = getStatechart(chartId);
        return statechart.states.stream().filter(state -> state.stateName.equals(stateName))
            .findFirst().get();
    }

    public static State newState(final String stateName) {
        return State.of(stateName);
    }

    private static Statechart getStatechart(final String chartId) {
        if (!statecharts.containsKey(chartId)) {
            throw new IllegalStateException("chart mit id <" + chartId + " nicht gefunden.");
        }
        return statecharts.get(chartId);
    }


}
