/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.Controller;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.Path;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import main.Models.Role;
import main.Models.User;
import main.Repository.RoleRepository;
import main.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "users")
@Path("/users")
@Component
public class UserController {
    
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    
    @PostConstruct 
    public void postconst(){
        Role role1 = new Role();
        role1.setRole_id("AUTHOR");
        Role role2 = new Role();
        role2.setRole_id("PC_CHAIR");
        Role role3 = new Role();
        role3.setRole_id("PC_MEMBER");
        roleRepo.save(role1);
        roleRepo.save(role2);
        roleRepo.save(role3);
        
        User user1 = new User();
        user1.setUsername("kt1");
        user1.setPassword("kt1");
        user1.setName("Petros");
        user1.getRoles().add(role1);
        user1.getRoles().add(role3);
        userRepo.save(user1);
        
        User user2 = new User();
        user2.setUsername("jim");
        user2.setPassword("jim");
        user2.setName("Dimitris");
        HashSet set1 = new HashSet<>();
        user2.getRoles().add(role1);
        user2.getRoles().add(role2);
        userRepo.save(user2);
        
        User user3 = new User();
        user3.setUsername("kathr32");
        user3.setPassword("kathr32");
        user3.setName("Katerina");
        user3.getRoles().add(role3);
        userRepo.save(user3);
        
    }
}
