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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "conference")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conference")
public class Conference implements Serializable {
    private static final long serialVersionUID = 1L;
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(name = "confid")
    private int confid;
  
    @Column(name = "name", nullable = false, unique=true)
    @XmlElement(name = "name")
    private String name;
    
    @Column(name = "description", nullable = true)
    @XmlElement(name = "description")
    private String description;
    
    @Column(name = "creation_date", nullable = false)
    @XmlElement(name = "creationdate")
    private Date creationDate;
    
    @Column(name="state")
    @XmlElement(name = "state")
    private String state;
    
    
    @ManyToMany(mappedBy = "conferences_chair", cascade = {  CascadeType.ALL },fetch = FetchType.EAGER)
    private Set<User> pcchairs = new HashSet<User>();
    
    @ManyToMany(mappedBy = "conferences_memb", cascade = {  CascadeType.ALL },fetch = FetchType.EAGER)
    private Set<User> pcmembers = new HashSet<User>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "conference_id", referencedColumnName = "confid")
    private Set<Paper> papers = new HashSet<>();
    

    
}
