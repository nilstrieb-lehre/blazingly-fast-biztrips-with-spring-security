package ch.clip.trips.controller;

import ch.clip.trips.ex.TriptNotFoundException;
import ch.clip.trips.model.BusinessTrip;
import ch.clip.trips.repo.BusinessTripRepository;
import ch.clip.trips.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class BusinessTripController {

    private final BusinessTripRepository tripRepository;
    private final EmployeeRepository employeeRepository;

    // Aggregate root
    @GetMapping("/trips")
    // @RequestMapping(value = "/trips", method = RequestMethod.GET, produces =
    // "application/json")
    @PreAuthorize("hasAuthority('STAFF_MEMBER')")
    List<BusinessTrip> all(Authentication authentication) {
        log.info("Get all biztrips for {}", authentication.getName());

        return employeeRepository.findByName(authentication.getName()).get(0).getTrips();
    }

    @PostMapping("/trips")
    @PreAuthorize("hasAnyAuthority('ASSISTANT_MANAGER', 'MANAGER', 'ADMIN')")
        // @RequestMapping(value = "/trips", method = RequestMethod.POST, produces =
        // "application/json", consumes = "appication/json")
    BusinessTrip newProduct(@RequestBody BusinessTrip newTrip) {
        return tripRepository.save(newTrip);
    }

    // single Item
    @GetMapping("/trips/{id}")
    BusinessTrip one(@PathVariable Long id) {
        return tripRepository.findById(id).orElseThrow(() -> new TriptNotFoundException(id));
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasAnyAuthority('ASSISTANT_MANAGER', 'MANAGER', 'ADMIN')")
    void deleteProduct(@PathVariable Long id) {
        tripRepository.deleteById(id);
    }

}
