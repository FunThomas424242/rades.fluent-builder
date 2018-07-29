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

import com.github.funthomas424242.rades.annotations.accessors.RadesAddAccessor;
import com.github.funthomas424242.rades.annotations.accessors.RadesNoAccessor;
import com.github.funthomas424242.rades.annotations.builder.RadesAddBuilder;
import com.github.funthomas424242.rades.annotations.builder.RadesNoBuilder;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

@RadesAddBuilder
@RadesAddAccessor
public class Statechart {

    @RadesNoBuilder
    @RadesNoAccessor
    @NotNull
    protected final HashMap<String, State> states = new HashMap<>();

    // full qualified class name for generation
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

    public PrintWriter createPrintWriter(final Path adocFilePath) {
        adocFilePath.getParent().toFile().mkdirs();
        final File adocFile =  adocFilePath.toFile();
        if(adocFile.exists()){
            adocFile.delete();
        }
        try {
            adocFilePath.toFile().createNewFile();
            final PrintWriter writer = new PrintWriter(new FileOutputStream(adocFile));
            return writer;
        } catch (Throwable ex) {
            throw new CreationException(ex);
        }
    }

    // TODO Accessor wird nicht korrekt erzeugt
    public PrintWriter createPrintWriter(final String folderPath, final String diagramName) {
        final Path filePath = Paths.get(folderPath, diagramName + ".adoc");
        return createPrintWriter(filePath);
    }

    /**
     *
     * @param folderPath
     * @param diagramName ohne Extension (wird automatisch um .adoc erweitert)
     */
    public void saveAsAdoc(final String folderPath, final String  diagramName){
        saveAsAdoc(createPrintWriter(folderPath,diagramName));
    }

    public void saveAsAdoc(final Path adocFilePath){
        saveAsAdoc(createPrintWriter(adocFilePath));
    }

    public void saveAsAdoc(final PrintWriter adocFileWriter){

        adocFileWriter.println("@startuml");
        states.values().forEach(state -> {
            state.transitions.stream().forEach(
                transition -> {
                    adocFileWriter.println(transition.startState.stateName +" -->" + transition.targetState.stateName + " : "+transition.transitionName);
                }
            );
        });
        adocFileWriter.println("@enduml");
    }
}
