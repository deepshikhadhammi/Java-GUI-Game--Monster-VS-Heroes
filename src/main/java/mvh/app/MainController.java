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
    private TextField column_input;

    @FXML
    private Button new_world;

    @FXML
    private TextField row_input;
    @FXML
    private TextArea world_view;
    @FXML
    private ToggleGroup entity;

    @FXML
    private TextField monster_column_textfield;

    @FXML
    private TextField monster_health_textfield;

    @FXML
    private RadioButton monster_rb;

    @FXML
    private TextField monster_row_textfield;

    @FXML
    private TextField monster_symbol_textfield;
    @FXML
    private ChoiceBox<String> choose_weapon;
    String[] weapon = {"Sword(4)", "Axe(3)", "Club(2)"};

    @FXML
    private TextField hero_armor_textfield;

    @FXML
    private TextField hero_column_textfield;

    @FXML
    private TextField hero_health_textfield;

    @FXML
    private RadioButton hero_rb;

    @FXML
    private TextField hero_row_textfield;

    @FXML
    private TextField hero_symbol_textfield;

    @FXML
    private TextField hero_weapon_textfield;
    @FXML
    private Button delete;
    @FXML
    private TextField row_location;
    @FXML
    private TextField column_location;
    @FXML
    private Button details;
    @FXML
    private TextArea details_view;
    @FXML
    private TextField view_column_textfield;

    @FXML
    private TextField view_row_textfield;
    @FXML
    private Label left_status;
    FileChooser fileChooser=new FileChooser();


    /**
     * Setup the window state
     */
    @FXML
    public void initialize() {
        choose_weapon.getItems().addAll(weapon);
    }

    @FXML
    void handle_load_action(ActionEvent event) {
        fileChooser.setTitle("Open File Dialog");
        File file = fileChooser.showOpenDialog(new Stage());
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        if (file != null) {
            world = loadWorld(file);
            assert world != null;
            world_view.setText(world.toString());
        }
        else
        {
            left_status.setText("Choose a file to read!");
        }

    }

    @FXML
    void handle_quit_action(ActionEvent event) {

    }

    @FXML
    void handle_save_action(ActionEvent event) {
        File file= fileChooser.showSaveDialog(new Stage());
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        if(file!=null) {
            try {
                FileWriter file_writer = new FileWriter(file);
                PrintWriter print_writer = new PrintWriter(file_writer);
                String rows = row_input.getText();
                String columns = column_input.getText();
                print_writer.println(rows);
                print_writer.println(columns);
                for (int r = 0, i = 0; i < Integer.parseInt(rows) * Integer.parseInt(columns) && r < Integer.parseInt(rows); i++, r++) {
                    for (int c = 0; c < Integer.parseInt(columns); c++) {
                        if (world.isHero(r, c)) {
                            print_writer.println(String.valueOf(r) + ',' + String.valueOf(c) + ',' + "HERO" + ',' + String.valueOf(world.getEntity(r, c).getSymbol()) + ',' + String.valueOf(world.getEntity(r, c).getHealth()) + ',' + String.valueOf(world.getEntity(r, c).weaponStrength()) + ',' + String.valueOf(world.getEntity(r, c).armorStrength()));
                        } else if (world.isMonster(r, c)) {
                            String w = "";
                            if (world.getEntity(r, c).weaponStrength() == 2) {
                                w = "C";
                            } else if (world.getEntity(r, c).weaponStrength() == 3) {
                                w = "A";
                            } else if (world.getEntity(r, c).weaponStrength() == 4) {
                                w = "S";
                            }
                            print_writer.println(String.valueOf(r) + ',' + String.valueOf(c) + ',' + "MONSTER" + ',' + String.valueOf(world.getEntity(r, c).getSymbol()) + ',' + String.valueOf(world.getEntity(r, c).getHealth()) + ',' + w);
                        } else if (world.getEntity(r, c) == null) {
                            print_writer.println(String.valueOf(r) + ',' + String.valueOf(c));
                        }
                    }
                }
                print_writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            left_status.setText("Choose a file to save into!");
        }

    }
    @FXML
    void about_mvh_map(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Message");
        alert.setTitle("About");
        alert.setContentText("Author: DEEPSHIKHA DHAMMI"+"\n"+"UCID: 30140157"+"\n"+"Tutorial: 08"+"\n"+"TA: KIMIYA SADAT"+"\n"+"email: deepshikha.dhammi@ucalgary.ca"+"\n"+"Version: v1.o"+"\n"+"This is a world editor for Monster VS Heroes");
        alert.show();
    }
    @FXML
    void create_world(MouseEvent event) {
        try {
            int rows = Integer.parseInt(row_input.getText());
            int columns = Integer.parseInt(column_input.getText());
            System.out.println("Rows" + rows + "   " + "Columns" + columns);
            world = new World(rows, columns);
            world_view.setText(world.toString());
        } catch (NumberFormatException | NullPointerException e) {
            if (row_input.getText().isEmpty()) {
                left_status.setText("Enter number of rows for creating a world!");
            } else if (column_input.getText().isEmpty()) {
                left_status.setText("Enter number of columns for creating a world!");
            } else
                left_status.setText("Enter a valid integer input!");
        }

    }
    @FXML
    void RadioSelect(MouseEvent event) {

        if (monster_rb.isSelected()) {

            try {
                if (world == null) {
                    left_status.setText("Create a world by entering rows and columns and then click create world button");
                } else {
                    if (monster_row_textfield.getText().isEmpty()) {
                        left_status.setText("Enter row index to add an entity!");
                    }
                    if (monster_column_textfield.getText().isEmpty()) {
                        left_status.setText("Enter column index to add an entity!");
                    }
                    if (monster_symbol_textfield.getText().isEmpty()) {
                        left_status.setText("Enter Monster's Symbol!");
                    }
                    if (monster_health_textfield.getText().isEmpty()) {
                        left_status.setText("Enter Monster's health!");
                    }
                    if(choose_weapon.getValue()==null)
                    {
                        left_status.setText("Select Monster's weapon");
                    }
                    if (!monster_symbol_textfield.getText().isEmpty() && !monster_health_textfield.getText().isEmpty() && !monster_row_textfield.getText().isEmpty() && !monster_column_textfield.getText().isEmpty()&& choose_weapon.getValue()!=null) {
                        char mon_symbol = monster_symbol_textfield.getText().charAt(0);
                        int mon_health = Integer.parseInt(monster_health_textfield.getText());
                        int mon_row = Integer.parseInt(monster_row_textfield.getText());
                        int mon_col = Integer.parseInt(monster_column_textfield.getText());
                        char weapon = choose_weapon.getValue().charAt(0);

                        if (mon_row >= 0 && mon_row < Integer.parseInt(row_input.getText()) && mon_col >= 0 && mon_col < Integer.parseInt(column_input.getText())) {
                            Monster monster = new Monster(mon_health, mon_symbol, WeaponType.getWeaponType(weapon));
                            world.addEntity(mon_row, mon_col, monster);
                            left_status.setText("Added a monster!");
                            world_view.setText(world.toString());
                        } else {
                            if (mon_row < 0 || mon_row >= Integer.parseInt(row_input.getText())) {
                                left_status.setText("Enter a valid range for row index ");
                            }
                            if (mon_col < 0 || mon_col >= Integer.parseInt(column_input.getText())) {
                                left_status.setText("Enter a valid range for column index");
                            }
                        }
                    }
                }
            } catch (NumberFormatException e) {
                left_status.setText("Enter a valid integer input! "+"  "+e);

            }
        }
        else if (hero_rb.isSelected()) {
            try {
                if (world == null) {
                    left_status.setText("Create a world by entering rows and columns and then click create world button!");
                } else {
                    if (hero_row_textfield.getText().isEmpty()) {
                        left_status.setText("Enter row index to add an entity!");
                    }
                    if (hero_column_textfield.getText().isEmpty()) {
                        left_status.setText("Enter column index to add an entity!");
                    }
                    if (hero_symbol_textfield.getText().isEmpty()) {
                        left_status.setText("Enter Hero's Symbol!");
                    }
                    if (hero_health_textfield.getText().isEmpty()) {
                        left_status.setText("Enter Hero's health!");
                    }
                    if (hero_weapon_textfield.getText().isEmpty()) {
                        left_status.setText("Enter Hero's weapon strength!");
                    }
                    if (hero_armor_textfield.getText().isEmpty()) {
                        left_status.setText("Enter Hero's armor strength!");
                    }
                    if (!hero_symbol_textfield.getText().isEmpty() && !hero_health_textfield.getText().isEmpty() && !hero_row_textfield.getText().isEmpty() && !hero_column_textfield.getText().isEmpty() && !hero_weapon_textfield.getText().isEmpty() && !hero_armor_textfield.getText().isEmpty()) {
                        char hero_symbol = hero_symbol_textfield.getText().charAt(0);
                        int hero_health = Integer.parseInt(hero_health_textfield.getText());
                        int hero_armor = Integer.parseInt(hero_armor_textfield.getText());
                        int hero_Weapon = Integer.parseInt(hero_health_textfield.getText());
                        int hero_row = Integer.parseInt(hero_row_textfield.getText());
                        int hero_col = Integer.parseInt(hero_column_textfield.getText());

                        if (hero_row >= 0 && hero_row < Integer.parseInt(row_input.getText()) && hero_col >= 0 && hero_col < Integer.parseInt(column_input.getText())) {
                            Hero hero = new Hero(hero_health, hero_symbol, hero_Weapon, hero_armor);
                            world.addEntity(hero_row, hero_col, hero);
                            left_status.setText("Added a Hero!");
                            world_view.setText(world.toString());
                        } else {
                            if (hero_row < 0 || hero_row >= Integer.parseInt(row_input.getText())) {
                                left_status.setText("Enter a valid range for row index ");
                            }
                            if (hero_col < 0 || hero_col >= Integer.parseInt(column_input.getText())) {
                                left_status.setText("Enter a valid range for column index");
                            }
                        }


                    }
                }
            }
            catch (NumberFormatException e)
            {
                left_status.setText("Enter a valid integer input! "+" "+ e);
            }
        }

    }
    @FXML
    void delete_entity(MouseEvent event) {
        try {
            if (world == null) {
                left_status.setText("Create a world by entering rows and columns and then click create world button");
            }
            else {
                if (row_location.getText().isEmpty()) {
                    left_status.setText("Enter row index to delete an entity!");
                }

                if (column_location.getText().isEmpty()) {
                    left_status.setText("Enter column index to delete an entity!");
                }
                if (!row_location.getText().isEmpty() && !column_location.getText().isEmpty()) {
                    if (Integer.parseInt(row_location.getText()) >= 0 && Integer.parseInt(row_location.getText()) < Integer.parseInt(row_input.getText()) && Integer.parseInt(column_location.getText()) >= 0 && Integer.parseInt(column_location.getText()) < Integer.parseInt(column_input.getText()))
                    {
                        int row = Integer.parseInt(row_location.getText());
                        int column = Integer.parseInt(column_location.getText());
                        world.addEntity(row, column, null);
                        left_status.setText("Deleted an entity!");
                        world_view.setText(world.toString());
                    }

                    else if (Integer.parseInt(row_location.getText()) < 0 || Integer.parseInt(row_location.getText()) >= Integer.parseInt(row_input.getText())) {
                        left_status.setText("Enter row index within a valid range!");
                    }
                    else if (Integer.parseInt(column_location.getText()) < 0 || Integer.parseInt(column_location.getText()) >= Integer.parseInt(column_input.getText())) {
                        left_status.setText("Enter column index within a valid range");
                    }
                }
            }


        } catch (NumberFormatException e) {
            left_status.setText("Enter a valid integer input! "+" "+e);
        }
    }
    @FXML
    void view_entity_detail(MouseEvent event) {
        try
        {
        if(view_row_textfield.getText().isEmpty())
        {
            left_status.setText("Enter the row index of an entity to view details!");
        }
        if(view_column_textfield.getText().isEmpty())
        {
            left_status.setText("Enter the column index of an entity to view details!");
        }

        int row= Integer.parseInt(view_row_textfield.getText());
        int column= Integer.parseInt(view_column_textfield.getText());
        world.getEntity(row,column);
        if(world.isMonster(row,column)) {
            String type = "MONSTER";
            char symbol = world.getEntity(row, column).getSymbol();
            int health=world.getEntity(row,column).getHealth();
            int weapon_strength=world.getEntity(row,column).weaponStrength();
            String weapon="";
            if(weapon_strength==4)
            {
                weapon="SWORD";

            }
            else if(weapon_strength==3)
            {
                weapon="AXE";
            }
            else if(weapon_strength==2)
            {
                weapon="CLUB";
            }
            details_view.setText("Type: " + type + "\n" +"Symbol: "+ symbol+"\n"+"Health: "+ health+"\n"+"Weapon: "+ weapon+"\n"+"Weapon strength: "+ weapon_strength);
        }
        else if(world.isHero(row,column))
        {
            String type = "HERO";
            char symbol = world.getEntity(row, column).getSymbol();
            int health=world.getEntity(row,column).getHealth();
            int weapon=world.getEntity(row,column).weaponStrength();
            int armor=world.getEntity(row,column).armorStrength();
            details_view.setText("Type: " + type + "\n" +"Symbol: "+ symbol+"\n"+"Health: "+ health+"\n"+"Weapon Strength: "+weapon+"\n"+"Armor Strength: "+armor);
        }
    }
        catch(NumberFormatException e)
        {
            left_status.setText("Enter a valid integer input! "+" '+e");
        }
}




}
