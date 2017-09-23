package mobi.stos.projetoestacio.common;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.List;
import mobi.stos.projetoestacio.util.Consulta;
import mobi.stos.projetoestacio.util.HibernateAlias;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public abstract class AbstractHibernateDao<T extends Serializable> implements IOperations<T> {

    private final Class<T> clazz;

    public AbstractHibernateDao(Class<T> clazz) {
        this.clazz = Preconditions.checkNotNull(clazz);
    }

    
    
    @Autowired
    protected SessionFactory sessionFactory;

    @Override
    public T load(Long id) {
        return (T) getCurrentSession().get(clazz, id);
    }

    @Override
    public List<T> list(final Consulta consulta) {
        Criteria criteria = getCurrentSession().createCriteria(clazz);
        List<HibernateAlias> alias = consulta.getAliasTable();
        if (!alias.isEmpty()) {
            for (HibernateAlias ha : alias) {
                criteria.createAlias(ha.getTableJoin(), ha.getAlias(), ha.getJoinType());
            }
        }
        for (Criterion criterion : consulta.getCondicaoConsulta()) {
            criteria.add(criterion);
        }

        criteria.setProjection(Projections.rowCount());
        consulta.setQtdResultados(((Long) criteria.uniqueResult()).intValue());
        criteria.setProjection(null);

        criteria.setFirstResult(consulta.getPaginacao()).setMaxResults(consulta.getLimiteResultados());
        if (consulta.getOrders() != null && !consulta.getOrders().isEmpty()) {
            for (Order order : consulta.getOrders()) {
                criteria.addOrder(order);
            }
        } else {
            criteria.addOrder(Order.asc(consulta.getCampo()));
        }

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public List<T> listall() {
        return getCurrentSession().createQuery("from " + clazz.getName()).list();
    }

    @Override
    public List<T> listall(String[] order) {
        Criteria criteria = getCurrentSession().createCriteria(clazz);
        for (String s : order) {
            if (s.endsWith("DESC") || s.endsWith("desc")) {
                s = s.replace("DESC", "").replace("desc", "");
                criteria.addOrder(Order.desc(s.trim()));
            } else {
                s = s.replace("ASC", "").replace("asc", "");
                criteria.addOrder(Order.asc(s.trim()));
            }
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public final T persist(T entity) {
        Preconditions.checkNotNull(entity);
        return (T) getCurrentSession().merge(entity);
    }

    @Override
    public List<T> persist(List<T> entities) {
        for (T t : entities) {
            Preconditions.checkNotNull(t);
            t = (T) getCurrentSession().merge(t);
        }
        return entities;
    }

    @Override
    public void delete(final long entityId) {
        final T entity = load(entityId);
        Preconditions.checkState(entity != null);
        Preconditions.checkNotNull(entity);
        getCurrentSession().delete(entity);
    }

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
