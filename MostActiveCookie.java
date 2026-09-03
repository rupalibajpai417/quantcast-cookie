import java.io.*;
import java.util.*;

/**
 * Command line program to find the most active cookie(s) for a given date.
 *
 * Usage: java MostActiveCookie -f <filename> -d <date>
 * The most active cookie is the one that appears most frequently
 * in the log file for the given date. If multiple cookies tie,
 * all of them are printed on separate lines.
 */
public class MostActiveCookie {

    public static void main(String[] args) throws IOException {
        // Step 1: Parse command line arguments
    
        String filename = null;
        String date = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-f")) {
                filename = args[i + 1]; // value after -f is the filename
            } else if (args[i].equals("-d")) {
                date = args[i + 1]; // value after -d is the date
            }
        }

        // If either argument is missing, print usage and exit
        if (filename == null || date == null) {
            System.err.println("Usage: MostActiveCookie -f <filename> -d <date>");
            return;
        }

        // Step 2: Find most active cookies and print each one
        List<String> results = findMostActiveCookies(filename, date);
        for (String cookie : results) {
            System.out.println(cookie);
        }
    }

    /**
     * Reads a cookie log file and returns the most active cookie(s)
     * for the given date.
     *
     * @param filename path to the CSV log file
     * @param date     date string in format YYYY-MM-DD
     * @return list of most active cookie names (multiple if tied)
     * @throws IOException if the file cannot be read
     */
    public static List<String> findMostActiveCookies(String filename, String date) throws IOException {
        // Map to store cookie name → number of appearances on the given date
        HashMap<String, Integer> cookieCount = new HashMap<>();
        List<String> result = new ArrayList<>();

        // Step 2: Read the file line by line
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // skip the header line (cookie,timestamp)

            String line;
            while ((line = br.readLine()) != null) {
                // Step 3: Parse each line and filter by date

                // Split line by comma → [cookieName, timestamp]
                String[] parts = line.split(",");
                String cookieName = parts[0];

                // Extract date from timestamp by splitting on "T"
                String lineDate = parts[1].split("T")[0];

                // Only count cookies that match the requested date
                if (lineDate.equals(date)) {
                    // Increment count for this cookie, starting at 0 if not seen before
                    cookieCount.put(cookieName, cookieCount.getOrDefault(cookieName, 0) + 1);
                }
            }
        }

        // Step 4: Find the maximum count across all cookies
        int maxCount = 0;
        for (int count : cookieCount.values()) {
            if (count > maxCount) {
                maxCount = count;
            }
        }

        // Step 5: Collect all cookies that match the maximum count
        for (Map.Entry<String, Integer> entry : cookieCount.entrySet()) {
            if (entry.getValue() == maxCount) {
                result.add(entry.getKey());
            }
        }

        return result;
    }
}