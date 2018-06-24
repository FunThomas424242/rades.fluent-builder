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

@RadesAddBuilder
@RadesAddAccessor
public class ParameterSignaturClass implements ParameterSignatur {

    protected Class typ;


    @Override
    public String getParameterName() {
        return null;
    }

    @Override
    public Class getParameterTyp() {
        return typ;
    }

    @Override
    public boolean isVarargTyp() {
        return false;
    }
}
