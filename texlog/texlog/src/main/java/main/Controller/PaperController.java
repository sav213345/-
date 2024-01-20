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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import main.Models.Paper;
import main.Models.User;
import main.Repository.ConferenceRepository;
import main.Repository.PaperRepository;
import main.Repository.UserRepository;
import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "papers")
@Path("/papers")
@Component
@Transactional
public class PaperController {
    @Autowired
    private ConferenceRepository conferenceRepo;
    @Autowired
    private PaperRepository paperRepo;
    @Autowired
    private UserRepository userRepo;

    @POST
    @Path("/{requestor}/conference/{conferenceid}")
    @Produces("application/json")
    @Consumes("application/json")
    public ResponseEntity createPaper(Paper newpaper, @PathParam("requestor") String requestor, @PathParam("conferenceid") Integer conferenceid) {
        List<Paper> templist =  paperRepo.findPaperByTitle(newpaper.getTitle());
        if (!templist.isEmpty()) return new ResponseEntity<>("Paper with that title already exists", HttpStatus.BAD_REQUEST);
        
        newpaper.setCreationDate(new Date());//topothetei tin trexousa imerominia
        newpaper.setState("CREATED"); //to state tou einai CREATED
        
        //anazitisi tou xristi apo tin vasi me to username tou
        Optional<User> u = userRepo.findById(requestor);
        
        if (!u.isEmpty()) {
            User t = u.get();//anazitisi tou xristi
            t.getAuthored_papers().add(newpaper);
            Optional<Conference> c = conferenceRepo.findById(conferenceid);
            if (!c.isEmpty()){
                Conference conf = c.get();
                conf.getPapers().add(newpaper);
                conferenceRepo.save(conf); //apothikeuoume to ananewmeno conference
            }else{
                return new ResponseEntity<>("Error, no such conference", HttpStatus.BAD_REQUEST);
            }
            
            paperRepo.save(newpaper);
            userRepo.save(t);//apothikeuoume ton ananewmeno xristi
            return new ResponseEntity<>(newpaper, HttpStatus.OK);
            
        } else {
            return new ResponseEntity<>("Error, no such requestor", HttpStatus.BAD_REQUEST);
        }
    }
    
