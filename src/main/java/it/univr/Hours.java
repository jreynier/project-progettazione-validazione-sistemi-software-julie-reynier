package it.univr;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Hours {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private int hour;
    private Date date;
    private Researcher researcher;
    private Project project;
}
