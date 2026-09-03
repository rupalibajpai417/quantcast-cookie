# Most Active Cookie

A command line program that reads a cookie log file and returns the most active cookie for a given date.

## What it does

Parses a CSV cookie log file and finds the cookie that appears most frequently on a given date. If multiple cookies tie for the highest count, all of them are returned on separate lines.

## File Format

The input file must be a CSV with the following format:

cookie,timestamp
AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00
SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00


- First line is the header and is skipped
- Timestamps are in UTC
- File is sorted by timestamp, most recent first

## Assumptions and Edge Cases

- If multiple cookies share the highest count, all are returned on separate lines
- Date parameter is assumed to be in UTC
- File fits in memory
- No heavy external libraries used — standard Java only

## How to Run

**Compile:**

javac MostActiveCookie.java MostActiveCookieTest.java


**Run:**

java MostActiveCookie -f cookie_log.csv -d 2018-12-09


**Run tests:**

java MostActiveCookieTest


## Example

Input file `cookie_log.csv`, querying for `2018-12-09`:

AtY0laUfhglK3lC7