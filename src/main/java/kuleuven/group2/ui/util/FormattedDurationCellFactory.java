package kuleuven.group2.ui.util;

/**
 * Formats a number table column as a duration.
 */
public class FormattedDurationCellFactory<T> extends FormattedCellFactory<T, Number> {

	@Override
	protected String format(Number value) {
		if (value == null) return null;

		long remaining = value.longValue();
		long milliseconds = remaining % 1000;
		long seconds = (remaining = (remaining / 1000) % 60);
		long minutes = (remaining = (remaining / 60) % 60);
		long hours = (remaining = (remaining / 60));

		return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
	}

}
