import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.http.HttpResponse;
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
	private static String baseURL = "https://api.groupme.com/v3";
	private static String auth;

	public static void main(String[] args) {
		auth = readAuth();

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(baseURL + "/groups/" + group_id + "?token=" + auth);
		
		try {
			HttpResponse resp = client.execute(get);
			JSONObject jO =  respsonseJSONConvert(resp);
			JSONObject response = (JSONObject) jO.get("response");
			System.out.println(((JSONObject)jO.get("response")).get("name"));
			JSONArray arr = response.getJSONArray("members");
//			JSONArray arr = .get("members");
			System.out.println((String) ((JSONObject)arr.get(1)).get("id") 
					+ "\n" + (String) ((JSONObject)arr.get(1)).get("user_id"));
			deleteMember(client, (String) ((JSONObject)arr.get(1)).get("id"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
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
		HttpPost post = new HttpPost(baseURL + "/groups/" + group_id + 
				"/members/" + memberId +"/remove?token=" + auth);
		
		try {
			HttpResponse resp = client.execute(post);
			System.out.println(resp.getStatusLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void addMember(HttpClient client) {
		HttpPost post = new HttpPost(baseURL + "/groups/" + group_id + "/members/add?token=" + auth);
		JSONObject json = new JSONObject();

		try {
			JSONArray mems = new JSONArray();
			JSONObject jul = new JSONObject();
			jul.put("nickname", "Jules");
			jul.put("phone_number", testNumber);
			mems.put(0, jul);
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