package mobi.stos.projetoestacio.soap;

import java.util.List;
import javax.jws.WebParam;
import javax.jws.WebService;
import mobi.stos.projetoestacio.bean.Aluno;
import mobi.stos.projetoestacio.bo.IAlunoBo;
import mobi.stos.projetoestacio.spring.AppConfig;
import mobi.stos.projetoestacio.util.Consulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author feito
 */
@WebService
public class AlunoWS {

    private IAlunoBo iAlunoBo;

    private void startContext() {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.iAlunoBo = (IAlunoBo) context.getBean(IAlunoBo.class);
    }

    public Aluno carregar(@WebParam(name = "id") Long id) {
        System.out.println("id: " + id);
        this.startContext();
        return this.iAlunoBo.load(id);
    }

    public List<Aluno> pesquisar(@WebParam(name = "query") String query, @WebParam(name = "page") int page) {
        this.startContext();

        System.out.println("query: " + query);
        System.out.println("page: " + page);

        Consulta consulta = new Consulta();
        consulta.setCampo("nome");
        consulta.setValor(query);
        consulta.setPaginaAtual(page);

        System.out.println("bo " + this.iAlunoBo);

        List<Aluno> list = this.iAlunoBo.list(consulta);
        System.out.println("list : " + list.size());
        return list;
    }

}
