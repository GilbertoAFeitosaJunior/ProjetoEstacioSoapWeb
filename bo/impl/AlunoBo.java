package mobi.stos.projetoestacio.bo.impl;

import mobi.stos.projetoestacio.bean.Aluno;
import mobi.stos.projetoestacio.bo.IAlunoBo;
import mobi.stos.projetoestacio.common.AbstractService;
import mobi.stos.projetoestacio.common.IOperations;
import mobi.stos.projetoestacio.dao.IAlunoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlunoBo extends AbstractService<Aluno> implements IAlunoBo {

    @Autowired
    private IAlunoDao dao;

    @Override
    protected IOperations<Aluno> getDao() {
        return dao;
    }

  

}
