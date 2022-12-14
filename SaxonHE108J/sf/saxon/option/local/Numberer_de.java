////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2018-2020 Saxonica Limited
// This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
// If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
// This Source Code Form is "Incompatible With Secondary Licenses", as defined by the Mozilla Public License, v. 2.0.
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package net.sf.saxon.option.local;

import net.sf.saxon.expr.number.AbstractNumberer;
import net.sf.saxon.regex.LatinString;

/**
 * Class Numberer_de provides localization for format-date() and xsl:number with language = "de" (German)
 *
 * @author Michael H. Kay
 */

public class Numberer_de extends AbstractNumberer {

    /**
     * Construct the ordinal suffix for a number, for example "st", "nd", "rd"
     *
     * @param ordinalParam the value of the ordinal attribute (used in non-English
     *                     language implementations)
     * @param number       the number being formatted
     * @return the ordinal suffix to be appended to the formatted number
     */

    @Override
    protected String ordinalSuffix(String ordinalParam, long number) {
        return ".";
    }

    /**
     * Show the number as words in title case. (We choose title case because
     * the result can then be converted algorithmically to lower case or upper case).
     */

    @Override
    public String toWords(long number) {
        if (number >= 1000000000000000L) {
            return format(number, new LatinString("1.000.000"), 3, ".", "", "");
        }
        if (number >= 1000000000000L) {
            long rem = number % 1000000000000L;
            long n = number / 1000000000000L;
            String s = (n == 1 ? "Eine" : toWords(n));
            return s + " Billion" +
                    (rem == 0 ? "" : ' ' + toWords(rem));
        } else if (number >= 1000000000) {
            long rem = number % 1000000000;
            long n = number / 1000000000;
            String s = (n == 1 ? "Eine" : toWords(n));
            return s + " Milliarde" +
                    (rem == 0 ? "" : ' ' + toWords(rem));
        } else if (number >= 1000000) {
            long rem = number % 1000000;
            long n = number / 1000000;
            String s = (n == 1 ? "Eine" : toWords(n));
            return s + " Million" +
                    (n == 1 ? "" : "en") +
                    (rem == 0 ? "" : toWords(rem));
        } else if (number >= 1000) {
            long rem = number % 1000;
            long n = number / 1000;
            String s = (n == 1 ? "Ein" : toWords(n));
            return s + "tausend" + (rem == 0 ? "" : toWords(rem, LOWER_CASE));
        } else if (number >= 100) {
            long rem = number % 100;
            long n = number / 100;
            String s = (n == 1 ? "Ein" : toWords(n));
            return s + "hundert" + (rem == 0 ? "" : toWords(rem, LOWER_CASE));
        } else {
            if (number < 20) return (number == 1 ? "Eins" : germanUnits[(int) number]);
            int rem = (int) (number % 10);
            int tens = (int) number / 10;
            return (germanUnits[rem]) +
                    (tens == 0 ? "" : (rem == 0 ? "" : "und") + germanTens[tens]);

        }
    }

    @Override
    public String zero() {
        return "Null";
    }

    private static String[] germanUnits = {
            "", "Ein", "Zwei", "Drei", "Vier", "F\u00fcnf", "Sechs", "Sieben", "Acht", "Neun",
            "Zehn", "Elf", "Zw\u00f6lf", "Dreizehn", "Vierzehn", "F\u00fcnfzehn", "Sechszehn",
            "Siebzehn", "Achtzehn", "Neunzehn"};

    private static String[] germanTens = {
            "", "Zehn", "Zwanzig", "Drei\u00dfig", "Vierzig", "F\u00fcnfzig",
            "Sechzig", "Siebzig", "Achtzig", "Neunzig"};

    /**
     * Show an ordinal number as German words (for example, Einundzwanzigste)
     */

