package mobi.stos.projetoestacio.dao.impl;

import mobi.stos.projetoestacio.bean.Curso;
import mobi.stos.projetoestacio.common.AbstractHibernateDao;
import mobi.stos.projetoestacio.dao.ICursoDao;
import org.springframework.stereotype.Repository;

@Repository
public class CursoDao extends AbstractHibernateDao<Curso> implements ICursoDao {

    public CursoDao() {
        super(Curso.class);
    }

}
