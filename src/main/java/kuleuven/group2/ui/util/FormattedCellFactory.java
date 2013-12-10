package kuleuven.group2.ui.util;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Formats a table column.
 */
public abstract class FormattedCellFactory<T, V> implements Callback<TableColumn<T, V>, TableCell<T, V>> {

	protected abstract String format(V value);

	@Override
	public TableCell<T, V> call(TableColumn<T, V> column) {
		return new TableCell<T, V>() {
			@Override
			protected void updateItem(V value, boolean empty) {
				super.updateItem(value, empty);
				if (!empty) {
					setText(format(value));
				} else {
					setText(null);
				}
			}
		};
	}

}