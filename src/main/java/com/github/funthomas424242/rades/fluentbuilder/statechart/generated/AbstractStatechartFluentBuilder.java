package com.github.funthomas424242.rades.fluentbuilder.statechart.generated;

import com.github.funthomas424242.rades.fluentbuilder.statechart.domain.Statechart;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatur;
import javax.annotation.Generated;

@Generated(
    value = "com.github.funthomas424242.rades.fluentbuilder.statechart.generators.AbstractFluentBuilderGenerator",
    date = "2018-08-05T20:04:45.125",
    comments = "TODO: com.github.funthomas424242.rades.fluentbuilder.statechart.generated.AbstractStatechartFluentBuilder"
)
public interface AbstractStatechartFluentBuilder {
  interface Zustand2 {
    Zustand3 withStartState(final String startStateName);

    Zustand2 addState(final String stateName);
  }

  interface Zustand1 {
    Zustand2 withQualifiedClassName(final String chartId);
  }

  interface Zustand3 {
    Zustand3 addEmission(final String srcStateName, final String emissionName,
        final ParameterSignatur returnType);

    Zustand3 addTransition(final String srcStateName, final String targetStateName,
        final String transitionName, final ParameterSignatur... parameterSignaturs);

    Statechart build();

    Zustand3 addEmission(final String srcStateName, final String emissionName,
        final ParameterSignatur returnType, final ParameterSignatur... parameterSignaturs);

    <A> A build(final Class<A> accessorClass);
  }

  interface AllStates extends Zustand2, Zustand1, Zustand3 {
  }
}
