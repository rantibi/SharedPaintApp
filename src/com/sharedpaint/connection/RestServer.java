package com.sharedpaint.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sharedpaint.IOUtils;
import com.sharedpaint.SharedPaintException;
import com.sharedpaint.drawables.Drawable;
import com.sharedpaint.transfer.BoardDetails;
import com.sharedpaint.transfer.BoardUpdate;
import com.sharedpaint.transfer.DrawableHolder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.spi.service.ServiceFinder;

/**
 * Server connection implementation - Rest connection
 * User jersey framework for Http transactions
 */
public class RestServer implements ServerInterface {
	
	private static final String AUTHORIZATION = "Authorization";
	private String authorization;
	
	public RestServer() {
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		ServiceFinder.setIteratorProvider(new AndroidServiceIteratorProvider());
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
	}
	
	private String getUrl() {
		return "http://" + ConfigManager.getInstance().getHost() + ":" + ConfigManager.getInstance().getPort() + "/SharedPaintServer/sharedpaint";
		
	}
	
	private WebResource getWebResource() {
		Client client = Client.create();
		client.setConnectTimeout(1000);
		return client.resource(getUrl());
	}

	@Override
	public String getUserEmail() {
		if (authorization == null) {
			return null;
		}

		StringTokenizer st = new StringTokenizer(new String(Base64.decode(
				authorization, 0)), ":");
		return st.hasMoreTokens() ? st.nextToken() : null;
	}
	
	private boolean login() throws ConnectionException {
		ClientResponse response;
		try {
			response = getResponse(getWebResource().path("login"));
		} catch (AuthenticationException e) {
			return false;
		}
		return response.getStatus() == 200;
	}
	
	private ClientResponse getResponse(WebResource webResource) throws ConnectionException, AuthenticationException {
		try{
		return webResource.accept(MediaType.APPLICATION_JSON)
				.header(AUTHORIZATION, authorization).get(ClientResponse.class);
		} catch (ClientHandlerException e) {
			if (e.getMessage().indexOf("No authentication challenges found") == -1) {
				throw new ConnectionException("No connection to the server");
			} else {
				throw new AuthenticationException();
			}
		}
		catch (RuntimeException e) {
			throw new ConnectionException("No connection to the server");
		}
	}

	public boolean loginFromCache() throws ConnectionException, AuthenticationException {
		authorization = ConfigManager.getInstance().getValue(AUTHORIZATION);
		return authorization != null && login();
	}

	@Override
	public boolean login(String email, String password)
			throws ConnectionException {
		authorization = Base64.encodeToString(
				(email + ":" + password).getBytes(), Base64.DEFAULT);

		if (!login()) {
			return false;
		}

		ConfigManager.getInstance().setValue(AUTHORIZATION, authorization);		
		return true;
	}
	
