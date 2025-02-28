/*
 *  Copyright (c) 2020 - 2022 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 */

package org.eclipse.edc.spi.system;

import org.eclipse.edc.runtime.metamodel.annotation.Setting;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.telemetry.Telemetry;
import org.eclipse.edc.spi.types.TypeManager;

import java.time.Clock;

/**
 * Context provided to extensions when they are initialized.
 */
public interface ServiceExtensionContext extends SettingResolver {

    @Setting(value = "Configures the participant id this runtime is operating on behalf of")
    String PARTICIPANT_ID = "edc.participant.id";

    String ANONYMOUS_PARTICIPANT = "anonymous";

    /**
     * Freeze the context. It should mark the ServiceExtensionContext as read-only, preventing the registration
     * of new services after the initialization phase
     */
    default void freeze() {

    }

    /**
     * Returns the id of the participant this runtime operates on behalf of. If not configured {@link #ANONYMOUS_PARTICIPANT} is used.
     *
     * @return the participant id.
     */
    String getParticipantId();

    /**
     * Fetches the unique ID of the connector. If the {@code dataspaceconnector.connector.name} config value has been set, that value is returned; otherwise  a random
     * name is chosen.
     */
    String getConnectorId();

    /**
     * Returns the system monitor.
     */
    default Monitor getMonitor() {
        return getService(Monitor.class);
    }

    /**
     * Returns the system telemetry object.
     *
     * @deprecated please, @Inject the Telemetry service instead of using this method, that will be removed in the next releases.
     */
    @Deprecated(since = "milestone8")
    default Telemetry getTelemetry() {
        return getService(Telemetry.class);
    }

    /**
     * Returns the type manager.
     *
     * @deprecated please, @Inject the TypeManager service instead of using this method, that will be removed in the next releases.
     */
    @Deprecated(since = "milestone8")
    default TypeManager getTypeManager() {
        return getService(TypeManager.class);
    }

    /**
     * Returns the {@link Clock} to retrieve the current time, which can be mocked in unit tests.
     *
     * @deprecated please, @Inject the Clock service instead of using this method, that will be removed in the next releases.
     */
    @Deprecated(since = "milestone8")
    default Clock getClock() {
        return getService(Clock.class);
    }

    /**
     * Returns true if the service type is registered.
     */
    <T> boolean hasService(Class<T> type);

    /**
     * Returns a system service.
     */
    <T> T getService(Class<T> type);

    /**
     * Returns a system service, but does not throw an exception if not found.
     *
     * @return null if not found
     */
    default <T> T getService(Class<T> type, boolean isOptional) {
        return getService(type);
    }

    /**
     * Registers a service
     */
    default <T> void registerService(Class<T> type, T service) {
    }

    /**
     * Initializes the service context. This should be used to perform tasks like service registrations, etc.
     */
    void initialize();
}
