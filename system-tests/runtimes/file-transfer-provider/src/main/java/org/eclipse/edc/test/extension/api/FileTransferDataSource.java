/*
 *  Copyright (c) 2022 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *
 */

package org.eclipse.edc.test.extension.api;

import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSource;
import org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult;
import org.eclipse.edc.spi.EdcException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.stream.Stream;

import static org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult.success;

class FileTransferDataSource implements DataSource, DataSource.Part {

    private final File file;

    FileTransferDataSource(File file) {
        this.file = file;
    }

    @Override
    public StreamResult<Stream<Part>> openPartStream() {
        return success(Stream.of(this));
    }

    @Override
    public String name() {
        return file.getName();
    }

    @Override
    public InputStream openStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new EdcException(e);
        }
    }
}
