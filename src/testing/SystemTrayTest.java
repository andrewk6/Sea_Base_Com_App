package testing;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class SystemTrayTest {
	BufferedImage icon;

	public SystemTrayTest() {
		Count c = new Count();
		icon = null;
		try {
			icon = ImageIO.read(new File("Sail Icon.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PopupMenu pop = new PopupMenu();
		TrayIcon tIcon = new TrayIcon(icon, "tray icon");
		SystemTray tray = SystemTray.getSystemTray();
		MenuItem stats = new MenuItem("Status");
		stats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, c.getVal());
			}
		});
		Menu actions = new Menu("Actions");
		MenuItem add = new MenuItem("Add");
		MenuItem del = new MenuItem("Delete");
		MenuItem pause = new MenuItem("Pause");
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(pause.getLabel().equals("Pause")) {
					try {
						c.wait();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					pause.setLabel("Start");
				}else {
					c.notify();
					pause.setLabel("Pause");
				}
//				c.stop();
			}
		});
		MenuItem exit = new MenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
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
		}
		c.run();
	}

	public class Count extends Thread{

		boolean running;
		int val = -100000;

		public void run() {
			running = true;
			while (running) {
				if (val == 20000)
					val = -100000;
				else
					val++;
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		public int getVal() {
			return val;
		}
	}

	public static void main(String[] args) {
		SystemTrayTest t = new SystemTrayTest();

	}
}