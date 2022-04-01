package mvh.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import mvh.world.*;

public class MainController {

    //Store the data of editor
    private World world;
    @FXML
    private TextField column_input;

    @FXML
    private Button new_world;

    @FXML
    private TextField rows_input;

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

}
