////package com.indifarm.machineryrental.controller;
////
////import com.indifarm.machineryrental.model.Machine;
////import com.indifarm.machineryrental.model.Owner;
////import com.indifarm.machineryrental.repository.MachineRepository;
////import com.indifarm.machineryrental.repository.OwnerRepository;
////import com.indifarm.machineryrental.service.BookingService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Controller;
////import org.springframework.ui.Model;
////import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.ModelAttribute;
////import org.springframework.web.bind.annotation.PostMapping;
////import org.springframework.web.bind.annotation.RequestMapping;
////import org.springframework.web.bind.annotation.RequestParam;
////import org.springframework.web.multipart.MultipartFile;
////import org.springframework.web.servlet.mvc.support.RedirectAttributes; // <-- 1. IMPORT THIS
////import org.springframework.web.bind.annotation.PathVariable; // <-- IMPORT
////import java.io.IOException;
////import java.nio.file.Files;
////import java.nio.file.Path;
////import java.nio.file.Paths;
////import java.security.Principal;
////import java.util.UUID;
////
////@Controller
////@RequestMapping("/owner")
////public class OwnerController {
////
////    @Autowired private MachineRepository machineRepo;
////    @Autowired private OwnerRepository ownerRepo;
////    @Autowired private BookingService bookingService;
////
////    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";
////
////    @GetMapping("/dashboard")
////    public String ownerDashboard(Model model, Principal principal) {
////        Owner owner = ownerRepo.findByUserUsername(principal.getName())
////                .orElseThrow(() -> new RuntimeException("Owner not found"));
////
////        model.addAttribute("owner", owner);
////        model.addAttribute("machines", owner.getMachines());
////        model.addAttribute("bookings", bookingService.getBookingsByOwner(owner));
//////        model.addAttribute("newMachine", new Machine());
////        return "owner-dashboard";
////    }
//////
////@GetMapping("/add-machine")
////public String addMachinePage(Model model) {
////    model.addAttribute("newMachine", new Machine());
////    model.addAttribute("pageTitle", "Add Your Machine");
////    model.addAttribute("formAction", "/owner/add-machine");
////    model.addAttribute("dashboardUrl", "/owner/dashboard");
////    return "add-machine"; // Return the new common template
////}
////    @PostMapping("/add-machine")
////    public String addMachine(@ModelAttribute("newMachine") Machine machine,
////                             @RequestParam("imageFile") MultipartFile imageFile,
////                             RedirectAttributes redirectAttributes, // <-- 2. ADD THIS
////                             Principal principal) throws IOException {
////
////        Owner owner = ownerRepo.findByUserUsername(principal.getName())
////                .orElseThrow(() -> new RuntimeException("Owner not found"));
////
////        if (!imageFile.isEmpty()) {
////            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
////            if (!Files.exists(uploadPath)) {
////                Files.createDirectories(uploadPath);
////            }
////
////            String originalFilename = imageFile.getOriginalFilename();
////            String extension = "";
////            if (originalFilename != null && originalFilename.contains(".")) {
////                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
////            }
////            String uniqueFileName = UUID.randomUUID().toString() + extension;
////
////            Path filePath = uploadPath.resolve(uniqueFileName);
////            Files.copy(imageFile.getInputStream(), filePath);
////
////            machine.setImageUrl("/uploads/" + uniqueFileName);
////        } else {
////            machine.setImageUrl("/images/default_machine.png");
////        }
////
////        machine.setOwner(owner);
////        machine.setAvailable(true); // <-- ADDED
////        machineRepo.save(machine);
////        redirectAttributes.addFlashAttribute("successMessage", "Machine added successfully!");
////        return "redirect:/owner/dashboard";
////
////        // --- 3. ADD THIS LINE ---
////
////
////
////    }
////}
//package com.indifarm.machineryrental.controller;
//
//import com.indifarm.machineryrental.model.Machine;
//import com.indifarm.machineryrental.model.Owner;
//import com.indifarm.machineryrental.repository.MachineRepository;
//import com.indifarm.machineryrental.repository.OwnerRepository;
//import com.indifarm.machineryrental.service.BookingService;
//import com.indifarm.machineryrental.service.MachineService; // <-- IMPORTED
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable; // <-- This import was correct
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.Principal;
//import java.util.UUID;
//
//@Controller
//@RequestMapping("/owner")
//public class OwnerController {
//
//    @Autowired private MachineRepository machineRepo;
//    @Autowired private OwnerRepository ownerRepo;
//    @Autowired private BookingService bookingService;
//    @Autowired private MachineService machineService; // <-- INJECTED
//
//    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";
//
//    @GetMapping("/dashboard")
//    public String ownerDashboard(Model model, Principal principal) {
//        Owner owner = ownerRepo.findByUserUsername(principal.getName())
//                .orElseThrow(() -> new RuntimeException("Owner not found"));
//
//        model.addAttribute("owner", owner);
//        model.addAttribute("machines", owner.getMachines());
//        model.addAttribute("bookings", bookingService.getBookingsByOwner(owner));
//        return "owner-dashboard";
//    }
//
//    @GetMapping("/add-machine")
//    public String addMachinePage(Model model) {
//        model.addAttribute("newMachine", new Machine());
//        model.addAttribute("pageTitle", "Add Your Machine");
//        model.addAttribute("formAction", "/owner/add-machine");
//        model.addAttribute("dashboardUrl", "/owner/dashboard");
//        return "add-machine"; // Return the new common template
//    }
//
//    @PostMapping("/add-machine")
//    public String addMachine(@ModelAttribute("newMachine") Machine machine,
//                             @RequestParam("imageFile") MultipartFile imageFile,
//                             RedirectAttributes redirectAttributes,
//                             Principal principal) throws IOException {
//
//        Owner owner = ownerRepo.findByUserUsername(principal.getName())
//                .orElseThrow(() -> new RuntimeException("Owner not found"));
//
//        if (!imageFile.isEmpty()) {
//            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            String originalFilename = imageFile.getOriginalFilename();
//            String extension = "";
//            if (originalFilename != null && originalFilename.contains(".")) {
//                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            }
//            String uniqueFileName = UUID.randomUUID().toString() + extension;
//
//            Path filePath = uploadPath.resolve(uniqueFileName);
//            Files.copy(imageFile.getInputStream(), filePath);
//
//            machine.setImageUrl("/uploads/" + uniqueFileName);
//        } else {
//            machine.setImageUrl("/images/default_machine.png");
//        }
//
//        machine.setOwner(owner);
//        machine.setAvailable(true); // <-- ADDED
//        machineRepo.save(machine);
//        redirectAttributes.addFlashAttribute("successMessage", "Machine added successfully!");
//        return "redirect:/owner/dashboard";
//    }
//
//    // --- NEW METHOD 1: Show the edit form ---
//    @GetMapping("/edit-machine/{id}")
//    public String showEditMachinePage(@PathVariable("id") Long id, Model model, Principal principal) {
//        Owner owner = ownerRepo.findByUserUsername(principal.getName())
//                .orElseThrow(() -> new RuntimeException("Owner profile not found"));
//
//        Machine machine = machineService.getMachineById(id)
//                .orElseThrow(() -> new RuntimeException("Machine not found"));
//
//        // Security Check: Ensure this owner owns this machine
//        if (!machine.getOwner().getId().equals(owner.getId())) {
//            return "redirect:/owner/dashboard?error=AccessDenied";
//        }
//
//        model.addAttribute("newMachine", machine);
//        model.addAttribute("pageTitle", "Edit Your Machine");
//        model.addAttribute("formAction", "/owner/edit-machine");
//        model.addAttribute("dashboardUrl", "/owner/dashboard");
//        return "add-machine"; // Re-use the same template
//    }
//
//    // --- NEW METHOD 2: Process the edit form ---
//    @PostMapping("/edit-machine")
//    public String editMachine(@ModelAttribute("newMachine") Machine machine,
//                              @RequestParam("imageFile") MultipartFile imageFile,
//                              RedirectAttributes redirectAttributes,
//                              Principal principal) throws IOException {
//
//        Owner owner = ownerRepo.findByUserUsername(principal.getName())
//                .orElseThrow(() -> new RuntimeException("Owner profile not found"));
//
//        Machine existingMachine = machineService.getMachineById(machine.getId())
//                .orElseThrow(() -> new RuntimeException("Machine not found"));
//
//        // Security Check: Ensure they are editing their own machine
//        if (!existingMachine.getOwner().getId().equals(owner.getId())) {
//            return "redirect:/owner/dashboard?error=AccessDenied";
//        }
//
//        // Handle the image upload
//        if (!imageFile.isEmpty()) {
//            // New image was uploaded, save it
//            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//            String originalFilename = imageFile.getOriginalFilename();
//            String extension = (originalFilename != null && originalFilename.contains(".")) ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
//            String uniqueFileName = UUID.randomUUID().toString() + extension;
//            Path filePath = uploadPath.resolve(uniqueFileName);
//            Files.copy(imageFile.getInputStream(), filePath);
//
//            machine.setImageUrl("/uploads/" + uniqueFileName);
//        } else {
//            // No new image, preserve the old one
//            machine.setImageUrl(existingMachine.getImageUrl());
//        }
//
//        // Preserve the owner and save
//        machine.setOwner(owner);
//        machineService.saveMachine(machine); // Use the service
//
//        redirectAttributes.addFlashAttribute("successMessage", "Machine updated successfully!");
//        return "redirect:/owner/dashboard";
//    }
//}
package com.indifarm.machineryrental.controller;

