package mvh.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mvh.enums.WeaponType;
import mvh.world.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static mvh.util.Reader.loadWorld;

public class MainController {

    //Store the data of editor
    private World world;
    @FXML
    private TextField column_input;   //textfield for entering column input

    @FXML
    private Button new_world;  //button for new_world

    @FXML
    private TextField row_input;   //textfield for row input
    @FXML
    private TextArea world_view;  // text area for world view
    @FXML
    private ToggleGroup entity;

    @FXML
    private TextField monster_column_textfield;  //textfield for monster_column_textfield

    @FXML
    private TextField monster_health_textfield;  //textfield for monster_health_textfield

    @FXML
    private RadioButton monster_rb;     //radiobutton monster

    @FXML
    private TextField monster_row_textfield;   //textfield monster_row

    @FXML
    private TextField monster_symbol_textfield;   //textfield for monster symbol
    @FXML
    private ChoiceBox<String> choose_weapon;      //Adding choices to weapons
    String[] weapon = {"Sword(4)", "Axe(3)", "Club(2)"};

    @FXML
    private TextField hero_armor_textfield;   //textfield for hero's armor strength

    @FXML
    private TextField hero_column_textfield;  //textfield for hero's column

    @FXML
    private TextField hero_health_textfield;  //textfield for hero's health

    @FXML
    private RadioButton hero_rb;         // radiobutton for hero

    @FXML
    private TextField hero_row_textfield;  //textfield for hero row

    @FXML
    private TextField hero_symbol_textfield;  //textfield for hero symbol

    @FXML
    private TextField hero_weapon_textfield;  //textfield for hero's weapon
    @FXML
    private Button delete;
    @FXML
    private TextField row_location;    //textfield for row location
    @FXML
    private TextField column_location;  //textfield for column location
    @FXML
    private Button details;
    @FXML
    private TextArea details_view;   //textarea for details view
    @FXML
    private TextField view_column_textfield;  //textfield for column

    @FXML
    private TextField view_row_textfield;   //textfield for row
    @FXML
    private Label right_status;    //right_status label
    @FXML
    private Label left_status;   //left_status label
    FileChooser fileChooser=new FileChooser();

    /**
     * Setup the window state
     */
    @FXML
    public void initialize() {
        choose_weapon.getItems().addAll(weapon);
    }

    /**
     *
     * Loading a file when user selects Load menu item from the menu File
     */
    @FXML
    void handle_load_action(ActionEvent event) {
        fileChooser.setTitle("Open File Dialog");
        File file = fileChooser.showOpenDialog(new Stage());  // Choosing a file to read
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        if (file != null) {
            world = loadWorld(file);   //storing the map into the world
            assert world != null;
            world_view.setText(world.toString());   //displaying the map created in world view
            right_status.setText("World drawn!");   // displaying success message of world drawn
        }
        else
        {
            left_status.setText("Choose a file to read!");   //if no file chosen give an error message to the user
        }

    }

    /**
     *
     * It quits the GUI window when user selects Quit menu item from the File menu
     */

    @FXML
    void handle_quit_action(ActionEvent event) {
        javafx.application.Platform.exit();  // quit the GUI window

    }

    /**
     *
     * It will write or save the world map created using GUI into the text file selected bu the user
     */

