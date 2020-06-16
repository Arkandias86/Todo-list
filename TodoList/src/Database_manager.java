import java.io.File;
import java.sql.*;

public class Database_manager {
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public  Database_manager() {
        String url = "jdbc:sqlite:/Users/nicolashuet/Documents/TodoList/sqlite/" + "TodoDatabase.db";

        File tempFile = new File("/Users/nicolashuet/Documents/TodoList/sqlite/TodoDatabase.db");
        boolean exists = tempFile.exists();

        try {
            conn = DriverManager.getConnection(url);
            if (conn != null && !exists) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
            else {
                System.out.println("Database already created");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Exiting constructor database manager");
    }

    public void createTablesForDatabase(){
        Statement stmt = null;

            if(conn != null) {
                String sqlTableListTodo = "CREATE TABLE IF NOT EXISTS listOfTodoList (\n"
                        + "    todoId integer PRIMARY KEY,\n"
                        + "    name text NOT NULL\n"
                        + ");";
                try {
                    stmt = conn.createStatement();
                    stmt.execute(sqlTableListTodo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
    }

    public void addNewTodoTable (String todoTableName){
        try {
            if (conn != null) {
                String sqlRowListTodo = "INSERT INTO listOfTodoList (name) VALUES ('"+todoTableName+"')";

                Statement st = conn.createStatement();
                st.execute(sqlRowListTodo);

                String sqlTableTodo = "CREATE TABLE IF NOT EXISTS '" + todoTableName + "' (\n"
                        + "    id integer PRIMARY KEY,\n"
                        + "    todoKey text NOT NULL,\n"
                        + "    todoStatusValue text NOT NULL,\n"
                        +"     todoId      INTEGER NOT NULL,\n"
                        + "    FOREIGN KEY (todoId) REFERENCES listOfTodoList (todoId) ON UPDATE CASCADE ON DELETE CASCADE\n"
                        + ");";

                Statement stmt = conn.createStatement();
                stmt.execute(sqlTableTodo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTodoTable (String name) {
        Statement st = null;
        String sql = "DROP TABLE IF EXISTS "+name;

        try {
            st = conn.createStatement();
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql1 = "DELETE FROM listOfTodoList WHERE name = '"+ name + "'";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewItem (String itemName, String todoTableName, int todoID){
        String todo = "To do";
        String sqlRowListTodo = "INSERT INTO "+todoTableName+" (todoKey, todoStatusValue, todoId) VALUES (?,?,?)";

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sqlRowListTodo);
            st.setString(1,itemName);
            st.setString(2, todo);
            st.setInt(3, todoID);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteItem (int id, String todoTableName){

        String sqlRowListTodo = "DELETE FROM "+todoTableName+" WHERE id = "+id;

        Statement st = null;
        try {
            st = conn.createStatement();
            st.execute(sqlRowListTodo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItemStatus (int id, String newStatus, String tableName){
        String sqlUpdate = "UPDATE "+tableName+" SET todoStatusValue = '"+newStatus+"' WHERE id = "+id+"";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(sqlUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItemName (int id, String newName, String tableName){
        String sqlUpdate = "UPDATE "+tableName+" SET todoKey = '"+newName+"' WHERE id = "+id+"";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(sqlUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet showTable(String tableName){
        String sqlSelectTable = "SELECT id, todoKey, todoStatusValue FROM '"+tableName+"'";

        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlSelectTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet showItem (String tableName, int id){

        String sqlShowItem = "SELECT todoKey, todoStatusValue FROM '"+tableName+"' WHERE id = '"+id+"'";
        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlShowItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet showItemList (){
        String sqlShowItemList = "SELECT todoId, name FROM 'listOfTodoList'";

        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlShowItemList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }



    public void closeConnectionToDatabase(){
        try
        {
            if(conn != null)
                conn.close();
        }
        catch(SQLException e)
        {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }



}
