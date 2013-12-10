package kuleuven.group2.ui.util;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * Formats a boolean table column using two images for both states.
 */
public class BooleanImageCellFactory<T> implements Callback<TableColumn<T, Boolean>, TableCell<T, Boolean>> {

	private final Image trueImage;
	private final Image falseImage;

	public BooleanImageCellFactory(Image trueImage, Image falseImage) {
		this.trueImage = trueImage;
		this.falseImage = falseImage;
	}

	@Override
	public TableCell<T, Boolean> call(TableColumn<T, Boolean> column) {
		return new TableCell<T, Boolean>() {
			@Override
			protected void updateItem(Boolean result, boolean empty) {
				super.updateItem(result, empty);
				if (result != null) {
					setGraphic(new ImageView(result ? trueImage : falseImage));
				} else {
					setGraphic(null);
				}
			}
		};
	}

}