import com.indifarm.machineryrental.model.Machine;
import com.indifarm.machineryrental.model.Owner;
import com.indifarm.machineryrental.repository.MachineRepository;
import com.indifarm.machineryrental.repository.OwnerRepository;
import com.indifarm.machineryrental.service.BookingService;
import com.indifarm.machineryrental.service.MachineService; // <-- IMPORTED
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable; // <-- This import was correct
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired private MachineRepository machineRepo;
    @Autowired private OwnerRepository ownerRepo;
    @Autowired private BookingService bookingService;
    @Autowired private MachineService machineService; // <-- INJECTED

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    @GetMapping("/dashboard")
    public String ownerDashboard(Model model, Principal principal) {
        Owner owner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        model.addAttribute("owner", owner);
        model.addAttribute("machines", owner.getMachines());
        model.addAttribute("bookings", bookingService.getBookingsByOwner(owner));
        return "owner-dashboard";
    }

    @GetMapping("/add-machine")
    public String addMachinePage(Model model) {
        model.addAttribute("newMachine", new Machine());
        model.addAttribute("pageTitle", "Add Your Machine");
        model.addAttribute("formAction", "/owner/add-machine");
        model.addAttribute("dashboardUrl", "/owner/dashboard");
        return "add-machine"; // Return the new common template
    }

    @PostMapping("/add-machine")
    public String addMachine(@ModelAttribute("newMachine") Machine machine,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             RedirectAttributes redirectAttributes,
                             Principal principal) throws IOException {

        Owner owner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

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
            machine.setImageUrl("/images/default_machine.png");
        }

        machine.setOwner(owner);
        machine.setAvailable(true); // <-- ADDED
        machineRepo.save(machine);
        redirectAttributes.addFlashAttribute("successMessage", "Machine added successfully!");
        return "redirect:/owner/dashboard";
    }

    // --- NEW METHOD 1: Show the edit form ---
    @GetMapping("/edit-machine/{id}")
    public String showEditMachinePage(@PathVariable("id") Long id, Model model, Principal principal) {
        Owner owner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Owner profile not found"));

        Machine machine = machineService.getMachineById(id)
                .orElseThrow(() -> new RuntimeException("Machine not found"));

        // Security Check: Ensure this owner owns this machine
        if (!machine.getOwner().getId().equals(owner.getId())) {
            return "redirect:/owner/dashboard?error=AccessDenied";
        }

        model.addAttribute("newMachine", machine);
        model.addAttribute("pageTitle", "Edit Your Machine");
        model.addAttribute("formAction", "/owner/edit-machine");
        model.addAttribute("dashboardUrl", "/owner/dashboard");
        return "add-machine"; // Re-use the same template
    }

    // --- NEW METHOD 2: Process the edit form ---
    @PostMapping("/edit-machine")
    public String editMachine(@ModelAttribute("newMachine") Machine machine,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes,
                              Principal principal) throws IOException {

        Owner owner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Owner profile not found"));

        Machine existingMachine = machineService.getMachineById(machine.getId())
                .orElseThrow(() -> new RuntimeException("Machine not found"));

        // Security Check: Ensure they are editing their own machine
        if (!existingMachine.getOwner().getId().equals(owner.getId())) {
            return "redirect:/owner/dashboard?error=AccessDenied";
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
        machine.setOwner(owner);
        machineService.saveMachine(machine); // Use the service

        redirectAttributes.addFlashAttribute("successMessage", "Machine updated successfully!");
        return "redirect:/owner/dashboard";
    }

    // --- NEW METHOD 3: Process the delete request ---
    @PostMapping("/delete-machine/{id}")
    public String deleteMachine(@PathVariable("id") Long id,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {

        try {
            Owner owner = ownerRepo.findByUserUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Owner profile not found"));

            Machine machine = machineService.getMachineById(id)
                    .orElseThrow(() -> new RuntimeException("Machine not found"));

            // Security Check: Ensure this owner owns this machine
            if (!machine.getOwner().getId().equals(owner.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: You do not have permission to delete this machine.");
                return "redirect:/owner/dashboard";
            }

            // Try to delete (service will throw exception if it has bookings)
            machineService.deleteMachine(id);
            redirectAttributes.addFlashAttribute("successMessage", "Machine deleted successfully.");

        } catch (RuntimeException e) {
            // Catch errors from service (e.g., machine not found, or has bookings)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/owner/dashboard";
    }
}