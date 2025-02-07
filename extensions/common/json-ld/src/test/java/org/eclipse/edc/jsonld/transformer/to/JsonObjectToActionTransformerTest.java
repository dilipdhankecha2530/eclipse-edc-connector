/*
 *  Copyright (c) 2023 Fraunhofer Institute for Software and Systems Engineering
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Fraunhofer Institute for Software and Systems Engineering - initial API and implementation
 *
 */

package org.eclipse.edc.jsonld.transformer.to;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import org.eclipse.edc.policy.model.AtomicConstraint;
import org.eclipse.edc.policy.model.Constraint;
import org.eclipse.edc.policy.model.LiteralExpression;
import org.eclipse.edc.policy.model.Operator;
import org.eclipse.edc.transform.spi.TransformerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.jsonld.spi.JsonLdKeywords.ID;
import static org.eclipse.edc.jsonld.spi.PropertyAndTypeNames.ODRL_ACTION_TYPE_ATTRIBUTE;
import static org.eclipse.edc.jsonld.spi.PropertyAndTypeNames.ODRL_INCLUDED_IN_ATTRIBUTE;
import static org.eclipse.edc.jsonld.spi.PropertyAndTypeNames.ODRL_REFINEMENT_ATTRIBUTE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JsonObjectToActionTransformerTest {
    
    private JsonBuilderFactory jsonFactory = Json.createBuilderFactory(Map.of());
    private TransformerContext context = mock(TransformerContext.class);
    
    private JsonObjectToActionTransformer transformer;
    
    @BeforeEach
    void setUp() {
        transformer = new JsonObjectToActionTransformer();
    }
    
    @Test
    void transform_onlyActionType_returnAction() {
        var action = jsonFactory.createObjectBuilder()
                .add(ODRL_ACTION_TYPE_ATTRIBUTE, "USE")
                .build();
    
        var result = transformer.transform(action, context);
    
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("USE");
        assertThat(result.getIncludedIn()).isNull();
        assertThat(result.getConstraint()).isNull();
        
        verifyNoInteractions(context);
    }

    @Test
    void transform_onlyId_returnAction() {
        var action = jsonFactory.createObjectBuilder()
                .add(ID, "use")
                .build();

        var result = transformer.transform(action, context);

        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("use");
        assertThat(result.getIncludedIn()).isNull();
        assertThat(result.getConstraint()).isNull();

        verifyNoInteractions(context);
    }
    
    @Test
    void transform_allAttributes_returnAction() {
        var constraint = AtomicConstraint.Builder.newInstance()
                .leftExpression(new LiteralExpression("left"))
                .operator(Operator.EQ)
                .rightExpression(new LiteralExpression("right"))
                .build();
        when(context.transform(any(JsonObject.class), eq(Constraint.class))).thenReturn(constraint);
        
        var action = jsonFactory.createObjectBuilder()
                .add(ODRL_ACTION_TYPE_ATTRIBUTE, "USE")
                .add(ODRL_INCLUDED_IN_ATTRIBUTE, "includedIn")
                .add(ODRL_REFINEMENT_ATTRIBUTE, jsonFactory.createObjectBuilder().build())
                .build();
        
        var result = transformer.transform(action, context);
        
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("USE");
        assertThat(result.getIncludedIn()).isEqualTo("includedIn");
        assertThat(result.getConstraint()).isEqualTo(constraint);
        
        verify(context, never()).reportProblem(anyString());
        verify(context, times(1)).transform(any(JsonObject.class), eq(Constraint.class));
    }
    
    @Test
    void transform_requiredAttributesMissing_reportProblem() {
        var action = jsonFactory.createObjectBuilder().build();
    
        var result = transformer.transform(action, context);
    
        assertThat(result).isNull();
        verify(context, times(1)).reportProblem(anyString());
    }
}
