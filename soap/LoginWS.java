package mobi.stos.projetoestacio.soap;

import javax.jws.WebParam;
import javax.jws.WebService;
import mobi.stos.projetoestacio.bean.Usuario;
import mobi.stos.projetoestacio.bo.IUsuarioBo;
import mobi.stos.projetoestacio.spring.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 *
 * @author feito
 */
@WebService
public class LoginWS {

    private IUsuarioBo iUsuarioBo;

    private void startContext() {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.iUsuarioBo = (IUsuarioBo) context.getBean(IUsuarioBo.class);
    }

    public Usuario login(@WebParam(name = "login") String login, @WebParam(name = "senha") String senha) throws Exception {
        this.startContext();

        System.out.println("login : " + login);
        System.out.println("senha : " + senha);

        return this.iUsuarioBo.login(login, senha);
    }

}
