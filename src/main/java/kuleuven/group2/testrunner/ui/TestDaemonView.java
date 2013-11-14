package kuleuven.group2.testrunner.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import kuleuven.group2.testrunner.TestDaemon;

import com.javarichclient.icon.tango.actions.MediaPlaybackStartIcon;
import com.javarichclient.icon.tango.actions.MediaPlaybackStopIcon;

public class TestDaemonView extends JFrame {

	private static final long serialVersionUID = -3861100747106978982L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestDaemonView frame = new TestDaemonView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JPanel contentPane;
	private JButton fbtnStart;
	private JButton fbtnStop;
	private LogPanel logPanel;

	private TestDaemon testDaemon;

	/**
	 * Create the frame.
	 */
	public TestDaemonView() {
		this.testDaemon = new TestDaemon();

		// Frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		// Content Panel
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[]", "[][][]"));

		initialize();
	}

	private TestDaemon getTestDaemon() {
		return this.testDaemon;
	}

	private void initialize() {

		this.fbtnStart = new JButton("");
		this.fbtnStart.setIcon(new MediaPlaybackStartIcon(32, 32));
		this.fbtnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getTestDaemon().start();
			}
		});
		this.contentPane.add(this.fbtnStart, "cell 0 0");

		this.fbtnStop = new JButton("");
		this.fbtnStop.setIcon(new MediaPlaybackStopIcon(32, 32));
		this.fbtnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getTestDaemon().stop();
			}
		});
		this.contentPane.add(this.fbtnStop, "cell 0 1");

		this.logPanel = new LogPanel(Logger.getGlobal());
		this.contentPane.add(this.logPanel, "cell 0 3");
	}
}
