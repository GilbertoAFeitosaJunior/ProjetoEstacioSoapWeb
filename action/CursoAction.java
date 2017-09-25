package mobi.stos.projetoestacio.action;

import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mobi.stos.projetoestacio.bean.Aluno;
import mobi.stos.projetoestacio.bean.Curso;
import mobi.stos.projetoestacio.bo.ICursoBo;
import mobi.stos.projetoestacio.exception.LoginExpiradoException;
import mobi.stos.projetoestacio.util.Consulta;
import mobi.stos.projetoestacio.util.Keys;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;

public class CursoAction extends GenericAction {

    private Curso curso;
    private List<Curso> cursos;

    @Autowired
    private ICursoBo iCursoBo;

    @Action(value = "persistCurso",
            results = {
                @Result(name = SUCCESS, location = "/app/notify/")
                ,
                @Result(name = ERROR, location = "/app/notify/")
            })
    public String salvar() {
        try {

            curso = this.iCursoBo.persist(curso);

            addActionMessage("Registro salvo com sucesso.");
            setRedirectURL("listCurso");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Erro ao tentar salvar a categoria. Erro: " + e.getMessage());
            return ERROR;
        }
    }

    @Action(value = "listCurso",
            results = {
                @Result(name = SUCCESS, location = "/app/curso/index.jsp")
                ,
                @Result(name = ERROR, location = "/app/notify/")
            })
    public String listar() {
        try {
            isLogged(request);
            if (getConsulta() == null) {
                String field = (String) getCamposConsultaEnum().get(0).getKey();
                setConsulta(new Consulta(field));
            }
            Consulta consulta = getConsulta();
            consulta.addOrder(Order.asc("nome"));
            this.cursos = this.iCursoBo.list(getConsulta());
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Erro ao tentar listar os registros. Erro: " + e.getMessage());
            return ERROR;
        }
    }

    @Action(value = "prepareCurso",
            interceptorRefs = {
                @InterceptorRef(value = "basicStack")},
            results = {
                @Result(name = ERROR, location = "/app/notify/")
                ,
                @Result(name = SUCCESS, location = "/app/curso/formulario.jsp")
            })
    public String preparar() {
        try {
            GenericAction.isLogged(request);
            if (curso != null && curso.getId() != null) {
                curso = this.iCursoBo.load(this.curso.getId());
            }
            return SUCCESS;
        } catch (Exception e) {
            addActionError("Erro ao processar a informação. Erro: " + e.getMessage());
            return ERROR;
        }
    }

    @Action(value = "deleteCurso",
            interceptorRefs = {
                @InterceptorRef(value = "basicStack")},
            results = {
                @Result(name = SUCCESS, location = "/app/notify/")
            })
    public String delete() {
        try {
            GenericAction.isLogged(request);
            iCursoBo.delete(curso.getId());
            addActionMessage("Registro excluído com sucesso.");
            setRedirectURL("listCurso");
        } catch (LoginExpiradoException e) {
            addActionError("Erro ao processar a informação. Erro: " + e.getMessage());
        }
        return SUCCESS;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

 

    @JSON(serialize = false)
    public List<Keys> getCamposConsultaEnum() {
        List<Keys> list = new ArrayList<>();
        list.add(new Keys("nome", "Nome"));
        return list;
    }

    @Override
    public void prepare() throws Exception {
        setMenu(Curso.class.getSimpleName());
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
