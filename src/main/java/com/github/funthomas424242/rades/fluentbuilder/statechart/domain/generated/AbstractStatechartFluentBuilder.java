package com.github.funthomas424242.rades.fluentbuilder.statechart.domain.generated;

import com.github.funthomas424242.rades.fluentbuilder.statechart.domain.Statechart;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatur;
import javax.annotation.Generated;

@Generated(
    value = "com.github.funthomas424242.rades.fluentbuilder.statechart.generators.AbstractFluentBuilderGenerator",
    date = "2019-02-05T22:59:39.256",
    comments = "TODO: com.github.funthomas424242.rades.fluentbuilder.statechart.domain.generated.AbstractStatechartFluentBuilder"
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

    Zustand3 addEmission(final String srcStateName, final String emissionName,
        final ParameterSignatur returnType, final ParameterSignatur... parameterSignaturs);

    Statechart build();

    <A> A build(final Class<A> accessorClass);

    Zustand3 addTransition(final String srcStateName, final String targetStateName,
        final String transitionName, final ParameterSignatur... parameterSignaturs);
  }

  interface AllStates extends Zustand2, Zustand1, Zustand3 {
  }
}
