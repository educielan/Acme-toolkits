package acme.features.inventor.chimpum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.chimpum.Chimpum;
import acme.entities.item.Item;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.features.authenticated.systemConfiguration.AuthenticatedSystemConfigurationRepository;
import acme.features.inventor.item.InventorItemRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractUpdateService;
import acme.roles.Inventor;
import spamDetector.SpamDetector;

@Service
public class InventorChimpumUpdateService implements AbstractUpdateService<Inventor,Chimpum> {
	
	@Autowired
	protected InventorChimpumRepository repository;
	
	@Autowired
	protected InventorItemRepository itemRepository;
	
	@Autowired
	protected AuthenticatedSystemConfigurationRepository systemConfigRepository;
	
	@Override
	public boolean authorise(final Request<Chimpum> request) {
		assert request != null;
		
		boolean result;
		int chimpumId;
		
		chimpumId = request.getModel().getInteger("id");
		final Chimpum chimpum = this.repository.findChimpumById(chimpumId);
		
		
		result = request.isPrincipal(chimpum.getItem().getInventor());
		
		return result;
	}

	@Override
	public void bind(final Request<Chimpum> request, final Chimpum entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		int itemId;
		Item item;
		
		itemId = request.getModel().getInteger("item");
		item = this.repository.findItemById(itemId);
		entity.setItem(item);
	
		
		request.bind(entity, errors,"title","description","startDate","endDate","budget","link");
		
	}
	

	@Override
	public void unbind(final Request<Chimpum> request, final Chimpum entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		int inventorId;
		inventorId = request.getPrincipal().getActiveRoleId();
		
		final Collection<Item> items;
		final Collection<Item> myPublishedItems= new HashSet<>();
		final Collection<Item> assignedItems = this.repository.findAllItemsWithChimpums();
		items = this.repository.findAllItemsOfInventor(inventorId);
		
		for(final Item i:items) {
			if(i.isPublished() && !assignedItems.contains(i)) {
				myPublishedItems.add(i);
			}
		}
		
		model.setAttribute("items", myPublishedItems);
		model.setAttribute("itemSelected", entity.getItem());
		request.unbind(entity, model,"title","description","creationMoment","startDate","endDate","budget","link");
		
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

        
//		if(!errors.hasErrors("code")) {
//			
////				final boolean codeIsOk;
////				boolean codeIsNotDuplicated;
//				
//				Chimpum exist;
//				exist = this.repository.findChimpumByCode(entity.getCode());
//				
////				final Date now = Calendar.getInstance().getTime();
////				final String codeTrim =entity.getCode().substring(0,8);
////				final SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
////				final String creationMoment = format.format(now);
////				
////				codeIsOk = creationMoment.equals(codeTrim);
//				
//				if(exist!=null) {
//	    			errors.state(request, exist.getId()==entity.getId(), "code", "inventor.chimpum.form.error.duplicated-code");
//	    			//errors.state(request, codeIsOk, "code", "inventor.chimpum.form.error.format");
//	    		}
//	
//		
//		}
		
        if(!errors.hasErrors("title")) {
        	
            final boolean res;
            
            final SystemConfiguration systemConfig = this.systemConfigRepository.findSystemConfiguration();
            final String StrongEN = systemConfig.getStrongSpamTermsEn();
            final String StrongES = systemConfig.getStrongSpamTermsEs();
            final String WeakEN = systemConfig.getWeakSpamTermsEn();
            final String WeakES = systemConfig.getWeakSpamTermsEs();

            final double StrongThreshold = systemConfig.getStrongThreshold();
            final double WeakThreshold = systemConfig.getWeakThreshold();
            
            res = SpamDetector.spamDetector(entity.getTitle(),StrongEN,StrongES,WeakEN,WeakES,StrongThreshold,WeakThreshold);
            
            errors.state(request, res, "title", "alert-message.form.spam");
        }
        
        if(!errors.hasErrors("description")) {
            final boolean res;
            
            final SystemConfiguration systemConfig = this.systemConfigRepository.findSystemConfiguration();
            final String StrongEN = systemConfig.getStrongSpamTermsEn();
            final String StrongES = systemConfig.getStrongSpamTermsEs();
            final String WeakEN = systemConfig.getWeakSpamTermsEn();
            final String WeakES = systemConfig.getWeakSpamTermsEs();

            final double StrongThreshold = systemConfig.getStrongThreshold();
            final double WeakThreshold = systemConfig.getWeakThreshold();
            
            res = SpamDetector.spamDetector(entity.getDescription(),StrongEN,StrongES,WeakEN,WeakES,StrongThreshold,WeakThreshold);
            
            errors.state(request, res, "description", "alert-message.form.spam");
        }
        
		if(!errors.hasErrors("startDate")) {
			
			final Date startDateMin = DateUtils.addMonths(entity.getCreationMoment(), 1);
			
			errors.state(request, entity.getStartDate().after(startDateMin), "startDate", "inventor.chimpum.form.error.start-date");			
		}
		
		if(!errors.hasErrors("endDate") && entity.getStartDate()!=null) {
			
			final Date endDateMin = DateUtils.addWeeks(entity.getStartDate(), 1);
			final Date endDate = entity.getEndDate();
			
			errors.state(request, endDate.before(endDateMin) , "endDate", "inventor.chimpum.form.error.end-date");
			
		}

		if (!errors.hasErrors("budget")) {
			
			final List<String> currencies = new ArrayList<>();
			String currency;
			Double amount;
			
			for(final String c: this.systemConfigRepository.acceptedCurrencies().split(",")) {
				currencies.add(c.trim());
			}
			
			currency = entity.getBudget().getCurrency();
			amount = entity.getBudget().getAmount();
			
			errors.state(request, currencies.contains(currency) , "budget","inventor.chimpum.form.error.currency-not-valid");
			errors.state(request, amount>0.00 , "budget","inventor.chimpum.form.error.amount-negative");
		}

		
		//Método auxiliar que genera automáticamente el código
		
//		public String codeGenerator(final LocalDate creationMoment) {
//			String result = "";
//			
//			final Integer day = creationMoment.getDayOfMonth();
//			final Integer month = creationMoment.getMonthValue();
//			final Integer year = creationMoment.getYear();
//
//			final String yearCode = year.toString().substring(2, 4);
//			String monthCode= "";
//			String dayCode= "";
//			
//			if(month.toString().length()==1) {
//				monthCode = "0" + month.toString();
//			}else{
//				monthCode = month.toString();
//			}
//				
//			if(day.toString().length()==1) {
//				dayCode = "0" + day.toString();
//			}else {
//				dayCode = day.toString();
//			}
//					
//			result = yearCode + "-" + monthCode + "-" + dayCode;
//				
//			return result;
//		}

		
		// Método para crear un diccionario con los meses
//		public Map<String,Integer> monthsMapGenerator(){
//			final Map<String,Integer> months = new HashMap<String,Integer>();
	//
//			for (int i=1 ; i<13 ; i++) {
//				months.put(Month.of(i).toString(), i);
//			}
//				
//			return months;
//		}
		
	}
		
		@Override
		public void update(final Request<Chimpum> request, final Chimpum entity) {
			assert request != null;
			assert entity != null;
			
			this.repository.save(entity);
		}

		
	}
