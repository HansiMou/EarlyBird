import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author Hansi Mou
 * @date Apr 5, 2016
 * @version 1.0
 */

public class Config {
	String dnfolder = "downloads";
	String indexfolder = "index";
	int weeknum = 1;
	String cachepath = "cache";

	public Config() {
		try {
			// read file content from file
			FileReader reader = new FileReader("config.txt");
			BufferedReader br = new BufferedReader(reader);

			String str = null;

			while ((str = br.readLine()) != null && !str.startsWith("#")) {
				if (str.startsWith("DownloadFolder")) {
					this.dnfolder = str.split("::")[1];
				} 
				else if (str.startsWith("TimePeroidOfWeeks")) {
					this.weeknum = Integer.parseInt(str.split("::")[1]);
				}
				else if (str.startsWith("CachePath")){
					this.cachepath = str.split("::")[1];
				}
				else if (str.startsWith("IndexFolder")){
					this.indexfolder = str.split("::")[1];
				}
			}

			br.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
