import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Seabase_Comtroller{
	BufferedImage icon;
	HashMap<String, String> docks;
	HashMap<String, String> snorkle;
	HashMap<String, String> beach;
	HashMap<String, String> gear;
	HashMap<String, String> midweek;
	
	Groupme_Com gCom;
	DataScan dScan;
	
	
	public Seabase_Comtroller() {
		buildTrayIcon();
	}
	
	public boolean buildTrayIcon() {
		icon = null;
		try {
			icon = ImageIO.read(new File("Sail Icon.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		PopupMenu pop = new PopupMenu();
		pop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayMenu();
			}
		});
		TrayIcon tIcon = new TrayIcon(icon, "tray icon");
		SystemTray tray = SystemTray.getSystemTray();
		MenuItem stats = new MenuItem("Status");
		stats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayMenu();
			}
		});
		Menu actions = new Menu("Actions");
		MenuItem add = new MenuItem("Add");
		MenuItem del = new MenuItem("Delete");
		MenuItem pause = new MenuItem("Pause");
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		MenuItem exit = new MenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int yesNo = JOptionPane.showConfirmDialog(null, "Do you wish to clear goups before exit",
						"Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
				if(yesNo == JOptionPane.YES_OPTION || yesNo == JOptionPane.NO_OPTION) {
					if(yesNo == JOptionPane.YES_OPTION) {
						
					}
					System.exit(0);
				}
			}
		});
		pop.add(stats);
		pop.addSeparator();
		actions.add(add);
		actions.addSeparator();
		actions.add(del);
		pop.add(actions);
		pop.addSeparator();
		pop.add(pause);
		pop.addSeparator();
		pop.add(exit);
		tIcon.setPopupMenu(pop);
		try {
			tray.add(tIcon);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void displayMenu() {
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {frame.dispose();}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {frame.dispose();}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			
		});
		
		JPanel content = new JPanel();
		frame.setContentPane(content);
		content.setLayout(new BorderLayout());
		JPanel sChangeTimes = new JPanel();
		JPanel cDisplay = new JPanel();
		cDisplay.setPreferredSize(new Dimension(400, 300));
		sChangeTimes.setLayout(new FlowLayout());
		cDisplay.setLayout(new GridLayout(1, 3));
		cDisplay.add(new Text("Modify"), 0, 0);
		cDisplay.add(new Text("Name"), 0, 1);
		cDisplay.add(new Text("Position"), 0, 2);
		
		
		content.add(cDisplay, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	
	class Text extends JTextField{
		public Text() {
			super();
			this.setEditable(false);
		}
		
		public Text(String s) {
			super();
			this.setEditable(false);
			this.setText(s);
		}
	}
	
	public static void main(String[]args) {
		Seabase_Comtroller cCom = new Seabase_Comtroller();
	}
}