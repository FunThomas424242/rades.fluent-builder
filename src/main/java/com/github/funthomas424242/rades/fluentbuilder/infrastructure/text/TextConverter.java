package com.github.funthomas424242.rades.fluentbuilder.infrastructure.text;

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

import com.google.common.base.CaseFormat;

public class TextConverter {

    protected final String text;

    public TextConverter(final String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text darf nicht null sein");
        }
        this.text = text;
    }


    public String convertToClassifierName() {
        final String classifierName = text.replace(' ', '_');
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, classifierName);
    }
}
