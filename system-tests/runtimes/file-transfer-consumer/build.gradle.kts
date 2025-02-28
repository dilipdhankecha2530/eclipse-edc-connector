/*
 *  Copyright (c) 2020, 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *       Fraunhofer Institute for Software and Systems Engineering - added dependencies
 *
 */

plugins {
    `java-library`
    id("application")
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":core:control-plane:control-plane-core"))
    implementation(project(":core:data-plane-selector:data-plane-selector-core"))
    implementation(project(":core:common:util"))
    implementation(project(":data-protocols:ids"))
    implementation(project(":extensions:control-plane:api:management-api"))
    implementation(project(":extensions:common:api:api-observability"))
    implementation(project(":extensions:common:configuration:configuration-filesystem"))
    implementation(project(":extensions:common:iam:iam-mock"))
    implementation(project(":extensions:common:json-ld"))

    api(libs.jakarta.rsApi)
}

application {
    mainClass.set("org.eclipse.edc.boot.system.runtime.BaseRuntime")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    exclude("**/pom.properties", "**/pom.xm")
    mergeServiceFiles()
    archiveFileName.set("consumer.jar")
}

edcBuild {
    publish.set(false)
}
