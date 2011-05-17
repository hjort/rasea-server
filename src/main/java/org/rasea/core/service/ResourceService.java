package org.rasea.core.service;

import java.util.List;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.rasea.core.entity.Application;
import org.rasea.core.entity.Resource;
import org.rasea.core.exception.DuplicatedException;
import org.rasea.core.exception.RequiredException;
import org.rasea.core.manager.ResourceManager;
import org.rasea.core.util.ServiceValidator;

@AutoCreate
@Name("resourceService")
public class ResourceService extends AbstractService {

	private static final long serialVersionUID = 6073247375003878035L;

	@In
	private ResourceManager resourceManager;

	@In
	private ServiceValidator serviceValidator;

	@Transactional(TransactionPropagationType.REQUIRED)
	public void delete(final Resource resource) throws RequiredException {
		this.serviceValidator.validateRequired("resource", resource);
		this.serviceValidator.validateRequired("resource.id", resource.getId());

		this.resourceManager.delete(resource);
	}

	public List<Resource> find(final Application application) throws RequiredException {
		this.serviceValidator.validateRequired("application", application);
		this.serviceValidator.validateRequired("application.id", application.getId());

		return this.resourceManager.find(application);
	}

	public Resource find(final Application application, final String name) throws RequiredException {
		this.serviceValidator.validateRequired("application", application);
		this.serviceValidator.validateRequired("application.id", application.getId());
		this.serviceValidator.validateRequired("name", name);

		return this.resourceManager.find(application, name);
	}

	public List<Resource> findByFilter(final Application application, final String filter) throws RequiredException {
		this.serviceValidator.validateRequired("application", application);

		final List<Resource> result;

		if (filter == null) {
			result = this.find(application);
		} else {
			result = this.resourceManager.findByFilter(application, filter);
		}

		return result;
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void insert(final Resource resource) throws RequiredException, DuplicatedException {
		this.serviceValidator.validateRequired("resource", resource);
		this.serviceValidator.validateRequired("resource.name", resource.getName());
		this.serviceValidator.validateRequired("resource.displayName", resource.getDisplayName());
		this.serviceValidator.validateRequired("resource.application", resource.getApplication());
		this.serviceValidator.validateRequired("resource.application.id", resource.getApplication().getId());
		this.serviceValidator.validateDuplicateName(resource);

		this.resourceManager.insert(resource);
	}

	public Resource load(final Long id) throws RequiredException {
		this.serviceValidator.validateRequired("id", id);

		return this.resourceManager.load(id);
	}

	@Transactional(TransactionPropagationType.REQUIRED)
	public void update(final Resource resource) throws RequiredException, DuplicatedException {
		this.serviceValidator.validateRequired("resource", resource);
		this.serviceValidator.validateRequired("resource.id", resource.getId());
		this.serviceValidator.validateRequired("resource.application", resource.getApplication());
		this.serviceValidator.validateRequired("resource.application.id", resource.getApplication().getId());
		this.serviceValidator.validateRequired("resource.name", resource.getName());
		this.serviceValidator.validateRequired("resource.displayName", resource.getDisplayName());
		this.serviceValidator.validateDuplicateName(resource);

		this.resourceManager.update(resource);
	}
}
