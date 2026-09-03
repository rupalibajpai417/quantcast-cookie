import java.io.*;
import java.util.*;

/**
 * Command line program to find the most active cookie(s) for a given date.
 *
 * Usage: java MostActiveCookie -f <filename> -d <date>
 * Example: java MostActiveCookie -f cookie_log.csv -d 2018-12-09
 *
 * The most active cookie is the one that appears most frequently
 * in the log file for the given date. If multiple cookies tie,
 * all of them are printed on separate lines.
 *
 */
public class MostActiveCookie {

    public static void main(String[] args) throws IOException {
        String filename = null;
        String date = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-f") && i + 1 < args.length) {
                filename = args[i + 1];
            } else if (args[i].equals("-d") && i + 1 < args.length) {
                date = args[i + 1];
            }
        }

        if (filename == null || date == null) {
            System.err.println("Usage: MostActiveCookie -f <filename> -d <date>");
            return;
        }

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
     * @return sorted list of most active cookie names (multiple if tied)
     * @throws IOException if the file cannot be read
     */
    public static List<String> findMostActiveCookies(String filename, String date) throws IOException {
        Map<String, Integer> cookieCount = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // skip header line

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                // Skip malformed rows
                if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) continue;

                String cookieName = parts[0];

                // Extract date portion from timestamp e.g. "2018-12-09T14:19:00+00:00" → "2018-12-09"
                String lineDate = parts[1].split("T")[0];

                // Input is sorted newest-first, so once we pass the target date
                // no further entries can match — stop reading
                if (lineDate.compareTo(date) < 0) break;

                if (lineDate.equals(date)) {
                    cookieCount.put(cookieName, cookieCount.getOrDefault(cookieName, 0) + 1);
                }
            }
        }

        // Find the maximum count
        int maxCount = 0;
        for (int count : cookieCount.values()) {
            if (count > maxCount) {
                maxCount = count;
            }
        }

        // Collect all cookies that match the maximum count
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : cookieCount.entrySet()) {
            if (entry.getValue() == maxCount) {
                result.add(entry.getKey());
            }
        }

        Collections.sort(result); // deterministic output order
        return result;
    }
}