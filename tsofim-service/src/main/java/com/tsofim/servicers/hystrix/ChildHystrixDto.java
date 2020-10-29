package com.tsofim.servicers.hystrix;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChildHystrixDto {

    String uuidChild;
    String firstName;
    String secondName;
    //    TODO to code
    String uuidParent;
    String uuidRespPers;
    String childTz;

}

