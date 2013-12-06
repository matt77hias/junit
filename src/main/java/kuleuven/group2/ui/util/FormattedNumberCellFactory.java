package kuleuven.group2.ui.util;

import java.text.NumberFormat;

/**
 * Formats a number table column using the given {@link NumberFormat}.
 */
public class FormattedNumberCellFactory<T> extends FormattedCellFactory<T, Number> {

	public FormattedNumberCellFactory(NumberFormat format) {
		super(format);
	}

}