import java.io.*;
import java.util.*;

/**
 * Tests for MostActiveCookie.
 * Covers: single winner, tie, high-count tie, empty file,
 * no matching date, date boundary, file not found.
 */
public class MostActiveCookieTest {

    public static void main(String[] args) throws IOException {
        testSingleMostActiveCookie();
        testTie();
        testHighCountTie();
        testEmptyFile();
        testNoMatchingDate();
        testDateBoundary();
        testFileNotFound();
        System.out.println("All tests passed.");
    }

    // One cookie appears more than all others
    static void testSingleMostActiveCookie() throws IOException {
        File tempFile = createTempCsv(
            "AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00",
            "AtY0laUfhglK3lC7,2018-12-09T10:13:00+00:00",
            "SAZuXPGUrfbcn5UA,2018-12-09T07:25:00+00:00"
        );
        List<String> result = MostActiveCookie.findMostActiveCookies(
            tempFile.getAbsolutePath(), "2018-12-09"
        );
        assertEqual(1, result.size(), "testSingleMostActiveCookie - size");
        assertEqual("AtY0laUfhglK3lC7", result.get(0), "testSingleMostActiveCookie - cookie name");
    }

    // Two cookies appear the same number of times
    static void testTie() throws IOException {
        File tempFile = createTempCsv(
            "AtY0laUfhglK3lC7,2018-12-08T14:19:00+00:00",
            "SAZuXPGUrfbcn5UA,2018-12-08T10:13:00+00:00"
        );
        List<String> result = MostActiveCookie.findMostActiveCookies(
            tempFile.getAbsolutePath(), "2018-12-08"
        );
        assertEqual(2, result.size(), "testTie - size");
        assertEqual(
            Arrays.asList("AtY0laUfhglK3lC7", "SAZuXPGUrfbcn5UA"),
            result,
            "testTie - cookies"
        );
    }

    // Two cookies each appear multiple times and tie
    static void testHighCountTie() throws IOException {
        File tempFile = createTempCsv(
            "AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00",
            "AtY0laUfhglK3lC7,2018-12-09T12:00:00+00:00",
            "AtY0laUfhglK3lC7,2018-12-09T10:00:00+00:00",
            "SAZuXPGUrfbcn5UA,2018-12-09T09:00:00+00:00",
            "SAZuXPGUrfbcn5UA,2018-12-09T08:00:00+00:00",
            "SAZuXPGUrfbcn5UA,2018-12-09T07:00:00+00:00",
            "fbcn5UAVanZf6UtG,2018-12-09T06:00:00+00:00"
        );
        List<String> result = MostActiveCookie.findMostActiveCookies(
            tempFile.getAbsolutePath(), "2018-12-09"
        );
        assertEqual(2, result.size(), "testHighCountTie - size");
        assertEqual(
            Arrays.asList("AtY0laUfhglK3lC7", "SAZuXPGUrfbcn5UA"),
            result,
            "testHighCountTie - cookies"
        );
    }

    // File has only the header, no data rows
    static void testEmptyFile() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        FileWriter fw = new FileWriter(tempFile);
        fw.write("cookie,timestamp\n");
        fw.close();
        List<String> result = MostActiveCookie.findMostActiveCookies(
            tempFile.getAbsolutePath(), "2018-12-09"
        );
        assertEqual(0, result.size(), "testEmptyFile - size");
    }

    // No cookies exist for the requested date
    static void testNoMatchingDate() throws IOException {
        File tempFile = createTempCsv(
            "AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00"
        );
        List<String> result = MostActiveCookie.findMostActiveCookies(
            tempFile.getAbsolutePath(), "2018-12-10"
        );
        assertEqual(0, result.size(), "testNoMatchingDate - size");
    }

    // Cookies near midnight should not cross date boundaries
    static void testDateBoundary() throws IOException {
        File tempFile = createTempCsv(
            "AtY0laUfhglK3lC7,2018-12-10T00:00:00+00:00",
            "SAZuXPGUrfbcn5UA,2018-12-09T23:59:59+00:00"
        );
        List<String> result = MostActiveCookie.findMostActiveCookies(
            tempFile.getAbsolutePath(), "2018-12-09"
        );
        assertEqual(1, result.size(), "testDateBoundary - size");
        assertEqual("SAZuXPGUrfbcn5UA", result.get(0), "testDateBoundary - cookie name");
    }

    // Reading a nonexistent file should throw IOException
    static void testFileNotFound() {
        try {
            MostActiveCookie.findMostActiveCookies("nonexistent.csv", "2018-12-09");
            throw new RuntimeException("testFileNotFound FAILED: expected IOException");
        } catch (IOException e) {
            System.out.println("testFileNotFound PASSED.");
        }
    }

    // Helper to create a temp CSV file with given data rows
    static File createTempCsv(String... rows) throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        FileWriter fw = new FileWriter(tempFile);
        fw.write("cookie,timestamp\n");
        for (String row : rows) {
            fw.write(row + "\n");
        }
        fw.close();
        return tempFile;
    }

    static void assertEqual(Object expected, Object actual, String testName) {
        if (!expected.equals(actual)) {
            throw new RuntimeException(
                testName + " FAILED: expected " + expected + " but got " + actual
            );
        }
        System.out.println(testName + " PASSED.");
    }
}