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

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

// TODO nutzen wenn issue #94 @Singular umgesetzt wurde
@RadesAddBuilder
@RadesAddAccessor
public class Statechart {

    @RadesNoBuilder
    @RadesNoAccessor
    @NotNull
    protected final Set<State> states = new HashSet<State>();

    protected String id;

    protected State startState;

    public Stream<State> states() {
        return this.states.stream();
    }


}
