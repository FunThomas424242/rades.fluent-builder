package com.github.funthomas424242.rades.fluentbuilder.statechart.generated;

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

import com.github.funthomas424242.rades.fluentbuilder.statechart.domain.Statechart;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatur;

public interface AbstractStatechartFluentBuilder {

    interface Zustand1 {
        Zustand2 withQualifiedClassName(final String chartId);
    }

    interface Zustand2 {
      Zustand2 addState(final String stateName);

      Zustand3 withStartState(final String startStateName);
  }


  interface Zustand3 {

    Zustand3 addTransition(final String srcStateName, final String targetStateName, final String transitionName, final ParameterSignatur... parameterSignaturs);

    Zustand3 addEmission(final String srcStateName, final String emissionName, final ParameterSignatur returnType);
    Zustand3 addEmission(final String srcStateName, final String emissionName, final ParameterSignatur returnType, final ParameterSignatur... parameterSignaturs);

    <A> A build(final Class<A> accessorClass);

    Statechart build();

  }


  interface AllStates extends Zustand1, Zustand2, Zustand3 {
  }

}
