import com.microsoft.sqlserver.jdbc.{SQLServerCallableStatement, SQLServerDataTable}

import java.sql.{Connection, DriverManager, Types}

object Main {
  def main(args: Array[String]): Unit = {
    val url = "jdbc:sqlserver://localhost:1433;trustServerCertificate=false;encrypt=false;"
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")

    val username = "sa"
    val password = "Password1"
    val connection = DriverManager.getConnection(url, username, password)
    try {
      // Create a table
//      createTable(connection)
//      createType(connection)
      // Create a stored procedure
//      createStoredProcedure(connection)

      // Call the stored procedure
      callStoredProcedure(connection)
    } finally {
      // Close the connection when done
      connection.close()
    }

  }

  def createTable(connection: Connection): Unit = {
    val createTableSQL =
      """
        |CREATE TABLE SampleTable (
        |  id INT PRIMARY KEY,
        |  value NUMERIC(10, 2)
        |)
      """.stripMargin

    val statement = connection.createStatement()
    statement.execute(createTableSQL)
    statement.close()
    println("Table created successfully.")
  }

  def createType(connection: Connection): Unit = {
    val sql = """CREATE TYPE SampleTableType AS TABLE
                |(
                |    id INT,
                |    value NUMERIC(38, 10)
                |);
                |""".stripMargin
    val st = connection.createStatement()
    st.execute(sql)
    st.close()
  }

  def createStoredProcedure(connection: Connection): Unit = {
    val createProcedureSQL =
      """
        |CREATE PROCEDURE UpsertData
        |  @dataToUpsert AS SampleTableType READONLY
        |AS
        |BEGIN
        |  MERGE INTO SampleTable AS target
        |  USING @dataToUpsert AS source
        |  ON (target.id = source.id)
        |  WHEN MATCHED THEN
        |    UPDATE SET target.value = source.value
        |  WHEN NOT MATCHED THEN
        |    INSERT (id, value) VALUES (source.id, source.value);
        |END
      """.stripMargin

    val statement = connection.createStatement()
    statement.execute(createProcedureSQL)
    statement.close()
    println("Stored procedure created successfully.")
  }

  def callStoredProcedure(connection: Connection): Unit = {
    val callProcedureSQL = "{call UpsertData (?)}"
    val preparedStatement = connection.prepareCall(callProcedureSQL).asInstanceOf[SQLServerCallableStatement]

    val tb = new SQLServerDataTable()
    tb.addColumnMetadata("id", Types.INTEGER)
    tb.addColumnMetadata("value", Types.NUMERIC)
    tb.addRow(1, BigDecimal(0.222).setScale(40))

    preparedStatement.setStructured(1, "SampleTableType", tb)
    preparedStatement.execute()
    preparedStatement.close()
    println("Stored procedure executed successfully.")
  }

}