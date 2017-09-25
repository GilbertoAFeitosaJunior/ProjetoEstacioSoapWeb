package mobi.stos.projetoestacio.bo.impl;

import mobi.stos.projetoestacio.bean.Curso;
import mobi.stos.projetoestacio.bo.ICursoBo;
import mobi.stos.projetoestacio.common.AbstractService;
import mobi.stos.projetoestacio.common.IOperations;
import mobi.stos.projetoestacio.dao.ICursoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CursoBo extends AbstractService<Curso> implements ICursoBo {

    @Autowired
    private ICursoDao dao;

    @Override
    protected IOperations<Curso> getDao() {
        return dao;
    }

  

}
