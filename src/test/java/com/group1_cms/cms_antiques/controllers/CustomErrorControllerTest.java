package com.group1_cms.cms_antiques.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomErrorControllerTest
{

    @Test
    public void testError() throws Exception
    {
        CustomErrorController customErrorController = new CustomErrorController();
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Exception newException = Mockito.mock(Exception.class);
        customErrorController.handleError(httpServletRequest, newException);
    }

}