package aware.database.util.settings

import aware.database.util.entity.DatabaseType

class DataSourceSettings(val liquibaseChangeLogPaths: List<String>, databaseType: DatabaseType) {
    private val databaseType: DatabaseType

    fun getDatabaseType(): DatabaseType {
        return databaseType
    }

    init {
        this.databaseType = databaseType
    }
}