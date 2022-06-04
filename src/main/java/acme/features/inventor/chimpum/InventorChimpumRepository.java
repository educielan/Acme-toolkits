package acme.features.inventor.chimpum;



import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.chimpum.Chimpum;
import acme.entities.item.Item;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface InventorChimpumRepository extends AbstractRepository {

	@Query("SELECT c FROM Chimpum c WHERE c.item.inventor.id =:inventorId")
	Collection<Chimpum> findAllChimpumsByInventorId(int inventorId);
	
	@Query("SELECT c FROM Chimpum c WHERE c.id =:id")
	Chimpum findChimpumById(int id);
	
	@Query("SELECT i FROM Item i WHERE i.inventor.id =:inventorId")
	Collection<Item> findAllItemsOfInventor(int inventorId);
	
	@Query("SELECT i FROM Item i WHERE i.id =:itemId")
	Item findItemById(int itemId);
	
	@Query("SELECT c.item FROM Chimpum c")
	Collection<Item> findAllItemsWithChimpums();

	@Query("SELECT c FROM Chimpum c WHERE c.code =:code")
	Chimpum findChimpumByCode(String code);
//	

//	
	@Query("select c from SystemConfiguration c")
	SystemConfiguration findSystemConfiguration();
//	

//	


//
//	@Query("SELECT c.code FROM Chimpum c")
//	Collection<String> findAllChimpumsCode();
//	
	@Query("select systemConfiguration from SystemConfiguration systemConfiguration")
	SystemConfiguration systemConfiguration();

}