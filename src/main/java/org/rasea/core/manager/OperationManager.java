package org.rasea.core.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.rasea.core.entity.Application;
import org.rasea.core.entity.Operation;
import org.rasea.core.entity.Resource;

@AutoCreate
@Name("operationManager")
public class OperationManager extends AbstractManager {

	private static final long serialVersionUID = -37119785921184139L;

	@Transactional(TransactionPropagationType.REQUIRED)
	public void delete(final Operation operation) {
		final Query query = this.getEntityManager().createQuery("delete Operation o where o.id = :operationId");

		query.setParameter("operationId", operation.getId());

		query.executeUpdate();
		this.getEntityManager().flush();
	}

	@SuppressWarnings("unchecked")
	public List<Operation> find(final Application application) {
		final Query query = this.getEntityManager().createQuery("from Operation o where o.application.id = :applicationId order by o.name");

		query.setParameter("applicationId", application.getId());

		return query.getResultList();
	}

	public Operation find(final Application application, final String name) {
		final StringBuffer ejbql = new StringBuffer(120);

		ejbql.append("   from Operation o ");
		ejbql.append("  where o.application.id = :applicationId ");
		ejbql.append("    and o.name = :name ");
		ejbql.append("  order by o.name ");

		final Query query = this.getEntityManager().createQuery(ejbql.toString());

		query.setParameter("applicationId", application.getId());
		query.setParameter("name", name);

		Operation result;

		try {
			result = (Operation) query.getSingleResult();
		} catch (final NoResultException cause) {
			result = null;
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Operation> find(final Resource resource) {
		final Query query = this.getEntityManager().createQuery(
				"select p.operation from Permission p where p.resource.id = :resourceId order by p.operation.name");

		query.setParameter("resourceId", resource.getId());

		return new ArrayList<Operation>(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Operation> findByFilter(final Application application, final String filter) {
		final StringBuffer ejbql = new StringBuffer(120);

		ejbql.append("  from Operation o ");
		ejbql.append(" where o.application.id = :applicationId ");
		ejbql.append("   and lower(o.name) like :filter ");
		ejbql.append("  order by o.name ");

		final Query query = this.getEntityManager().createQuery(ejbql.toString());

		query.setParameter("filter", "%" + filter + "%");
		query.setParameter("applicationId", application.getId());
		return query.getResultList();
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void insert(final Operation operation) {
		this.getEntityManager().persist(operation);
		this.getEntityManager().flush();
	}

	public Operation load(final Long id) {
		return this.getEntityManager().find(Operation.class, id);
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void update(final Operation operation) {
		this.getEntityManager().merge(operation);
		this.getEntityManager().flush();
	}

}
