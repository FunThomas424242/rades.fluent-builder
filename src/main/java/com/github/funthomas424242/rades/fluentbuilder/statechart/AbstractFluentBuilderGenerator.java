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

public class AbstractFluentBuilderGenerator {

    protected final StatechartAccessor statechart;

    public AbstractFluentBuilderGenerator(final StatechartAccessor statechart) {
        this.statechart = statechart;
    }


    public String computeClassName() {
        final int lastDot = statechart.getId().lastIndexOf('.');
        return statechart.getId().substring(lastDot + 1);
    }

    public String computePackageName() {
        final int lastDot = statechart.getId().lastIndexOf('.');
        return this.statechart.getId().substring(0, lastDot);
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
            throw new RuntimeException(ex);
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

    public void generate(final PrintWriter writer) {

        final String packageName = computePackageName();
        final String className = computeClassName();


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


}
