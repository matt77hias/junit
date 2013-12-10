package kuleuven.group2.ui.util;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * Formats an enum table column using images to represent states.
 */
public abstract class EnumImageCellFactory<T, E extends Enum<E>> implements
		Callback<TableColumn<T, E>, TableCell<T, E>> {

	protected abstract Image getImage(E value);

	protected Image getNullImage() {
		return null;
	}

	@Override
	public TableCell<T, E> call(TableColumn<T, E> column) {
		return new TableCell<T, E>() {
			@Override
			protected void updateItem(E value, boolean empty) {
				super.updateItem(value, empty);
				Image image = null;
				if (value != null) {
					image = getImage(value);
				} else {
					image = getNullImage();
				}
				setGraphic(image == null ? null : new ImageView(image));
			}
		};
	}

}