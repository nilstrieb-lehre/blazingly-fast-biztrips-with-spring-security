package ch.clip.trips.controller;

import ch.clip.trips.ex.TriptNotFoundException;
import ch.clip.trips.model.Employee;
import ch.clip.trips.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeRepository employeeRepository;


	/**
	 * Method that returns the list of products in the current shopping cart
	 *
	 * @param shoppingCart List of products injected by Spring MVC from the session
	 * @return List of products
	 */
	//@CrossOrigin(origins ="http://localhost:3001")
	// @RequestMapping(value = "/cart/items", method = RequestMethod.GET, produces =
	// "application/json")
	@GetMapping("/employees")
	@PreAuthorize("hasAnyAuthority('ASSISTANT_MANAGER', 'MANAGER', 'ADMIN')")
	List<Employee> allItems() {
		System.out.println("hello employees");
		return (List<Employee>) employeeRepository.findAll();

	}

	/**
	 * add a new Item to the cart
	 *
	 * @param newItem Request object
	 * @return true/false
	 */
	@CrossOrigin(origins = "http://localhost:3001")
	@PostMapping("/employees")
	@PreAuthorize("hasAnyAuthority('ASSISTANT_MANAGER', 'MANAGER', 'ADMIN')")
	Employee newItem(@RequestBody Employee newItem) {
		return employeeRepository.save(newItem);
	}

	// single Item

	@GetMapping("/employees/{id}")
	Employee one(@PathVariable Long id) {
		return employeeRepository.findById(id).orElseThrow(() -> new TriptNotFoundException(id));
	}

	@PutMapping("/cart/items/{id}")
	@PreAuthorize("hasAnyAuthority('ASSISTANT_MANAGER', 'MANAGER', 'ADMIN')")
	Employee replaceItem(@RequestBody Employee newItem, @PathVariable Long id) {
		return employeeRepository.findById(id).map(item -> {
			item.setName(newItem.getName());
			item.setJobTitle(newItem.getJobTitle());

			return employeeRepository.save(item);

		}).orElseGet(() -> {
			newItem.setId(id);
			return employeeRepository.save(newItem);
		});
	}

	/**
	 * Method that deletes an item from the cart
	 *
	 * @param request Request object
	 * @return Status boolean
	 */
	@CrossOrigin(origins = "http://localhost:3000")
	@DeleteMapping("/employees/{id}")
	@PreAuthorize("hasAnyAuthority('ASSISTANT_MANAGER', 'MANAGER', 'ADMIN')")
	void deleteItem(@PathVariable Long id) {
		employeeRepository.deleteById(id);
	}

	/**
	 * Method that empties the shopping cart
	 *
	 * @return Status string
	 */
	@CrossOrigin(origins = "http://localhost:3000")
	@DeleteMapping("/employees")
	@PreAuthorize("hasAnyAuthority('ASSISTANT_MANAGER', 'MANAGER', 'ADMIN')")
	void emptyCart() {
		log.info("hello");
		employeeRepository.deleteAll();
	}

}
