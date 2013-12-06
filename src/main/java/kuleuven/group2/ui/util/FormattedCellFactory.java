package kuleuven.group2.ui.util;

import java.text.Format;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Formats a table column using the given {@link Format}.
 */
public class FormattedCellFactory<T, V> implements Callback<TableColumn<T, V>, TableCell<T, V>> {

	private final Format format;

	public FormattedCellFactory(Format format) {
		this.format = format;
	}

	@Override
	public TableCell<T, V> call(TableColumn<T, V> column) {
		return new TableCell<T, V>() {
			@Override
			protected void updateItem(V value, boolean empty) {
				super.updateItem(value, empty);
				if (!empty) {
					setText(format.format(value));
				} else {
					setText(null);
				}
			}
		};
	}

}