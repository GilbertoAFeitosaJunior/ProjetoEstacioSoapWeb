package mobi.stos.projetoestacio.dao.impl;

import mobi.stos.projetoestacio.bean.Aluno;
import mobi.stos.projetoestacio.common.AbstractHibernateDao;
import mobi.stos.projetoestacio.dao.IAlunoDao;
import org.springframework.stereotype.Repository;

@Repository
public class AlunoDao extends AbstractHibernateDao<Aluno> implements IAlunoDao {

    public AlunoDao() {
        super(Aluno.class);
    }

}
