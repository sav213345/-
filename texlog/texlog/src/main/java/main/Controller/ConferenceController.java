/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.Controller;

import main.Models.Conference;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import main.Models.User;
import main.Repository.ConferenceRepository;
import main.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "conferences")
@Path("/conferences")
@Component
public class ConferenceController {

    @Autowired
    private ConferenceRepository conferenceRepo;
    @Autowired
    private UserRepository userRepo;

    @POST
    @Path("/{pcchair}")
    @Produces("application/json")
    @Consumes("application/json")
    public Conference createConference(Conference newconf, @PathParam("pcchair") String pcchair) {
        newconf.setCreationDate(new Date());//topothetei tin trexousa imerominia
        newconf.setState("CREATED");
        Conference result = conferenceRepo.save(newconf);
        
        //anazitisi tou xristi apo tin vasi me to username tou
        Optional<User> u = userRepo.findById(pcchair);
        System.out.println(pcchair);
        if (!u.isEmpty()) {
            User t = u.get();
            t.getConferences_chair().add(result);
            userRepo.save(t);
        } else {
            System.out.println("empty");
        }

        return result;
    }

    //prosthiki enos xristi as pcchair sto conference
    @PUT
    @Path("/{conference}/pcchairs")
    @Produces("application/json")
    @Consumes("application/json")
    public ResponseEntity pcchairAddition(@PathParam("conference") int conf, List<User> users) {
        Optional<Conference> confereopt = conferenceRepo.findById(conf);
        if (!confereopt.isEmpty()) {
            for (User u : users) {
                userRepo.findById(u.getUsername()).ifPresent(existingUser -> {
                    System.out.println("Before: " + existingUser.getUsername());
                    existingUser.getConferences_chair().add(confereopt.get());
                    System.out.println("After: " + existingUser.getUsername());
                    userRepo.save(existingUser);
                });
                System.out.println(confereopt.get().getName());
            }
            return new ResponseEntity<>("Users added", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("No such conference", HttpStatus.BAD_REQUEST);
        }
    }

    @PUT
    @Path("/{conference}/members")
    @Produces("application/json")
    @Consumes("application/json")
    public ResponseEntity pcmemberAddition(@PathParam("conference") int conf, List<User> users) {
        Optional<Conference> confereopt = conferenceRepo.findById(conf);
        if (!confereopt.isEmpty()) {
            for (User u : users) {
                userRepo.findById(u.getUsername()).ifPresent(existingUser -> {
                    System.out.println("Before: " + existingUser.getUsername());
                    existingUser.getConferences_memb().add(confereopt.get());
                    System.out.println("After: " + existingUser.getUsername());
                    userRepo.save(existingUser);
                });
                System.out.println(confereopt.get().getName());
            }
            return new ResponseEntity<>("Users added", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("No such conference", HttpStatus.BAD_REQUEST);
        }
    }

    @GET
    @Path("/search")
    @Produces("application/json")
    @Consumes("application/json")
    public ResponseEntity searchConference(@DefaultValue("") @QueryParam("name") String name, @DefaultValue("") @QueryParam("description") String description) {
        //ean den exei orisei pedio
        if (name.equals("") && description.equals("")) {
            List<Conference> conferelist = conferenceRepo.findAll();
            //vriskoume kai epistrefoume ola ta conferences
            return new ResponseEntity<>(conferelist, HttpStatus.OK);
        } else if (name.equals("")) {
            //ean exei dwsei description
            List<Conference> conferelist = conferenceRepo.findConferenceByDescription(description);
            //vriskoume kai epistrefoume ola ta conferences vasei description
            return new ResponseEntity<>(conferelist, HttpStatus.OK);

        } else if (description.equals("")) {
            //ean exei dwsei description
            List<Conference> conferelist = conferenceRepo.findConferenceByName(name);
            //vriskoume kai epistrefoume ola ta conferences vasei name
            return new ResponseEntity<>(conferelist, HttpStatus.OK);
        } else {
            List<Conference> conferelist = conferenceRepo.findConferenceByNameAndDescription(name, description);
            //vriskoume kai epistrefoume ola ta conferences vasei name kai description
            return new ResponseEntity<>(conferelist, HttpStatus.OK);
        }

    }

    @DELETE
    @Path("/{conference}/delete")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity deleteConference(@PathParam("conference") int conf) {
        Optional<Conference> confereopt = conferenceRepo.findById(conf);
        System.out.println("gggg");
        if (!confereopt.isEmpty()) {
            if (confereopt.get().getState().equals("CREATED")){
                List<User> users = userRepo.findAll();
                for (User u : users){
                    u.getConferences_chair().remove(confereopt.get());
                    u.getConferences_memb().remove(confereopt.get());
                    userRepo.save(u);
                }
                //diagrafi tou conference
                conferenceRepo.delete(confereopt.get());
                
                 return new ResponseEntity<>("To conference diagraftike", HttpStatus.OK);
            }
            else      
                return new ResponseEntity<>("To conference den diagraftike afou einai se lathos state", HttpStatus.BAD_REQUEST);
           
        }
        else 
            return new ResponseEntity<>("To conference den vrethike", HttpStatus.NOT_FOUND);
           
    }
    
    @PUT
    @Path("/{conference}/submit")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity startSubmission(@PathParam("conference") int conf){
        Optional<Conference> confereopt = conferenceRepo.findById(conf);
        if (!confereopt.isEmpty()) {
            Conference conference = confereopt.get();
            if (conference.getState().equals("CREATED")){
                //allagi state
                conference.setState("SUBMISSION");
                conferenceRepo.save(conference);
                return new ResponseEntity<>("To conference egine submit", HttpStatus.OK);
            }
            else      
                return new ResponseEntity<>("To conference den egine submit afou einai se lathos state", HttpStatus.BAD_REQUEST);
           
        }
        else 
            return new ResponseEntity<>("To conference den vrethike", HttpStatus.NOT_FOUND);
    }
    
    @PUT
    @Path("/{conference}/assign")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity startAssign(@PathParam("conference") int conf){
        Optional<Conference> confereopt = conferenceRepo.findById(conf);
        if (!confereopt.isEmpty()) {
            Conference conference = confereopt.get();
            if (conference.getState().equals("SUBMISSION")){
                //allagi state
                conference.setState("ASSIGNMENT");
                conferenceRepo.save(conference);
                return new ResponseEntity<>("To conference egine assign", HttpStatus.OK);
            }
            else      
                return new ResponseEntity<>("To conference den egine assign afou einai se lathos state", HttpStatus.BAD_REQUEST);
           
        }
        else 
            return new ResponseEntity<>("To conference den vrethike", HttpStatus.NOT_FOUND);
    }
    
    @PUT
    @Path("/{conference}/review")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity startReview(@PathParam("conference") int conf){
        Optional<Conference> confereopt = conferenceRepo.findById(conf);
        if (!confereopt.isEmpty()) {
            Conference conference = confereopt.get();
            if (conference.getState().equals("ASSIGNMENT")){
                //allagi state
                conference.setState("REVIEW");
                conferenceRepo.save(conference);
                return new ResponseEntity<>("To conference egine review", HttpStatus.OK);
            }
            else      
                return new ResponseEntity<>("To conference den egine review afou einai se lathos state", HttpStatus.BAD_REQUEST);
           
        }
        else 
            return new ResponseEntity<>("To conference den vrethike", HttpStatus.NOT_FOUND);
    }
    
    @PUT
    @Path("/{conference}/decision")
    @Consumes("application/json")
    public ResponseEntity startDecision(@PathParam("conference") int conf){
        Optional<Conference> confereopt = conferenceRepo.findById(conf);
        if (!confereopt.isEmpty()) {
            Conference conference = confereopt.get();
            if (conference.getState().equals("REVIEW")){
                //allagi state
                conference.setState("DECISION");
                conferenceRepo.save(conference);
                return new ResponseEntity<>("To conference egine decision", HttpStatus.OK);
            }
            else      
                return new ResponseEntity<>("To conference den egine decision afou einai se lathos state", HttpStatus.BAD_REQUEST);
           
        }
        else 
            return new ResponseEntity<>("To conference den vrethike", HttpStatus.NOT_FOUND);
    }
    
    
    @PUT
    @Path("/{conference}/finalsubmit")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity startFinalSubm(@PathParam("conference") int conf){
        Optional<Conference> confereopt = conferenceRepo.findById(conf);
        if (!confereopt.isEmpty()) {
            Conference conference = confereopt.get();
            if (conference.getState().equals("DECISION")){
                //allagi state
                conference.setState("FINAL_SUBMISSION");
                conferenceRepo.save(conference);
                return new ResponseEntity<>("To conference egine final submit", HttpStatus.OK);
            }
            else      
                return new ResponseEntity<>("To conference den egine final submit afou einai se lathos state", HttpStatus.BAD_REQUEST);
           
        }
        else 
            return new ResponseEntity<>("To conference den vrethike", HttpStatus.NOT_FOUND);
    }
    
    @PUT
    @Path("/{conference}/final")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity startfinal(@PathParam("conference") int conf){
        Optional<Conference> confereopt = conferenceRepo.findById(conf);
        if (!confereopt.isEmpty()) {
            Conference conference = confereopt.get();
            if (conference.getState().equals("FINAL_SUBMISSION")){
                //allagi state
                conference.setState("FINAL");
                conferenceRepo.save(conference);
                return new ResponseEntity<>("To conference egine final", HttpStatus.OK);
            }
            else      
                return new ResponseEntity<>("To conference den egine final afou einai se lathos state", HttpStatus.BAD_REQUEST);
           
        }
        else 
            return new ResponseEntity<>("To conference den vrethike", HttpStatus.NOT_FOUND);
    }
    
}