      //prosthiki enos xristi as pcchair sto conference
    @PUT
    @Path("/{paperid}/authors")
    @Produces("application/json")
    @Consumes("application/json")
    public ResponseEntity authorAddition(@PathParam("paperid") int paperid, List<User> users) {
        Optional<Paper> paperopt = paperRepo.findById(paperid);
        if (!paperopt.isEmpty()) {
            for (User u : users) {
                userRepo.findById(u.getUsername()).ifPresent(existingUser -> {
                    System.out.println("Before: " + existingUser.getUsername());
                    existingUser.getAuthored_papers().add(paperopt.get());
                    System.out.println("After: " + existingUser.getUsername());
                    userRepo.save(existingUser);
                });
                System.out.println(paperopt.get().getTitle());
            }
            return new ResponseEntity<>("Users added", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("No such paper", HttpStatus.BAD_REQUEST);
        }
    }
    
    @DELETE
    @Path("/{paperid}/withdraw")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity paperwithdraw(@PathParam("paperid") int paperid) {
        Optional<Paper> paperopt = paperRepo.findById(paperid);
        if (!paperopt.isEmpty()) {
                List<User> users = userRepo.findAll();
                for (User u : users){
                    u.getAuthored_papers().remove(paperopt.get());
                    u.getReview_paper().remove(paperopt.get());
                    userRepo.save(u); //apothikeuoume tis allages ston xristi
                }
                //diagrafi tou paper
                paperRepo.delete(paperopt.get());
                
                 return new ResponseEntity<>("To conference diagraftike", HttpStatus.OK);
            }
        else 
            return new ResponseEntity<>("To conference den vrethike", HttpStatus.NOT_FOUND);
           
    }
    
    
    
    @GET
    @Path("/search")
    @Produces("application/json")
    @Consumes("application/json")
    public ResponseEntity searchPaper(@DefaultValue("") @QueryParam("title") String title, @DefaultValue("") @QueryParam("abstract") String pabstract, @DefaultValue("") @QueryParam("authors") String authors) {
        //ean den exei orisei pedio
        if (title.equals("") && pabstract.equals("") && authors.equals("")) {
            List<Paper> paperlist = paperRepo.findAll();
            //vriskoume kai epistrefoume ola ta papers
            return new ResponseEntity<>(paperlist, HttpStatus.OK);
        } else if (title.equals("") && authors.equals("")) {
            //ean exei dwsei abstract
            List<Paper> paperlist = paperRepo.findPaperByPabstract(pabstract);
            //vriskoume kai epistrefoume ola ta paper vasei abstract
            return new ResponseEntity<>(paperlist, HttpStatus.OK);
        } else if (title.equals("") && pabstract.equals("")) {
            String[] authorss = authors.split(",");
            List<String> authorsList = Arrays.asList(authorss);
            //ean exei dwsei mono authors
            List<Paper> paperlist = paperRepo.findByAuthors_UsernameIn(authorsList);
            //vriskoume kai epistrefoume ola ta papers
            return new ResponseEntity<>(paperlist, HttpStatus.OK);
        } else if (authors.equals("") && pabstract.equals("")) {
            //ean exei dwsei mono titlo
            List<Paper> paperlist = paperRepo.findPaperByTitle(title);
            //vriskoume kai epistrefoume ola ta papers
            return new ResponseEntity<>(paperlist, HttpStatus.OK);
        } else if (authors.equals("")){
            //ean exei dwsei mono titlo kai abstract
            List<Paper> paperlist = paperRepo.findPaperByTitleAndPabstract(title, pabstract);
            //vriskoume kai epistrefoume ola ta papers
            return new ResponseEntity<>(paperlist, HttpStatus.OK);
        }
        return new ResponseEntity<>("error in request", HttpStatus.BAD_REQUEST);
        
    }
    
    
    @PUT
    @Path("/{paper}/submit")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity startSubmission(@PathParam("paper") int paperid){
        Optional<Paper> paperopt = paperRepo.findById(paperid);
        if (!paperopt.isEmpty()) {
            Paper paper = paperopt.get();
            if (paper.getState().equals("CREATED")){
                //allagi state
                paper.setState("SUBMITTED");
                paperRepo.save(paper);
                return new ResponseEntity<>("To paper egine submit", HttpStatus.OK);
            }
            else      
                return new ResponseEntity<>("To paper den egine submit afou einai se lathos state", HttpStatus.BAD_REQUEST);
           
        }
        else 
            return new ResponseEntity<>("To paper den vrethike", HttpStatus.NOT_FOUND);
    }
    
    @PUT
    @Path("/{paper}/review")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity startReview(@PathParam("paper") int paperid){
        Optional<Paper> paperopt = paperRepo.findById(paperid);
        if (!paperopt.isEmpty()) {
            Paper paper = paperopt.get();
            if (paper.getState().equals("SUBMITTED")){
                //allagi state
                paper.setState("REVIEWED");
                paperRepo.save(paper);
                return new ResponseEntity<>("To paper egine review", HttpStatus.OK);
            }
            else      
                return new ResponseEntity<>("To paper den egine review afou einai se lathos state", HttpStatus.BAD_REQUEST);
           
        }
        else 
            return new ResponseEntity<>("To paper den vrethike", HttpStatus.NOT_FOUND);
    }
    
    @PUT
    @Path("/{paper}/reject")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity startReject(@PathParam("paper") int paperid){
        Optional<Paper> paperopt = paperRepo.findById(paperid);
        if (!paperopt.isEmpty()) {
            Paper paper = paperopt.get();
            if (paper.getState().equals("REVIEWED")){
                //allagi state
                paper.setState("REJECTED");
                paperRepo.save(paper);
                return new ResponseEntity<>("To paper egine reject", HttpStatus.OK);
            }
            else      
                return new ResponseEntity<>("To paper den egine reject afou einai se lathos state", HttpStatus.BAD_REQUEST);
           
        }
        else 
            return new ResponseEntity<>("To paper den vrethike", HttpStatus.NOT_FOUND);
    }
    
    @PUT
    @Path("/{paper}/approve")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity startApprove(@PathParam("paper") int paperid){
        Optional<Paper> paperopt = paperRepo.findById(paperid);
        if (!paperopt.isEmpty()) {
            Paper paper = paperopt.get();
            if (paper.getState().equals("REVIEWED")){
                //allagi state
                paper.setState("APPROVED");
                paperRepo.save(paper);
                return new ResponseEntity<>("To paper egine approved", HttpStatus.OK);
            }
            else      
                return new ResponseEntity<>("To paper den egine approved afou einai se lathos state", HttpStatus.BAD_REQUEST);
           
        }
        else 
            return new ResponseEntity<>("To paper den vrethike", HttpStatus.NOT_FOUND);
    }
    
    @PUT
    @Path("/{paper}/accept")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity startAccept(@PathParam("paper") int paperid){
        Optional<Paper> paperopt = paperRepo.findById(paperid);
        if (!paperopt.isEmpty()) {
            Paper paper = paperopt.get();
            if (paper.getState().equals("REVIEWED")){
                //allagi state
                paper.setState("ACCEPTED");
                paperRepo.save(paper);
                return new ResponseEntity<>("To paper egine accept", HttpStatus.OK);
            }
            else      
                return new ResponseEntity<>("To paper den egine accept afou einai se lathos state", HttpStatus.BAD_REQUEST);
           
        }
        else 
            return new ResponseEntity<>("To paper den vrethike", HttpStatus.NOT_FOUND);
    }
    
}
