package mobi.stos.projetoestacio.util;

import org.apache.commons.lang3.StringUtils;

public class MenuBuilder {

    private static String url;
    private static String title;
    private static String tooltip;
    private static boolean active;
    private static String icon;

    public MenuBuilder() {
    }

    public static String create(String url, String title, String tooltip,
            boolean active, String icon) throws NullPointerException {
        MenuBuilder.url = url;
        MenuBuilder.title = title;
        MenuBuilder.tooltip = tooltip;
        MenuBuilder.active = active;
        MenuBuilder.icon = icon;

        return create();
    }

    private static String create() throws NullPointerException {
        if (StringUtils.isEmpty(url)) {
            throw new NullPointerException("Url obrigatório");
        }
        if (StringUtils.isEmpty(title)) {
            throw new NullPointerException("Título obrigatório");
        }

        StringBuilder menu = new StringBuilder();
        menu.append("<li>");
        menu.append("<a href=\"").append(url).append("\"");
        if (active) {
            menu.append(" class=\"active\"");
        }
        menu.append(">");
        if (!StringUtils.isEmpty(icon)) {
            menu.append("<i class=\"fa ").append(icon).append("\"></i>");
        }
        menu.append("<span>").append(title).append("</span>");
        menu.append("</a>");
        menu.append("</li>");
        return menu.toString();
    }
}
