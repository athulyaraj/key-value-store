package com.keyvaluestore.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@XmlRootElement
public class Entry implements BaseDto {
    private String key;
    private String value;
    private long createdTime;
}

