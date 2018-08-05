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
import com.github.funthomas424242.rades.fluentbuilder.infrastructure.io.PrintWriterFactory;
import com.github.funthomas424242.rades.fluentbuilder.infrastructure.text.TextConverter;

import javax.validation.constraints.NotNull;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.stream.Stream;


@RadesAddBuilder
@RadesAddAccessor
public class Statechart {

    @RadesNoBuilder
    public static final String PLANTUML_ENDUNG = ".txt";

    @RadesNoBuilder
    @RadesNoAccessor
    @NotNull
    protected final HashMap<String, State> states = new HashMap<>();

    // full qualified class name for generation
    protected String id;

    protected State startState;

    protected Statechart() {
    }

    public Stream<State> states() {
        return this.states.values().stream();
    }

    public void addState(final String stateName, final State state) {
        states.put(stateName, state);
    }

    public State getState(final String stateName) {
        return states.get(stateName);
    }

    public void saveAsAdoc(final PrintWriterFactory fileWriter) {

        final PrintWriter adocFileWriter = fileWriter.createPrintWriter();

        adocFileWriter.println("@startuml");
        states.values().stream().forEachOrdered(state ->
            state.transitions.stream().forEachOrdered(
                transition -> {
                    final String startStateName = transition.startState == null ? "[*]" : new TextConverter(transition.startState.stateName).convertToClassifierName();
                    final String targetStateName = transition.targetState == null ? "[*]" : new TextConverter(transition.targetState.stateName).convertToClassifierName();

                    if (transition.startState != null) {
                        adocFileWriter.println("state \"" + transition.startState.stateName + "\" as " + startStateName);
                    }
                    if (transition.targetState != null) {
                        adocFileWriter.println("state \"" + transition.targetState.stateName + "\" as " + targetStateName);
                    }
                    adocFileWriter.println(startStateName + " --> " + targetStateName + " : " + transition.transitionName);
                })
        );
        adocFileWriter.println("@enduml");
    }


}
