package com.github.funthomas424242.rades.fluentbuilder.statechart.generated;

import com.github.funthomas424242.rades.fluentbuilder.statechart.domain.Statechart;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatur;
import java.lang.Class;
import java.lang.String;
import javax.annotation.Generated;

@Generated(
    value = "com.github.funthomas424242.rades.fluentbuilder.statechart.generators.AbstractFluentBuilderGenerator",
    date = "2018-08-05T19:07:28.907",
    comments = "TODO: com.github.funthomas424242.rades.fluentbuilder.statechart.generated.AbstractStatechartFluentBuilder"
)
public interface AbstractStatechartFluentBuilder {
  interface Zustand2 {
    Zustand2 addState(final String stateName);

    Zustand3 withStartState(final String startStateName);
  }

  interface Zustand1 {
    Zustand2 withQualifiedClassName(final String chartId);
  }

  interface Zustand3 {
    Zustand3 addEmission(final String srcStateName, final String emissionName,
        final ParameterSignatur returnType, final ParameterSignatur... parameterSignaturs);

    Zustand3 addTransition(final String srcStateName, final String targetStateName,
        final String transitionName, final ParameterSignatur... parameterSignaturs);

    Statechart build();

    <A> A build(final Class<A> accessorClass);

    Zustand3 addEmission(final String srcStateName, final String emissionName,
        final ParameterSignatur returnType);
  }

  interface AllStates extends Zustand2, Zustand1, Zustand3 {
  }
}