    @Override
    public String toOrdinalWords(String ordinalParam, long number, int wordCase) {
        String suffix = "e";
        if (ordinalParam.equalsIgnoreCase("-er")) {
            suffix = "er";
        } else if (ordinalParam.equalsIgnoreCase("-es")) {
            suffix = "es";
        } else if (ordinalParam.equalsIgnoreCase("-en")) {
            suffix = "en";
        }
        long mod100 = number % 100;

        if (number < 20) {
            String ord = germanOrdinalUnits[(int) number] + suffix;
            if (wordCase == UPPER_CASE) {
                return ord.toUpperCase();
            } else if (wordCase == LOWER_CASE) {
                return ord.toLowerCase();
            } else {
                return ord;
            }
        } else if (mod100 < 20 && mod100 > 0) {
            return toWords(number - (mod100), wordCase) +
                    toOrdinalWords(ordinalParam, mod100,
                            (wordCase == TITLE_CASE ? LOWER_CASE : wordCase));
        } else {
            String ending = "st" + suffix;
            if (wordCase == UPPER_CASE) {
                ending = ending.toUpperCase();
            }
            return toWords(number, wordCase) +
                    (wordCase == UPPER_CASE ? ending.toUpperCase() : ending);
        }
    }

    private static String[] germanOrdinalUnits = {
            "", "Erst", "Zweit", "Dritt", "Viert", "F\u00fcnft", "Sechst", "Siebt", "Acht", "Neunt",
            "Zehnt", "Elft", "Zw\u00f6lft", "Dreizehnt", "Vierzehnt", "F\u00fcnfzehnt", "Sechszehnt",
            "Siebzehnt", "Achtzehnt", "Neunzehnt"};

    /**
     * Get a month name or abbreviation
     *
     * @param month    The month number (1=January, 12=December)
     * @param minWidth The minimum number of characters
     * @param maxWidth The maximum number of characters
     */

    @Override
    public String monthName(int month, int minWidth, int maxWidth) {
        String name = germanMonths[month - 1];
        if (maxWidth < 3) {
            maxWidth = 3;
        }
        if (name.length() > maxWidth) {
            name = name.substring(0, maxWidth);
        }
        while (name.length() < minWidth) {
            name = name + " ";
        }
        return name;
    }

    private static String[] germanMonths = {
            "Januar", "Februar", "M\u00e4rz", "April", "Mai", "Juni",
            "Juli", "August", "September", "Oktober", "November", "Dezember"
    };

    /**
     * Get a day name or abbreviation
     *
     * @param day      The month number (1=Sunday, 7=Saturday)
     * @param minWidth The minimum number of characters
     * @param maxWidth The maximum number of characters
     */

    @Override
    public String dayName(int day, int minWidth, int maxWidth) {
        String name = germanDays[day - 1];
        if (maxWidth < 10) {
            name = name.substring(0, 2);
        }
        while (name.length() < minWidth) {
            name = name + ' ';
        }
        return name;
    }

    private static String[] germanDays = {
            "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"
    };

    /**
     * Get an ordinal suffix for a particular component of a date/time.
     *
     * @param component the component specifier from a format-dateTime picture, for
     *                  example "M" for the month or "D" for the day.
     * @return a string that is acceptable in the ordinal attribute of xsl:number
     *         to achieve the required ordinal representation. For example, "-e" for the day component
     *         in German, to have the day represented as "dritte August".
     */

    @Override
    public String getOrdinalSuffixForDateTime(String component) {
        return "-e";
    }

    /**
     * Get the name for an era (e.g. "BC" or "AD")
     *
     * @param year the proleptic gregorian year, using "0" for the year before 1AD
     */

    @Override
    public String getEraName(int year) {
        return (year <= 0 ? "v. Chr." : "n. Chr.");
    }

    /**
     * Get the name of a calendar
     *
     * @param code The code representing the calendar as in the XSLT 2.0 spec, e.g. AD for the Gregorian calendar
     */

    /*@NotNull*/
    @Override
    public String getCalendarName(String code) {
        if (code.equals("AD")) {
            return "Gregorianisch";
        } else {
            return code;
        }
    }

}

