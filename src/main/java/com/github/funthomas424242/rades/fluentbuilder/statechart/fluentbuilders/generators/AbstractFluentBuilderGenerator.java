package com.github.funthomas424242.rades.fluentbuilder.statechart.fluentbuilders.generators;

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
import com.github.funthomas424242.rades.fluentbuilder.statechart.ParameterSignaturAccessor;
import com.github.funthomas424242.rades.fluentbuilder.statechart.State;
import com.github.funthomas424242.rades.fluentbuilder.statechart.StateAccessor;
import com.github.funthomas424242.rades.fluentbuilder.statechart.StatechartAccessor;
import com.github.funthomas424242.rades.fluentbuilder.statechart.TransitionAccessor;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AbstractFluentBuilderGenerator {

    protected final StatechartAccessor statechart;

    public AbstractFluentBuilderGenerator(final StatechartAccessor statechart) {
        this.statechart = statechart;
    }

    public String computeClassName(final String fullQualifiedClassName) {
        final int lastDot = fullQualifiedClassName.lastIndexOf('.');
        return fullQualifiedClassName.substring(lastDot + 1);
    }

    public String computeClassName() {
        return computeClassName(statechart.getId());
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
            , this.computeClassName() + ".java");
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
        final String className = computeClassName();

        final List<TypeSpec> interfaceDefinitions = new ArrayList<>();

        this.statechart.states().map(state -> new StateAccessor(state)).forEach(state -> {
            // add transitions as methods
            final TypeSpec.Builder stateInterface = computeInterfaceTypeSpec(state);
            state.transitions().map(transition -> new TransitionAccessor(transition)).forEach(transition -> {
                final String methodName = transition.getTransitionName();
                final State targetState = transition.getTargetState();
                final MethodSpec method;
                if (targetState == null) {
                    // Emission
                    final String returnType = transition.getReturnType();
                    final ClassName returnTyp = ClassName.get(computePackageName(returnType), computeClassName(returnType));
                    method = MethodSpec.methodBuilder(methodName)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(returnTyp)
                        .build();
                } else {
                    // Transition
                    final String targetStateName = convertStringToClassifier(new StateAccessor(transition.getTargetState()).getStateName());
                    final ClassName returnTyp = ClassName.get(packageName, className, targetStateName);
                    final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(returnTyp);
                    final Counter count = new Counter();
                    new ParameterSignaturAccessor(transition.getParameterSignatur()).getParameterList().stream().forEach(
                        clazz -> methodBuilder.addParameter(clazz, "p" + count.value++, Modifier.FINAL)
                    );
                    method = methodBuilder.build();
                }

                stateInterface.addMethod(method);
            });
            interfaceDefinitions.add(stateInterface.build());
        });


        final TypeSpec.Builder abstractClassBuilder = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        interfaceDefinitions.forEach(typeSpec -> abstractClassBuilder.addType(typeSpec));
        final TypeSpec abstractClass = abstractClassBuilder.build();

        JavaFile javaFile = JavaFile.builder(packageName, abstractClass).build();

        try {
            javaFile.writeTo(writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected TypeSpec.Builder computeInterfaceTypeSpec(final StateAccessor state) {
        return TypeSpec.interfaceBuilder(convertStringToClassifier(state.getStateName()))
            .addModifiers(Modifier.PUBLIC);
    }


}
