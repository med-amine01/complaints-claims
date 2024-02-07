package de.tekup.complaintsclaims.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "complaints")
public class Complaint extends AbstractEntity {

    @Lob
    private String content;

    @ManyToOne
    @JsonIgnore
    private User user;
}
