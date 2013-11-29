package kuleuven.group2.ui;

import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.WindowEvent;
import kuleuven.group2.Pipeline;
import kuleuven.group2.policy.TestSortingPolicy;
import kuleuven.group2.store.DirectoryStore;
import kuleuven.group2.store.Store;
import kuleuven.group2.ui.model.PolicyModel;
import kuleuven.group2.ui.model.TestRunModel;
import kuleuven.group2.ui.model.TestRunsModel;

public class MainController implements EventHandler<WindowEvent> {

	/*
	 * Models
	 */

	private Pipeline pipeline;

	/*
	 * Components
	 */

	@FXML
	private Configuration configuration;

	@FXML
	private TestRuns testRuns;

	@FXML
	private Button buttonStart;

	@FXML
	private Button buttonStop;

	/*
	 * Properties
	 */

	public StringProperty classSourcesDirProperty() {
		return configuration.classSourceDirProperty();
	}

	public StringProperty testSourcesDirProperty() {
		return configuration.testSourceDirProperty();
	}

	public StringProperty binariesDirProperty() {
		return configuration.binaryDirProperty();
	}

	public ObjectProperty<PolicyModel> policyProperty() {
		return configuration.selectedPolicyProperty();
	}

	public ListProperty<TestRunModel> testRunsProperty() {
		return testRuns.runsProperty();
	}

	private final BooleanProperty running = new SimpleBooleanProperty(false);
	private final TestRunsModel testRunsModel = new TestRunsModel();

	public boolean isConfigured() {
		return running.get();
	}

	public BooleanBinding configured() {
		return configuration.configured();
	}

	protected BooleanProperty canConfigure() {
		return configuration.canConfigure();
	}

	public boolean isRunning() {
		return running.get();
	}

	protected void setRunning(boolean isRunning) {
		running.set(isRunning);
	}

	@FXML
	public void initialize() {
		// Disable reconfigurations while running
		canConfigure().bind(running.not());

		// Disable start/stop when not configured
		// Disable start/stop when (not) running
		buttonStart.disableProperty().bind(configured().not().or(running));
		buttonStop.disableProperty().bind(configured().not().or(running.not()));

		// Update policy in pipeline
		policyProperty().addListener(new ChangeListener<PolicyModel>() {
			@Override
			public void changed(ObservableValue<? extends PolicyModel> observable, PolicyModel oldValue,
					PolicyModel newValue) {
				setPolicy(newValue);
			}
		});

		// Bind test runs list
		testRunsProperty().bind(testRunsModel);
	}

	protected void setup() throws IOException {
		Store classSourceStore = new DirectoryStore(classSourcesDirProperty().get());
		Store testSourceStore = new DirectoryStore(testSourcesDirProperty().get());
		Store binaryStore = new DirectoryStore(binariesDirProperty().get());
		TestSortingPolicy sortPolicy = policyProperty().get().getPolicy();
		pipeline = new Pipeline(classSourceStore, testSourceStore, binaryStore, sortPolicy);
		pipeline.getTestDatabase().addListener(testRunsModel);
	}

	@FXML
	public void start() {
		try {
			setup();
			setRunning(true);
			new Thread(new Runnable() {
				@Override
				public void run() {
					pipeline.start();
				}
			}).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void stop() {
		setRunning(false);
		if (pipeline != null) {
			// Shut down, we won't re-use it
			pipeline.shutdown();
		}
	}

	public void setPolicy(PolicyModel policyModel) {
		policyProperty().set(policyModel);
		if (pipeline != null) {
			pipeline.setSortPolicy(policyModel.getPolicy());
		}
	}

	@Override
	public void handle(WindowEvent e) {
		// Shutdown pipeline
		stop();
	}

}
