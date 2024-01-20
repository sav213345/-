/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package main;

import main.Controller.ConferenceController;
import jakarta.ws.rs.ApplicationPath;
import main.Controller.PaperController;
import main.Controller.UserController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
 
//@Component
@ApplicationPath("/")
@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(ConferenceController.class);
		register(UserController.class);
		register(PaperController.class);
	}

}