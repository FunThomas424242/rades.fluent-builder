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

import com.github.funthomas424242.rades.fluentbuilder.lib.streaming.Counter;
import com.github.funthomas424242.rades.fluentbuilder.statechart.State;
import com.github.funthomas424242.rades.fluentbuilder.statechart.StateAccessor;
import com.github.funthomas424242.rades.fluentbuilder.statechart.StatechartAccessor;
import com.github.funthomas424242.rades.fluentbuilder.statechart.TransitionAccessor;
import com.github.funthomas424242.rades.fluentbuilder.statechart.fluentbuilders.generators.CreationException;
import com.github.funthomas424242.rades.fluentbuilder.statechart.fluentbuilders.generators.ParameterSignatur;
import com.github.funthomas424242.rades.fluentbuilder.statechart.fluentbuilders.generators.ParameterSignatur.Parameterart;
import com.github.funthomas424242.rades.fluentbuilder.statechart.fluentbuilders.generators.ParameterSignaturTypeBuilder;
import com.github.funthomas424242.rades.fluentbuilder.statechart.fluentbuilders.generators.ParameterSignaturs;
import com.github.funthomas424242.rades.fluentbuilder.statechart.fluentbuilders.generators.ParameterSignatursAccessor;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AbstractFluentBuilderGenerator {

    protected final StatechartAccessor statechart;

    public AbstractFluentBuilderGenerator(final StatechartAccessor statechart) {
        this.statechart = statechart;
    }

    public String computeJavaIdentifier(final String fullQualifiedClassName) {
        final int lastDot = fullQualifiedClassName.lastIndexOf('.');
        return fullQualifiedClassName.substring(lastDot + 1);
    }

    public String computeJavaIdentifier() {
        return computeJavaIdentifier(statechart.getId());
    }

    public String computePackageName(final String fullQualifiedClassName) {
        final int lastDot = fullQualifiedClassName.lastIndexOf('.');
        return fullQualifiedClassName.substring(0, lastDot);
    }

    public String computePackageName() {
        return computePackageName(statechart.getId());
    }

    public static String packageAsPathString(final String packageName) {
        return packageName.replace('.', File.separatorChar);
    }

    public PrintWriter createPrintWriter(final String folderPath) {
        final Path filePath = Paths.get(folderPath, this.packageAsPathString(this.computePackageName())
            , this.computeJavaIdentifier() + ".java");
        filePath.getParent().toFile().mkdirs();
        try {
            filePath.toFile().createNewFile();
            final PrintWriter writer = new PrintWriter(new FileOutputStream(filePath.toFile()));
            return writer;
        } catch (Throwable ex) {
            throw new CreationException(ex);
        }
    }

    public void generate(final Filer filer) {
        try {
            generate(new PrintWriter(filer.createSourceFile(this.statechart.getId()).openWriter()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generate(final String folderPath) {
        this.generate(this.createPrintWriter(folderPath));
    }

    public String convertStringToClassifier(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("name darf nicht null sein");
        }
        final String classifierName = name.replace(' ', '_');
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, classifierName);
    }


    public void generate(final PrintWriter writer) {

        final String packageName = computePackageName();
        final String outerInterfaceName = computeJavaIdentifier();

        final List<TypeSpec> interfaceDefinitions = createStateInterfaces(packageName, outerInterfaceName);

        final TypeSpec.Builder outerInterfaceBuilder = TypeSpec.interfaceBuilder(outerInterfaceName)
            .addModifiers(Modifier.PUBLIC);
        interfaceDefinitions.forEach(typeSpec -> outerInterfaceBuilder.addType(typeSpec));
        final TypeSpec outerInterface = outerInterfaceBuilder.build();

        JavaFile javaFile = JavaFile.builder(packageName, outerInterface).build();

        try {
            javaFile.writeTo(writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected List<TypeSpec> createStateInterfaces(final String packageName, final String outerInterfaceName) {
        final List<TypeSpec> interfaceDefinitions = new ArrayList<>();

        this.statechart.states().map(state -> new StateAccessor(state)).forEach(state -> {
            // add transitions as methods
            final TypeSpec.Builder stateInterfaceBuilder = computeInterfaceTypeSpec(state.getStateName());
            state.transitions().map(transition -> new TransitionAccessor(transition)).forEach(transition -> {
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
                    final String targetStateName = convertStringToClassifier(new StateAccessor(transition.getTargetState()).getStateName());
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
        if (returnTyp.getParameterart().equals(Parameterart.TYPEVAR)) {
            if (!typeVariableNames.contains(returnTypeName)) {
                methodBuilder.addTypeVariable((TypeVariableName) returnTypeName);
                typeVariableNames.add((TypeVariableName) returnTypeName);
            }
        }
        addParameters(typeVariableNames, parameterSignaturs, methodBuilder);
        return methodBuilder.build();
    }

    protected void addParameters(final Set<TypeVariableName> typeVariableNames, final ParameterSignaturs parameterSignaturs, final MethodSpec.Builder methodBuilder) {
        final Counter count = new Counter();
        new ParameterSignatursAccessor(parameterSignaturs).getParameterList().stream().forEach(
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
        return TypeSpec.interfaceBuilder(convertStringToClassifier(stateName))
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
    }


}
