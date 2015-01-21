package it.swb.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbTool {
	
	public Connection connection;
	public PreparedStatement preparedStatement;
	public Statement statement;
	public ResultSet resultSet;
	
	public DbTool(){
		super();
		this.connection = DataSource.getLocalConnection();
		this.preparedStatement = null;
		this.statement = null;
		this.resultSet = null;
	}
	
	public void commit() throws SQLException{
		this.connection.commit();
	}
	
	public void rollback() throws SQLException{
		this.connection.rollback();
	}
	
	public void close(){
		DataSource.closeConnections(this.connection, this.statement, this.preparedStatement, this.resultSet);
	}
	

	public DbTool(Connection connection, PreparedStatement preparedStatement,
			Statement statement, ResultSet resultSet) {
		super();
		this.connection = connection;
		this.preparedStatement = preparedStatement;
		this.statement = statement;
		this.resultSet = resultSet;
	}
	
	public DbTool(Connection connection, Statement statement, ResultSet resultSet) {
		super();
		this.connection = connection;
		this.statement = statement;
		this.resultSet = resultSet;
	}
	
	public DbTool(Connection connection, Statement statement) {
		super();
		this.connection = connection;
		this.statement = statement;
	}

	public DbTool(Connection connection, PreparedStatement preparedStatement,
			ResultSet resultSet) {
		super();
		this.connection = connection;
		this.preparedStatement = preparedStatement;
		this.resultSet = resultSet;
	}
	
	public DbTool(Connection connection, PreparedStatement preparedStatement) {
		super();
		this.connection = connection;
		this.preparedStatement = preparedStatement;
	}

	public DbTool(Connection connection) {
		super();
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

	public void setPreparedStatement(PreparedStatement preparedStatement) {
		this.preparedStatement = preparedStatement;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	
	

}
