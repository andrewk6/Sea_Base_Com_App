import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*TODO
 * Import info from Stalker Sheet, hard written
 * Adding and deleting
 */
public class Groupme_Com {
	private final String auth = "?token=fgOPbOPGwP4PXXCrT0pfLaTMjtRBz3dy1DshiDKc";
	private final String baseURL = "https://api.groupme.com/v3";
	private final String errorID = "41325837";
	private final HashMap<String, String> stalkerSheet = new HashMap<String, String>();

	private ArrayList<String> snorkel, dock, beach, gear, midweek;
	private ArrayList<String> groupIDs;
	private boolean paused;
	private HttpClient cl;

	public Groupme_Com() {
		//fillStalker();
		snorkel = new ArrayList<String>();
		dock = new ArrayList<String>();
		beach = new ArrayList<String>();
		gear = new ArrayList<String>();
		midweek = new ArrayList<String>();
		paused = false;
		cl = HttpClientBuilder.create().build();
		groupIDs = new ArrayList<String>();

	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void deleteGroups() {
		for (String s : groupIDs) {
			HttpPost post = new HttpPost(baseURL + "/groups/" + s + "/destroy" + auth);
			try {
				HttpResponse resp = cl.execute(post);
				if (resp.getStatusLine().getStatusCode() != 200) {
					postError("Delete Error: " + resp.getStatusLine().getStatusCode());
				}
				post.releaseConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		groupIDs.clear();
		snorkel.clear();
		dock.clear();
		beach.clear();
		gear.clear();
		midweek.clear();
	}

	public void makeGroups() {
		makeGroup(snorkel, 0);
		makeGroup(dock, 1);
		makeGroup(beach, 2);
		makeGroup(gear, 3);
		makeGroup(midweek, 4);
	}
	
	public void makeGroup(ArrayList<String> mems, int i) {
		System.out.println("In makeGroup: Group Type ID = " + i + " Team Length: " + mems.size());
		String groupName = "";
		switch(i) {
		case 0: groupName = "Snorkel Team"; break;
		case 1: groupName = "Dock Team"; break;
		case 2: groupName = "Beach Team"; break;
		case 3: groupName = "Gear Team"; break;
		case 4: groupName = "Midweeks"; break;
		default: postError("Group Name Error, Invalid name code");
		}
		
		try {
			JSONObject cont = new JSONObject();
			cont.put("name", groupName);
			HttpPost post = new HttpPost(baseURL + "/groups" + auth);
			post.setEntity(new StringEntity(cont.toString()));
			HttpResponse resp = cl.execute(post);
			JSONObject respData = respsonseJSONConvert(resp);
			post.releaseConnection();
			String groupID = respData.getJSONObject("response").getString("id");
			groupIDs.add(groupID);
			
			JSONArray sendMems = new JSONArray();
			for(String s : mems) {
				System.out.println("Name: " + s + " / Number: " + stalkerSheet.get(s));
				JSONObject mem = new JSONObject();
				mem.put("nickname", s);
				mem.put("phone_number", stalkerSheet.get(s));
				sendMems.put(mem);
			}
			JSONObject postData = new JSONObject();
			postData.put("members", sendMems);
			post = new HttpPost(baseURL + "/groups/" + groupID + "/members/add" + auth);
			post.setEntity(new StringEntity(postData.toString()));
			resp = cl.execute(post);
			if(resp.getStatusLine().getStatusCode() != 202) {
				postError("Members Add Error: " + resp.getStatusLine().getStatusCode());
			}
			post.releaseConnection();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JSONObject respsonseJSONConvert(HttpResponse resp) {
		try {
			StringBuilder build = new StringBuilder();
			BufferedReader read = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
			String line;
			while ((line = read.readLine()) != null) {
				build.append(line);
			}
			return new JSONObject(build.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("Error parsing data " + e.toString());
		}
		return null;
	}

	public void postError(String text) {
		JFrame fr = new JFrame();
		fr.setAlwaysOnTop(true);
		fr.setVisible(false);
		JOptionPane.showMessageDialog(fr, "Talk to Andrew, an Error occured", "ERROR",
				JOptionPane.ERROR_MESSAGE);
		JSONObject msg = new JSONObject();
		try {
			msg.put("source_guid", "GUID");
			msg.put("text", text);
			HttpPost post = new HttpPost(baseURL + "/groups/" + errorID + "/messages");
			post.setEntity(new StringEntity(msg.toString()));
			cl.execute(post);
			post.releaseConnection();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<String> getSnorkel() {
		return snorkel;
	}

	public void setSnorkel(ArrayList<String> snorkel) {
		this.snorkel = snorkel;
	}

	public ArrayList<String> getDock() {
		return dock;
	}

	public void setDock(ArrayList<String> dock) {
		this.dock = dock;
	}

	public ArrayList<String> getBeach() {
		return beach;
	}

	public void setBeach(ArrayList<String> beach) {
		this.beach = beach;
	}

	public ArrayList<String> getGear() {
		return gear;
	}

	public void setGear(ArrayList<String> gear) {
		this.gear = gear;
	}

	public ArrayList<String> getMidweek() {
		return midweek;
	}

	public void setMidweek(ArrayList<String> midweek) {
		this.midweek = midweek;
	}
	
	public void fillStalker() {
		stalkerSheet.put("Lukas", "7062470014");
		stalkerSheet.put("Adam", "4352137528");
		stalkerSheet.put("Will J.", "8435303527");
		stalkerSheet.put("Tara", "8473370337");
		stalkerSheet.put("Anna", "3037269646");
		stalkerSheet.put("Sean", "6103143092");
		stalkerSheet.put("Jake", "6304577192");
		stalkerSheet.put("Kyle", "9722519716");
		stalkerSheet.put("Ozzy", "8032398193");
		stalkerSheet.put("Courtney", "7172090957");
		stalkerSheet.put("Brad", "8176946088");
		stalkerSheet.put("Patrick", "4076229167");
		stalkerSheet.put("Graci", "5186308723");
		stalkerSheet.put("Thomas", "7039653038");
		stalkerSheet.put("Noah", "7656674075");
		stalkerSheet.put("Drew A.", "5402923482");
		stalkerSheet.put("Jimmy", "3367105629");//NEED NUMBER
		stalkerSheet.put("Kory", "3146297112");
		stalkerSheet.put("Lexi", "8029890149");
		stalkerSheet.put("Josh", "8179755390");
		stalkerSheet.put("Lacey", "2552295323");
		stalkerSheet.put("Lukas", "7062470014");
		stalkerSheet.put("Andrew K.", "3015008562");
		stalkerSheet.put("Julia", "2406886273");
		stalkerSheet.put("Alex", "5044013752");
		stalkerSheet.put("Max", "9375722375");
		stalkerSheet.put("Kumi", "8474214917");
		stalkerSheet.put("John B.", "4784476157");
		stalkerSheet.put("Mike O.", "3167063366");
		stalkerSheet.put("Jacob", "9102321342");
	}
	public HashMap<String, String> getStalker() {
		return stalkerSheet;
	}
	
	public static void jsonPrint(JSONObject resp) {
		try {
			StringBuilder build = new StringBuilder();
			BufferedReader read = new BufferedReader(new InputStreamReader(
					new StringEntity(resp.toString()).getContent()));
			String line;
			while ((line = read.readLine()) != null) {
				build.append(line);
			}
			System.out.println("Convert to json:");
			JSONObject jsonO = new JSONObject(build.toString());
			Iterator it = jsonO.keys();
			while (it.hasNext()) {
				System.out.println(jsonO.get((String) it.next()));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("Error parsing data " + e.toString());
		}
	}
}