package acme.entities.patronageReport;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.patronage.Patronage;
import acme.framework.entities.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PatronageReport extends AbstractEntity{
	
	// Serialisation identifier -----------------------------------------------
	protected static final long serialVersionUID = 1L;
	
	// Attributes -------------------------------------------------------------
	
	@NotBlank       
	@Pattern(regexp = "^[A-Z]{3}-[0-9]{3}(-[A-Z])?:[0-9]{4}$")
	protected String sequenceNumber;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	protected Date creationMoment;
	
	@NotBlank
	@Length(max=255)
	protected String memorandum;
	
	@URL
	protected String moreInfo;
	

	// Relationships ----------------------------------------------------------
	
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Patronage patronage;
}
