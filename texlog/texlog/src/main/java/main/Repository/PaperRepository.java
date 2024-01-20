package main.Repository;

import java.util.List;
import main.Models.Conference;
import main.Models.Paper;
import main.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Integer>{
    List<Paper> findPaperByTitle(String title); 
     
    List<Paper> findPaperByTitleAndPabstract(String title, String pabstract);
    
    List<Paper> findPaperByPabstract(String pabstract);
    
    List<Paper> findByAuthors_UsernameIn(List<String> authors);
}
