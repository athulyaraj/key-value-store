package com.keyvaluestore.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@XmlRootElement
public class Response implements Serializable {

    int errorCode;
    String errorReason;
    int statusCode;
    Entry data;
}