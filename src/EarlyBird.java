import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Hansi Mou
 * @date Apr 5, 2016
 * @version 1.0
 */

/**
 * @author Hansi Mou
 *
 *         Apr 5, 2016
 */
public class EarlyBird {
	static HashMap<String, StartingUrl> urls = new HashMap<String, StartingUrl>();
	static HashSet<String> exception = new HashSet<String>();  
	static Config config = new Config();
	/**
	 * Description:
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// System.out.println(config.folder);
		// System.out.println(config.weeknum);

		CheckFolder();
		GetUrls();
//		for (Map.Entry<String, StartingUrl> entry : urls.entrySet()) {  
//		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue().print());  
//		}  
	}

	/**
	 * Description: 
	 */
	private static void GetUrls() {
		// TODO Auto-generated method stub
		try {
			// read file content from file
			FileReader reader = new FileReader("config.txt");
			BufferedReader br = new BufferedReader(reader);

			String str = null;
			StartingUrl u = new StartingUrl();

			while ((str = br.readLine()) != null) {
				if (str.startsWith("//"))
					continue;
				else if (str.startsWith("#")){
					if (!u.name.equals("")){
						urls.put(u.link, u);
						u = new StartingUrl();
					}
					u.name = str.substring(1);
				}
				else if (u.name.equals("")){
					continue;
				}
				else if (str.startsWith("Link")){
					u.link = str.split("::")[1];
				}
				else if (str.startsWith("Level")){
					u.lv = Integer.parseInt(str.split("::")[1]);
				}
				else if (str.startsWith("First_Level_Pre_Filter")){
					u.flpre = str.split("::")[1];
				}
				else if (str.startsWith("First_Level_Post_Filter")){
					u.flpost = str.split("::")[1];
				}
				else if (str.startsWith("Second_Level_Pre_Filter")){
					u.secpre = str.split("::")[1];
				}
				else if (str.startsWith("Second_Level_Post_Filter")){
					u.secpost = str.split("::")[1];
				}
				else if (str.startsWith("Second_Level_Post_Filter")){
					u.secpost = str.split("::")[1];
				}
				else if (str.startsWith("Exception")){
					exception.add(str.split("::")[1]);
				}
			}
			urls.put(u.link, u);

			br.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description: check whether fold exists, and whether files in the folder
	 * are out of date
	 * 
	 * @param folder
	 * @param weeknum
	 */
	private static void CheckFolder() {
		// TODO Auto-generated method stub
		File file = new File(config.folder);
		if (!file.exists() && !file.isDirectory()) {
			// if folder does not exist, then make one.
			file.mkdir();
		} else {
			// check date
//			File[] files = file.listFiles();  
//			for (File file2 : files) {  
//		        // create time
//		        long modifiedTime = file.lastModified();  
//		        
//		        Date d = new Date();
//		        System.out.println(format.format(d));  
//			}
		}
	}
}
