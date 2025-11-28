////package com.indifarm.machineryrental.controller;
////
////import com.indifarm.machineryrental.repository.MachineRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Controller;
////import org.springframework.ui.Model;
////import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.RequestMapping;
////
////@Controller
////@RequestMapping("/admin")
////public class AdminController {
////    @Autowired
////    private MachineRepository machineRepo;
////
////    @GetMapping("/dashboard")
////    public String adminDashboard(Model model) {
////        model.addAttribute("chcMachines", machineRepo.findAll());
////        return "admin-dashboard";
////    }
////
////    @GetMapping("/add-machine")
////    public String addCHCMachinePage() {
////        return "add-machine";
////    }
////}
////
//
//package com.indifarm.machineryrental.controller;
//
//import com.indifarm.machineryrental.model.Owner;
//import com.indifarm.machineryrental.repository.OwnerRepository;
//import com.indifarm.machineryrental.service.BookingService;
//import com.indifarm.machineryrental.service.MachineService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/admin")
//public class AdminController {
//
//    @Autowired private MachineService machineService;
//    @Autowired private OwnerRepository ownerRepo;
//    @Autowired private BookingService bookingService;
//
//    @GetMapping("/dashboard")
//    public String adminDashboard(Model model, Principal principal) {
//        // We assume the "ADMIN" user is linked to a "GOVERNMENT_CHC" Owner profile
//        Owner adminOwner = ownerRepo.findByUserUsername(principal.getName())
//                .orElseThrow(() -> new RuntimeException("Admin owner profile not found"));
//
//        model.addAttribute("adminName", adminOwner.getName());
//        model.addAttribute("chcMachines", adminOwner.getMachines());
//        model.addAttribute("bookings", bookingService.getBookingsByOwner(adminOwner));
//        return "admin-dashboard";
//    }
//
//    @GetMapping("/add-machine")
//    public String addCHCMachinePage() {
//        return "add-machine";
//    }
//}
package com.indifarm.machineryrental.controller;

//import com.indifarm.machineryrental.model.Owner;
//import com.indifarm.machineryrental.repository.OwnerRepository;
//import com.indifarm.machineryrental.service.BookingService;
//import com.indifarm.machineryrental.service.FarmerService; // Import this
//import com.indifarm.machineryrental.service.MachineService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping; // Import this
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam; // Import this

import com.indifarm.machineryrental.model.Farmer;
import com.indifarm.machineryrental.model.Machine; // <-- IMPORT
import com.indifarm.machineryrental.model.Owner;
import com.indifarm.machineryrental.repository.OwnerRepository;
import com.indifarm.machineryrental.service.BookingService;
import com.indifarm.machineryrental.service.FarmerService; // Import this
import com.indifarm.machineryrental.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // <-- IMPORT
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.io.IOException; // <-- IMPORT
import java.nio.file.Files; // <-- IMPORT
import java.nio.file.Path; // <-- IMPORT
import java.nio.file.Paths; // <-- IMPORT
import java.security.Principal;
import java.util.UUID; // <-- IMPORT

import static com.indifarm.machineryrental.controller.OwnerController.UPLOAD_DIRECTORY;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private MachineService machineService;
    @Autowired private OwnerRepository ownerRepo;
    @Autowired private BookingService bookingService;
    @Autowired private FarmerService farmerService; // Inject FarmerService
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    @GetMapping("/dashboard")
    public String adminDashboard(Model model, Principal principal) {
        // We assume the "ADMIN" user is linked to a "GOVERNMENT_CHC" Owner profile
        Owner adminOwner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin owner profile not found"));

        model.addAttribute("adminName", adminOwner.getName());
        model.addAttribute("chcMachines", adminOwner.getMachines());
        model.addAttribute("bookings", bookingService.getBookingsByOwner(adminOwner));

        // Add the list of unverified farmers to the model
        model.addAttribute("unverifiedFarmers", farmerService.getUnverifiedFarmers());

        return "admin-dashboard";
    }
    @GetMapping("/all-farmers")
    public String showAllFarmers(Model model) {
        // Use the existing farmerService to get all farmers
        model.addAttribute("allFarmers", farmerService.getAllFarmers());
        return "all-farmers"; // Return the name of our new HTML file
    }
    // ... (after the /all-farmers mapping)

    // --- NEW METHOD FOR FARMER DETAILS PAGE ---
    @GetMapping("/farmer-details/{id}")
    public String showFarmerDetails(@PathVariable("id") Long id, Model model) {
        Farmer farmer = farmerService.getFarmerById(id)
                .orElseThrow(() -> new RuntimeException("Farmer not found with id: " + id));

        model.addAttribute("farmer", farmer);
        return "farmer-details"; // Name of the new HTML file
    }
