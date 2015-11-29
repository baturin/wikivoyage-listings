package org.wikivoyage.listings.language.french.template;

import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;
import org.wikivoyage.listings.utils.StringUtils;

/**
 * Parse French "Horaire" template (working hours), per https://fr.wikivoyage.org/wiki/Mod%C3%A8le:Horaire
 */
public class HoraireTemplateToStringConverter implements TemplateToStringConverter {
    @Override
    public String convertToString(TemplateNode template) {
        String days = "";
        if (!template.isAbsentOrEmptyPositionalArg(0)) {
            days += convertDay(template.getPositionalArg(0));
        }
        if (!template.isAbsentOrEmptyPositionalArg(1)) {
            days += " - " + convertDay(template.getPositionalArg(1));
        }

        String timeGaps;
        String timeGap1 = timeGap(
            template.getPositionalArg(2, ""), template.getPositionalArg(3, ""),
            template.getPositionalArg(4, ""), template.getPositionalArg(5, "")
        );
        String timeGap2 = timeGap(
            template.getPositionalArg(6, ""), template.getPositionalArg(7, ""),
            template.getPositionalArg(8, ""), template.getPositionalArg(9, "")
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
