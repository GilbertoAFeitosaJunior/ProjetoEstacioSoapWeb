package mobi.stos.projetoestacio.action;

import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mobi.stos.projetoestacio.bean.Aluno;
import mobi.stos.projetoestacio.bo.IAlunoBo;
import mobi.stos.projetoestacio.enumm.SexoEnum;
import mobi.stos.projetoestacio.exception.LoginExpiradoException;
import mobi.stos.projetoestacio.util.Consulta;
import mobi.stos.projetoestacio.util.Keys;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;

public class AlunoAction extends GenericAction {

    private Aluno aluno;
    private List<Aluno> alunos;

    @Autowired
    private IAlunoBo iAlunoBo;

    @Action(value = "persistAluno",
            results = {
                @Result(name = SUCCESS, location = "/app/notify/")
                ,
                @Result(name = ERROR, location = "/app/notify/")
            })
    public String salvar() {
        try {

            aluno = this.iAlunoBo.persist(aluno);

            addActionMessage("Registro salvo com sucesso.");
            setRedirectURL("listAluno");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Erro ao tentar salvar a categoria. Erro: " + e.getMessage());
            return ERROR;
        }
    }

    @Action(value = "listAluno",
            results = {
                @Result(name = SUCCESS, location = "/app/aluno/index.jsp")
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
            this.alunos = this.iAlunoBo.list(getConsulta());
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Erro ao tentar listar os registros. Erro: " + e.getMessage());
            return ERROR;
        }
    }

    @Action(value = "prepareAluno",
            interceptorRefs = {
                @InterceptorRef(value = "basicStack")},
            results = {
                @Result(name = ERROR, location = "/app/notify/")
                ,
                @Result(name = SUCCESS, location = "/app/aluno/formulario.jsp")
            })
    public String preparar() {
        try {
            GenericAction.isLogged(request);
            if (aluno != null && aluno.getId() != null) {
                aluno = this.iAlunoBo.load(this.aluno.getId());
            }
            return SUCCESS;
        } catch (Exception e) {
            addActionError("Erro ao processar a informação. Erro: " + e.getMessage());
            return ERROR;
        }
    }

    @Action(value = "deleteAluno",
            interceptorRefs = {
                @InterceptorRef(value = "basicStack")},
            results = {
                @Result(name = SUCCESS, location = "/app/notify/")
            })
    public String delete() {
        try {
            GenericAction.isLogged(request);
            iAlunoBo.delete(aluno.getId());
            addActionMessage("Registro excluído com sucesso.");
            setRedirectURL("listAluno");
        } catch (LoginExpiradoException e) {
            addActionError("Erro ao processar a informação. Erro: " + e.getMessage());
        }
        return SUCCESS;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public List getSexoEnum() {
        return Arrays.asList(SexoEnum.values());
    }

    @JSON(serialize = false)
    public List<Keys> getCamposConsultaEnum() {
        List<Keys> list = new ArrayList<>();
        list.add(new Keys("nome", "Nome"));
        return list;
    }

    @Override
    public void prepare() throws Exception {
        setMenu(Aluno.class.getSimpleName());
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
