package ch.clip.trips.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class BusinessTrip implements Serializable {

	private static final long serialVersionUID = 67027563808382509L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	private LocalDateTime startTrip;
	private LocalDateTime endTrip;

	@OneToMany(mappedBy = "businessTrip")
	@JsonManagedReference
	private List<Meeting> meetings;

	@ManyToMany(mappedBy = "trips")
	@JsonBackReference
	private List<Employee> employees;

	public BusinessTrip() {
		super();

	}

	public BusinessTrip(Long id, String title, String description, LocalDateTime startTrip, LocalDateTime endTrip) {
		this();
		this.id = id;
		this.title = title;
		this.description = description;
		this.startTrip = startTrip;
		this.endTrip = endTrip;
	}

	public BusinessTrip(Long id, String title, String description, LocalDateTime startTrip, LocalDateTime endTrip, List<Employee> employees) {
		this();
		this.id = id;
		this.title = title;
		this.description = description;
		this.startTrip = startTrip;
		this.endTrip = endTrip;
		this.employees = employees;
	}
}
