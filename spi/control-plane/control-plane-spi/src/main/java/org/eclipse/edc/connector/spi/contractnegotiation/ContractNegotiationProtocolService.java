/*
 *  Copyright (c) 2022 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
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

package org.eclipse.edc.connector.spi.contractnegotiation;

import org.eclipse.edc.connector.contract.spi.types.agreement.ContractAgreementMessage;
import org.eclipse.edc.connector.contract.spi.types.agreement.ContractAgreementVerificationMessage;
import org.eclipse.edc.connector.contract.spi.types.agreement.ContractNegotiationEventMessage;
import org.eclipse.edc.connector.contract.spi.types.negotiation.ContractNegotiation;
import org.eclipse.edc.connector.contract.spi.types.negotiation.ContractNegotiationTerminationMessage;
import org.eclipse.edc.connector.contract.spi.types.negotiation.ContractRequestMessage;
import org.eclipse.edc.service.spi.result.ServiceResult;
import org.eclipse.edc.spi.iam.ClaimToken;
import org.jetbrains.annotations.NotNull;

/**
 * Mediates access to and modification of {@link ContractNegotiation}s on protocol messages reception.
 */
public interface ContractNegotiationProtocolService {

    /**
     * Notifies the ContractNegotiation that it has been requested by the consumer.
     * Only callable on provider ContractNegotiation.
     *
     * @param message the incoming message
     * @param claimToken the counter-party claim token
     * @return a succeeded result if the operation was successful, a failed one otherwise
     */
    @NotNull
    ServiceResult<ContractNegotiation> notifyRequested(ContractRequestMessage message, ClaimToken claimToken);

    /**
     * Notifies the ContractNegotiation that it has been offered by the provider.
     * Only callable on consumer ContractNegotiation.
     *
     * @param message the incoming message
     * @param claimToken the counter-party claim token
     * @return a succeeded result if the operation was successful, a failed one otherwise
     */
    @NotNull
    ServiceResult<ContractNegotiation> notifyOffered(ContractRequestMessage message, ClaimToken claimToken);

    /**
     * Notifies the ContractNegotiation that it has been agreed by the accepted.
     * Only callable on provider ContractNegotiation.
     *
     * @param message the incoming message
     * @param claimToken the counter-party claim token
     * @return a succeeded result if the operation was successful, a failed one otherwise
     */
    @NotNull
    ServiceResult<ContractNegotiation> notifyAccepted(ContractNegotiationEventMessage message, ClaimToken claimToken);

    /**
     * Notifies the ContractNegotiation that it has been agreed by the provider.
     * Only callable on consumer ContractNegotiation.
     *
     * @param message the incoming message
     * @param claimToken the counter-party claim token
     * @return a succeeded result if the operation was successful, a failed one otherwise
     */
    @NotNull
    ServiceResult<ContractNegotiation> notifyAgreed(ContractAgreementMessage message, ClaimToken claimToken);

    /**
     * Notifies the ContractNegotiation that it has been verified by the consumer.
     * Only callable on provider ContractNegotiation.
     *
     * @param message the incoming message
     * @param claimToken the counter-party claim token
     * @return a succeeded result if the operation was successful, a failed one otherwise
     */
    @NotNull
    ServiceResult<ContractNegotiation> notifyVerified(ContractAgreementVerificationMessage message, ClaimToken claimToken);

    /**
     * Notifies the ContractNegotiation that it has been finalized by the provider.
     * Only callable on consumer ContractNegotiation.
     *
     * @param message the incoming message
     * @param claimToken the counter-party claim token
     * @return a succeeded result if the operation was successful, a failed one otherwise
     */
    @NotNull
    ServiceResult<ContractNegotiation> notifyFinalized(ContractNegotiationEventMessage message, ClaimToken claimToken);

    /**
     * Notifies the ContractNegotiation that it has been terminated by the counter-part.
     *
     * @param message the incoming message
     * @param claimToken the counter-party claim token
     * @return a succeeded result if the operation was successful, a failed one otherwise
     */
    @NotNull
    ServiceResult<ContractNegotiation> notifyTerminated(ContractNegotiationTerminationMessage message, ClaimToken claimToken);
}
