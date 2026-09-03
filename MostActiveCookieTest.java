import java.io.*;
import java.util.*;

/**
 * Tests for MostActiveCookie.
 * Covers: single winner, tie, no matching date, file not found.
 */
public class MostActiveCookieTest {

    public static void main(String[] args) throws IOException {
        testSingleMostActiveCookie();
        testTie();
        testNoMatchingDate();
        testFileNotFound();
        System.out.println("All tests passed.");
    }

    /**
     * Tests that the single most active cookie is returned
     * when one cookie appears more times than others on a given date.
     */
    static void testSingleMostActiveCookie() throws IOException {
        // Create a temporary CSV file with known test data
        File tempFile = File.createTempFile("test", ".csv");
        FileWriter fw = new FileWriter(tempFile);
        fw.write("cookie,timestamp\n");
        fw.write("AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00\n"); // appears twice
        fw.write("AtY0laUfhglK3lC7,2018-12-09T10:13:00+00:00\n");
        fw.write("SAZuXPGUrfbcn5UA,2018-12-09T07:25:00+00:00\n"); // appears once
        fw.close();

        List<String> result = MostActiveCookie.findMostActiveCookies(
            tempFile.getAbsolutePath(), "2018-12-09"
        );

        // Expect exactly one cookie returned
        assertEqual(1, result.size(), "testSingleMostActiveCookie - size");
        // Expect it to be the cookie that appeared twice
        assertEqual("AtY0laUfhglK3lC7", result.get(0), "testSingleMostActiveCookie - cookie name");
    }

    /**
     * Tests that all cookies are returned when multiple cookies
     * share the highest count on a given date.
     */
    static void testTie() throws IOException {
        // Create a temporary CSV file where two cookies appear once each
        File tempFile = File.createTempFile("test", ".csv");
        FileWriter fw = new FileWriter(tempFile);
        fw.write("cookie,timestamp\n");
        fw.write("AtY0laUfhglK3lC7,2018-12-08T14:19:00+00:00\n"); // appears once
        fw.write("SAZuXPGUrfbcn5UA,2018-12-08T10:13:00+00:00\n"); // appears once
        fw.close();

        List<String> result = MostActiveCookie.findMostActiveCookies(
            tempFile.getAbsolutePath(), "2018-12-08"
        );

        // Expect both cookies to be returned
        assertEqual(2, result.size(), "testTie - size");
    }

    /**
     * Tests that an empty list is returned when no cookies
     * exist for the given date.
     */
    static void testNoMatchingDate() throws IOException {
        // Create a temporary CSV file with data only for 2018-12-09
        File tempFile = File.createTempFile("test", ".csv");
        FileWriter fw = new FileWriter(tempFile);
        fw.write("cookie,timestamp\n");
        fw.write("AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00\n");
        fw.close();

        // Query for a date that has no entries
        List<String> result = MostActiveCookie.findMostActiveCookies(
            tempFile.getAbsolutePath(), "2018-12-10"
        );

        // Expect empty result
        assertEqual(0, result.size(), "testNoMatchingDate - size");
    }

    /**
     * Tests that an IOException is thrown when the file does not exist.
     */
    static void testFileNotFound() {
        try {
            // Attempt to read a file that doesn't exist
            MostActiveCookie.findMostActiveCookies("nonexistent.csv", "2018-12-09");
            // If no exception thrown, the test fails
            throw new RuntimeException("testFileNotFound FAILED: expected IOException");
        } catch (IOException e) {
            // Expected — file not found throws IOException
            System.out.println("testFileNotFound PASSED.");
        }
    }

    /**
     * Helper method to assert equality between expected and actual values.
     * Throws RuntimeException with a descriptive message if they don't match.
     */
    static void assertEqual(Object expected, Object actual, String testName) {
        if (!expected.equals(actual)) {
            throw new RuntimeException(
                testName + " FAILED: expected " + expected + " but got " + actual
            );
        }
        System.out.println(testName + " PASSED.");
    }
}