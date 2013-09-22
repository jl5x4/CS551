package hdf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Path("generic")
public class HDFWS {
    @SuppressWarnings("unused")
    @Context
    private UriInfo context;

    /**
     * Default constructor. 
     */
    public HDFWS() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Retrieves representation of an instance of HDFWS
     * @return an instance of String
     */
    @GET
    @Produces("application/x-javascript")
    @Path("hadoopPut/{LocalhadoopInputDir:.+}") /// format: localpath hadoopInputpath
    public String hadoopPut(@QueryParam("callback") String callback, @PathParam("LocalhadoopInputDir") String LocalhadoopInputDir) {
        String line="";
        StringBuffer sb = new StringBuffer();
    	
    	try
    	{
    	 Process process = Runtime.getRuntime().exec ("hadoop fs -put " +LocalhadoopInputDir);
    	 BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	
    	 while ((line = br.readLine ()) != null){
    		 sb.append(line).append("\n"); 
    	 }
    	}
    	catch (java.io.IOException e){
    	 System.err.println ("IOException " + e.getMessage());
    	 return "IOException " + e.getMessage();
    	}
    	
    	return sb.toString();
    }

    @GET
    @Produces("application/x-javascript")
    @Path("hadoopRun/{hadoopInOutDir:.+}")//// format: hadoopInputpath hadooopOutputpath
    public String hadoopRun(@QueryParam("callback") String callback,@PathParam("hadoopInOutDir") String hadoopInOutDir) {
        String line="";
        StringBuffer sb = new StringBuffer();
    	
    	try
    	{
    	 //Process process = Runtime.getRuntime().exec ("pwd");
    		Process process = Runtime.getRuntime().exec 
    				("hadoop jar /mnt/biginsights/opt/ibm/biginsights/IHC/hadoop-example.jar wordcount "+hadoopInOutDir);
    		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
    		
    			 //InputStreamReader ir=new InputStreamReader(process.getInputStream());
    	 //LineNumberReader input = new LineNumberReader (ir);
    	 
    	 while ((line = br.readLine ()) != null){
    		 sb.append(line).append("\n");    	 
    	 }
    	    	
    	}
    	catch (java.io.IOException e){
    	 System.err.println ("IOException " + e.getMessage());
    	 return "IOException " + e.getMessage();
    	}
    	
    	return sb.toString();
    }
    
    
    
    
    @GET
    @Produces("application/x-javascript")
    @Path("viewResult/{hadoopOutputDir:.+}")
    public String getResult(@QueryParam("callback") String callback, @PathParam("hadoopOutputDir") String hadoopOutputDir) {
        String line="";
    	StringBuffer sb = new StringBuffer();
    	
    	try
    	{
    		Process process = Runtime.getRuntime().exec ("hadoop fs -cat "+hadoopOutputDir+"/*00");
    		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

    	 while ((line = br.readLine ()) != null){
    		 sb.append(line).append("\n");
    	 
    	 }
    	    	
    	}
    	catch (java.io.IOException e){
    	 System.err.println ("IOException " + e.getMessage());
    	 return "IOException " + e.getMessage();
    	}
    	
    	return sb.toString();
    }

    /**
     * PUT method for updating or creating an instance of HDFWS
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }

}