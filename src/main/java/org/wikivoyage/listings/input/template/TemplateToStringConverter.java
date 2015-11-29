package org.wikivoyage.listings.input.template;

public interface TemplateToStringConverter {
    String getTemplateName();
    String convertToString(TemplateNode template);
}
