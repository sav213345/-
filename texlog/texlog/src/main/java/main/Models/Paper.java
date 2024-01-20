///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//
package main.Models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@XmlRootElement(name = "paper")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "paper")
public class Paper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(name = "paperid")
    private int paperid;
  
    @Column(name = "title", nullable = false, unique=true)
    @XmlElement(name = "title")
    private String title;
    
    @Column(name = "abstract", nullable = false)
    @XmlElement(name = "abstract")
    private String pabstract;
    
    @Column(name = "content", nullable = false)
    @XmlElement(name = "content")
    private String content;
    
    @Column(name = "creation_date", nullable = false)
    @XmlElement(name = "creationdate")
    private Date creationDate;
    
    @Column(name="state")
    @XmlElement(name = "name")
    private String state;
    
    //user-PC_MEMBER (Many-To-Many)
    @ManyToMany(mappedBy = "authored_papers", cascade = { CascadeType.ALL })
    private Set<User> authors = new HashSet<User>();
    
       //user-PC_MEMBER (Many-To-Many)
    @ManyToMany(mappedBy = "review_paper", cascade = { CascadeType.ALL })
    private Set<User> reviewers = new HashSet<User>();
    
}
