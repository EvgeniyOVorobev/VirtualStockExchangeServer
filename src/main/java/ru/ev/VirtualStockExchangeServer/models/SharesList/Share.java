package ru.ev.VirtualStockExchangeServer.models.SharesList;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;


import javax.annotation.processing.Generated;
import java.time.LocalDate;

@Entity
@Table(name="sharlist")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("jsonschema2pojo")
@JsonIgnoreProperties(value = { "id" })
public class Share {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="shortname")
    private String shortName;
    @Column(name="secid")
    private String secid;


    @Override
    public String toString() {
        return "Share{" +
                "shortName='" + shortName + '\'' +
                ", secid='" + secid + '\'' +
                '}';
    }
}
