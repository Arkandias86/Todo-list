import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Todo {
    public static void main(String[] args) {
        Database_manager database_manager = new Database_manager();
        database_manager.createTablesForDatabase();
        System.out.println("data base manager object created");
        TodoView todoView = new TodoView(database_manager);
        System.out.println("view created");
        todoView.run_program();
        System.out.println("run programme started");
    }
}
