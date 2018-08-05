package com.github.funthomas424242.rades.fluentbuilder.statechart.generators;

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
import com.github.funthomas424242.rades.fluentbuilder.infrastructure.streaming.Counter;
import com.github.funthomas424242.rades.fluentbuilder.infrastructure.text.TextConverter;
import com.github.funthomas424242.rades.fluentbuilder.statechart.domain.State;
import com.github.funthomas424242.rades.fluentbuilder.statechart.domain.StateAccessor;
import com.github.funthomas424242.rades.fluentbuilder.statechart.domain.StatechartAccessor;
import com.github.funthomas424242.rades.fluentbuilder.statechart.domain.TransitionAccessor;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatur;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatur.Parameterart;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignaturTypeBuilder;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignaturs;
import com.github.funthomas424242.rades.fluentbuilder.statechart.modelling.ParameterSignatursAccessor;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AbstractFluentBuilderGenerator {

    protected final Logger LOG = LoggerFactory.getLogger(AbstractFluentBuilderGenerator.class);

    protected final StatechartAccessor statechart;

    public AbstractFluentBuilderGenerator(final StatechartAccessor statechart) {
        this.statechart = statechart;
    }

    protected String computeJavaIdentifier(final String fullQualifiedClassName) {
        final int lastDot = fullQualifiedClassName.lastIndexOf('.');
        return fullQualifiedClassName.substring(lastDot + 1);
    }

    protected String computeJavaIdentifier() {
        return computeJavaIdentifier(statechart.getId());
    }

    protected String computePackageName(final String fullQualifiedClassName) {
        final int lastDot = fullQualifiedClassName.lastIndexOf('.');
        return fullQualifiedClassName.substring(0, lastDot);
    }

    protected String computePackageName() {
        return computePackageName(statechart.getId());
    }

    protected static String packageAsPathString(final String packageName) {
        return packageName.replace('.', File.separatorChar);
    }

    public void generate(final Filer filer) {
        try {
            generate(PrintWriterFactory.createPrintWriter(filer, this.statechart.getId()));
        } catch (IOException e) {
            LOG.error("Bei der Generierung ist ein Fehler aufgetreten.", e);
        }
    }

    public void generate(final String folderPath) {
        final Path filePath = Paths.get(folderPath, packageAsPathString(this.computePackageName())
            , this.computeJavaIdentifier() + ".java");
        this.generate(new PrintWriterFactory(filePath).createPrintWriter());
    }

    protected void generate(final PrintWriter writer) {

        final String packageName = computePackageName();
        final String outerInterfaceName = computeJavaIdentifier();

        final List<TypeSpec> interfaceDefinitions = createStateInterfaces(packageName, outerInterfaceName);

        final TypeSpec.Builder outerInterfaceBuilder = TypeSpec.interfaceBuilder(outerInterfaceName)
            .addModifiers(Modifier.PUBLIC);

        final AnnotationSpec annonSpec = AnnotationSpec
            .builder(Generated.class)
            .addMember("value", "$S", this.getClass().getName())
            .addMember("date", "$S", LocalDateTime.now().toString())
            .addMember("comments", "$S", "TODO: " + packageName + "." + outerInterfaceName)
            .build();
        outerInterfaceBuilder.addAnnotation(annonSpec);

        interfaceDefinitions.forEach(outerInterfaceBuilder::addType);
        final TypeSpec outerInterface = outerInterfaceBuilder.build();

        final JavaFile javaFile = JavaFile.builder(packageName, outerInterface)
            .skipJavaLangImports(true)
            .build();
        try {
            javaFile.writeTo(writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected List<TypeSpec> createStateInterfaces(final String packageName, final String outerInterfaceName) {
        final List<TypeSpec> interfaceDefinitions = new ArrayList<>();

        this.statechart.states().map(StateAccessor::new).forEach(state -> {
            // add transitions as methods
            final TypeSpec.Builder stateInterfaceBuilder = computeInterfaceTypeSpec(state.getStateName());
            state.transitions().map(TransitionAccessor::new).forEach(transition -> {
                final String methodName = transition.getTransitionName();
                final State targetState = transition.getTargetState();
                final Set<TypeVariableName> typeVariableNames = new HashSet<>();
                final MethodSpec method;
                if (targetState == null) {
                    // Emission
                    final ParameterSignatur returnTyp = transition.getReturnType();
                    method = getMethodSpec(typeVariableNames, methodName, returnTyp, transition.getParameterSignatur());
                } else {
                    // Transition
                    final String targetStateName = new TextConverter(new StateAccessor(transition.getTargetState()).getStateName()).convertToClassifierName();
                    final ClassName returnClassName = ClassName.get(packageName, outerInterfaceName, targetStateName);
                    final ParameterSignatur returnTyp = new ParameterSignaturTypeBuilder().withTyp(returnClassName).build();
                    method = getMethodSpec(typeVariableNames, methodName, returnTyp, transition.getParameterSignatur());
                }
                stateInterfaceBuilder.addMethod(method);
            });

            interfaceDefinitions.add(stateInterfaceBuilder.build());
        });

        final TypeSpec.Builder stateInterfaceBuilder = computeInterfaceTypeSpec("All States");
        interfaceDefinitions.forEach(typeSpec -> {
            final ClassName superInterface = ClassName.get(packageName, outerInterfaceName, typeSpec.name);
            stateInterfaceBuilder.addSuperinterface(superInterface);
        });
        interfaceDefinitions.add(stateInterfaceBuilder.build());


        return interfaceDefinitions;
    }

    protected MethodSpec getMethodSpec(final Set<TypeVariableName> typeVariableNames, final String methodName, ParameterSignatur returnTyp, final ParameterSignaturs parameterSignaturs) {
        final TypeName returnTypeName = returnTyp.getParameterTypAsTypeName();

        final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        methodBuilder.returns(returnTypeName);
        if (returnTyp.getParameterart().equals(Parameterart.TYPEVAR) && !typeVariableNames.contains(returnTypeName)) {
            methodBuilder.addTypeVariable((TypeVariableName) returnTypeName);
            typeVariableNames.add((TypeVariableName) returnTypeName);
        }
        addParameters(typeVariableNames, parameterSignaturs, methodBuilder);
        return methodBuilder.build();
    }

    protected void addParameters(final Set<TypeVariableName> typeVariableNames, final ParameterSignaturs parameterSignaturs, final MethodSpec.Builder methodBuilder) {
        final Counter count = new Counter();
        new ParameterSignatursAccessor(parameterSignaturs).getParameterList().forEach(
            signatur -> {
                if (signatur.isVarargTyp()) {
                    methodBuilder.varargs();
                }
                if (signatur.getParameterart().equals(Parameterart.TYPEVAR)) {
                    final TypeVariableName typeVariableName = (TypeVariableName) signatur.getParameterTypAsTypeName();
                    if (!typeVariableNames.contains(typeVariableName)) {
                        methodBuilder.addTypeVariable(typeVariableName);
                        typeVariableNames.add(typeVariableName);
                    }
                }
                methodBuilder.addParameter(signatur.getParameterTypAsTypeName(), computeParameterName(signatur.getParameterName(), count), Modifier.FINAL);
            }
        );
    }

    protected String computeParameterName(final String parameterName, final Counter counter) {
        if (parameterName != null) {
            return parameterName;
        } else {
            return "p" + counter.value++;
        }
    }

    protected TypeSpec.Builder computeInterfaceTypeSpec(final String stateName) {
        return TypeSpec.interfaceBuilder(new TextConverter(stateName).convertToClassifierName())
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
    }


}