//    @PostMapping("/verify-farmer")
//    public String verifyFarmer(@RequestParam("farmerId") Long farmerId) {
//        farmerService.verifyFarmer(farmerId);
//        return "redirect:/admin/dashboard";
//    }
    @GetMapping("/add-machine")
    public String addCHCMachinePage(Model model) {
        model.addAttribute("newMachine", new Machine());
        model.addAttribute("pageTitle", "Add CHC Machine");
        model.addAttribute("formAction", "/admin/add-machine");
        model.addAttribute("dashboardUrl", "/admin/dashboard");
        return "add-machine"; // Return the new common template
    }

    // NEW METHOD: Handles the form submission for adding a CHC machine
    @PostMapping("/add-machine")
    public String addMachine(@ModelAttribute("newMachine") Machine machine,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             RedirectAttributes redirectAttributes,
                             Principal principal) throws IOException {

        Owner adminOwner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin owner profile not found"));

        if (!imageFile.isEmpty()) {
            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = imageFile.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(imageFile.getInputStream(), filePath);

            machine.setImageUrl("/uploads/" + uniqueFileName);
        } else {
            machine.setImageUrl("/images/default_machine.png"); // A sensible default
        }

        machine.setOwner(adminOwner);
        machine.setAvailable(true); // Default to available
        machineService.saveMachine(machine); // Use the service

        redirectAttributes.addFlashAttribute("successMessage", "Machine added successfully!");

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete-machine/{id}")
    public String deleteMachine(@PathVariable("id") Long id,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {

        try {
            Owner adminOwner = ownerRepo.findByUserUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Admin owner profile not found"));

            Machine machine = machineService.getMachineById(id)
                    .orElseThrow(() -> new RuntimeException("Machine not found"));

            // Security Check: Ensure this admin owns this machine
            if (!machine.getOwner().getId().equals(adminOwner.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: You do not have permission to delete this machine.");
                return "redirect:/admin/dashboard";
            }

            // Try to delete (service will throw exception if it has bookings)
            machineService.deleteMachine(id);
            redirectAttributes.addFlashAttribute("successMessage", "Machine deleted successfully.");

        } catch (RuntimeException e) {
            // Catch errors from service (e.g., machine not found, or has bookings)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }
    @GetMapping("/edit-machine/{id}")
    public String showEditMachinePage(@PathVariable("id") Long id, Model model, Principal principal) {
        Owner adminOwner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin owner profile not found"));

        Machine machine = machineService.getMachineById(id)
                .orElseThrow(() -> new RuntimeException("Machine not found"));

        // Security Check: Ensure this admin owns this machine
        if (!machine.getOwner().getId().equals(adminOwner.getId())) {
            // This machine does not belong to this admin
            return "redirect:/admin/dashboard?error=AccessDenied";
        }

        model.addAttribute("newMachine", machine); // Use the same object name as the "add" form
        model.addAttribute("pageTitle", "Edit CHC Machine");
        model.addAttribute("formAction", "/admin/edit-machine");
        model.addAttribute("dashboardUrl", "/admin/dashboard");
        return "add-machine"; // Re-use the same template
    }

    // --- NEW METHOD 2: Process the edit form ---
    @PostMapping("/edit-machine")
    public String editMachine(@ModelAttribute("newMachine") Machine machine,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes,
                              Principal principal) throws IOException {

        Owner adminOwner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin owner profile not found"));

        // Get the existing machine from the DB
        Machine existingMachine = machineService.getMachineById(machine.getId())
                .orElseThrow(() -> new RuntimeException("Machine not found"));

        // Security Check: Ensure they are editing their own machine
        if (!existingMachine.getOwner().getId().equals(adminOwner.getId())) {
            return "redirect:/admin/dashboard?error=AccessDenied";
        }

        // Handle the image upload
        if (!imageFile.isEmpty()) {
            // New image was uploaded, save it
            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalFilename = imageFile.getOriginalFilename();
            String extension = (originalFilename != null && originalFilename.contains(".")) ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String uniqueFileName = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(imageFile.getInputStream(), filePath);

            machine.setImageUrl("/uploads/" + uniqueFileName);
        } else {
            // No new image, preserve the old one
            machine.setImageUrl(existingMachine.getImageUrl());
        }

        // Preserve the owner and save
        machine.setOwner(adminOwner);
        machineService.saveMachine(machine);

        redirectAttributes.addFlashAttribute("successMessage", "Machine updated successfully!");
        return "redirect:/admin/dashboard";
    }

    // Add this new method to handle the verification POST request
    @PostMapping("/verify-farmer")
    public String verifyFarmer(@RequestParam("farmerId") Long farmerId) {
        farmerService.verifyFarmer(farmerId);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete-farmer/{id}")
    public String deleteFarmer(@PathVariable("id") Long id,
                               RedirectAttributes redirectAttributes) {
        try {
            // Calls the core service logic (which includes the booking check and user deletion)
            farmerService.deleteFarmer(id);
            redirectAttributes.addFlashAttribute("successMessage", "Farmer deleted successfully.");
        } catch (RuntimeException e) {
            // Catches the exception thrown by FarmerService (e.g., if farmer has bookings)
            // The service logic handles the foreign key check.
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        // Redirect back to the all-farmers list to see the updated table
        return "redirect:/admin/all-farmers";
    }



}