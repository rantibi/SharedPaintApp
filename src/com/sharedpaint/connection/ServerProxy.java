package com.sharedpaint.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sharedpaint.IOUtils;
import com.sharedpaint.R;
import com.sharedpaint.SharedPaintException;
import com.sharedpaint.drawables.Drawable;
import com.sharedpaint.transfer.BoardDetails;
import com.sharedpaint.transfer.BoardUpdate;
import com.sharedpaint.transfer.DrawableHolder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class ServerProxy {
	private static ServerProxy instance = new ServerProxy();

	private static final String AUTHORIZATION = "Authorization";
	private static final String LOGGIN = "LOGGIN";
	private static final String URL = "http://192.168.0.131:8080/SharedPaintServer/sharedpaint";

	private Activity activity;
	private String authorization;

	public static ServerProxy getInstance(Activity activity) {
		instance.activity = activity;
		return instance;
	}

	public ServerProxy() {
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
	}

	public String getUserEmail() {
		if (authorization == null) {
			return null;
		}

		StringTokenizer st = new StringTokenizer(new String(Base64.decode(
				authorization, 0)), ":");
		return st.hasMoreTokens() ? st.nextToken() : null;
	}

	private WebResource getWebResource() {
		Client client = Client.create();
		return client.resource(URL);
	}

	public boolean loginFromCache() {
		SharedPreferences sharedPref = activity.getSharedPreferences(LOGGIN,
				Context.MODE_PRIVATE);
		authorization = sharedPref
				.getString(
						activity.getResources().getString(
								R.string.authorization), null);

		return authorization != null && login();
	}

	public boolean login(String email, String password) {
		authorization = Base64.encodeToString(
				(email + ":" + password).getBytes(), Base64.DEFAULT);

		if (!login()) {
			return false;
		}

		SharedPreferences sharedPref = activity.getSharedPreferences(LOGGIN,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(activity.getString(R.string.authorization),
				authorization);
		editor.commit();
		return true;

	}
	
	

	private boolean login() {
		ClientResponse response = getResponse(getWebResource().path("login"));
		return response.getStatus() == 200;
	}

	public void register(String email, String password) throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource()
				.path("/register")
				.queryParam("user_email", email)
				.queryParam("password", password));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}
		
		login(email, password);
	}
	public void logout() {
		SharedPreferences sharedPref = activity.getSharedPreferences(LOGGIN,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(activity.getString(R.string.authorization));
		editor.commit();
	}

	public List<BoardDetails> getBoards() throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource().path("boards"));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}

		Gson gson = new Gson();
		InputStreamReader result = new InputStreamReader(
				response.getEntityInputStream());
		Type collectionType = new TypeToken<List<BoardDetails>>() {
		}.getType();
		List<BoardDetails> boards = gson.fromJson(result, collectionType);

		return boards;
	}

	private ClientResponse getResponse(WebResource webResource) {
		return webResource.accept(MediaType.APPLICATION_JSON)
				.header(AUTHORIZATION, authorization).get(ClientResponse.class);
	}

	private String getStringEntity(ClientResponse response) {
		InputStream is = response.getEntityInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public List<Long> getNewDrawaleIds(long boardId, int count)
			throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource()
				.path("/new_drawble_ids")
				.queryParam("board_id", Long.toString(boardId))
				.queryParam("count", Integer.toString(count)));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}

		Gson gson = new Gson();
		InputStreamReader result = new InputStreamReader(
				response.getEntityInputStream());
		Type collectionType = new TypeToken<List<Long>>() {
		}.getType();
		List<Long> boards = gson.fromJson(result, collectionType);
		return boards;
	}

	public void addNewDrawable(long boardId, Drawable drawable)
			throws SharedPaintException {
		DrawableHolder drawableHolder = new DrawableHolder();
		try {
			byte[] bytes = IOUtils.ObjectToByteArray(drawable);
			drawableHolder.setId(drawable.getId());
			drawableHolder.setDrawable(bytes);

			HttpClient httpClient = new DefaultHttpClient();

			try {
				Map<String, String> formData = new HashMap<String, String>();
				formData.put("drawable", new Gson().toJson(drawableHolder));
				formData.put("board_id", Long.toString(boardId));

				HttpPost request = new HttpPost(URL + "/add_drawable_to_board");
				StringEntity params = new StringEntity("params="
						+ new Gson().toJson(formData));
				request.addHeader("content-type",
						"application/x-www-form-urlencoded");
				request.addHeader(AUTHORIZATION, authorization);
				request.setEntity(params);
				HttpResponse response = httpClient.execute(request);
				response.getStatusLine();
				// TODO handle response here...
			} catch (Exception ex) {
				// TODO: handle exception here
			} finally {
				httpClient.getConnectionManager().shutdown();
			}
		} catch (IOException e) {
			throw new SharedPaintException(e);
		}

	}

	public BoardUpdate getDrawablesInBoard(long boardId)
			throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource().path(
				"/drawables_in_board").queryParam("board_id",
				Long.toString(boardId)));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}

		Gson gson = new Gson();
		InputStreamReader result = new InputStreamReader(
				response.getEntityInputStream());
		return gson.fromJson(result, BoardUpdate.class);
	}

	public BoardUpdate getBoardUpdate(long boardId, long from)
			throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource()
				.path("/board_update")
				.queryParam("board_id", Long.toString(boardId))
				.queryParam("from", Long.toString(from)));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}

		Gson gson = new Gson();
		InputStreamReader result = new InputStreamReader(
				response.getEntityInputStream());
		return gson.fromJson(result, BoardUpdate.class);

	}

	public void undoInBoard(long boardId) throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource().path(
				"/board_undo").queryParam("board_id", Long.toString(boardId)));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}
	}

	public void redoInBoard(long boardId) throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource().path(
				"/board_redo").queryParam("board_id", Long.toString(boardId)));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}
	}

	public BoardDetails createNewBoard(String boardName)
			throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource().path(
				"/new_board").queryParam("board_name", boardName));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}

		Gson gson = new Gson();
		InputStreamReader result = new InputStreamReader(
				response.getEntityInputStream());
		return gson.fromJson(result, BoardDetails.class);
	}

	public List<String> getBoardMembers(long boardId)
			throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource().path(
				"/board_members")
				.queryParam("board_id", Long.toString(boardId)));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}

		Gson gson = new Gson();
		InputStreamReader result = new InputStreamReader(
				response.getEntityInputStream());
		Type collectionType = new TypeToken<List<String>>() {
		}.getType();
		return gson.fromJson(result, collectionType);
	}

	public void removeMemberFromBoard(long boardId, String userEmail)
			throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource()
				.path("/remove_board_member")
				.queryParam("board_id", Long.toString(boardId))
				.queryParam("user_email", userEmail));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}
	}

	public void addMemberToBoard(long boardId, String userEmail)
			throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource()
				.path("/add_board_member")
				.queryParam("board_id", Long.toString(boardId))
				.queryParam("user_email", userEmail));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}
	}

	public void deleteBoard(long boardId) throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource().path(
				"/delete_board").queryParam("board_id", Long.toString(boardId)));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}
	}

	public void leaveBoard(long boardId) throws SharedPaintException {
		ClientResponse response = getResponse(getWebResource().path(
				"/leave_board").queryParam("board_id", Long.toString(boardId)));

		if (response.getStatus() != 200) {
			throw new SharedPaintException(getStringEntity(response));
		}
	}

	

}
