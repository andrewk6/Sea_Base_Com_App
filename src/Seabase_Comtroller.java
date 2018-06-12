import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Seabase_Comtroller{
	BufferedImage icon;
	HashMap<String, String> docks;
	HashMap<String, String> snorkle;
	HashMap<String, String> beach;
	HashMap<String, String> gear;
	HashMap<String, String> midweek;
	
	private int mHour, eHour, mMin, eMin;
	
	
	private final Font header = new Font("Monospaced", Font.BOLD, 12);
	private final Font body = new Font("Monospaced", Font.PLAIN, 10);
	
	private Groupme_Com gCom;
	private DataScan dScan;
	private ScheduledExec mExec, eExec;
	
	//private Calendar morn, even;
	
	
	public Seabase_Comtroller() {
		gCom = new Groupme_Com();
		dScan = new DataScan(gCom);
		//morn = Calendar.getInstance();
		
		loadConfig();
		
		mExec = new ScheduledExec(new Runnable() {
			public void run() {
				dScan.readExcel();
				gCom.makeGroups();
			}
		}, mHour, mMin, 0);
		eExec = new ScheduledExec(new Runnable() {
			public void run() {
				gCom.deleteGroups();
			}
		}, (eHour + 12), eMin, 0);
		mExec.startExecution();
		eExec.startExecution();
		buildTrayIcon();
	}
	
	public void loadConfig() {
		try {
			Scanner in = new Scanner(new File("Config.bin"));
			in.nextLine();
			mHour = in.nextInt();
			mMin = in.nextInt();
			eHour = in.nextInt();
			eMin = in.nextInt();
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean buildTrayIcon() {
		icon = null;
		try {
			icon = ImageIO.read(this.getClass().getResourceAsStream("Sail Icon.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		PopupMenu pop = new PopupMenu();
		/*pop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayMenu();
			}
		});*/
		MenuItem pause = new MenuItem("Pause");
		TrayIcon tIcon = new TrayIcon(icon, "Sea Base Com App");
		SystemTray tray = SystemTray.getSystemTray();
		MenuItem stats = new MenuItem("Status");
		stats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayMenu(pause);
			}
		});
		Menu actions = new Menu("Actions");
		MenuItem add = new MenuItem("Add");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dScan.readExcel();
				gCom.makeGroups();
			}
		});
		MenuItem del = new MenuItem("Delete");
		del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gCom.deleteGroups();
			}
		});
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(gCom.isPaused()) {
					gCom.setPaused(false);
					pause.setLabel("Pause");
				}else {
					gCom.setPaused(true);
					int clear = JOptionPane.showConfirmDialog(null, "Clear groups now?", "Pause Clear Confirm",
							JOptionPane.YES_NO_OPTION);
					if(clear == JOptionPane.YES_OPTION) {
						gCom.deleteGroups();
					}
					pause.setLabel("Start");
				}
			}
		});
		MenuItem exit = new MenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int yesNo = JOptionPane.showConfirmDialog(null, "Do you wish to clear goups before exit",
						"Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
				if(yesNo == JOptionPane.YES_OPTION || yesNo == JOptionPane.NO_OPTION) {
					if(yesNo == JOptionPane.YES_OPTION) {
						gCom.deleteGroups();
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
	
	public void displayMenu(MenuItem pauseBtn) {
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
		Text morn = new Text("Morning Time(AM): ");
		morn.setBorder(null);
		morn.setFont(header);
		sChangeTimes.add(morn);
		JTextField mTField = new JTextField();
		mTField.setText(((mHour < 10) ? "0" + mHour : mHour) + ":" + ((mMin < 10) ? "0" + mMin : mMin));
		mTField.setColumns(5);
		sChangeTimes.add(mTField);
		Text even = new Text("Evening Time(PM): ");
		even.setBorder(null);
		even.setFont(header);
		sChangeTimes.add(even);
		JTextField eTField = new JTextField();
		eTField.setText(((eHour < 10) ? "0" + eHour : eHour) + ":" + ((eMin < 10) ? "0" + eMin : eMin));
		eTField.setColumns(5);
		sChangeTimes.add(eTField);
		JButton btn = new JButton("Update Time");
		sChangeTimes.add(btn);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTime(eTField.getText(), mTField.getText());
			}
		});
		JButton pauseRes = new JButton(pauseBtn.getLabel());
		pauseRes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(gCom.isPaused()) {
					gCom.setPaused(false);
					pauseRes.setText("Pause");
					pauseBtn.setLabel("Pause");
				}else {
					gCom.setPaused(true);
					int clear = JOptionPane.showConfirmDialog(null, "Clear groups now?", "Pause Clear Confirm",
							JOptionPane.YES_NO_OPTION);
					if(clear == JOptionPane.YES_OPTION) {
						gCom.deleteGroups();
					}
					pauseRes.setText("Start");
					pauseBtn.setLabel("Start");
				}
			}
		});
		sChangeTimes.add(pauseRes);
		
		JPanel cDisplay = new JPanel();
		//cDisplay.setPreferredSize(new Dimension(400, 300));
		sChangeTimes.setLayout(new FlowLayout());
		int rows = gCom.getBeach().size() + gCom.getDock().size() + gCom.getGear().size() +
				gCom.getMidweek().size() + gCom.getSnorkel().size() + 1;
		System.out.println("Rows: " + rows);
		cDisplay.setLayout(new GridLayout(rows, 2));
		//cDisplay.add(new Text("Modify"), 0, 0);
		cDisplay.add(new Text("Name", Text.TYPE_HEADER));
		cDisplay.add(new Text("Position", Text.TYPE_HEADER));
		for(String s : gCom.getSnorkel()) {
			cDisplay.add(new Text(s));
			cDisplay.add(new Text("Snorkel"));
		}
		for(String s : gCom.getDock()) {
			cDisplay.add(new Text(s));
			cDisplay.add(new Text("Dock"));
		}
		for(String s : gCom.getBeach()) {
			cDisplay.add(new Text(s));
			cDisplay.add(new Text("Beach/Luau"));
		}
		for(String s : gCom.getGear()) {
			cDisplay.add(new Text(s));
			cDisplay.add(new Text("Gear"));
		}
		for(String s : gCom.getMidweek()) {
			cDisplay.add(new Text(s));
			cDisplay.add(new Text("Midweek"));
		}
		
		
		JScrollPane cScrollDisplay = new JScrollPane(cDisplay);
		cScrollDisplay.setPreferredSize(new Dimension(400, 300));
		content.add(cScrollDisplay, BorderLayout.CENTER);
		content.add(sChangeTimes, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public void updateTime(String m, String e) {
		try {
			int mH = Integer.parseInt(m.split(":")[0]);
			int mM = Integer.parseInt(m.split(":")[1]);
			int eH = Integer.parseInt(e.split(":")[0]);
			int eM = Integer.parseInt(e.split(":")[1]);
			
			if(mH <= 12 && mH >= 1 && mM <= 59 && mM >= 0 && 
					eH <= 12 && eH >= 1 && eM <= 59 && eM >= 0) {
				try {
					Scanner in = new Scanner(new File("Config.bin"));
					PrintWriter write = new PrintWriter(new FileWriter(new File("Config.bin")));
					write.println(in.nextLine());
					write.println(mH);
					write.println(mM);
					write.println(eH);
					write.println(eM);
					in.close();
					write.close();
					this.mHour = mH;
					this.mMin = mM;
					this.eHour = eH;
					this.eMin = eM;
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else {
				JOptionPane.showMessageDialog(null, "Invalid Number", 
						"Error Message", JOptionPane.ERROR_MESSAGE);
			}
		}catch(NumberFormatException err) {
			JOptionPane.showMessageDialog(null, "Invalid Number", 
					"Error Message", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	class Text extends JTextField{
		public static final int TYPE_HEADER = 0;
		public static final int TYPE_BODY = 1;
		
		public Text() {
			super();
			this.setEditable(false);
		}
		
		public Text(String s) {
			super();
			this.setEditable(false);
			this.setText(s);
			this.setFont(body);
		}
		
		public Text(String s, int style) {
			super();
			this.setEditable(false);
			this.setText(s);
			this.setFont(((style == Text.TYPE_HEADER) ? header : body));
		}
	}
	
	public static void main(String[]args) {
		Seabase_Comtroller cCom = new Seabase_Comtroller();
	}
}