package com.indifarm.machineryrental.service;


import com.indifarm.machineryrental.model.Farmer;
import com.indifarm.machineryrental.repository.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Retain this import
import com.indifarm.machineryrental.repository.UserRepository; // <-- ADD THIS IMPORT
import com.indifarm.machineryrental.repository.BookingRepository; // <-- ADD THIS IMPORT

import java.util.List;
import java.util.Optional;

@Service
public class FarmerService {

    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository; // <-- NEW
    private final BookingRepository bookingRepository; // <-- NEW

    @Autowired
    // CONSTRUCTOR UPDATED TO INJECT NEW REPOSITORIES
    public FarmerService(FarmerRepository farmerRepository, UserRepository userRepository, BookingRepository bookingRepository) {
        this.farmerRepository = farmerRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    public Optional<Farmer> getFarmerById(Long id) {
        return farmerRepository.findById(id);
    }

    public Farmer saveFarmer(Farmer farmer) {
        return farmerRepository.save(farmer);
    }

    /**
     * Deletes a Farmer and their associated User account.
     * This method ensures data integrity by checking for existing bookings first.
     */
    @Transactional // <-- CRITICAL: Ensures deletion is atomic (rolls back on failure)
    public void deleteFarmer(Long id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farmer not found with id: " + id));

        // 1. CRITICAL CHECK: Prevent deletion if farmer has existing bookings.
        if (!bookingRepository.findByFarmer(farmer).isEmpty()) {
            throw new RuntimeException("Cannot delete farmer: Farmer has existing or pending bookings. Please delete all related bookings first.");
        }

        // 2. Delete the associated User.
        // This automatically deletes the Farmer record due to the CascadeType.ALL on the User entity.
        userRepository.delete(farmer.getUser());
    }

    // --- New Methods for Verification ---

    /**
     * Finds all farmers who have not yet been verified.
     * @return A list of farmers with isVerified = false
     */
    public List<Farmer> getUnverifiedFarmers() {
        return farmerRepository.findByIsVerified(false);
    }

    /**
     * Verifies a farmer by their ID.
     * @param farmerId The ID of the farmer to verify.
     */
    public void verifyFarmer(Long farmerId) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found with id: " + farmerId));

        farmer.setVerified(true); // Set their status to verified
        farmerRepository.save(farmer);
    }
}