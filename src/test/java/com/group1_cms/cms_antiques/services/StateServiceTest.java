package com.group1_cms.cms_antiques.services;


import com.group1_cms.cms_antiques.models.*;
import com.group1_cms.cms_antiques.repositories.PermissionRepository;
import com.group1_cms.cms_antiques.repositories.RoleRepository;
import com.group1_cms.cms_antiques.repositories.StateRepository;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class StateServiceTest {

    private StateRepository stateRepository = Mockito.mock(StateRepository.class);
    private StateService stateService = new StateService(stateRepository);

    @Test
    public void findAllStates(){
        List<State> stateList = new ArrayList<>();

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        State state1 = new State();
        State state2 = new State();
        State state3 = new State();

        state1.setId(id1);
        state1.setName("California");
        state1.setCreatedOn(createdOn);
        state1.setModifiedOn(modifiedOn);

        state2.setId(id2);
        state2.setName("Utah");
        state2.setCreatedOn(createdOn);
        state2.setModifiedOn(modifiedOn);

        state3.setId(id3);
        state3.setName("Idaho");
        state3.setCreatedOn(createdOn);
        state3.setModifiedOn(modifiedOn);

        stateList.add(state1);
        stateList.add(state2);
        stateList.add(state3);

        when(stateRepository.getAllStates()).thenReturn(stateList);

        List<StateDto> stateDtos = stateService.findAllStates();

        assertEquals(3, stateDtos.size());
        verify(stateRepository, times(1)).getAllStates();
        for(int i = 0; i < stateDtos.size(); i++){
            String name = stateDtos.get(i).getStateName();
            assertEquals(stateList.get(i).getName(), name);
        }
    }

    @Test
    public void checkForDuplicates(){
        UUID id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        State stateToTest = new State();
        stateToTest.setName("Utah");
        stateToTest.setId(id);
        stateToTest.setCreatedOn(createdOn);
        stateToTest.setModifiedOn(modifiedOn);
        when(stateRepository.getStateByName("Utah")).thenReturn(stateToTest);

        boolean isDuplicate = stateService.checkForDuplicateName("Utah");
        boolean isNotDuplicate = stateService.checkForDuplicateName("Maine");
        assertEquals(true, isDuplicate);
        assertEquals(false, isNotDuplicate);
    }

    @Test
    public void findStateByName(){
        UUID id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        State stateToTest = new State();
        stateToTest.setName("Utah");
        stateToTest.setId(id);
        stateToTest.setCreatedOn(createdOn);
        stateToTest.setModifiedOn(modifiedOn);
        when(stateRepository.getStateByName("Utah")).thenReturn(stateToTest);

        State state = stateService.findStateByName("Utah");
        assertEquals("Utah", state.getName());
        assertEquals(id, state.getId());
        assertEquals(createdOn, state.getCreatedOn());
        assertEquals(modifiedOn, state.getModifiedOn());
    }

    @Test
    public void findStateById(){
        UUID id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        State stateToTest = new State();
        stateToTest.setName("Utah");
        stateToTest.setId(id);
        stateToTest.setCreatedOn(createdOn);
        stateToTest.setModifiedOn(modifiedOn);
        when(stateRepository.getStateById(id.toString())).thenReturn(stateToTest);

        StateDto state = stateService.findStateById(id.toString());
        assertEquals("Utah", state.getStateName());
        assertEquals(id.toString(), state.getId());
        assertEquals(createdOn.format(DateTimeFormatter.ofPattern("d MMM uuuu")), state.getCreatedOn());
        assertEquals(modifiedOn.format(DateTimeFormatter.ofPattern("d MMM uuuu")), state.getModifiedOn());
    }

    @Test
    public void saveStateStateDto(){
        UUID id = UUID.randomUUID();
        String name = "Utah";
        String newStateName = "California";
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        StateDto stateDto = new StateDto(id, name, createdOn, modifiedOn);

        State state = new State();
        state.setName(name);
        state.setId(id);

        when(stateRepository.getStateById(state.getId().toString())).thenReturn(state);

        stateService.saveState(stateDto);

        verify(stateRepository, times(1)).save(state);

    }

    @Test
    public void saveState(){
        UUID id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        State stateToTest = new State();
        stateToTest.setName("Utah");
        stateToTest.setId(id);
        stateToTest.setCreatedOn(createdOn);
        stateToTest.setModifiedOn(modifiedOn);
        stateService.saveState(stateToTest);

        verify(stateRepository, times(1)).save(stateToTest);


        State newState = new State();
        newState.setName("California");
        newState.setCreatedOn(createdOn);
        newState.setModifiedOn(modifiedOn);
        stateService.saveState(newState);

        verify(stateRepository, times(1)).save(newState);
    }

    @Test
    public void deleteStateFromDbById(){
        State newState = new State();
        newState.setName("Utah");
        newState.setId(UUID.randomUUID());
        when(stateRepository.deleteStateById(newState.getId().toString())).thenReturn(1);
        assertEquals(false, stateService.deleteStateFromDbById(UUID.randomUUID().toString()));
        assertEquals(true, stateService.deleteStateFromDbById(newState.getId().toString()));
    }
}
