package de.tekup.complaintsclaims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "complaints")
public class Complaint extends AbstractEntity {

    private String content;

    @ManyToOne
    private User user;
}
