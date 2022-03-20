package com.learning.springdbencryption.entity;

import com.learning.springdbencryption.converters.EntityAttributeConverter;
import lombok.*;

import javax.persistence.*;

@Table(name="ENC_TABLE")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class EncTable {

    @Id
    @Column(name="ID")
    private Long id;

    @Lob
    @Column(name="ENC_DATA")
    @Convert(converter = EntityAttributeConverter.class)
    private String encData;

}
