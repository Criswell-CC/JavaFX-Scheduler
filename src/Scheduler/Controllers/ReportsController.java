package Scheduler.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for report windows allowing for dynamic generation of tables and other UI elements
 *
 * @author Chris Criswell
 */
public class ReportsController implements Initializable {

    /**
     * Main TableView container
     */
    @FXML
    HBox tableContainer;

    /**
     * Main header for report
     */
    @FXML
    Label mainLabel;

    /**
     * Text for main header
     */
    String mainLabelText;

    /**
     * Table to set with generated report TableView object
     */
    TableView reportTable;

    /**
     * Padding to add to table container
     */
    Insets tableInsets;

    /**
     * Custom-defined inset
     */
    double[] tableInsetValues;

    /**
     * Mutator for main header text
     * @param labelText header text
     */
    public void setMainLabelText(String labelText) {
        mainLabelText = labelText;
    }

    /**
     * Mutator for custom inset values
     * @param insets array of inset values in top-right-bottom-left order
     */
    public void setTableInsetValues(double[] insets) {
        this.tableInsetValues = insets;
    }

    /**
     * Mutator for main report table
     * @param tableView table with custom report data
     */
    public void setTable(TableView tableView) {
        this.reportTable = tableView;
    }

    /**
     * Override of standard JavaFX method to initialize UI. Initializes header text and adds report table to scene.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tableContainer.getChildren().add(reportTable);

        if (mainLabelText != null) {
            mainLabel.setText(mainLabelText);
        }

        if (tableInsetValues == null) {
            tableInsets = new Insets(0, 30, 0, 30);
        }

        else {
            tableInsets = new Insets(tableInsetValues[0], tableInsetValues[1], tableInsetValues[2], tableInsetValues[3]);
        }

        tableContainer.setPadding(tableInsets);

    }
}
