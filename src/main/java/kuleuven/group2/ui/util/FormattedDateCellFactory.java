package kuleuven.group2.ui.util;

import java.text.DateFormat;
import java.util.Date;

/**
 * Formats a date table column using the given {@link DateFormat}.
 */
public class FormattedDateCellFactory<T> extends FormattedCellFactory<T, Date> {

	private final DateFormat dateFormat;

	public FormattedDateCellFactory(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	protected String format(Date value) {
		return dateFormat.format(value);
	}

}