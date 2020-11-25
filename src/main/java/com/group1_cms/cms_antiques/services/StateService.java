package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.State;
import com.group1_cms.cms_antiques.repositories.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class StateService {

    private final StateRepository stateRepository;

    @Autowired
    public StateService(StateRepository stateRepository){
        this.stateRepository = stateRepository;
    }

    public List<State> findAllStates(){
        return stateRepository.getAllStates();
    }

    public State findStateByName(String name){
        return stateRepository.getStateByName(name);
    }

    public State saveState(State state){
        State stateFromDb = stateRepository.getStateByName(state.getName());
        if(stateFromDb == null){
            state.setId(UUID.randomUUID());
            state.setCreatedOn(ZonedDateTime.now());
            state.setModifiedOn(ZonedDateTime.now());
        }
        else{
            state.setId(stateFromDb.getId());
            state.setModifiedOn(ZonedDateTime.now());
        }

        return stateRepository.save(state);
    }
}
