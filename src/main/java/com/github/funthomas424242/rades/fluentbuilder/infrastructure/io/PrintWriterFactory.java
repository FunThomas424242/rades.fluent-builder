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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Filer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PrintWriterFactory {

    public final Logger LOG = LoggerFactory.getLogger(PrintWriter.class);

    protected final Path adocFilePath;

    public PrintWriterFactory(final Path adocFilePath) {
        this.adocFilePath = adocFilePath;
    }

    /**
     * @param folderPath
     * @param diagramName mit Extension
     */
    public PrintWriterFactory(final String folderPath, final String diagramName) {
        this(Paths.get(folderPath, diagramName));
    }

    /**
     * @param folderPath
     * @param fileExtension mit Trenner wie Punkt z.B. ".txt"
     */
    public PrintWriterFactory(final String folderPath, final String diagramName, final String fileExtension) {
        this(folderPath, diagramName + fileExtension);
    }

    public PrintWriter createPrintWriter() {
        if(!adocFilePath.getParent().toFile().mkdirs()){
          LOG.warn("Directory Struktur konnte nicht erstellt werden.");
        }
        final File adocFile = adocFilePath.toFile();
        if (adocFile.exists()) {
            adocFile.delete();
        }
        try {
            if(!adocFilePath.toFile().createNewFile()){
                LOG.warn("Datei "+adocFilePath.toFile().getAbsolutePath()+" konnte nicht angelegt werden.");
            }
            final PrintWriter writer = new PrintWriter(new FileOutputStream(adocFile), true);
            return writer;
        } catch (Throwable ex) {
            throw new CreationException(ex);
        }
    }

    public static PrintWriter createPrintWriter(final Filer filer, final String className) throws IOException {
        return new PrintWriter(filer.createSourceFile(className).openWriter());
    }

}
