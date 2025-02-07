/*
 *  Copyright (c) 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package org.eclipse.edc.api.transformer;

import jakarta.json.Json;
import org.eclipse.edc.jsonld.transformer.to.JsonValueToGenericTypeTransformer;
import org.eclipse.edc.transform.spi.TransformerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.jsonld.util.JacksonJsonLd.createObjectMapper;
import static org.eclipse.edc.spi.types.domain.callback.CallbackAddress.EVENTS;
import static org.eclipse.edc.spi.types.domain.callback.CallbackAddress.IS_TRANSACTIONAL;
import static org.eclipse.edc.spi.types.domain.callback.CallbackAddress.URI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonObjectToCallbackAddressDtoTransformerTest {

    private JsonObjectToCallbackAddressDtoTransformer transformer;

    @BeforeEach
    void setUp() {
        transformer = new JsonObjectToCallbackAddressDtoTransformer();
    }

    @Test
    void transform() {
        var jobj = Json.createObjectBuilder()
                .add(IS_TRANSACTIONAL, true)
                .add(URI, "http://test.local/")
                .add(EVENTS, Json.createArrayBuilder()
                        .add("foo")
                        .add("bar")
                        .add("baz")
                        .build())
                .build();

        var contextMock = mock(TransformerContext.class);
        var genericTransformer = new JsonValueToGenericTypeTransformer(createObjectMapper());
        when(contextMock.transform(any(), eq(String.class))).thenAnswer(a -> genericTransformer.transform(a.getArgument(0), contextMock));

        var cba = transformer.transform(jobj, contextMock);

        assertThat(cba).isNotNull();
        assertThat(cba.getEvents()).containsExactlyInAnyOrder("foo", "bar", "baz");
        assertThat(cba.getUri()).isEqualTo("http://test.local/");
        assertThat(cba.isTransactional()).isTrue();
    }
}
