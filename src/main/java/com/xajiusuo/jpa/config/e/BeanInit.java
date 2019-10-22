package com.xajiusuo.jpa.config.e;

import com.google.common.collect.Sets;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * Created by hadoop on 19-6-14.
 */
public class BeanInit {

    @Transient
    protected Set<String> init;

    public void initProper(String ... pros){
        if(pros == null || pros.length == 0){
            return;
        }
        init = Sets.newHashSet(pros);
    }

    public Set<String> init(){
        if(init == null){
            return Collections.emptySet();
        }
        return init;
    }

    public void clear(){
        init().clear();
    }


}
