package aware.database.util;

import aware.database.util.entity.DatabaseType;
import aware.database.util.settings.DataSourceSettings;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static liquibase.database.DatabaseFactory.getInstance;

public class TestcontainersDataSource {
    private static ConcurrentHashMap<DatabaseType, DataSource> datasourceByDatabaseType = new ConcurrentHashMap<>();

    public static DataSource getOrBuildDataSource(DataSourceSettings settings) {
        return datasourceByDatabaseType.computeIfAbsent(
                settings.getDatabaseType(),
                databaseType -> getDataSourceAndRunLiquibase(settings)
        );
    }

    private static DataSource getDataSourceAndRunLiquibase(DataSourceSettings settings) {
        DataSource dataSource = buildDataSource(settings);

        runLiquibase(dataSource, settings.getLiquibaseChangeLogPaths());

        return dataSource;
    }

    private static DataSource buildDataSource(DataSourceSettings settings) {
        if (DatabaseType.POSTGRES.equals(settings.getDatabaseType())) {
            PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>();
            postgreSQLContainer.start();

            return getPostgreSQLDataSource(postgreSQLContainer);
        }

        throw new NotImplementedException();
    }

    private static DataSource getPostgreSQLDataSource(PostgreSQLContainer postgreSQLContainer) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setUser(postgreSQLContainer.getUsername());
        dataSource.setPassword(postgreSQLContainer.getPassword());
        dataSource.setUrl(postgreSQLContainer.getJdbcUrl());

        return dataSource;
    }

    private static void runLiquibase(DataSource dataSource, List<String> liquibaseChangeLogPaths) {
        try (Connection c = dataSource.getConnection()) {
            Database database = getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));

            liquibaseChangeLogPaths.forEach(path -> runLiquibase(database, path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void runLiquibase(Database database, String path) {
        try {
            Liquibase liquibase = new Liquibase(path, new FileSystemResourceAccessor(), database);

            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