    @FXML
    void handle_save_action(ActionEvent event) {
        File file= fileChooser.showSaveDialog(new Stage());  //choose file to save
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        if(file!=null) {
            try {
                //writing to a file
                FileWriter file_writer = new FileWriter(file);
                PrintWriter print_writer = new PrintWriter(file_writer);
                String rows = row_input.getText();   //finding rows and columns in a map
                String columns = column_input.getText();
                print_writer.println(rows);  //print rows of the map in the first line
                print_writer.println(columns);  //print columns in the map in the second line
                //looping through every row and column index and writing the entity details into the file
                int AXE=3, CLUB=2, SWORD=4;
                for (int r = 0, i = 0; i < Integer.parseInt(rows) * Integer.parseInt(columns) && r < Integer.parseInt(rows); i++, r++) {
                    for (int c = 0; c < Integer.parseInt(columns); c++) {
                        if (world.isHero(r, c)) {  //writing hero details
                            print_writer.println(String.valueOf(r) + ',' + String.valueOf(c) + ',' + "HERO" + ',' + String.valueOf(world.getEntity(r, c).getSymbol()) + ',' + String.valueOf(world.getEntity(r, c).getHealth()) + ',' + String.valueOf(world.getEntity(r, c).weaponStrength()) + ',' + String.valueOf(world.getEntity(r, c).armorStrength()));
                        } else if (world.isMonster(r, c)) {   //writing monster details
                            String w = "";
                            if (world.getEntity(r, c).weaponStrength() == CLUB) {
                                w = "C";
                            } else if (world.getEntity(r, c).weaponStrength() == AXE) {
                                w = "A";
                            } else if (world.getEntity(r, c).weaponStrength() == SWORD) {
                                w = "S";
                            }
                            print_writer.println(String.valueOf(r) + ',' + String.valueOf(c) + ',' + "MONSTER" + ',' + String.valueOf(world.getEntity(r, c).getSymbol()) + ',' + String.valueOf(world.getEntity(r, c).getHealth()) + ',' + w);
                        } else if (world.getEntity(r, c) == null) {  // writing row and column value if entity is null
                            print_writer.println(String.valueOf(r) + ',' + String.valueOf(c));
                        }
                    }
                }
                print_writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch(NullPointerException| NumberFormatException e)   //exceptions
            {
                if(row_input.getText().isEmpty() || column_input.getText().isEmpty())
                   left_status.setText("World is empty!");
                else
                    left_status.setText("Enter a valid integer input "+ e);
            }
        }
        else
        {
            left_status.setText("Choose a file to save into!");   //error message
        }

    }

    /**
     *
     * Display the about info when the user prsses About menu item from File menu
     */
    @FXML
    void about_mvh_map(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);  //add an alet info box
        alert.setHeaderText("Message");   //display header
        alert.setTitle("About");   //set title of box
        //shoeing author details
        alert.setContentText("Author: DEEPSHIKHA DHAMMI"+"\n"+"UCID: 30140157"+"\n"+"Tutorial: 08"+"\n"+"TA: KIMIYA SADAT"+"\n"+"email: deepshikha.dhammi@ucalgary.ca"+"\n"+"Version: v1.o"+"\n"+"This is a world editor for Monster VS Heroes");
        alert.show();
    }

    /**
     *
     * Create a world by entering number of rows and columns in the textfields and then pressing Create world button
     */
    @FXML
    void create_world(MouseEvent event) {
        try {
            int rows = Integer.parseInt(row_input.getText());   //get row input for world
            int columns = Integer.parseInt(column_input.getText());  //get column input for world
            world = new World(rows, columns);
            world_view.setText(world.toString());   //displaying the world
            right_status.setText("World drawn!");    //displaying success message
        } catch (NumberFormatException | NullPointerException e) {
            if (row_input.getText().isEmpty()) {
                left_status.setText("Enter number of rows for creating a world!");  //exception when textfield is empty
            } else if (column_input.getText().isEmpty()) {
                left_status.setText("Enter number of columns for creating a world!");
            } else
                left_status.setText("Enter a valid integer input!");   //exception when user entered  a wrong input
        }

    }

