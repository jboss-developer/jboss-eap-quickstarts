package org.kitchensinkfx.controller;

import java.util.List;

import org.kitchensinkfx.model.Member;
import org.kitchensinkfx.model.Messages;

/**
 * Handlers the server responses when you want to make a non thread locking
 * request
 * 
 * @author William Antônio
 * 
 */
public interface ServerResponseHandler {
	public void onMembersRetrieve(List<Member> members);

	public void onMemberCreation(Messages messages);
	
	public void onServerError(Exception e);
}
