import java.util.ArrayList;
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

public class Groupme_Com{
	private boolean paused;
	private HttpClient cl;
	
	public Groupme_Com() {
		paused = false;
		cl = HttpClientBuilder.create().build();
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public void deleteGroups() {
		
	}
	
	public void makeGroups(ArrayList<String> snorkel, ArrayList<String> docks,
			ArrayList<String> beach, ArrayList<String> gear, ArrayList<String> midweek) {
		
		
	}
}