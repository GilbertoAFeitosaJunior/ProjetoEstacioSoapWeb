package mobi.stos.projetoestacio.action;

import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mobi.stos.projetoestacio.bean.Usuario;
import mobi.stos.projetoestacio.bo.IUsuarioBo;
import mobi.stos.projetoestacio.exception.AccessDeniedException;
import mobi.stos.projetoestacio.exception.LoginException;
import mobi.stos.projetoestacio.exception.LoginExpiradoException;
import mobi.stos.projetoestacio.exception.SenhaException;
import mobi.stos.projetoestacio.util.Captcha;
import mobi.stos.projetoestacio.util.Consulta;
import mobi.stos.projetoestacio.util.Keys;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;

public class UsuarioAction extends GenericAction {

    private Usuario usuario;
    private List<Usuario> usuarios;
    @Autowired
    private IUsuarioBo usuarioBo;

    @Action(value = "resurrectLogin",
            interceptorRefs = {
                @InterceptorRef(value = "basicStack")},
            results = {
                @Result(name = INPUT, location = "/app/home/login.jsp")
                ,
                @Result(name = SUCCESS, location = "/app/home/index.jsp")
            })
    public String resurrectLogin() {
        try {
            String hash = GenericAction.testarCookie();
            if (hash == null) {
                return INPUT;
            } else {
                usuario = usuarioBo.byHash(hash);
                if (usuario == null) {
                    return INPUT;
                }
                usuario = usuarioBo.login(usuario.getLogin(), usuario.getSenha());
                Map session = ActionContext.getContext().getSession();
                session.put("logged", usuario);

                return SUCCESS;
            }
        } catch (LoginException | SenhaException e) {
            addActionError("Erro ao tentar logar. " + e.getMessage());
            return ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Erro ao tentar logar. " + e.getMessage());
            return ERROR;
        }
    }

    @Action(value = "login",
            interceptorRefs = {
                @InterceptorRef(value = "basicStack")},
            results = {
                @Result(name = ERROR, location = "/app/home/login.jsp")
                ,
                @Result(name = SUCCESS, location = "/app/home/index.jsp")
//                @Result(name = SUCCESS, type = "redirectAction", params = {"actionName", "dashboard"})
            })
    public String login() {
        System.out.println("login......");
        Map session = ActionContext.getContext().getSession();
        try {
            if (session.get("tentativa") != null && ((Integer) session.get("tentativa") >= 3)) {
                if (!Captcha.solveCaptcha(request)) {
                    addFieldError("captcha", "Captcha inválido!");
                    return ERROR;
                }
            }
            usuario = usuarioBo.login(usuario.getLogin(), usuario.getSenha());
            if (usuario == null) {
                throw new AccessDeniedException();
            }
            session.put("logged", this.usuario);
            if (request.getParameter("keepConnect") != null) {
                if (request.getParameter("keepConnect").equals("true")) {
                    Cookie biscoito = new Cookie("uid", usuario.getHash());
                    biscoito.setPath("/");
                    biscoito.setMaxAge(60 * 60 * 24 * 14);
                    biscoito.setHttpOnly(true);
                    response.addCookie(biscoito);
                }
            } else {
                removeCookie();
            }

            return SUCCESS;
        } catch (AccessDeniedException | LoginException | SenhaException e) {
            int tentativas = 1;
            if (session.get("tentativa") != null) {
                tentativas = (Integer) session.get("tentativa");
                tentativas++;
            }
            session.put("tentativa", tentativas);

            addActionError("Erro ao tentar logar. " + e.getMessage());
            return ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Erro ao tentar logar. " + e.getMessage());
            return ERROR;
        }
    }

    @Action(value = "logout",
            interceptorRefs = {
                @InterceptorRef(value = "basicStack")},
            results = {
                @Result(name = SUCCESS, location = "/app/home/login.jsp")
            })
    public String logout() {
        Map session = ActionContext.getContext().getSession();
        session.clear();

        removeCookie();
        return SUCCESS;
    }

