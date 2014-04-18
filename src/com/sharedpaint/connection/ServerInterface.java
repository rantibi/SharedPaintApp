package com.sharedpaint.connection;

import java.util.List;

import com.sharedpaint.SharedPaintException;
import com.sharedpaint.drawables.Drawable;
import com.sharedpaint.transfer.BoardDetails;
import com.sharedpaint.transfer.BoardUpdate;

public interface ServerInterface {

	public abstract String getUserEmail();

	public abstract boolean loginFromCache() throws ConnectionException, AuthenticationException;

	public abstract boolean login(String email, String password)
			throws ConnectionException;

	public abstract void register(String email, String password)
			throws SharedPaintException, ConnectionException;

	public abstract void logout();

	public abstract List<BoardDetails> getBoards() throws SharedPaintException,
			ConnectionException;

	public abstract List<Long> getNewDrawaleIds(long boardId, int count)
			throws SharedPaintException, ConnectionException;

	public abstract void addNewDrawable(long boardId, Drawable drawable)
			throws SharedPaintException;

	public abstract BoardUpdate getDrawablesInBoard(long boardId)
			throws SharedPaintException, ConnectionException;

	public abstract BoardUpdate getBoardUpdate(long boardId, long from)
			throws SharedPaintException, ConnectionException;

	public abstract void undoInBoard(long boardId) throws SharedPaintException,
			ConnectionException;

	public abstract void redoInBoard(long boardId) throws SharedPaintException,
			ConnectionException;

	public abstract BoardDetails createNewBoard(String boardName)
			throws SharedPaintException, ConnectionException;

	public abstract List<String> getBoardMembers(long boardId)
			throws SharedPaintException, ConnectionException;

	public abstract void removeMemberFromBoard(long boardId, String userEmail)
			throws SharedPaintException, ConnectionException;

	public abstract void addMemberToBoard(long boardId, String userEmail)
			throws SharedPaintException, ConnectionException;

	public abstract void deleteBoard(long boardId) throws SharedPaintException,
			ConnectionException;

	public abstract void leaveBoard(long boardId) throws SharedPaintException,
			ConnectionException;

}