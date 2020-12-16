# MSA SQL

Microservices Architecture SQL

![build](https://github.com/zero88/msa-sql/workflows/build/badge.svg?branch=master)
![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/zero88/msa-sql?sort=semver)
![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/io.github.zero88/msa-sql?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.zero88/msa-sql?server=https%3A%2F%2Foss.sonatype.org%2F)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=zero88_msa-sql&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=zero88_msa-sql)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=zero88_msa-sql&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=zero88_msa-sql)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=zero88_msa-sql&metric=security_rating)](https://sonarcloud.io/dashboard?id=zero88_msa-sql)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=zero88_msa-sql&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=zero88_msa-sql)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=zero88_msa-sql&metric=coverage)](https://sonarcloud.io/dashboard?id=zero88_msa-sql)


## Overview

TBD

## How To

- Prepare sql ddl files and put it into `src/main/resources`. The tips if many sql ddl files, name it with prefix `01_`, `02_` with appropriate order
- Adapt build script `build.gradle` similar as:

```gradle

import org.jooq.meta.jaxb.ForcedType

import com.nubeiot.buildscript.jooq.DB
import com.nubeiot.buildscript.jooq.JooqGenerateTask
import com.nubeiot.buildscript.jooq.JooqGenerateTask.JsonDataType
import com.nubeiot.buildscript.jooq.Utils

dependencies {
    compile project(':core:sql')
    compile project.deps.database.h2
}

task jooqGen(type: JooqGenerateTask) {
    packageName = "com.nubeiot.edge.core.model"
    enumTypes = project(':core:sql').ext.enumTypes
    dbTypes = project(':core:sql').ext.dbTypes + [
        new ForcedType(userType: "com.nubeiot.edge.core.loader.ModuleType", types: DB.TYPES.varchar,
                       expression: Utils.toRegexIgnoreCase("service_type"),
                       converter: "com.nubeiot.edge.core.model.converter.ModuleTypeConverter")
    ]
    javaTypes = project(':core:sql').ext.javaTypes + [
        new JsonDataType(className: "com.nubeiot.edge.core.loader.ModuleType",
                         converter: "%s.name()",
                         parser: "com.nubeiot.edge.core.loader.ModuleTypeFactory.factory((String)%s)",
                         defVal: "com.nubeiot.edge.core.loader.ModuleTypeFactory.getDefault()")
    ]
}
```

- Run `gradle jooq`
- Generated class should be in `${project_folder}/generated/main/java/${your_define_package}`

## Current limitations

### PostgreSQL

- Timestamp with timezone (`timestamptz`) data type doesn't work.
- Function `CURRENT_TIMESTAMP` doesn't work.