    private void removeCookie() {
        Cookie[] biscoitos = request.getCookies();
        if (biscoitos != null) {
            Cookie wafe;
            for (Cookie biscoito : biscoitos) {
                wafe = biscoito;
                wafe.setPath("/");
                if (wafe.getName().equals("uid")) {
                    wafe.setMaxAge(0);
                    response.addCookie(wafe);
                }
            }
        }
    }

    @Action(value = "prepareUsuario",
            interceptorRefs = {
                @InterceptorRef(value = "basicStack")},
            results = {
                @Result(name = ERROR, location = "/app/notify/")
                ,
                @Result(name = SUCCESS, location = "/app/usuario/formulario.jsp")
            })
    public String preparar() {
        try {
            GenericAction.isLogged(request);
            if (usuario != null && usuario.getId() != null) {
                usuario = this.usuarioBo.load(this.usuario.getId());
            }
            return SUCCESS;
        } catch (Exception e) {
            addActionError("Erro ao processar a informação. Erro: " + e.getMessage());
            return ERROR;
        }
    }

    @Action(value = "persistUsuario",
            interceptorRefs = {
                @InterceptorRef(value = "fileUploadStack")
                ,
                @InterceptorRef(value = "basicStack")},
            results = {
                @Result(name = SUCCESS, location = "/app/notify/")
            })
    public String persist() {
        try {
            GenericAction.isLogged(request);

            if (usuario.getId() != null) {
                Usuario entity = usuarioBo.load(usuario.getId());
                entity.setLogin(usuario.getLogin());
                entity.setNome(usuario.getNome());
                if (StringUtils.isNotEmpty(usuario.getSenha())) {
                    entity.setSenha(usuario.getSenha());
                }
                usuario = entity;
            }
            usuarioBo.persist(usuario);
            addActionMessage("Registro salvo com sucesso.");
            setRedirectURL("listUsuario");
        } catch (Exception e) {
            addActionError("Erro ao processar a informação. Erro: " + e.getMessage());
        }
        return SUCCESS;
    }

    @Action(value = "deleteUsuario",
            interceptorRefs = {
                @InterceptorRef(value = "basicStack")},
            results = {
                @Result(name = SUCCESS, location = "/app/notify/")
            })
    public String delete() {
        try {
            GenericAction.isLogged(request);
            usuarioBo.delete(usuario.getId());
            addActionMessage("Registro excluído com sucesso.");
            setRedirectURL("listUsuario");
        } catch (LoginExpiradoException e) {
            addActionError("Erro ao processar a informação. Erro: " + e.getMessage());
        }
        return SUCCESS;
    }

    @Action(value = "listUsuario",
            interceptorRefs = {
                @InterceptorRef(value = "basicStack")},
            results = {
                @Result(name = ERROR, location = "/app/notify/")
                ,
                @Result(name = SUCCESS, location = "/app/usuario/")
            })
    public String list() {
        try {
            GenericAction.isLogged(request);
            if (getConsulta() == null) {
                String field = (String) getCamposConsultaEnum().get(0).getKey();
                setConsulta(new Consulta(field));
            }
            usuarios = usuarioBo.list(getConsulta());

            return SUCCESS;
        } catch (Exception e) {
            addActionError("Erro ao processar a informação. Erro: " + e.getMessage());
            e.printStackTrace();
            return ERROR;
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @JSON(serialize = false)
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    @JSON(serialize = false)
    public List<Keys> getCamposConsultaEnum() {
        List<Keys> list = new ArrayList<>();
        list.add(new Keys("nome", "Nome"));
        return list;
    }

    @Override
    public void prepare() throws Exception {
        setMenu(Usuario.class.getSimpleName());
    }

    @Override
    public void setServletRequest(HttpServletRequest hsr) {
        GenericAction.request = hsr;
    }

    @Override
    public void setServletResponse(HttpServletResponse hsr) {
        GenericAction.response = hsr;
    }

}
