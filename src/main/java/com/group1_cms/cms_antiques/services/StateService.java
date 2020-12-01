package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.models.RoleDto;
import com.group1_cms.cms_antiques.models.State;
import com.group1_cms.cms_antiques.models.StateDto;
import com.group1_cms.cms_antiques.repositories.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StateService {

    private final StateRepository stateRepository;

    @Autowired
    public StateService(StateRepository stateRepository){
        this.stateRepository = stateRepository;
    }

    public List<StateDto> findAllStates(){
        List<StateDto> stateDtoList = new ArrayList<>();
        for(State state : stateRepository.getAllStates()){
            stateDtoList.add(new StateDto(state.getId(), state.getName(), state.getCreatedOn(), state.getModifiedOn()));
        }
        return stateDtoList;
    }

    public State findStateByName(String name){
        return stateRepository.getStateByName(name);
    }

    public StateDto findStateById(String id){

        State stateFromDb = stateRepository.getStateById(id);
        if(stateFromDb.getId() != null){
            return new StateDto(stateFromDb.getId(), stateFromDb.getName(), stateFromDb.getCreatedOn(), stateFromDb.getModifiedOn());
        }
        return null;
    }

    public State saveState(StateDto state){
        State stateFromDb = stateRepository.getStateByName(state.getStateName());
        if(stateFromDb == null){
            stateFromDb = new State();
            stateFromDb.setId(UUID.randomUUID());
            stateFromDb.setName(state.getStateName());
            stateFromDb.setCreatedOn(ZonedDateTime.now());
            stateFromDb.setModifiedOn(ZonedDateTime.now());
        }
        else{
            stateFromDb.setName(state.getStateName());
            stateFromDb.setModifiedOn(ZonedDateTime.now());
        }

        return stateRepository.save(stateFromDb);
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

    public boolean deleteStateFromDbById(String id){
        if(stateRepository.deleteStateById(id) > 0){
            return true;
        }
        else{
            return false;
        }
    }
}
