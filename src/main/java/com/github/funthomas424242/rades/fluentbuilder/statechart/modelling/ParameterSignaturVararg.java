package com.github.funthomas424242.rades.fluentbuilder.statechart.modelling;

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
import com.squareup.javapoet.TypeName;

import javax.validation.constraints.NotNull;

@RadesAddBuilder
@RadesAddAccessor
public class ParameterSignaturVararg implements ParameterSignatur {

    @RadesNoAccessor
    protected String parameterName;

    @RadesNoAccessor
    @NotNull
    protected Class varargTyp;

    public static ParameterSignatur of(final Class parameterTyp) {
        return of(null, parameterTyp);
    }

    public static ParameterSignatur of(final String parameterName, final Class parameterTyp) {
        return new ParameterSignaturVarargBuilder()
            .withParameterName(parameterName)
            .withVarargTyp(parameterTyp)
            .build();
    }


    @Override
    public Parameterart getParameterart() {
        return Parameterart.VARARG;
    }

    @Override
    public String getParameterName() {
        return parameterName;
    }

    @Override
    public TypeName getParameterTypAsTypeName() {
        return TypeName.get(varargTyp);
    }

    @Override
    public boolean isVarargTyp() {
        return true;
    }
}
