//package com.indifarm.machineryrental.service;
//
//import com.indifarm.machineryrental.model.User;
//import com.indifarm.machineryrental.repository.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public User register(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//    }
//
//    public Optional<User> findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//}

package com.indifarm.machineryrental.service;

import com.indifarm.machineryrental.dto.RegistrationRequest;
import com.indifarm.machineryrental.model.Farmer;
import com.indifarm.machineryrental.model.Owner;
import com.indifarm.machineryrental.model.User;
import com.indifarm.machineryrental.repository.FarmerRepository;
import com.indifarm.machineryrental.repository.OwnerRepository;
import com.indifarm.machineryrental.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.io.IOException; // <-- 2. IMPORT
import java.nio.file.Files; // <-- 3. IMPORT
import java.nio.file.Path; // <-- 4. IMPORT
import java.nio.file.Paths; // <-- 5. IMPORT
import java.util.UUID;
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FarmerRepository farmerRepository;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    public static String PROOF_UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/proofs";//    public static String PROOF_UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/target/classes/static/proofs";
    public UserService(UserRepository userRepository, FarmerRepository farmerRepository, OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.farmerRepository = farmerRepository;
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegistrationRequest request, MultipartFile categoryProofFile) throws IOException {
        // 1. Create and save the User
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        // 2. Create and save the associated profile
        if ("FARMER".equals(request.getRole())) {
            Farmer farmer = new Farmer();
            farmer.setUser(savedUser);
            farmer.setName(request.getName());
            farmer.setPhone(request.getPhone());
            farmer.setEmail(request.getEmail());
            farmer.setAddress(request.getAddress());
            farmer.setDistrict(request.getDistrict());
            farmer.setAadharNumber(request.getAadharNumber());
            farmer.setLandSizeInAcres(request.getLandSizeInAcres());
            farmer.setFarmerCategory(request.getFarmerCategory()); // <-- ADD THIS
            farmer.setVerified(false); // Admin must verify later
            //
            if (categoryProofFile != null && !categoryProofFile.isEmpty()) {
                Path uploadPath = Paths.get(PROOF_UPLOAD_DIRECTORY);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String originalFilename = categoryProofFile.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String uniqueFileName = UUID.randomUUID().toString() + extension;

                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(categoryProofFile.getInputStream(), filePath);

                // Set the web-accessible path on the farmer object
                farmer.setCategoryProofUrl("/proofs/" + uniqueFileName);
            }
             //
            farmerRepository.save(farmer);
            savedUser.setFarmer(farmer);

        } else if ("OWNER".equals(request.getRole())) {
            Owner owner = new Owner();
            owner.setUser(savedUser);
            owner.setName(request.getName());
            owner.setPhone(request.getPhone());
            owner.setEmail(request.getEmail());
            owner.setAddress(request.getAddress());
            // For demo, we'll set the owner type based on name
            // In a real app, you'd have an admin set this.
            if (request.getName().toLowerCase().contains("chc")) {
                owner.setOwnerType("GOVERNMENT_CHC");
            } else {
                owner.setOwnerType("PRIVATE");
            }
            owner.setKycStatus("PENDING");
            ownerRepository.save(owner);
            savedUser.setOwner(owner);
        }

        return savedUser;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}