    /**
     *
     * Add a monster to a map after taking the monster symbol, health , choosing weapon and giving row and column coordinates in the textfields.
     * Press on add monster radio button.
     */
    @FXML
    void RadioSelect(MouseEvent event) {

        if (monster_rb.isSelected()) {  //if add monster radio button is selected

            try {
                if (world == null) {   //check if world is null
                    left_status.setText("Create a world by entering rows and columns and then click create world button");
                } else {
                    if (monster_row_textfield.getText().isEmpty()) {  //check if monster row is null
                        left_status.setText("Enter row index to add an entity!");
                    }
                    if (monster_column_textfield.getText().isEmpty()) {  //check if monster column is null
                        left_status.setText("Enter column index to add an entity!");
                    }
                    if (monster_symbol_textfield.getText().isEmpty()) {  //check if monster synbol is null
                        left_status.setText("Enter Monster's Symbol!");
                    }
                    if (monster_health_textfield.getText().isEmpty()) {  //check if monster health is null
                        left_status.setText("Enter Monster's health!");
                    }
                    if(choose_weapon.getValue()==null)   //check if user has chosena  weapon
                    {
                        left_status.setText("Select Monster's weapon");
                    }
                    if (!monster_symbol_textfield.getText().isEmpty() && !monster_health_textfield.getText().isEmpty() && !monster_row_textfield.getText().isEmpty() && !monster_column_textfield.getText().isEmpty()&& choose_weapon.getValue()!=null) {
                        char mon_symbol = monster_symbol_textfield.getText().charAt(0);  //get monster symbol
                        int mon_health = Integer.parseInt(monster_health_textfield.getText()); //get monster health
                        int mon_row = Integer.parseInt(monster_row_textfield.getText()); //get monster row
                        int mon_col = Integer.parseInt(monster_column_textfield.getText()); //get monster column
                        char weapon = choose_weapon.getValue().charAt(0);  //get monster weapon

                        if (mon_row >= 0 && mon_row < Integer.parseInt(row_input.getText()) && mon_col >= 0 && mon_col < Integer.parseInt(column_input.getText())) {
                            Monster monster = new Monster(mon_health, mon_symbol, WeaponType.getWeaponType(weapon));
                            if(world.getEntity(mon_row,mon_col)==null) {  //if row and column location is null add the monster
                                world.addEntity(mon_row, mon_col, monster);
                                left_status.setText("Added a monster!");  //success message
                                world_view.setText(world.toString());   //drawing the updated world in world view
                                right_status.setText("Updated world drawn!");
                            }
                            else
                            {left_status.setText("Cannot add a Monster as row and column index is not null!");} // if index is not null monster cannot be added
                        } else {  //if user doesnot enter the row and column value in the valid range prompt the user for it
                            if (mon_row < 0 || mon_row >= Integer.parseInt(row_input.getText())) {
                                left_status.setText("Enter a valid range for row index ");
                            }
                            if (mon_col < 0 || mon_col >= Integer.parseInt(column_input.getText())) {
                                left_status.setText("Enter a valid range for column index");
                            }
                        }
                    }
                }
            } catch (NumberFormatException e) {  //catch exception
                left_status.setText("Enter a valid integer input! "+"  "+e);

            }
            catch (NullPointerException e)  //catch exception
            {left_status.setText(String.valueOf(e));}
        }
        else if (hero_rb.isSelected()) {  //if add hero is selected
            try {
                if (world == null) { //check if world is null
                    left_status.setText("Create a world by entering rows and columns and then click create world button!");
                } else {
                    if (hero_row_textfield.getText().isEmpty()) {
                        left_status.setText("Enter row index to add an entity!");  //ask user for row input to add hero
                    }
                    if (hero_column_textfield.getText().isEmpty()) { // ask user for col input to add hero
                        left_status.setText("Enter column index to add an entity!");
                    }
                    if (hero_symbol_textfield.getText().isEmpty()) { //ask user for hero's symbol
                        left_status.setText("Enter Hero's Symbol!");
                    }
                    if (hero_health_textfield.getText().isEmpty()) {  //ask user for hero's health
                        left_status.setText("Enter Hero's health!");
                    }
                    if (hero_weapon_textfield.getText().isEmpty()) {  //ask user for hero's weapon strength
                        left_status.setText("Enter Hero's weapon strength!");
                    }
                    if (hero_armor_textfield.getText().isEmpty()) {  //ask user for hero's armor strength
                        left_status.setText("Enter Hero's armor strength!");
                    }
                    if (!hero_symbol_textfield.getText().isEmpty() && !hero_health_textfield.getText().isEmpty() && !hero_row_textfield.getText().isEmpty() && !hero_column_textfield.getText().isEmpty() && !hero_weapon_textfield.getText().isEmpty() && !hero_armor_textfield.getText().isEmpty()) {
                        char hero_symbol = hero_symbol_textfield.getText().charAt(0);  //getting hero's symbol
                        int hero_health = Integer.parseInt(hero_health_textfield.getText());  //hero's health
                        int hero_armor = Integer.parseInt(hero_armor_textfield.getText());  //hero's armor strength
                        int hero_Weapon = Integer.parseInt(hero_health_textfield.getText());  //hero's weapon strength
                        int hero_row = Integer.parseInt(hero_row_textfield.getText());  //hero's row
                        int hero_col = Integer.parseInt(hero_column_textfield.getText());  //hero's column

                        if (hero_row >= 0 && hero_row < Integer.parseInt(row_input.getText()) && hero_col >= 0 && hero_col < Integer.parseInt(column_input.getText())) {
                            Hero hero = new Hero(hero_health, hero_symbol, hero_Weapon, hero_armor);
                            if(world.getEntity(hero_row,hero_col)==null) {  //add a hero to a location if the location is empty
                                world.addEntity(hero_row, hero_col, hero);
                                left_status.setText("Added a Hero!");  //success message
                                world_view.setText(world.toString());  //draw the updated world
                                right_status.setText("Updated world drawn!");
                            }
                            else
                            {left_status.setText("Cannot add a hero as the row and column location is not null!");}// show message if location is not null
                        }
                        //prompt the user for valid row and column inputs
                        else  if (hero_row < 0 || hero_row >= Integer.parseInt(row_input.getText())) {
                                left_status.setText("Enter a valid range for row index ");
                            }
                        else if (hero_col < 0 || hero_col >= Integer.parseInt(column_input.getText())) {
                                left_status.setText("Enter a valid range for column index");
                            }
                        }


                    }
            }
            catch (NumberFormatException e)  //catch exceptions
            {
                left_status.setText("Enter a valid integer input! "+" "+ e);
            }
            catch (NullPointerException e)
            {left_status.setText(String.valueOf(e));}
        }

    }

