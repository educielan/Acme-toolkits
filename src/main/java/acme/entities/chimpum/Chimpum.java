package acme.entities.chimpum;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.item.Item;
import acme.framework.datatypes.Money;
import acme.framework.entities.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Chimpum extends AbstractEntity{

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;
	
	
	// Attributes -------------------------------------------------------------
	
	//"^[A-Z]{3}-[0-9]{2}-[0-9]{2}-[0-9]{2}" para ABC-22-05-24
	@NotBlank
	@Pattern(regexp ="^[0-9]{2}-[0-9]{2}-[0-9]{2}$")
	protected String			code; //no unico porque no especifica en enunciado
	
	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	protected Date creationMoment;
	
	@NotBlank
	@Length(min = 1, max = 100)
	protected String title;
	
	@NotBlank
	@Length(min = 1, max = 255)
	protected String description;
	

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				startDate;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				endDate;
	
	@NotNull
	@Valid
	protected Money budget;
	
	@URL
	protected String			link;
	



	// Relationships ----------------------------------------------------------
	
	
	//CONTROL CHECK----------------
	@NotNull
	@OneToOne(optional=false)
	@Valid
	protected Item item;

	
}

