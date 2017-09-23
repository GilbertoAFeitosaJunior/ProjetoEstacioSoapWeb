package mobi.stos.projetoestacio.bo;

import mobi.stos.projetoestacio.bean.Usuario;
import mobi.stos.projetoestacio.common.IOperations;
import mobi.stos.projetoestacio.exception.LoginException;
import mobi.stos.projetoestacio.exception.SenhaException;

public interface IUsuarioBo extends IOperations<Usuario> {

    Usuario login(String login, String senha) throws LoginException, SenhaException;

    Usuario byHash(String hash);
}
