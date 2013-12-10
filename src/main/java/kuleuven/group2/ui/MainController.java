package kuleuven.group2.ui;

import java.io.IOException;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import kuleuven.group2.ui.model.PoliciesModel;
import kuleuven.group2.ui.model.PolicyModel;
import kuleuven.group2.ui.model.TestBatchesModel;

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
	private PolicyComposer policyComposer;

	@FXML
	private TestResults testResults;

	@FXML
	private Button buttonStart;

	@FXML
	private Button buttonStop;

	/*
	 * Properties
	 */

	private final BooleanProperty running = new SimpleBooleanProperty(false);
	private final PoliciesModel policiesModel = new PoliciesModel();
	private final TestBatchesModel testBatchesModel = new TestBatchesModel();

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
		configuration.selectedPolicyProperty().addListener(new ChangeListener<PolicyModel>() {
			@Override
			public void changed(ObservableValue<? extends PolicyModel> observable, PolicyModel oldValue,
					PolicyModel newValue) {
				setPolicy(newValue);
			}
		});

		// Bind policies model
		configuration.policiesProperty().bind(policiesModel.getAllPolicies());
		configuration.selectedPolicyProperty().set(policiesModel.getAllPolicies().get(0));

		policyComposer.allPoliciesProperty().bind(policiesModel.getAllPolicies());
		policyComposer.compositePoliciesProperty().bind(policiesModel.getCompositePolicies());

		// Bind test batches model
		testResults.batchesProperty().bind(testBatchesModel);
	}

	protected void setup() throws IOException {
		Store classSourceStore = new DirectoryStore(configuration.classSourceDirProperty().get());
		Store testSourceStore = new DirectoryStore(configuration.testSourceDirProperty().get());
		Store binaryStore = new DirectoryStore(configuration.binaryDirProperty().get());
		TestSortingPolicy sortPolicy = configuration.selectedPolicyProperty().get().getPolicy();
		pipeline = new Pipeline(classSourceStore, testSourceStore, binaryStore, sortPolicy);
		pipeline.getTestDatabase().addListener(testBatchesModel);
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
		configuration.selectedPolicyProperty().set(policyModel);
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
