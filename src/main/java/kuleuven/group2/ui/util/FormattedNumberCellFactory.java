package kuleuven.group2.ui.util;

import java.text.NumberFormat;

/**
 * Formats a number table column using the given {@link NumberFormat}.
 */
public class FormattedNumberCellFactory<T> extends FormattedCellFactory<T, Number> {

	private final NumberFormat numberFormat;

	public FormattedNumberCellFactory(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

	@Override
	protected String format(Number value) {
		return numberFormat.format(value);
	}

}