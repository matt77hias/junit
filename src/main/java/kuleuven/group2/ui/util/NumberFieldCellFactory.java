package kuleuven.group2.ui.util;

import java.text.NumberFormat;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class NumberFieldCellFactory<T> implements Callback<TableColumn<T, Number>, TableCell<T, Number>> {

	@Override
	public TableCell<T, Number> call(TableColumn<T, Number> column) {
		return new NumberFieldCell<>(NumberFormat.getIntegerInstance());
	}

	protected static class NumberFieldCell<T> extends TableCell<T, Number> {

		protected final NumberFormat numberFormat;
		protected NumberTextField numberField;

		public NumberFieldCell(NumberFormat numberFormat) {
			this.numberFormat = numberFormat;
		}

		protected String getItemText() {
			return getItem() != null ? numberFormat.format(getItem()) : "";
		}

		@Override
		public void startEdit() {
			if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) return;
			super.startEdit();
			if (numberField == null) {
				numberField = new NumberTextField(getItem(), numberFormat);
				numberField.setOnKeyReleased(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent t) {
						if (t.getCode().equals(KeyCode.ENTER)) {
							commitEdit(numberField.getNumber());
						} else if (t.getCode().equals(KeyCode.ESCAPE)) {
							cancelEdit();
						}
					}
				});
			}
			numberField.setText(getItemText());
			setText(null);
			setGraphic(numberField);
			numberField.selectAll();
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();
			setText(getItemText());
			setGraphic(null);
		}

		@Override
		protected void updateItem(Number value, boolean empty) {
			super.updateItem(value, empty);
			if (empty) {
				setText(null);
				setGraphic(null);
			} else if (isEditing()) {
				if (numberField != null) {
					numberField.setText(getItemText());
				}
				setText(null);
				setGraphic(numberField);
			} else {
				setText(getItemText());
				setGraphic(null);
			}
		}

	}

}