	private void checkResponseStatus(ClientResponse response)
			throws SharedPaintException {
		
		switch (response.getStatus()) {
		case 200:
			return;
		case 401:
			throw new AuthenticationException(getStringEntity(response));
		default:
			throw new SharedPaintException(getStringEntity(response));
		}
		
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

	@Override
	public void register(String email, String password)
			throws SharedPaintException, ConnectionException {
		ClientResponse response = getResponse(getWebResource()
				.path("/register")
				.queryParam("user_email", email)
				.queryParam("password", password));

		checkResponseStatus(response);
		
		login(email, password);
	}

	@Override
	public void logout() {
		ConfigManager.getInstance().setValue(AUTHORIZATION, null);		
	}

	@Override
	public List<BoardDetails> getBoards() throws SharedPaintException,
			ConnectionException {
		ClientResponse response = getResponse(getWebResource().path("boards"));

		checkResponseStatus(response);

		Gson gson = new Gson();
		InputStreamReader result = new InputStreamReader(
				response.getEntityInputStream());
		Type collectionType = new TypeToken<List<BoardDetails>>() {
		}.getType();
		List<BoardDetails> boards = gson.fromJson(result, collectionType);

		return boards;
	}

	
	@Override
	public List<Long> getNewDrawaleIds(long boardId, int count)
			throws SharedPaintException, ConnectionException {
		ClientResponse response = getResponse(getWebResource()
				.path("/new_drawble_ids")
				.queryParam("board_id", Long.toString(boardId))
				.queryParam("count", Integer.toString(count)));

		checkResponseStatus(response);

		Gson gson = new Gson();
		InputStreamReader result = new InputStreamReader(
				response.getEntityInputStream());
		Type collectionType = new TypeToken<List<Long>>() {
		}.getType();
		List<Long> boards = gson.fromJson(result, collectionType);
		return boards;
	}
	
	

	@Override
	public void addNewDrawable(long boardId, Drawable drawable)
			throws SharedPaintException {
		DrawableHolder drawableHolder = new DrawableHolder();
		try {
			byte[] bytes = IOUtils.ObjectToByteArray(drawable);
			drawableHolder.setId(drawable.getId());
			drawableHolder.setDrawable(bytes);

			
			MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
			formData.add("drawable", new Gson().toJson(drawableHolder));			
			ClientResponse response = getWebResource().path("add_drawable_to_board")
					.queryParam("board_id", Long.toString(boardId))
					.type(MediaType.APPLICATION_FORM_URLENCODED).header(AUTHORIZATION, authorization).post(ClientResponse.class, formData);
			int status = response.getStatus();
		} catch (IOException e) {
			throw new SharedPaintException(e);
		}

	}

	@Override
	public BoardUpdate getDrawablesInBoard(long boardId)
			throws SharedPaintException, ConnectionException {
		ClientResponse response = getResponse(getWebResource().path(
				"/drawables_in_board").queryParam("board_id",
				Long.toString(boardId)));

		checkResponseStatus(response);

		Gson gson = new Gson();
		InputStreamReader result = new InputStreamReader(
				response.getEntityInputStream());
		return gson.fromJson(result, BoardUpdate.class);
	}

	@Override
	public BoardUpdate getBoardUpdate(long boardId, long from)
			throws SharedPaintException, ConnectionException {
		ClientResponse response = getResponse(getWebResource()
				.path("/board_update")
				.queryParam("board_id", Long.toString(boardId))
				.queryParam("from", Long.toString(from)));

		checkResponseStatus(response);

		Gson gson = new Gson();
		InputStreamReader result = new InputStreamReader(
				response.getEntityInputStream());
		return gson.fromJson(result, BoardUpdate.class);

	}

	@Override
	public void undoInBoard(long boardId) throws SharedPaintException, ConnectionException {
		ClientResponse response = getResponse(getWebResource().path(
				"/board_undo").queryParam("board_id", Long.toString(boardId)));

		checkResponseStatus(response);
	}

	/* (non-Javadoc)
	 * @see com.sharedpaint.connection.ServerInterface#redoInBoard(long)
	 */
	@Override
	public void redoInBoard(long boardId) throws SharedPaintException, ConnectionException {
		ClientResponse response = getResponse(getWebResource().path(
				"/board_redo").queryParam("board_id", Long.toString(boardId)));

		checkResponseStatus(response);
	}

	@Override
	public BoardDetails createNewBoard(String boardName)
			throws SharedPaintException, ConnectionException {
		ClientResponse response = getResponse(getWebResource().path(
				"/new_board").queryParam("board_name", boardName));

		checkResponseStatus(response);

		Gson gson = new Gson();
		InputStreamReader result = new InputStreamReader(
				response.getEntityInputStream());
		return gson.fromJson(result, BoardDetails.class);
	}

	@Override
	public List<String> getBoardMembers(long boardId)
			throws SharedPaintException, ConnectionException {
		ClientResponse response = getResponse(getWebResource().path(
				"/board_members")
				.queryParam("board_id", Long.toString(boardId)));

		checkResponseStatus(response);

		Gson gson = new Gson();
		InputStreamReader result = new InputStreamReader(
				response.getEntityInputStream());
		Type collectionType = new TypeToken<List<String>>() {
		}.getType();
		return gson.fromJson(result, collectionType);
	}

	@Override
	public void removeMemberFromBoard(long boardId, String userEmail)
			throws SharedPaintException, ConnectionException {
		ClientResponse response = getResponse(getWebResource()
				.path("/remove_board_member")
				.queryParam("board_id", Long.toString(boardId))
				.queryParam("user_email", userEmail));

		checkResponseStatus(response);
	}

	@Override
	public void addMemberToBoard(long boardId, String userEmail)
			throws SharedPaintException, ConnectionException {
		ClientResponse response = getResponse(getWebResource()
				.path("/add_board_member")
				.queryParam("board_id", Long.toString(boardId))
				.queryParam("user_email", userEmail));

		checkResponseStatus(response);
	}

	@Override
	public void deleteBoard(long boardId) throws SharedPaintException, ConnectionException {
		ClientResponse response = getResponse(getWebResource().path(
				"/delete_board").queryParam("board_id", Long.toString(boardId)));

		checkResponseStatus(response);
	}
	
	@Override
	public void leaveBoard(long boardId) throws SharedPaintException, ConnectionException {
		ClientResponse response = getResponse(getWebResource().path(
				"/leave_board").queryParam("board_id", Long.toString(boardId)));

		checkResponseStatus(response);
	}

}
