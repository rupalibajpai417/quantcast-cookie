# Most Active Cookie

A command-line Java application that finds the most active cookie(s) in a
cookie log file for a given date.

If multiple cookies share the highest count, all of them are printed on
separate lines in alphabetical order.

## Requirements

- Java 17+
- Maven 3.8+

## File Format

The input file must be a CSV with the following format:

    cookie,timestamp
    AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00
    SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00

- First line is the header and is skipped
- Timestamps are in UTC
- File is sorted by timestamp, most recent first
- File is processed as a stream — does not need to fit entirely in memory

## Build and Test

Compile and run all tests:

    mvn clean test

## Run

    java -cp target/classes MostActiveCookie -f cookie_log.csv -d 2018-12-09

## Example

Input file `cookie_log.csv`, querying for `2018-12-09`:

    AtY0laUfhglK3lC7

## Design

The application processes the log file line by line without loading it fully
into memory. For each row, the date is extracted from the timestamp and
compared to the requested date. Because the input is sorted newest-first,
processing stops as soon as an older date is encountered.

Cookie counts are tracked in a HashMap. After processing, the maximum count
is found and all cookies matching that count are returned in sorted order for
deterministic output.

## Complexity

- Time: O(n + k log k) where n is the number of rows read and k is the number
  of unique cookies on the target date
- Space: O(k)

## Assumptions and Edge Cases

- If multiple cookies share the highest count, all are returned on separate lines
- Date parameter is in UTC format YYYY-MM-DD
- Malformed rows are skipped silently
- Input is guaranteed to be sorted by timestamp, newest first