    /**
     *
     * Delete an entity from the world by entering row and column index and then pressing Delete BUTTON
     */
    @FXML
    void delete_entity(MouseEvent event) {
        try {
            if (world == null) {  //check if the world is null
                left_status.setText("Create a world by entering rows and columns and then click create world button");
            }
            else {
                if (row_location.getText().isEmpty()) {
                    left_status.setText("Enter row index to delete an entity!");//ask user for row input
                }

                if (column_location.getText().isEmpty()) {
                    left_status.setText("Enter column index to delete an entity!"); //ask user for column input
                }
                if (!row_location.getText().isEmpty() && !column_location.getText().isEmpty()) {
                    if (Integer.parseInt(row_location.getText()) >= 0 && Integer.parseInt(row_location.getText()) < Integer.parseInt(row_input.getText()) && Integer.parseInt(column_location.getText()) >= 0 && Integer.parseInt(column_location.getText()) < Integer.parseInt(column_input.getText())) {
                        int row = Integer.parseInt(row_location.getText());  //row location
                        int column = Integer.parseInt(column_location.getText()); //column location
                        if (world.getEntity(row, column) != null) { //delete an entity if it is monster or hero and is not null
                            world.addEntity(row, column, null);
                            left_status.setText("Deleted an entity!");
                            world_view.setText(world.toString());
                            right_status.setText("Updated world drawn!");
                        } else {//display the message if the entity at that location is already null
                            left_status.setText("Cannot delete an entity as it is already null!");
                        }
                    }
                    //prompt the user for valid row and column input
                    else if (Integer.parseInt(row_location.getText()) < 0 || Integer.parseInt(row_location.getText()) >= Integer.parseInt(row_input.getText())) {
                        left_status.setText("Enter row index within a valid range!");
                    } else if (Integer.parseInt(column_location.getText()) < 0 || Integer.parseInt(column_location.getText()) >= Integer.parseInt(column_input.getText())) {
                        left_status.setText("Enter column index within a valid range");
                    }
                }
                }



        } catch (NumberFormatException e) {  //catch exceptions
            left_status.setText("Enter a valid integer input! "+" "+e);
        }
        catch (NullPointerException e)
        {left_status.setText(String.valueOf(e));}
    }

