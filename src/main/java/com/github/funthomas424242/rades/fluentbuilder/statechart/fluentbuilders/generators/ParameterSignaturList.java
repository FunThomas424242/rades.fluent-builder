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

import com.github.funthomas424242.rades.annotations.accessors.RadesAddAccessor;
import com.github.funthomas424242.rades.annotations.builder.RadesAddBuilder;
import com.github.funthomas424242.rades.annotations.builder.RadesNoBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RadesAddBuilder
@RadesAddAccessor
public class ParameterSignaturList {

    @NotNull
    @RadesNoBuilder
    protected List<ParameterSignatur> parameterList = new ArrayList<>();


    public void addParameterSignatur(final ParameterSignatur parameterSignatur) {
        this.parameterList.add(parameterSignatur);
    }

    public void addTypes(final Class... parameterTyp) {
        Arrays.stream(parameterTyp)
            .map(clazz -> new ParameterSignaturClassBuilder().withTyp(clazz).build())
            .forEach(signatur -> parameterList.add(signatur));
    }

    public static ParameterSignaturList of() {
        return new ParameterSignaturListBuilder().build();
    }

    public static ParameterSignaturList of(final ParameterSignatur... parameterSignaturs) {
        final ParameterSignaturListAccessor parameterSignaturListAccessor =
            new ParameterSignaturListBuilder().build(ParameterSignaturListAccessor.class);
        Arrays.stream(parameterSignaturs)
            .forEach(signatur -> parameterSignaturListAccessor.addParameterSignatur(signatur));
        return parameterSignaturListAccessor.toParameterSignaturList();
    }

    public static ParameterSignaturList of(final Class... parameterTyp) {
        final ParameterSignaturListAccessor parameterSignaturListAccessor =
            new ParameterSignaturListBuilder().build(ParameterSignaturListAccessor.class);
        Arrays.stream(parameterTyp)
            .map(clazz -> new ParameterSignaturClassBuilder().withTyp(clazz).build())
            .forEach(signatur -> parameterSignaturListAccessor.addParameterSignatur(signatur));
        return parameterSignaturListAccessor.toParameterSignaturList();
    }

}
