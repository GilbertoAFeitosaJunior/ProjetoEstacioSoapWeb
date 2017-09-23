package mobi.stos.projetoestacio.dao.impl;

import mobi.stos.projetoestacio.bean.Usuario;
import mobi.stos.projetoestacio.common.AbstractHibernateDao;
import mobi.stos.projetoestacio.dao.IUsuarioDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDao extends AbstractHibernateDao<Usuario> implements IUsuarioDao {

    public UsuarioDao() {
        super(Usuario.class);
    }

    @Override
    public Usuario byLogin(String login) {
        Criteria criteria = getCurrentSession().createCriteria(Usuario.class);
        criteria.add(Restrictions.eq("login", login));
        criteria.setMaxResults(1);
        return (Usuario) criteria.uniqueResult();
    }

    @Override
    public Usuario byHash(String hash) {
        Criteria criteria = getCurrentSession().createCriteria(Usuario.class);
        criteria.add(Restrictions.eq("hash", hash));
        criteria.setMaxResults(1);
        return (Usuario) criteria.uniqueResult();
    }

}
