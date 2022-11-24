package com.instagram;

import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import java.io.Closeable;
import java.sql.*;

public class InstDAO implements Closeable {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "723dee56";

    private final Connection connection;

    private static final DbSpec spec = new DbSpec();
    private static final DbSchema schema = spec.addDefaultSchema();

    public InstDAO() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean dropTables() {
        String dropTablesSql = SqlUtil.readSqlFile("sql/drop_tables.sql");
        return executeSqlWithNoResultSet(dropTablesSql);
    }

    public boolean fillData() {
        String fillDataSql = SqlUtil.readSqlFile("sql/fill_data.sql");
        return executeSqlWithNoResultSet(fillDataSql);
    }

    public boolean createTables() {
        String createTablesSql = SqlUtil.readSqlFile("sql/create_tables.sql");
        return executeSqlWithNoResultSet(createTablesSql);
    }

    public boolean addNewUser(String user, String password) {
        DbTable dbTable = schema.addTable("\"user\"");
        String sql = new InsertQuery(dbTable)
                .addColumn(dbTable.addColumn("user"), user)
                .addColumn(dbTable.addColumn("password"), password)
                .toString();

        return executeSqlWithNoResultSet(sql);
    }

    public boolean addNewPost(String text, Integer userId) {
        DbTable dbTable = schema.addTable("\"post\"");
        String sql = new InsertQuery(dbTable)
                .addColumn(dbTable.addColumn("text"), text)
                .addColumn(dbTable.addColumn("user_id"), userId)
                .toString();
        return executeSqlWithNoResultSet(sql);
    }

    public boolean addNewComment(String text, Integer userId, Integer postId) {
        DbTable dbTable = schema.addTable("\"comment\"");
        String createUserSql = new InsertQuery(dbTable)
                .addColumn(dbTable.addColumn("text"), text)
                .addColumn(dbTable.addColumn("user_id"), userId)
                .addColumn(dbTable.addColumn("post_id"), postId)
                .toString();

        return executeSqlWithNoResultSet(createUserSql);
    }

    public boolean addNewLike(Integer postId, Integer commentId, Integer userId) {
        DbTable dbTable = schema.addTable("\"like\"");
        String createUserSql = new InsertQuery(dbTable)
                .addColumn(dbTable.addColumn("post_id"), postId)
                .addColumn(dbTable.addColumn("comment_id"), commentId)
                .addColumn(dbTable.addColumn("user_id"), userId)
                .toString();

        return executeSqlWithNoResultSet(createUserSql);
    }

    public String statRequest() {
        String statRequestSql = SqlUtil.readSqlFile("sql/stat_request.sql");
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(statRequestSql);
            resultSet.next();
            return SqlUtil.getResultToString(resultSet, 1, 5);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserInfo(int id) {
        String userSql = String.format(SqlUtil.readSqlFile("sql/user_Request.sql", "\n"), id);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(userSql);
            if (resultSet.next()) {
                return SqlUtil.getResultToString(resultSet, 1, 5);

            } else {
                return "Пользователь не найден!";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private boolean executeSqlWithNoResultSet(String sql) {
        try (Statement statement = connection.createStatement()) {
            return statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
