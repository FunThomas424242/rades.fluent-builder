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

import com.github.funthomas424242.rades.annotations.accessors.RadesAddAccessor;
import com.github.funthomas424242.rades.annotations.accessors.RadesNoAccessor;
import com.github.funthomas424242.rades.annotations.builder.RadesAddBuilder;
import com.github.funthomas424242.rades.annotations.builder.RadesNoBuilder;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.stream.Stream;

@RadesAddBuilder
@RadesAddAccessor
public class Statechart {

    @RadesNoBuilder
    @RadesNoAccessor
    @NotNull
    protected final HashMap<String, State> states = new HashMap<>();

    // full qualified class name
    protected String id;

    protected State startState;

    public Stream<State> states() {
        return this.states.values().stream();
    }

    public void addState(final String stateName, final State state) {
        states.put(stateName, state);
    }

    public State getState(final String stateName) {
        return states.get(stateName);
    }

    public void generate(final Filer filer) {
        try {
            generate(new PrintWriter(filer.createSourceFile(id).openWriter()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generate(final PrintWriter writer) {

        final String fullQualifiedClassName = this.id;
        final String packageName = computePackage(fullQualifiedClassName);
        final String className = computeClassName(fullQualifiedClassName);


        MethodSpec main = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args")
            .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
            .build();

        TypeSpec abstractClass = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main)
            .build();

        JavaFile javaFile = JavaFile.builder(packageName, abstractClass).build();

        try {
            javaFile.writeTo(writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  String computeClassName(final String fullQualifiedClassName) {
        final int lastDot = fullQualifiedClassName.lastIndexOf('.');
        return fullQualifiedClassName.substring(lastDot + 1);
    }

    public static  String computePackage(final String fullQualifiedClassName) {
        final int lastDot = fullQualifiedClassName.lastIndexOf('.');
        return fullQualifiedClassName.substring(0, lastDot);
    }

    public static String packageAsPathString(final String packageName){
        return packageName.replace('.', File.separatorChar);
    }
}
