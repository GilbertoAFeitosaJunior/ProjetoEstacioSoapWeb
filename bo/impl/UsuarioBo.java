package mobi.stos.projetoestacio.bo.impl;

import java.util.Date;
import mobi.stos.projetoestacio.bean.Usuario;
import mobi.stos.projetoestacio.bo.IUsuarioBo;
import mobi.stos.projetoestacio.common.AbstractService;
import mobi.stos.projetoestacio.common.IOperations;
import mobi.stos.projetoestacio.dao.IUsuarioDao;
import mobi.stos.projetoestacio.exception.LoginException;
import mobi.stos.projetoestacio.exception.SenhaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioBo extends AbstractService<Usuario> implements IUsuarioBo {

    @Autowired
    private IUsuarioDao dao;

    @Override
    protected IOperations<Usuario> getDao() {
        return dao;
    }

    @Override
    public Usuario login(String login, String senha) throws LoginException, SenhaException {
        Usuario usuario = dao.byLogin(login);
        if (usuario == null) {
            throw new LoginException();
        }
        if (!usuario.getSenha().equals(senha)) {
            throw new SenhaException();
        }
        usuario.setUltimoAcesso(new Date());
        dao.persist(usuario);
        return usuario;
    }

    @Override
    public Usuario byHash(String hash) {
        return dao.byHash(hash);
    }

}
