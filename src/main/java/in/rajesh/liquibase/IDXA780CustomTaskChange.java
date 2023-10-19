package in.rajesh.liquibase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import liquibase.Scope;
import liquibase.change.custom.CustomSqlChange;
import liquibase.change.custom.CustomTaskRollback;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.RollbackImpossibleException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.UpdateStatement;

import static in.rajesh.liquibase.LiquibaseUtils.getLong;

public class IDXA780CustomTaskChange implements CustomSqlChange, CustomTaskRollback {

    @Override
    public SqlStatement[] generateStatements(Database database) throws CustomChangeException {
        var logger = Scope.getCurrentScope()
                .getLog(this.getClass());

        List<SqlStatement> statements = new ArrayList<>();
        logger.info("Start generating uuid for order and quote");
        try {
            var jdbcConnection = (JdbcConnection) database.getConnection();
            var wrappedConnection = jdbcConnection.getWrappedConnection();

            statements.addAll(this.generateUpdateStatements(wrappedConnection, "order_"));
            statements.addAll(this.generateUpdateStatements(wrappedConnection, "quote"));
        } catch (SQLException e) {
            throw new CustomChangeException(e);
        }

        return statements.toArray(SqlStatement[]::new);
    }

    private List<UpdateStatement> generateUpdateStatements(Connection wrappedConnection, String tableName) throws SQLException {
        var result = new ArrayList<UpdateStatement>();
        var quoteStatement = wrappedConnection.prepareStatement("SELECT x.id FROM " + tableName + " x INNER JOIN tradable t ON (x.id = t.id) WHERE t.uuid IS NULL");
        try (var resultSet = quoteStatement.executeQuery()) {
            while (resultSet.next()) {
                result.add(this.generateUpdateStatement(resultSet));
            }
        }

        return result;
    }

    private UpdateStatement generateUpdateStatement(ResultSet resultSet) throws SQLException {
        var id = getLong(resultSet, "id");
        var uuid = UUID.randomUUID();

        var updateStatement = new UpdateStatement(null, null, "tradable");
        updateStatement.addNewColumnValue("uuid", String.valueOf(uuid));
        updateStatement.setWhereClause("id=" + id);

        return updateStatement;
    }

    @Override
    public void rollback(Database database) throws CustomChangeException, RollbackImpossibleException {
        throw new RollbackImpossibleException();
    }

    @Override
    public String getConfirmationMessage() {
        return "uuid generated for orders and quotes";
    }

    @Override
    public void setUp() throws SetupException {
    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
    }

    @Override
    public ValidationErrors validate(Database database) {
        return new ValidationErrors();
    }
}