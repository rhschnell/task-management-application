package client.windows.tags.edit;

import client.windows.tags.view.CustomTagCellCtrl;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EditTagCtrl {

    private final EditTagService service;

    private CustomTagCellCtrl customTagCellCtrl;

    private Tag tag;

    @FXML
    private TextField tagTitle;

    @FXML
    private ColorPicker tagColor;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    /**
     * Constructor for the EditTagCtrl
     * @param service Corresponding service
     */
    @Inject
    public EditTagCtrl(EditTagService service){
        this.service = service;
    }

    /**
     * Setter for the tag
     * @param tag The new tag
     */
    public void setTag(Tag tag){
        this.tag = tag;
        setTagTitle(tag.getName());
        setTagColor(Color.web(tag.getColor()));
    }

    /**
     * Setter for the title of the tag
     * @param title The new title of the tag
     */
    public void setTagTitle(String title){
        tagTitle.setText(title);
    }

    /**
     * Setter for the color of tag
     * @param color The new color of the tag
     */
    public void setTagColor(Color color){
        tagColor.setId(color.toString());
    }

    /**
     * Method to save the just edited tag
     */
    public void save() {
        String title = tagTitle.getText();
        Color color = tagColor.getValue();

        tag.setName(title);
        tag.setColor(color.toString());

        service.insertTag(tag);
        customTagCellCtrl.getTagOverviewCtrl().updateDisplayedTags();

        ((Stage)saveButton.getScene().getWindow()).close();
    }

    /**
     * Method to delete the tag that is currently being edited
     */
//    public void delete(){
//        service.deleteTag(tag);
//
//        customTagCellCtrl.getTagOverviewCtrl().updateDisplayedTags();
//
//        ((Stage)deleteButton.getScene().getWindow()).close();
//    }

    /**
     * Method to close the popup window when the cancel button is clicked
     */
    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    /**
     * Method to set the customTagCellCtrl
     * @param customTagCellCtrl The new CustomTagCellCtrl
     */
    public void setCustomTagCellCtrl(CustomTagCellCtrl customTagCellCtrl) {
        this.customTagCellCtrl = customTagCellCtrl;
    }

    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */

}
