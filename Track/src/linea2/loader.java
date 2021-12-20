package linea2;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dialog.ModalityType;

public class loader extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final JPanel contentPanel = new JPanel();
	private static final String IMAGE_URL = "/resource/trace.jpg";

	

	/**
	 * Create the dialog.
	 */
	public loader() {
		
		//setOpacity(0.8f);
		setResizable(false);
		setAlwaysOnTop(true);
		setUndecorated(true);
		//setAutoRequestFocus(false);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		try {
			setContentPane(new JLabel(new ImageIcon(ImageIO.read(getClass().getResourceAsStream(IMAGE_URL)))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - this.getWidth()) / 2;
		final int y = (screenSize.height - this.getHeight()) / 2;
		setLocation(x, y);
		setVisible(true);
	}
	
	public void nascondi() {
		//setVisible(false);
	}

}
