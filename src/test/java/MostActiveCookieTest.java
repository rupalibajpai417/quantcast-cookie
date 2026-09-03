import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MostActiveCookieTest {

    @TempDir
    Path tempDir;

    @Test
    void returnsMostActiveCookie() throws IOException {
        Path file = createCsv(
                "AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00",
                "AtY0laUfhglK3lC7,2018-12-09T10:13:00+00:00",
                "SAZuXPGUrfbcn5UA,2018-12-09T07:25:00+00:00"
        );
        List<String> result = MostActiveCookie.findMostActiveCookies(
                file.toString(), "2018-12-09"
        );
        assertEquals(List.of("AtY0laUfhglK3lC7"), result);
    }

    @Test
    void returnsAllCookiesWhenThereIsATie() throws IOException {
        Path file = createCsv(
                "AtY0laUfhglK3lC7,2018-12-08T14:19:00+00:00",
                "SAZuXPGUrfbcn5UA,2018-12-08T10:13:00+00:00"
        );
        List<String> result = MostActiveCookie.findMostActiveCookies(
                file.toString(), "2018-12-08"
        );
        assertEquals(Arrays.asList("AtY0laUfhglK3lC7", "SAZuXPGUrfbcn5UA"), result);
    }

    @Test
    void returnsAllCookiesWhenMultipleCookiesHaveSameHighestCount() throws IOException {
        Path file = createCsv(
                "AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00",
                "AtY0laUfhglK3lC7,2018-12-09T12:00:00+00:00",
                "AtY0laUfhglK3lC7,2018-12-09T10:00:00+00:00",
                "SAZuXPGUrfbcn5UA,2018-12-09T09:00:00+00:00",
                "SAZuXPGUrfbcn5UA,2018-12-09T08:00:00+00:00",
                "SAZuXPGUrfbcn5UA,2018-12-09T07:00:00+00:00",
                "fbcn5UAVanZf6UtG,2018-12-09T06:00:00+00:00"
        );
        List<String> result = MostActiveCookie.findMostActiveCookies(
                file.toString(), "2018-12-09"
        );
        assertEquals(Arrays.asList("AtY0laUfhglK3lC7", "SAZuXPGUrfbcn5UA"), result);
    }

    @Test
    void returnsEmptyListForHeaderOnlyFile() throws IOException {
        Path file = createCsv();
        List<String> result = MostActiveCookie.findMostActiveCookies(
                file.toString(), "2018-12-09"
        );
        assertEquals(List.of(), result);
    }

    @Test
    void returnsEmptyListWhenDateHasNoMatchingCookies() throws IOException {
        Path file = createCsv("AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00");
        List<String> result = MostActiveCookie.findMostActiveCookies(
                file.toString(), "2018-12-10"
        );
        assertEquals(List.of(), result);
    }

    @Test
    void respectsDateBoundary() throws IOException {
        Path file = createCsv(
                "AtY0laUfhglK3lC7,2018-12-10T00:00:00+00:00",
                "SAZuXPGUrfbcn5UA,2018-12-09T23:59:59+00:00"
        );
        List<String> result = MostActiveCookie.findMostActiveCookies(
                file.toString(), "2018-12-09"
        );
        assertEquals(List.of("SAZuXPGUrfbcn5UA"), result);
    }

    @Test
    void handlesMalformedRows() throws IOException {
        Path file = createCsv(
                "AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00",
                "malformed-row",
                "SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00"
        );
        List<String> result = MostActiveCookie.findMostActiveCookies(
                file.toString(), "2018-12-09"
        );
        assertEquals(2, result.size());
    }

    @Test
    void throwsExceptionForMissingFile() {
        assertThrows(IOException.class, () ->
                MostActiveCookie.findMostActiveCookies(
                        tempDir.resolve("missing.csv").toString(), "2018-12-09"
                )
        );
    }

    private Path createCsv(String... rows) throws IOException {
        Path file = tempDir.resolve("cookie_log.csv");
        List<String> lines = new java.util.ArrayList<>();
        lines.add("cookie,timestamp");
        lines.addAll(Arrays.asList(rows));
        Files.write(file, lines);
        return file;
    }
}