package org.wikivoyage.ru.listings.input.template;

import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.wikivoyage.ru.listings.utils.StringUtils;

/**
 * Parse French "Horaire" template (working hours), per https://fr.wikivoyage.org/wiki/Mod%C3%A8le:Horaire
 */
public class FrenchHoraireTemplateParser implements TemplateParser {
    @Override
    public String parse(WtTemplate template) {
        String [] arguments = TemplateUtils.convertTemplateToStringArray(template);
        String days = "";
        if (!isEmpty(arguments, 0)) {
            days += convertDay(getArg(arguments, 0));
        }
        if (!isEmpty(arguments, 1)) {
            days += " - " + convertDay(getArg(arguments, 1));
        }

        String timeGaps;
        String timeGap1 = timeGap(
            getArg(arguments, 2), getArg(arguments, 3), getArg(arguments, 4), getArg(arguments, 5)
        );
        String timeGap2 = timeGap(
            getArg(arguments, 6), getArg(arguments, 7), getArg(arguments, 8), getArg(arguments, 9)
        );
        if (timeGap2.equals("")) {
            timeGaps = timeGap1;
        } else {
            timeGaps = timeGap1 + " et " + timeGap2;
        }

        if (timeGaps.equals("")) {
            return days;
        } else {
            if (days.equals("")) {
                return timeGaps;
            } else {
                return days + ": " + timeGaps;
            }
        }
    }

    @Override
    public String getTemplateName() {
        return "horaire";
    }

    private String timeGap(String hours1, String minutes1, String hours2, String minutes2)
    {
        String timeItem1 = timeItem(hours1, minutes1);
        String timeItem2 = timeItem(hours2, minutes2);
        if (timeItem2.equals("")) {
            return timeItem1;
        } else {
            return timeItem1 + " - " + timeItem2;
        }
    }

    private String timeItem(String hours, String minutes)
    {
        if (hours.equals("")) {
            return "";
        } else {
            if (minutes.equals("")) {
                return hours + " h";
            } else {
                return hours + " h " + minutes;
            }
        }
    }

    private boolean isEmpty(String[] arguments, int index)
    {
        if (arguments.length > index) {
            return arguments[index].trim().equals("");
        } else {
            return true;
        }
    }

    private String getArg(String[] arguments, int index)
    {
        if (arguments.length > index) {
            return arguments[index];
        } else {
            return "";
        }
    }

    private String convertDay(String day)
    {
        String[][] days = new String[][]{
            {"lun.", "1", "l", "lu", "lun", "lundi"},
            {"mar.", "2", "ma", "mar", "mardi"},
            {"mer.", "3", "me", "mer", "mercredi"},
            {"jeu.", "4", "j", "je", "jeu", "jeudi"},
            {"ven.", "5", "v", "ve", "ven", "vendredi"},
            {"sam.", "6", "s", "sa", "sam", "samedi"},
            {"dim.", "7", "d", "di", "dim", "dimanche"},
        };
        for (String [] dayRow: days) {
            for (String dayValue: dayRow) {
                if (StringUtils.equalsCaseInsensitive(dayValue, day)) {
                    return dayRow[0];
                }
            }
        }
        return day;
    }
}
