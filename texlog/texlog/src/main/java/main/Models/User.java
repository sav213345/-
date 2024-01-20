///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//
package main.Models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "user")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @XmlAttribute(name = "username")
    private String username;
  
    @Column(name = "password", nullable = false)
    @XmlElement(name = "password")
    private String password;
    
    @Column(name = "name", nullable = false)
    @XmlElement(name = "name")
    private String name;
    
    @Column(name = "role", nullable = false)
    @XmlElement(name = "role")
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "User_Role", 
        joinColumns = { @JoinColumn(name = "username") }, 
        inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private Set<Role> roles = new HashSet<>();
    
    @ManyToMany(cascade = {
         CascadeType.ALL
    },fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_conferences",
        joinColumns = {
            @JoinColumn(name = "username")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "confid")
        }
    )
    Set <Conference> conferences_chair = new HashSet <> ();
    
    @ManyToMany(cascade = {
         CascadeType.ALL
    },fetch = FetchType.EAGER)
    @JoinTable(
        name = "member_conferences",
        joinColumns = {
            @JoinColumn(name = "username")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "confid")
        }
    )
    Set <Conference> conferences_memb = new HashSet <> ();
    
    @ManyToMany(cascade = {
        CascadeType.ALL
    })
    @JoinTable(
        name = "authored_paper",
        joinColumns = {
            @JoinColumn(name = "username")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "paperid")
        }
    )
    Set <Paper> authored_papers = new HashSet <> ();
    
    @ManyToMany(cascade = {
        CascadeType.ALL
    })
    @JoinTable(
        name = "review_paper",
        joinColumns = {
            @JoinColumn(name = "username")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "paperid")
        }
    )
    Set <Paper> review_paper = new HashSet <> ();
    
    
    
}
