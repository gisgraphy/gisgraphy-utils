/*******************************************************************************
 * Gisgraphy Project 
 *  
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *  
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 *  
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 *  
 *   Copyright 2008  Gisgraphy project 
 * 
 *   David Masclet <davidmasclet@gisgraphy.com>
 ******************************************************************************/
package com.gisgraphy.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.exception.AddressParserErrorVisitor;
import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.addressparser.web.AddressQueryHttpBuilder;
import com.gisgraphy.domain.Constants;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.helper.HTMLHelper;
import com.gisgraphy.serializer.common.IoutputFormatVisitor;
import com.gisgraphy.serializer.common.OutputFormat;

public abstract class AbstractAddressServlet extends GisgraphyServlet {

    /**
     * The logger
     */
    protected static Logger logger = Logger.getLogger(AbstractAddressServlet.class);

    public final static String ADDRESS_PARAMETER = "address";
    public final static String COUNTRY_PARAMETER = "country";
    public final static String OUTPUT_FORMAT_PARAMETER = "format";
    public final static String CALLBACK_PARAMETER = "callback";
    public final static String INDENT_PARAMETER = "indent";
    public final static String POSTAL_PARAMETER = "postal";
    public final static String STANDARDIZE_PARAMETER = "standardize";
	public static final String LAT_PARAMETER = "lat";
	public static final String LONG_PARAMETER = "lng";
	public static final String RADIUS_PARAMETER = "radius";
	public static final String FUZZY_PARAMETER = "fuzzy";
	public static final String PARSED_PARAMETER = "parsed";
	public static final String LIMIT_PARAMETER = "limitnbresult";
	
    
    
    public static final int QUERY_MAX_LENGTH = 400;

    
    
    protected AddressQueryHttpBuilder getAddressQueryHttpBuilder(){
    	return AddressQueryHttpBuilder.getInstance();
    }
    
    /**
	 * 
	 */
    private static final long serialVersionUID = 7804855543117309510L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws AddressParserException ,IOException{
	OutputFormat format = OutputFormat.getDefault();
	try {
	    format = setResponseContentType(req, resp);
	    // check empty query
	    if (checkparameter()==true){
	    if (HTMLHelper.isParametersEmpty(req, ADDRESS_PARAMETER,COUNTRY_PARAMETER)) {
	    	sendCustomError("address and country parameters are required",400, format, resp,req);
		return;
	    }
	    }
	    
		AddressQuery query = getAddressQueryHttpBuilder().buildFromRequest(req);
	    if (logger.isDebugEnabled()){
	    	logger.debug("query=" + query);
	    }
	    String UA = req.getHeader(Constants.HTTP_USER_AGENT_HEADER_NAME);
	    String referer = req.getHeader(Constants.HTTP_REFERER_HEADER_NAME);
	    if (logger.isInfoEnabled()){
		logger.info("A "+getGisgraphyServiceType()+" request from "+req.getRemoteHost()+" / "+req.getRemoteAddr()+" was received , Referer : "+referer+" , UA : "+UA);
	    }
	    
	    processRequest(query,resp);
	} catch (AddressParserException e) {
	    logger.error("error while execute an address query from http request : "+dumpRequestParameters(req)+" : " + e);
	    String errorMessage = isDebugMode() ? " : " + e.getMessage() : "";
	    sendCustomError("Internal error : "
		    + errorMessage, format, resp,req);
	}
	catch (RuntimeException e) {
	    logger.error("error while execute a Parser query from http request "+dumpRequestParameters(req)+" : " + e);
	    String errorMessage = isDebugMode() ? " : " + e.getMessage() : "";
	    sendCustomError(errorMessage, format, resp,req);
	}
    }

    public abstract boolean checkparameter();

	public abstract void processRequest(AddressQuery query,HttpServletResponse resp) throws IOException ;

    @Override
    public IoutputFormatVisitor getErrorVisitor(String errorMessage) {
	return new AddressParserErrorVisitor(errorMessage);
    }

    @Override
    public abstract GisgraphyServiceType getGisgraphyServiceType();
    
    public String dumpRequestParameters(HttpServletRequest request){
    	StringBuffer sb =new StringBuffer("REQUEST : [");
		if  (request!=null){
			Enumeration parameterNames = request.getParameterNames();
			if (parameterNames!=null){
    		while( parameterNames.hasMoreElements()){
    			String parameterName = (String)parameterNames.nextElement();
				if (request.getParameter(parameterName)!=null){
    				sb.append(parameterName).append(" : '").append(request.getParameter(parameterName)).append("' ; ");
    			}
    		}
    	}}
		sb.append("]");
		return sb.toString();
    }

}
