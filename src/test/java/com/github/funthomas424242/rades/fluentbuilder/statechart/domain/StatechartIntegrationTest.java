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

import com.github.funthomas424242.rades.fluentbuilder.infrastructure.io.PrintWriterFactory;
import com.github.funthomas424242.rades.fluentbuilder.statechart.generators.AbstractFluentBuilderGenerator;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatur;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignaturParameterizedType;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignaturType;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignaturTypeVariable;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignaturVararg;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StatechartIntegrationTest {

    @Test
    public void createStatechartStatechart() throws IOException {

        final String id = "com.github.funthomas424242.rades.fluentbuilder.statechart.generated.AbstractStatechartFluentBuilder";
        final StatechartAccessor statechart = StatechartFluentBuilder.newStatechart()
            .withQualifiedClassName(id)
            .addState("Zustand 1")
            .addState("Zustand 2")
            .addState("Zustand 3")
            .withStartState("Zustand 1")
            .addTransition("Zustand 1", "Zustand 2", "withQualifiedClassName",
                ParameterSignaturType.of("chartId", String.class))

            .addTransition("Zustand 2", "Zustand 2", "addState", ParameterSignaturType.of("stateName",String.class))
            .addTransition("Zustand 2", "Zustand 3", "withStartState", ParameterSignaturType.of("startStateName",String.class))

            .addTransition("Zustand 3", "Zustand 3", "addTransition",
                ParameterSignaturType.of("srcStateName",String.class),
                ParameterSignaturType.of("targetStateName",String.class),
                ParameterSignaturType.of("transitionName",String.class),
                ParameterSignaturVararg.of("parameterSignaturs", ParameterSignatur[].class))

            .addTransition("Zustand 3", "Zustand 3", "addEmission",
                ParameterSignaturType.of("srcStateName",String.class),
                ParameterSignaturType.of("emissionName", String.class),
                ParameterSignaturType.of("returnType", ParameterSignatur.class))
            .addTransition("Zustand 3", "Zustand 3", "addEmission",
                ParameterSignaturType.of("srcStateName",String.class),
                ParameterSignaturType.of("emissionName", String.class),
                ParameterSignaturType.of("returnType", ParameterSignatur.class),
                ParameterSignaturVararg.of("parameterSignaturs", ParameterSignatur[].class))


            .addEmission("Zustand 3", "build", ParameterSignaturType.of(Statechart.class))
            .addEmission("Zustand 3", "build", ParameterSignaturTypeVariable.of("A"),
                ParameterSignaturParameterizedType.of("accessorClass", Class.class, ParameterSignaturTypeVariable.of("A")))
            .build(StatechartAccessor.class);

        assertEquals(3, statechart.states().count());

        final PrintWriterFactory writerFactory = new PrintWriterFactory("src/site/plantuml/generated-diagrams/", "StatechartStatechart", statechart.getPLANTUML_ENDUNG());
        statechart.saveAsAdoc(writerFactory);

        final AbstractFluentBuilderGenerator generator = new AbstractFluentBuilderGenerator(statechart);
        generator.generate("src/main/java/");
    }

}
