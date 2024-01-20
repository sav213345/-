/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package main.Repository;

import java.util.List;
import main.Models.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Integer>{
   
    List<Conference> findConferenceByNameAndDescription(String name, String description);
    
    List<Conference> findConferenceByDescription(String description);
            
    List<Conference> findConferenceByName(String name);
}
