package mobi.stos.projetoestacio.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mobi.stos.projetoestacio.bean.Usuario;
import mobi.stos.projetoestacio.exception.LoginExpiradoException;
import mobi.stos.projetoestacio.util.Consulta;
import mobi.stos.projetoestacio.util.JsonReturn;
import mobi.stos.projetoestacio.util.Keys;
import mobi.stos.projetoestacio.util.LimiteResultadoEnum;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.json.annotations.JSON;

@ParentPackage("json-default")
public abstract class GenericAction extends ActionSupport implements Preparable, ServletRequestAware, ServletResponseAware {

    public static JsonReturn jsonReturn;
    private String redirectURL;
    private String html;
    private Consulta consulta;
    public static HttpServletRequest request;
    public static HttpServletResponse response;
    private static Usuario logged;

    private String menu;
    private String submenu;

    public static void isLogged(HttpServletRequest request) throws LoginExpiradoException {
        Map session = ActionContext.getContext().getSession();
        logged = (Usuario) session.get("logged");
        if (logged == null) {
            if (!tryResurrect()) {
                throw new LoginExpiradoException();
            }
        }
    }

    public static Usuario getLogged() {
        Map session = ActionContext.getContext().getSession();
        return logged = (Usuario) session.get("logged");
    }

    protected static String testarCookie() {
        String hash = "";

        Cookie uid = null;
        Cookie biscoitos[] = request.getCookies();
        if (biscoitos != null) {
            for (Cookie biscoito : biscoitos) {
                if (biscoito.getName().equals("uid")) {
                    uid = biscoito;
                    break;
                }
            }
        }

        try {
            if (uid != null) {
                hash = uid.getValue();
            }
        } catch (Exception e) {
        }
        return hash;
    }

    private static boolean tryResurrect() {
//        String hash = testarCookie();
//        if (hash != null && !hash.equals("")) {
//            try {
//                logged = (new UsuarioBo()).byHash(hash);
//                if (logged != null) {
//                    Map session = ActionContext.getContext().getSession();
//                    session.put("logged", logged);
//                    return true;
//                } else {
//                    return false;
//                }
//            } catch (Exception e) {
//                return false;
//            }
//        } else {
//            return false;
//        }
        return false;
    }

    // ** Getters and setters
    @JSON(serialize = false)
    public List getLimiteResultadoEnum() {
        return Arrays.asList(LimiteResultadoEnum.values());
    }

    @JSON(serialize = false)
    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    @JSON(serialize = false)
    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    @JSON(serialize = false)
    public Date getCurrentDate() {
        return new Date();
    }

    @JSON(serialize = false)
    public String getMenu() {
        return menu;
    }

    @JSON(serialize = false)
    public String getSubmenu() {
        return submenu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public void setSubmenu(String submenu) {
        this.submenu = submenu;
    }

    @JSON(serialize = false)
    public List getBooleanConditionEnum() {
        List<Keys> keys = new ArrayList<>();
        keys.add(new Keys("true", "Sim"));
        keys.add(new Keys("false", "Não"));
        return keys;
    }

    public JsonReturn getJsonReturn() {
        return jsonReturn;
    }

    @JSON(serialize = false)
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

}
