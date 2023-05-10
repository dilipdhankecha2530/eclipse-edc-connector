plugins {
    `java-library`
    id("application")
    id("com.github.johnrengelman.shadow") version "8.0.0"
}

dependencies {
    //Postgresql
    implementation("org.postgresql:postgresql:42.6.0")

    //SQL
    implementation(project(":extensions:common:sql:sql-core"))
    implementation(project(":extensions:control-plane:store:sql:control-plane-sql"))
    implementation(project(":extensions:common:sql:sql-pool:sql-pool-apache-commons"))

    //DataSource registry --> (resolve :: java.lang.IllegalStateException: Object has already been returned to this pool or is invalid)
    implementation(project(":extensions:common:transaction:transaction-local"))

    //Management
    implementation(project(":extensions:control-plane:api:management-api"))

    //IDS
    implementation(project(":data-protocols:ids"))
    implementation(project(":core:control-plane:contract-core"))
    implementation(project(":core:control-plane:transfer-core"))
    implementation(project(":core:control-plane:control-plane-aggregate-services"))

    //Vault
    implementation(project(":extensions:common:vault:vault-hashicorp"))
    implementation(project(":core:common:junit"))

    //Identity Oauth2
    //implementation(project(":extensions:common:iam:iam-mock"))
    //implementation(project(":extensions:common:iam:oauth2:oauth2-core"))
    implementation(project(":core:control-plane:catalog-core"))
    implementation(project(":extensions:common:iam:decentralized-identity:identity-did-service"))
    implementation(project(":sample:credential-verifier"))
    implementation(project(":extensions:common:iam:decentralized-identity:identity-did-core"))
    implementation(project(":extensions:common:iam:decentralized-identity:identity-did-web"))

    //FileSystem Configuration --> -Dedc.fs.config=config.properties
    implementation(project(":extensions:common:configuration:configuration-filesystem"))

    //Health Checks
    implementation(project(":extensions:common:api:api-observability"))
    implementation(project(":core:common:connector-core"))

    //Base Requirement
    implementation(project(":core:common:boot"))

    //Metrics --> thread pool size and execution timings,Jersey framework and Jettyserver
    implementation(project(":extensions:common:metrics:micrometer-core"))
    implementation(project(":extensions:common:http:jersey-micrometer"))
    implementation(project(":extensions:common:http:jetty-micrometer"))

    //Logger --> -Djava.util.logging.config.file=logging.properties
    implementation(project(":extensions:common:monitor:monitor-jdk-logger"))

    // =========================== data-plane ===========================

    //Data Transfer Protocol --> Support HTTPData type,S3,GCP,Azure supported
    implementation(project(":extensions:data-plane:data-plane-http"))
    implementation(project(":extensions:data-plane:data-plane-api"))

    //Pipeline service
    implementation(project(":core:data-plane:data-plane-framework"))

    //SQL
//    implementation(project(":extensions:data-plane:store:sql:data-plane-store-sql"))
    implementation(project(":extensions:data-plane-selector:store:sql:data-plane-instance-store-sql"))

    //Register Dataflow controller with selector
    implementation(project(":extensions:control-plane:transfer:transfer-data-plane"))
    implementation(project(":extensions:data-plane-selector:data-plane-selector-client"))
    implementation(project(":core:data-plane-selector:data-plane-selector-core"))
    implementation(project(":extensions:data-plane-selector:data-plane-selector-api"))

    //Register Receiver
    //implementation(project(":extensions:control-plane:transfer:transfer-pull-http-receiver"))
    implementation(project(":extensions:control-plane:transfer:transfer-pull-http-dynamic-receiver"))
}

application {
    mainClass.set("org.eclipse.edc.boot.system.runtime.BaseRuntime")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    exclude("**/pom.properties", "**/pom.xm")
    mergeServiceFiles()
    archiveFileName.set("consumer.jar")
}