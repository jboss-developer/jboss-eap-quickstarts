package org.jboss.as.quickstarts.kitchensink.decorator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistrationInt;

@Decorator
public class RegistrationDecorator implements MemberRegistrationInt{
	@Inject @Delegate MemberRegistrationInt registration;
	
	@Override
	public void register(Member member) throws Exception {

              BufferedWriter bw = null;

	      try {
                 // Writes into the AS data dir a log of all users registered.
	         bw = new BufferedWriter(new FileWriter(System.getProperty("jboss.server.data.dir") +"/kitchensink.dat", true));
		 bw.write("Registering User "+member.toString());
		 bw.newLine();
		 bw.flush();
	      } catch (IOException ioe) {
		 ioe.printStackTrace();
	      } finally {
		 if (bw != null) try {
		    bw.close();
		 } catch (IOException ioe2) {

		 }
	      } // end try/catch/finally



              registration.register(member);

	}

}
