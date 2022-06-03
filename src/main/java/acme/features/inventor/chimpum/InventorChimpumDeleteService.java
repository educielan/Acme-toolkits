package acme.features.inventor.chimpum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.chimpum.Chimpum;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractDeleteService;
import acme.roles.Inventor;

@Service
public class InventorChimpumDeleteService implements AbstractDeleteService<Inventor,Chimpum>{
	
	@Autowired
	protected InventorChimpumRepository repository;
	
	@Override
	public boolean authorise(final Request<Chimpum> request) {
		
	
		assert request != null;
		boolean result;
		int chimpumId;
		Inventor inventor;
		
		chimpumId = request.getModel().getInteger("id");
		final Chimpum chimpum = this.repository.findChimpumById(chimpumId);
		
		inventor = chimpum.getItem().getInventor();
		result = request.isPrincipal(inventor);
		
		return result;
	
	
	}
	
	@Override
	public void bind(final Request<Chimpum> request, final Chimpum entity, final Errors errors) {
		
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		
		request.bind(entity, errors, "code","title","description","startDate","endDate","budget","link");
		
	}
	

	@Override
	public void unbind(final Request<Chimpum> request, final Chimpum entity, final Model model) {
		
		assert request != null;
		assert entity != null;
		assert model != null;

		
		request.unbind(entity, model, "code","title","description","creationMoment","startDate","endDate","budget","link");
	}
	
	
	@Override
	public Chimpum findOne(final Request<Chimpum> request) {
	
		assert request != null;
		final Chimpum result;
		int chimpumId;
		
		chimpumId = request.getModel().getInteger("id");
		result = this.repository.findChimpumById(chimpumId);
		
		return result;
	}
	
	@Override
	public void validate(final Request<Chimpum> request, final Chimpum entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
	}

	@Override
	public void delete(final Request<Chimpum> request, final Chimpum entity) {
		assert request != null;
		assert entity!= null;
		this.repository.delete(entity);
		
	}

	
	
}