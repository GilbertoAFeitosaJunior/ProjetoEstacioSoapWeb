package mobi.stos.projetoestacio.dao;

import mobi.stos.projetoestacio.bean.Usuario;
import mobi.stos.projetoestacio.common.IOperations;

public interface IUsuarioDao extends IOperations<Usuario> {

    Usuario byLogin(String login);

    Usuario byHash(String hash);

}
