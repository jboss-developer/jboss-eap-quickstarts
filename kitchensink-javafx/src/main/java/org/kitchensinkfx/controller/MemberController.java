package org.kitchensinkfx.controller;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.util.GenericType;
import org.kitchensinkfx.model.Member;
import org.kitchensinkfx.model.Messages;

/**
 * 
 * Our application controler. This class modify the model according the view
 * actions. It uses REST call the perform the update.
 * 
 * @author William Antônio
 *
 */
public class MemberController {
	/**
	 * The Base Url for the service
	 */
	private String applicationUrl = "http://localhost:8080/jboss-as-kitchensink/rest/members";

	/**
	 * 
	 * It simply makes an HTTP request to create a Member
	 * 
	 * @param member
	 */
	@SuppressWarnings("unchecked")
	public Messages createMember(Member member) {
		ClientRequest cr = new ClientRequest(applicationUrl);
		cr.body(MediaType.APPLICATION_JSON, member);
		try {
			return (Messages) cr.post().getEntity(Messages.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void createMember(final Member member,
			final ServerResponseHandler serverResponseHandler) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					serverResponseHandler
							.onMemberCreation(createMember(member));
				} catch (Exception e) {
					serverResponseHandler.onServerError(e);
				}
			}
		});
		t.start();
	}

	@SuppressWarnings("unchecked")
	public List<Member> getAllMembers() throws Exception {
		ClientRequest cr = new ClientRequest(applicationUrl);
		cr.accept(MediaType.APPLICATION_JSON);
		return (List<Member>) cr.get().getEntity(
				new GenericType<List<Member>>() {
				});
	}

	public void getAllMembers(final ServerResponseHandler serverResponseHandler) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					serverResponseHandler.onMembersRetrieve(getAllMembers());
				} catch (Exception e) {
					serverResponseHandler.onServerError(e);
				}
			}
		});
		t.start();
	}
}
