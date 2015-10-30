package eu.europa.esig.dss.applet.main;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

/**
 * The main class containing the main method.
 */
public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	private final JFrame frame;

	Main(JFrame frame) {

		if (frame == null) {
			throw new NullPointerException("frame");
		}
		this.frame = frame;
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {

		// set system look and feel
		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			LOGGER.info("Set system look and feel.");
		} catch (ClassNotFoundException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		}
        /* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

				JFrame frame = new JFrame();
//				Main main = new Main(frame);
				// center on screen (need to set the panel first to obtain correct size)
				DSSAppletCore appletCore = new DSSAppletCore();
				appletCore.init();
				appletCore.setPreferredSize(new Dimension(600,400));
				frame.setContentPane(appletCore);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
