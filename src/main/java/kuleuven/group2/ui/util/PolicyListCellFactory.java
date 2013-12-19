package kuleuven.group2.ui.util;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import kuleuven.group2.ui.model.PolicyModel;

public class PolicyListCellFactory implements Callback<ListView<PolicyModel>, ListCell<PolicyModel>> {
	@Override
	public ListCell<PolicyModel> call(ListView<PolicyModel> view) {
		return new ListCell<PolicyModel>() {
			@Override
			protected void updateItem(PolicyModel model, boolean empty) {
				super.updateItem(model, empty);
				if (!empty) {
					textProperty().bind(model.nameProperty());
				} else {
					textProperty().unbind();
				}
			}
		};
	}
}