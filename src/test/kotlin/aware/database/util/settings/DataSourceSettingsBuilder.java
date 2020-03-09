package aware.database.util.settings;

import aware.database.util.entity.DatabaseType;

import java.util.ArrayList;
import java.util.List;

public final class DataSourceSettingsBuilder {
    private List<String> liquibaseChangeLogPaths;
    private DatabaseType databaseType;

    private DataSourceSettingsBuilder() {
        liquibaseChangeLogPaths = new ArrayList<>();
    }

    public static DataSourceSettingsBuilder aDataSourceSettings() {
        return new DataSourceSettingsBuilder();
    }

    public DataSourceSettingsBuilder addLiquibaseChangeLogPath(String liquibaseChangeLogPath) {
        this.liquibaseChangeLogPaths.add(liquibaseChangeLogPath);
        return this;
    }

    public DataSourceSettingsBuilder setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
        return this;
    }

    public DataSourceSettings build() {
        return new DataSourceSettings(liquibaseChangeLogPaths, databaseType);
    }
}
