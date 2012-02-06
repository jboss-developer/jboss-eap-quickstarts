/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates,
 * and individual contributors as indicated by the @author tags.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2011,
 * @author JBoss, by Red Hat.
 */

package org.jboss.as.quickstarts.tempconvert.ejb;

import javax.ejb.Stateless;

/**
 * A simple SLSB EJB. The EJB does not use an interface.
 *
 * @author bwolfe@redhat.com
 */

@Stateless
public class TempConvertEJB {
    /**
     * This method takes a temperature in Celsius or Farhenheit and returns a converted temperature.
     *
     * @param temp the temperature to convert, assumes "xx.x [C|F]"
     * @return the converted temperature.
     */
    public String convert(String temperature) {
        
        double convertTemp, convertedTemp = 0;
        String convertTo = "Zero Kelvin";
        
	    // Check we got passed something - return Zero Kelvin for bad temp :-)
	    int len = temperature.length()-1;
        if (len <= 0) {
	        return "Zero Kelvin";
	    }
        
	    // Extract conversion from and source temperature.
	    String convertFrom = temperature.substring(len);
	    try {
	        convertTemp = Double.parseDouble(temperature.substring(0, len).trim());
	    }
	    catch (NumberFormatException e) {
	        return "Zero Kelvin";
	    }
	    
	    //Convert our Temperature
	    if (convertFrom.equalsIgnoreCase("C")) {
	        convertTo = " F";
	        convertedTemp = convertTemp * 9 / 5 + 32;
	    } else if (convertFrom.equalsIgnoreCase("F")) {
            convertTo = " C";
            convertedTemp = (convertTemp - 32) * 5 / 9;
	    }
	    
	    return convertTo.equalsIgnoreCase("Zero Kelvin") ? convertTo : convertedTemp + convertTo; 
	}
}
