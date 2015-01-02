/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/

package com.gisgraphy.helper;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * Provides some usefull methods
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class HTMLHelper {

    protected static final Logger logger = Logger
	    .getLogger(HTMLHelper.class);

    /**
     * @param req
     *                The HttpServletRequest from which we'd like to test the
     *                parameters
     * @param parameterNames
     *                the parameters to tests
     * @return true if one of the parameters is null or equals to an empty
     *         string
     */
    public static boolean isParametersEmpty(HttpServletRequest req,
	    String... parameterNames) {
	for (String parameter : parameterNames) {
	    if (req.getParameter(parameter) == null
		    || "".equals(req.getParameter(parameter).trim())) {
		return true;
	    }
	}
	return false;
    }

}
