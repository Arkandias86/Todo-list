import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TodoView {
    Database_manager databaseManager;

    TodoView(Database_manager db){
        this.databaseManager = db;
        databaseManager.createTablesForDatabase();
    }

    public void run_program (){
        System.out.println("Entering run-program");


        while(true){ // exiting the loop by quitting the app from the menu, not by condition
            System.out.println("Beginning while loop");
            printMenu();
            System.out.println("");
            System.out.println("Menu printed");
            int choice = 0;

            BufferedReader br =
                         new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Entering choice");

            boolean valid = false; // to go out of the loop if user entered an int
            while (!valid) { //force user to enter valid input without crashing the app
                try {
                    choice = Integer.parseInt(br.readLine());
                    System.out.println("");
                    System.out.println("Choice made");
                    valid = true;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number: "+ e.getMessage());
                } catch (IOException i) {
                    System.out.println("Could not acquire user input correctly: "+ i.getMessage());
                }
            }
            System.out.println("Choice is " +choice);
            if(choice <= 0 || choice > 10){
                System.out.println("Please enter a valid choice ");
                return;
            }
            switch (choice){
                case 1 : addNewTodoList();
                    break;

                case 2 : showListItem();
                    break;

                case 3 : showList();
                    break;

                case 4 : showItem();
                    break;

                case 5 : updateItemStatus();
                    break;

                case 6 : updateItemName();
                    break;

                case 7 : deleteItem();
                    break;

                case 8 : addNewItem();
                    break;

                case 9 : deleteList();
                    break;

                case 10 : quit();
                    break;

            }

        }
    }

    public void printMenu(){
        System.out.println("To add a new list, type \"1\"");
        System.out.println("To show all the lists, type \"2\"");
        System.out.println("To display all the item of a list, type \"3\"");
        System.out.println("To display one element from a list, type \"4\"");
        System.out.println("To update an item status, type \"5\"");
        System.out.println("To change the name of an item, type \"6\"");
        System.out.println("To delete an item from a list, type \"7\"");
        System.out.println("To add a new item to a list, type \"8\"");
        System.out.println("To delete a list, type \"9\"");
        System.out.println("To quit the program, type \"10\"");
    }

    public void quit(){
        databaseManager.closeConnectionToDatabase();
        System.out.println("Connection to database closed.");
        System.out.println("Exiting the programme now");
        System.exit(0);
    }

    public void addNewTodoList (){
        System.out.println("Entering addMNewTodoList");
        String todoListName = "";
        System.out.println("Please enter a new list name of tasks to do: ");
        BufferedReader br =
                     new BufferedReader(new InputStreamReader(System.in));
    try{
            todoListName = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(todoListName.equals("")){
            System.out.println("Please enter a name next time.");
            return;
        }

        databaseManager.addNewTodoTable(todoListName);
        System.out.println("New list created.");
        System.out.println("To update the status, simply enter the item id, press enter then enter the new status ");

    }

    public void updateItemStatus (){
        System.out.println("In which list would you like to update a status?");
        showListItem();
        System.out.println("Enter the list name to display it to choose the item you want to modify: ");
        String table_name = "";
        BufferedReader br =
                     new BufferedReader(new InputStreamReader(System.in));
        try{
            table_name = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResultSet rs = databaseManager.showTable(table_name);

        try {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("todoKey") + "\t" +
                        rs.getString("todoStatusValue")+ "\t");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("To update the status, simply enter the item id, press enter then enter the new status ");

        int id = 0;
        String newStatus = "";
        br = new BufferedReader(new InputStreamReader(System.in));
        try{
            id = Integer.parseInt(br.readLine());
            newStatus = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseManager.updateItemStatus(id, newStatus, table_name);
        System.out.println("New status updated");
        System.out.println("-------------------------------------------------------------------");
    }

    public void deleteItem(){
        System.out.println("In which list would you like to delete an item?");
        showListItem();

        System.out.println("Enter the list name to display it to choose the item you want to delete: ");
        String table_name = "";
        BufferedReader br =
                     new BufferedReader(new InputStreamReader(System.in));
        try{
            table_name = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResultSet rs =  databaseManager.showTable(table_name);

        try {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("todoKey") + "\t" +
                        rs.getString("todoStatusValue")+ "\t");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("To delete the item, simply enter the item id.");

        int id = 0;
        br = new BufferedReader(new InputStreamReader(System.in));
        try{
            id = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseManager.deleteItem(id, table_name);
        System.out.println("Item deleted.");
        System.out.println("-------------------------------------------------------------------");

    }

    public void addNewItem (){
        System.out.println("In which list would you like to delete an item?");
        showListItem();

        System.out.println("Enter the list name to display it and verify if you want to add an item in it.");
        String table_name = "";
        BufferedReader br =
                     new BufferedReader(new InputStreamReader(System.in));
        try{
            table_name = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResultSet rs = databaseManager.showTable(table_name);

        try {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("todoKey") + "\t" +
                        rs.getString("todoStatusValue")+ "\t");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("To add a new item enter it's name. The default status will be \"To do\" ");

        String itemName = "";
        br = new BufferedReader(new InputStreamReader(System.in));
        try{
            itemName = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Next enter the number of the list in the list of list (primary key)");
        int todoId = 0;

        br = new BufferedReader(new InputStreamReader(System.in));
        try{
            todoId = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseManager.addNewItem(itemName, table_name, todoId);
        System.out.println("Item added to the list.");
        System.out.println("-------------------------------------------------------------------");
    }

    public void updateItemName(){
        System.out.println("In which list would you like to update an item name?");
        showListItem();
        System.out.println("Enter the list name to display it to choose the item you want to modify: ");
        String table_name = "";
        BufferedReader br =
                     new BufferedReader(new InputStreamReader(System.in));
        try{
            table_name = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResultSet rs = databaseManager.showTable(table_name);

        try {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("todoKey") + "\t" +
                        rs.getString("todoStatusValue")+ "\t");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("To update the item name, simply enter the item id, press enter then enter the new name.");

        int id = 0;
        String newName = "";
        br = new BufferedReader(new InputStreamReader(System.in));
        try{
            id = Integer.parseInt(br.readLine());
            newName = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseManager.updateItemName(id, newName, table_name );
        System.out.println("Item name updated.");
        System.out.println("-------------------------------------------------------------------");

    }

    public void deleteList(){
        System.out.println("Wich list would you like to delete?");
        showListItem();
        System.out.println("Please enter the name of the list you wish to delete: ");
        String tableId = "";
        BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));
        try{
            tableId = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        databaseManager.deleteTodoTable(tableId);

        System.out.println("List deleted");

    }

    public void showList(){
        System.out.println("Wich list would you like to see?");
        showListItem();
        System.out.println("Enter the list name to display it.");
        String table_name = "";
        BufferedReader br =
                     new BufferedReader(new InputStreamReader(System.in));
        try{
            table_name = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResultSet rs = databaseManager.showTable(table_name);

        try {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("todoKey") + "\t" +
                        rs.getString("todoStatusValue")+ "\t");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("-------------------------------------------------------------------");

    }

    public void showItem () {
        System.out.println("In which list would you like to see an item?");
        showListItem();
        System.out.println("Enter the list name to display it to choose the item you want to see: ");
        String table_name = "";
        BufferedReader br =
                     new BufferedReader(new InputStreamReader(System.in));
        try{
            table_name = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResultSet rs = databaseManager.showTable(table_name);

        try {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("todoKey") + "\t" +
                        rs.getString("todoStatusValue")+ "\t");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        System.out.println("To show the item, simply enter the item id.");
        int id = 0;
        br = new BufferedReader(new InputStreamReader(System.in));
        try{
            id = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResultSet rs1 = databaseManager.showItem(table_name, id);

        try {
            while (rs1.next()) {
                System.out.println(rs1.getString("todoKey") + "\t" +
                        ">>>>>>>>>>\t" +
                        rs1.getString("todoStatusValue") + "\t");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showListItem(){
    ResultSet rs = databaseManager.showItemList();

        try {
            while (rs.next()) {
                System.out.println(rs.getInt("todoId") + "\t" +
                        rs.getString("name") + "\t");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
