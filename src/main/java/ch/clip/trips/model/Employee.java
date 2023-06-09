package ch.clip.trips.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Employee implements Serializable {

	private static final long serialVersionUID = 6705527563808382509L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String jobTitle;

	@OneToMany(mappedBy = "employee")
	@JsonManagedReference
	private List<Flight> flights;


	@ManyToMany
	@JoinTable(name = "trip_employee",
	joinColumns = @JoinColumn(name = "employee_id_fs"),
	inverseJoinColumns = @JoinColumn(name = "trip_id_fs"))
	@JsonManagedReference
	private List<BusinessTrip> trips;

	public Employee() {
		super();
		flights = new ArrayList<>();
	}

	public Employee(Long id, String name, String jobTitle) {
		this();
		this.id = id;
		this.name = name;
		this.jobTitle = jobTitle;

	}

	public Employee(Long id, String name, String jobTitle, List<Flight> flights) {
		this();
		this.id = id;
		this.name = name;
		this.jobTitle = jobTitle;
		this.flights = flights;
	}
}
