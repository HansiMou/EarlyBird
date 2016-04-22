import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * @author Hansi Mou
 * @date Apr 15, 2016
 * @version 1.0
 */

/**
 * @author Hansi Mou
 *
 *         Apr 15, 2016
 */
public class Searcher3 {
	static Config config = new Config();


	/**
	 * Description:
	 * 
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Charset charset = Charset.forName("UTF-8");
		// System.out.println("”");
		HashMap<Character, String> hm = new HashMap<Character, String>();
		
		try {
			InputStream inputStream = new FileInputStream(
					"downloads/_doi_10.1002_anie.201511882_full");
			int num = inputStream.available();
			// System.out.println("available bytes number is : " + num);
			byte[] bytes1 = null;
			if (num > 0) {
				bytes1 = new byte[num];
				// System.out.println("bytes1 length is : " + bytes1.length);
				inputStream.read(bytes1);
				// charset指定的是字节数组原来的字符编码集
				String res = new String(bytes1, charset);
				
				for (char c : res.toCharArray()) {
					if (hm.containsKey(c)) {
						o.append(hm.get(c));
					} else
						o.append(c);
				}
				System.out.println((o.toString()));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// try {
		// Directory directory = FSDirectory.open(new File(config.indexfolder)
		// .toPath());
		// DirectoryReader reader = DirectoryReader.open(directory);
		// IndexSearcher searcher = new IndexSearcher(reader);
		//
		// String[] stringQuery = new String[4];
		// Arrays.fill(stringQuery, args[0]);
		// String[] ff = { "title", "author", "keywords", "abstract" };
		// Occur[] oc = new Occur[4];
		// Arrays.fill(oc, Occur.SHOULD);
		// Query query = MultiFieldQueryParser.parse(stringQuery, ff, oc,
		// new StandardAnalyzer());
		//
		// TopDocs rs = searcher.search(query, 50);
		// SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
		// "<span style=\"background:yellow\">", "</span>");
		// Highlighter highlighter = new Highlighter(simpleHTMLFormatter,
		// new QueryScorer(query, "title"));
		// highlighter.setTextFragmenter(new SimpleFragmenter(1024));
		// SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		// for (int i = 0; i < 1; i++) {
		// Document doc = searcher.doc(rs.scoreDocs[i].doc);
		// System.out.print(doc.get("title"));
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// System.out.print("---------");
	}
}
