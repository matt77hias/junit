package kuleuven.group2.ui.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

import com.google.common.base.Strings;

/**
 * {@link TextField} implementation that accepts formatted number and stores
 * them in a BigDecimal property. The user input is formatted when the focus is
 * lost or the user hits RETURN.
 * 
 * @author Thomas Bolz
 * @see <a
 *      href="http://java.dzone.com/articles/javafx-numbertextfield-and">Original
 *      article</a>
 */
public class NumberTextField extends TextField {

	protected final NumberFormat numberFormat;
	protected final ObjectProperty<BigDecimal> number = new SimpleObjectProperty<>();

	public NumberTextField() {
		this(BigDecimal.ZERO);
	}

	public NumberTextField(Number value) {
		this(value, NumberFormat.getInstance());
		initHandlers();
	}

	public NumberTextField(Number value, NumberFormat nf) {
		this.numberFormat = nf;
		initHandlers();
		setNumber(value);
	}

	public final BigDecimal getNumber() {
		return number.get();
	}

	public final void setNumber(BigDecimal value) {
		number.set(value);
	}

	public final void setNumber(Number value) {
		if (value == null) {
			setNumber(BigDecimal.ZERO);
		} else if (value instanceof BigDecimal) {
			setNumber((BigDecimal) value);
		} else if (value instanceof BigInteger) {
			setNumber(new BigDecimal((BigInteger) value));
		} else if (value instanceof Integer || value instanceof Long) {
			setNumber(new BigDecimal(value.longValue()));
		} else if (value instanceof Float || value instanceof Double) {
			setNumber(new BigDecimal(value.doubleValue()));
		} else {
			setNumber(value.toString());
		}
	}

	public final void setNumber(String value) {
		try {
			setNumber(new BigDecimal(value.toString()));
		} catch (NumberFormatException e) {
			setNumber(BigDecimal.ZERO);
		}
	}

	public ObjectProperty<BigDecimal> numberProperty() {
		return number;
	}

	private void initHandlers() {
		// Try to parse when focus is lost or RETURN is hit
		setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				parseAndFormatInput();
			}
		});

		focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue.booleanValue()) {
					parseAndFormatInput();
				}
			}
		});

		// Set text in field if BigDecimal property is changed from outside.
		numberProperty().addListener(new ChangeListener<BigDecimal>() {
			@Override
			public void changed(ObservableValue<? extends BigDecimal> obserable, BigDecimal oldValue,
					BigDecimal newValue) {
				setText(numberFormat.format(newValue));
			}
		});
	}

	/**
	 * Tries to parse the user input to a number according to the provided
	 * NumberFormat
	 */
	protected void parseAndFormatInput() {
		try {
			String input = getText();
			if (Strings.isNullOrEmpty(input)) return;
			Number parsedNumber = numberFormat.parse(input);
			BigDecimal newValue = new BigDecimal(parsedNumber.toString());
			setNumber(newValue);
			selectAll();
		} catch (ParseException ex) {
			// If parsing fails, keep old number
			setText(numberFormat.format(number.get()));
		}
	}

}
