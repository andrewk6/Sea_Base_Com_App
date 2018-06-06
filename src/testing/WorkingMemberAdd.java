package testing;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

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

public class WorkingMemberAdd {

	private static String group_id = "41189646";
	private static String testNumber = "2406886273";
	private static String number = "9103786594";
	private static String baseURL = "https://api.groupme.com/v3";
	private static String auth;

	public static void main(String[] args) {
		auth = readAuth();

		HttpClient client = HttpClientBuilder.create().build();
		makeGroup(client);
		addMember(client);
		deleteGroup(client);
	}
	
	public static void deleteGroup(HttpClient client) {
		HttpPost delGroup = new HttpPost(baseURL + "/groups/" + group_id + "/destroy?token=" + auth);
		try {
			HttpResponse resp = client.execute(delGroup);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void makeGroup(HttpClient client) {
		HttpPost nGroup = new HttpPost(baseURL + "/groups?token=" + auth);
		JSONObject groupData = new JSONObject();
		try {
			groupData.put("name", "Test2");
			groupData.put("description", "Create group");
			nGroup.setEntity(new StringEntity(groupData.toString()));
			HttpResponse resp = client.execute(nGroup);
			JSONObject rData =  respsonseJSONConvert(resp);
			group_id = (String) ((JSONObject)rData.get("response")).get("id");
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

	public static void delete(HttpClient client) {
		HttpGet get = new HttpGet(baseURL + "/groups/" + group_id + "?token=" + auth);

		try {
			HttpResponse resp = client.execute(get);
			JSONObject jO = respsonseJSONConvert(resp);
			JSONObject response = (JSONObject) jO.get("response");
			System.out.println(((JSONObject) jO.get("response")).get("name"));
			JSONArray arr = response.getJSONArray("members");
			// JSONArray arr = .get("members");
			for(int in = 0; in < arr.length(); in ++) {
				System.out.println(((JSONObject)arr.get(in)).get("nickname"));
			}
			deleteMember(client, (String) ((JSONObject) arr.get(2)).get("id"));
			System.out.println("Exit First");
			Thread.sleep(3000);
			deleteMember(client, (String) ((JSONObject) arr.get(1)).get("id"));
			System.out.println("Exit Second");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static JSONObject respsonseJSONConvert(HttpResponse resp) {
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

	public static void deleteMember(HttpClient client, String memberId) {
		System.out.println("Deleting: " + memberId);
		HttpPost post = new HttpPost(
				baseURL + "/groups/" + group_id + "/members/" + memberId + "/remove?token=" + auth);

		try {
			System.out.println("Delete Sam");
			HttpResponse resp = client.execute(post);
			System.out.println(resp.getStatusLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Exiting Delete");
	}

	public static void addMember(HttpClient client) {
		HttpPost post = new HttpPost(baseURL + "/groups/" + group_id + "/members/add?token=" + auth);
		JSONObject json = new JSONObject();

		try {
			JSONArray mems = new JSONArray();
			JSONObject jul = new JSONObject();
			jul.put("nickname", "Jules");
			jul.put("phone_number", testNumber);
			JSONObject sam = new JSONObject();
			sam.put("nickname", "Sam");
			sam.put("phone_number", number);
			mems.put(0, jul);
			mems.put(1, sam);
			json.put("members", mems);
			post.setEntity(new StringEntity(json.toString()));
			HttpResponse resp = client.execute(post);
			System.out.println("Reponse: ");
			System.out.println(resp.getStatusLine());
			System.out.println(resp.getEntity().getContentLength());
			System.out.println(resp.getEntity().getContent().read());
			System.out.println(Arrays.toString(resp.getAllHeaders()));
			System.out.println(resp.getParams());
			jsonPrint(resp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void jsonPrint(HttpResponse resp) {
		try {
			StringBuilder build = new StringBuilder();
			BufferedReader read = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
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

	public static String readAuth() {
		try {
			Scanner in = new Scanner(new File("C:\\Users\\Spyro\\Desktop\\Auth.bin"));
			String auth = in.nextLine();
			in.close();
			return auth;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}