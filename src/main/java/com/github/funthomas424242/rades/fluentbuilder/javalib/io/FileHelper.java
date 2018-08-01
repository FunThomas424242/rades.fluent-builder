package com.github.funthomas424242.rades.fluentbuilder.javalib.io;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {

    public static PrintWriter createPrintWriter(final Path adocFilePath) {
        adocFilePath.getParent().toFile().mkdirs();
        final File adocFile =  adocFilePath.toFile();
        if(adocFile.exists()){
            adocFile.delete();
        }
        try {
            adocFilePath.toFile().createNewFile();
            final PrintWriter writer = new PrintWriter(new FileOutputStream(adocFile),true);
            return writer;
        } catch (Throwable ex) {
            throw new CreationException(ex);
        }
    }

    public static PrintWriter createPrintWriter(final String folderPath, final String diagramName, final String fileExtension) {
        final Path filePath = Paths.get(folderPath, diagramName + fileExtension);
        return createPrintWriter(filePath);
    }

}