    /**
     *
     * Vie Entity details by entering row and column ins=dex in the textfield and the press the View Detail button.
     */
    @FXML
    void view_entity_detail(MouseEvent event) {
        try {
            if (world == null) {  //prompt the user for creating a world
                left_status.setText("Create a world by entering rows and columns and then click create world button");
            } else {
                if (view_row_textfield.getText().isEmpty()) {   //ask user for row input
                    left_status.setText("Enter the row index of an entity to view details!");
                }
                if (view_column_textfield.getText().isEmpty()) {
                    left_status.setText("Enter the column index of an entity to view details!"); //ask user for column input
                }
                if (world != null && !view_row_textfield.getText().isEmpty() && !view_column_textfield.getText().isEmpty()) {
                    int row = Integer.parseInt(view_row_textfield.getText());  //row index
                    int column = Integer.parseInt(view_column_textfield.getText());  //column index
                    world.getEntity(row, column);
                    int AXE=3,CLUB=2,SWORD=4;
                    if (world.isMonster(row, column)) {  //check if entity is monster
                        String type = "MONSTER";
                        char symbol = world.getEntity(row, column).getSymbol();  //monster's symbol
                        int health = world.getEntity(row, column).getHealth();  //monster's health
                        int weapon_strength = world.getEntity(row, column).weaponStrength();
                        String weapon = "";
                        if (weapon_strength == SWORD) {
                            weapon = "SWORD";

                        } else if (weapon_strength == AXE) {
                            weapon = "AXE";
                        } else if (weapon_strength == CLUB) {
                            weapon = "CLUB";
                        }
                        //printing monster's detail
                        details_view.setText("Type: " + type + "\n" + "Symbol: " + symbol + "\n" + "Health: " + health + "\n" + "Weapon: " + weapon + "\n" + "Weapon strength: " + weapon_strength);
                        right_status.setText("Details of a monster!");
                    } else if (world.isHero(row, column)) {  //check for hero
                        String type = "HERO";
                        char symbol = world.getEntity(row, column).getSymbol();  //hero's symbol
                        int health = world.getEntity(row, column).getHealth();   //hero's health
                        int weapon = world.getEntity(row, column).weaponStrength();  //hero's weapon strength
                        int armor = world.getEntity(row, column).armorStrength();   //hero's armor strength
                        //printing hero's details
                        details_view.setText("Type: " + type + "\n" + "Symbol: " + symbol + "\n" + "Health: " + health + "\n" + "Weapon Strength: " + weapon + "\n" + "Armor Strength: " + armor);
                        right_status.setText("Details of a hero!");
                    } else if (world.getEntity(row, column) == null) {   //if the entity is null print the message
                        right_status.setText("Cannot access details of an entity as it is null!");  // print the message
                    }
                }

            }
        }
        catch(NumberFormatException e) //catch exceptions
        {
            left_status.setText("Enter a valid integer input! "+" "+ e );
        }
        catch (NullPointerException e)  //catch exceptions
        {
            left_status.setText(String.valueOf(e));
        }
}




}
