import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Parser {
	private static int row = 29; // Assuming the table is displaying the last 29 observations
	private static final int TABLE_INDEX = 1; //index 0 gets the first table
	private static final int OBSERVATION_LINE_NUM = 58;
	private static final int COL = 14;
	private static final String MESO_LINK = "http://goo.gl/y4zRFf"; // this is shorten link of the one below
	// "http://mesowest.utah.edu/cgi-bin/droman/meso_table_mesodyn.cgi?stn=WWA01&unit=0&time=LOCAL&year1=&month1=&day1=0&hour1=00&hours=24&past=0&order=1";
	private static String[][] mesoTable;
	private static File WEATHER_TABLE = new File("weathertable.csv");
	private static PrintWriter writer;
    
	public static void main(String[] args) {
		Document doc = null;
		try {
			doc = Jsoup.connect(MESO_LINK).get();
			if(WEATHER_TABLE.exists())
				WEATHER_TABLE.delete();
			WEATHER_TABLE = new File("weathertable.csv");
			writer = new PrintWriter(WEATHER_TABLE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document documentBody = Jsoup.parseBodyFragment(doc.body().html());
		Element table = documentBody.select("table").get(TABLE_INDEX);
		String oberservationNumString = documentBody.body().getElementsByTag("font").get(OBSERVATION_LINE_NUM)
				.ownText();
		row = Integer.parseInt(oberservationNumString.substring(18, 21).trim()) + 1;
		mesoTable = new String[row][COL];

		for (int i = 1; i < row; i++) {
			Element line = table.getElementsByTag("tr").get(i);
			for (int j = 0; j < COL; j++) {
				String cell = line.getElementsByTag("td").get(j).ownText();
				mesoTable[i][j] = cell;
			}
		}
		writer.println(oberservationNumString + "\n");
		writer.println(oberservationNumString.substring(39,51).trim()+ ", " + oberservationNumString.substring(19,21).trim() + "\n");
		writer.print(printHeading());
		writeTable(mesoTable);
		writer.close();
	}

	/**
	 * Prints file heading
	 * @return
	 */
	private static String printHeading() {
		StringBuilder builder = new StringBuilder();

		builder.append("Time (MDT), 2.0m Temperature (F), 2.0m Dew Point (F), 2m Wet bulb Temperature (F), ");
		builder.append("2.0m Relative Humidity (%), 2.5m wind speed (mph), 2.5m wind gust (mph), ");
		builder.append("Precipitation accumulated (in), Snow depth (in), K_Count snow water equivalent (in), ");
		builder.append("TL_Count snow water equivalent (in), Soil temperature (F), Battery voltage (volt), ");
		builder.append("Quality Control\n");
		return builder.toString();
	}
	
	/**
	 * Writes the table to a file
	 * @param table
	 */
	private static void writeTable(String[][] table) {
		for (int i = 1; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				writer.print(table[i][j] + ", ");
			}
			// add a new line for the next row
			writer.println();
		}
	}
}