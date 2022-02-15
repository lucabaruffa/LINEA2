package linea;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class Go {
	private JWindow window;
	private Timer timer;
	private static main windowHome;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageIcon splash = new ImageIcon(Go.class.getResource("/resource/splash.gif"));

					JFrame frmFattura = new JFrame();
					frmFattura.setBackground(Color.black);
					Go g = new Go(frmFattura, splash, 10, false);

					Thread.sleep(2000);

					// windowHome = new main();
					Runnable target = new Runnable() {
						@Override
						public void run() {
							try {
								windowHome = new main();
								windowHome.Visible(true);

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					};

					new Thread(target).start();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Go(Window owner, ImageIcon imgIcon, int seconds, boolean closeable) {
		window = new JWindow(owner);

		Container contentPane = window.getContentPane();

		JLabel imageLabel = new JLabel(imgIcon);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension labelSize = imageLabel.getPreferredSize();

		contentPane.add(imageLabel, BorderLayout.CENTER);

		window.setBounds((screenSize.width - labelSize.width) / 2, (screenSize.height - labelSize.height) / 2,
				labelSize.width, labelSize.height);

		if (closeable) {
			window.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					// close ();
				}
			});
		}

		timer = new Timer(seconds * 1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});

		timer.setRepeats(false);
		timer.start();

		window.setVisible(true);

	}

	private void close() {
		timer.stop();
		window.setVisible(false);
		window.dispose();

		// windowHome.Visible(true);
		// windowHome.setVisible(true);
	}
}