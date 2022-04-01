package mvh.app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import mvh.enums.WeaponType;
import mvh.world.*;

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


    /**
     * Setup the window state
     */
    @FXML
    public void initialize() {
        //
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
                    if (row_location.getText().isEmpty()) {
                        left_status.setText("Enter row index to add an entity!");
                    }
                    if (column_location.getText().isEmpty()) {
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
                    if (!monster_symbol_textfield.getText().isEmpty() && !monster_health_textfield.getText().isEmpty() && !row_location.getText().isEmpty() && !column_location.getText().isEmpty()&& choose_weapon.getValue()!=null) {
                        char mon_symbol = monster_symbol_textfield.getText().charAt(0);
                        int mon_health = Integer.parseInt(monster_health_textfield.getText());
                        int mon_row = Integer.parseInt(row_location.getText());
                        int mon_col = Integer.parseInt(column_location.getText());
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

}
