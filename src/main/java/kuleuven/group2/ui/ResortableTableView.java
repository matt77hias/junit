package kuleuven.group2.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import com.google.common.collect.Ordering;

/**
 * Re-sorts a {@link TableView} whenever the underlying
 * {@link TableView#itemsProperty() items list} changes.
 */
public class ResortableTableView<T> {

	protected final TableView<T> tableView;
	protected final ListChangeListener<T> listChangeListener;
	protected final ChangeListener<ObservableList<T>> changeListener;

	private boolean isResorting = false;

	public ResortableTableView(TableView<T> tableView) {
		this.tableView = tableView;
		this.listChangeListener = new ResortOnListChangeListener();
		this.changeListener = new RebindOnChangeListener<T>(listChangeListener);
	}

	public void setup() {
		tableView.itemsProperty().addListener(changeListener);
		tableView.getItems().addListener(listChangeListener);
	}

	public void teardown() {
		tableView.itemsProperty().removeListener(changeListener);
		tableView.getItems().removeListener(listChangeListener);
	}

	protected void resort() {
		if (!isResorting) {
			isResorting = true;
			FXCollections.sort(tableView.getItems(), createComparator());
			isResorting = false;
		}
	}

	/**
	 * Create a comparator which matches the {@link #tableView table}'s current
	 * {@link TableView#getSortOrder() sort order}.
	 * 
	 * @return
	 */
	protected Comparator<T> createComparator() {
		return createComparator(tableView.getSortOrder());
	}

	@SuppressWarnings("unchecked")
	protected static <T> Comparator<T> createComparator(List<TableColumn<T, ?>> sortColumns) {
		List<Comparator<T>> comparators = new ArrayList<>();
		for (TableColumn<T, ?> column : sortColumns) {
			comparators.add(new ColumnComparator(column));
		}
		return Ordering.compound(comparators);
	}

	/**
	 * Compare two items in a {@link TableColumn} as if they were being sorted
	 * in the {@link TableView}.
	 */
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	protected static class ColumnComparator implements Comparator {

		private final TableColumn column;

		public ColumnComparator(TableColumn column) {
			this.column = column;
		}

		@Override
		public int compare(Object o1, Object o2) {
			// Could not find a way to do this without casts unfortunately
			// Get the value of the column using the column's cell value factory
			final ObservableValue<?> obj1 = (ObservableValue) column.getCellValueFactory().call(
					new TableColumn.CellDataFeatures(column.getTableView(), column, o1));
			final ObservableValue<?> obj2 = (ObservableValue) column.getCellValueFactory().call(
					new TableColumn.CellDataFeatures(column.getTableView(), column, o2));
			// Compare the column values using the column's given comparator
			final int compare = column.getComparator().compare(obj1.getValue(), obj2.getValue());
			// Sort by proper ascending or descending
			return column.getSortType() == TableColumn.SortType.ASCENDING ? compare : -compare;
		}
	}

	/**
	 * Re-sort the table view whenever the list contents change.
	 */
	protected class ResortOnListChangeListener implements ListChangeListener<T> {

		@Override
		public void onChanged(ListChangeListener.Change<? extends T> change) {
			resort();
		}

	}

	/**
	 * Re-binds a {@link ListChangeListener} whenever the bound
	 * {@link ObservableList} changes.
	 */
	protected static class RebindOnChangeListener<T> implements ChangeListener<ObservableList<T>> {

		protected final ListChangeListener<T> listChangeListener;

		public RebindOnChangeListener(ListChangeListener<T> listChangeListener) {
			this.listChangeListener = listChangeListener;
		}

		@Override
		public void changed(ObservableValue<? extends ObservableList<T>> observable, ObservableList<T> oldList,
				ObservableList<T> newList) {
			oldList.removeListener(listChangeListener);
			newList.addListener(listChangeListener);
		}

	}

}
