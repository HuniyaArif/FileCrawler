import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
 
public class FileCrawler {
 
  private String fileNameToSearch;
  static int drives;
  //private List<String> result = new ArrayList<String>();

  Map<String, List<String>> resultlist = new HashMap<String, List<String>>();
  List<String> val = new ArrayList<String>();
  
  public String getFileNameToSearch() {
	return fileNameToSearch;
  }
 
  public void setFileNameToSearch(String fileNameToSearch) {
	this.fileNameToSearch = fileNameToSearch;
  }
 
  public Map<String, List<String>> getResult() {
	return resultlist;
  }
  
  
  public Runnable run(File path){
	   
	     try{      
	        
	           System.out.println( path);
	           
	           File[] files = path.listFiles();
	           FileFilter fileFilter = new FileFilter() {
	              public boolean accept(File file) {
	                 return file.isDirectory();
	              }
	           };
	           files = path.listFiles(fileFilter);
	           try{
	           System.out.println(files.length);
	           if (files.length == 0) {
	              System.out.println("Either dir does not exist or is not a directory");
	           }
	           else {
	              for (int i=0; i< files.length; i++) {
	                 File filename = files[i];
	                 System.out.println(filename.toString());
	                 
	             
	                 searchDirectory(new File(filename.toString()), fileNameToSearch);
	                 
	                	Map<String, List<String>>temp = new HashMap();
	                	temp=getResult();
	                	int count=temp.size();
	                	
	                	if(count ==0){
	                	    System.out.println("\nNo result found!");
	                	}else{
	                	    System.out.println("\nFound " + count + " result!\n");
	                	    for (Map.Entry<String, List<String>> entry : temp.entrySet()) {
	                	    	
	                	    	String key = entry.getKey();
	                	    
	                	    	List<String> values = entry.getValue();
	                	    	
	                	    	System.out.println("FileName = " + key);
	                	    	
	                	    	System.out.println("Path = " + values);
	                	    	
	                	    	}
	                	    }
	                	
	                	   
	                	    
	                	
	                	
	                 }
	              }
	           
	           }
	        catch(NullPointerException npe){
	        }
	          
	       
	     }catch(Exception e){
	        // if any error occurs
	        e.printStackTrace();
	     }
	  
	return null;
	  
  }
 
  public static void main(String[] args) {
 
	FileCrawler fileSearch = new FileCrawler();
	Scanner scan = new Scanner(System.in);
	drives=0;
	System.out.println("Enter the filename:");
	String a = scan.nextLine();
	fileSearch.setFileNameToSearch(a);
	
	
	File[] paths;
	 try{      
         // returns pathnames for files and directory
         paths = File.listRoots();
         
         // for each pathname in pathname array
         for(File path:paths)
         {
        	  drives++;
         }
      }catch(Exception e){
         // if any error occurs
         e.printStackTrace();
      }
	ArrayList<Thread> thread = new ArrayList<Thread>(drives);
	
	
	  try{      
	         // returns pathnames for files and directory
	         paths = File.listRoots();
	         
	         // for each pathname in pathname array
	         for(File path:paths)
	         {
	        	   Thread t = new Thread(fileSearch.run(path));
	        	   thread.add(t);
	        	   t.start();
	         }
	      }catch(Exception e){
	         // if any error occurs
	         e.printStackTrace();
	      }
 
	  for (int i = 0; i < drives; i++){
		   try {
		    thread.get(i).join();
		   } catch (Exception e) {};
		  }

  }
 
  public void searchDirectory(File directory, String fileNameToSearch) {
 
	setFileNameToSearch(fileNameToSearch);
 
	if (directory.isDirectory()) {
	    search(directory);
	} else {
	    System.out.println(directory.getAbsoluteFile() + " is not a directory!");
	}
 
  }
 
  private void search(File file) {
	  
	  
	if (file.isDirectory()) {
	  System.out.println("Directory:" + file.getAbsoluteFile());

	
	    if (file.canRead()) {
	    	
	    	try{
		for (File temp : file.listFiles()) {
			
		    if (temp.isDirectory()) {
			search(temp);
		    } else {
		    
			if (temp.getName().contains(getFileNameToSearch().toLowerCase())) {	
				
				val.add(temp.getAbsoluteFile().toString());
			    resultlist.put(fileNameToSearch, val);

		    }
			
			//finding content in a file
			
			Scanner scanner = new Scanner(temp);
            if (scanner.findWithinHorizon(getFileNameToSearch(), 0) != null) {
            	val.add(temp.getAbsoluteFile().toString());
            	resultlist.put(fileNameToSearch, val);
            }
            scanner.close();
 
		}
	    }
	    	}
	    	catch(NullPointerException npe){
	    		
	    	} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
	 } else {
		System.out.println(file.getAbsoluteFile() + "Permission Denied");
	 }
      }
 
  }
 
}
