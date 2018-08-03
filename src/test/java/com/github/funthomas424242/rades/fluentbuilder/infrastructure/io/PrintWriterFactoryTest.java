package com.github.funthomas424242.rades.fluentbuilder.infrastructure.io;

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

import com.github.funthomas424242.rades.fluentbuilder.statechart.domain.CreationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import test.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrintWriterFactoryTest {


    @Test
    @DisplayName("Erzeuge PrintWriter wenn File bereits existiert.")
    public void createWriterExistingFile(@Mock Path pathMock, @Mock File fileMock) throws IOException {

        when(pathMock.getParent()).thenReturn(pathMock);
        when(pathMock.toFile()).thenReturn(fileMock);
        when(fileMock.exists()).thenReturn(true);

        final PrintWriterFactory factory = new PrintWriterFactory(pathMock);
        assertThrows(CreationException.class, () -> factory.createPrintWriter());
        verify(fileMock, times(1)).delete();
        verify(fileMock, times(1)).createNewFile();
    }

    @Test
    @DisplayName("Erzeuge PrintWriter wenn File noch nicht existiert.")
    public void createWriterNotExistingFile(@Mock Path pathMock, @Mock File fileMock) throws IOException {

        when(pathMock.getParent()).thenReturn(pathMock);
        when(pathMock.toFile()).thenReturn(fileMock);
        when(fileMock.exists()).thenReturn(false);

        final PrintWriterFactory factory = new PrintWriterFactory(pathMock);
        assertThrows(CreationException.class, () -> factory.createPrintWriter());
        verify(fileMock, times(0)).delete();
        verify(fileMock, times(1)).createNewFile();
    }

}
