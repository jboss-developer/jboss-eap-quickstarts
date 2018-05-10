package com.sample.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;



@Stateless
@Remote(SampleBeanRemote.class) 
public class  SampleBeanRemoteImpl implements SampleBeanRemote  {

	@Override
	public String echo(String s) {

		return "Hello "+s;
	}


}
