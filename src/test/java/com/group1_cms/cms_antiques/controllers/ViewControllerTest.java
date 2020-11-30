package com.group1_cms.cms_antiques.controllers;

import com.group1_cms.cms_antiques.models.*;
import com.group1_cms.cms_antiques.services.PostsService;
import com.group1_cms.cms_antiques.services.StateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
class ViewControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ViewController viewController;

    @MockBean
    private StateService stateService;

    //region GET tests
    @Test
    void publicHomePage() throws Exception
    {
        // Mocks our services
        viewController = Mockito.mock(ViewController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
        // Index
        Mockito.when(viewController.publicHomePage()).thenReturn(new ModelAndView("index"));
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void loginPage() throws Exception
    {
        // Mocks our services
        viewController = Mockito.mock(ViewController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
        // Login page
        Mockito.when(viewController.loginPage()).thenReturn(new ModelAndView("/"));
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        mockMvc.perform(MockMvcRequestBuilders.get("/login")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void loginPageError() throws Exception
    {
        // Mocks our services
        viewController = Mockito.mock(ViewController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
        // Creates a login Error
        Mockito.when(viewController.loginPageError()).thenReturn(new ModelAndView("login")
                .addObject("loginError", true).addObject("user", new User()));
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404 and make sure we have
        // an error too
        mockMvc.perform(MockMvcRequestBuilders.get("/login-error")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("loginError"));
    }

    @Test
    void registerPage() throws Exception
    {
        // Mocks our services
        viewController = Mockito.mock(ViewController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        mockMvc.perform(MockMvcRequestBuilders.get("/register")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void registerUser() throws Exception
    {
        // Mocks our services
        User newUser = new User();
        BindingResult newResult = new BindingResult()
        {
            @Override
            public Object getTarget()
            {
                return null;
            }

            @Override
            public Map<String, Object> getModel()
            {
                return null;
            }

            @Override
            public Object getRawFieldValue(String s)
            {
                return null;
            }

            @Override
            public PropertyEditor findEditor(String s, Class<?> aClass)
            {
                return null;
            }

            @Override
            public PropertyEditorRegistry getPropertyEditorRegistry()
            {
                return null;
            }

            @Override
            public String[] resolveMessageCodes(String s)
            {
                return new String[0];
            }

            @Override
            public String[] resolveMessageCodes(String s, String s1)
            {
                return new String[0];
            }

            @Override
            public void addError(ObjectError objectError)
            {

            }

            @Override
            public String getObjectName()
            {
                return null;
            }

            @Override
            public void setNestedPath(String s)
            {

            }

            @Override
            public String getNestedPath()
            {
                return null;
            }

            @Override
            public void pushNestedPath(String s)
            {

            }

            @Override
            public void popNestedPath() throws IllegalStateException
            {

            }

            @Override
            public void reject(String s)
            {

            }

            @Override
            public void reject(String s, String s1)
            {

            }

            @Override
            public void reject(String s, Object[] objects, String s1)
            {

            }

            @Override
            public void rejectValue(String s, String s1)
            {

            }

            @Override
            public void rejectValue(String s, String s1, String s2)
            {

            }

            @Override
            public void rejectValue(String s, String s1, Object[] objects, String s2)
            {

            }

            @Override
            public void addAllErrors(Errors errors)
            {

            }

            @Override
            public boolean hasErrors()
            {
                return false;
            }

            @Override
            public int getErrorCount()
            {
                return 0;
            }

            @Override
            public List<ObjectError> getAllErrors()
            {
                return null;
            }

            @Override
            public boolean hasGlobalErrors()
            {
                return false;
            }

            @Override
            public int getGlobalErrorCount()
            {
                return 0;
            }

            @Override
            public List<ObjectError> getGlobalErrors()
            {
                return null;
            }

            @Override
            public ObjectError getGlobalError()
            {
                return null;
            }

            @Override
            public boolean hasFieldErrors()
            {
                return false;
            }

            @Override
            public int getFieldErrorCount()
            {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors()
            {
                return null;
            }

            @Override
            public FieldError getFieldError()
            {
                return null;
            }

            @Override
            public boolean hasFieldErrors(String s)
            {
                return false;
            }

            @Override
            public int getFieldErrorCount(String s)
            {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors(String s)
            {
                return null;
            }

            @Override
            public FieldError getFieldError(String s)
            {
                return null;
            }

            @Override
            public Object getFieldValue(String s)
            {
                return null;
            }

            @Override
            public Class<?> getFieldType(String s)
            {
                return null;
            }
        };
        HttpServletResponse newResponse = new HttpServletResponse()
        {
            @Override
            public void addCookie(Cookie cookie)
            {

            }

            @Override
            public boolean containsHeader(String s)
            {
                return false;
            }

            @Override
            public String encodeURL(String s)
            {
                return null;
            }

            @Override
            public String encodeRedirectURL(String s)
            {
                return null;
            }

            @Override
            public String encodeUrl(String s)
            {
                return null;
            }

            @Override
            public String encodeRedirectUrl(String s)
            {
                return null;
            }

            @Override
            public void sendError(int i, String s) throws IOException
            {

            }

            @Override
            public void sendError(int i) throws IOException
            {

            }

            @Override
            public void sendRedirect(String s) throws IOException
            {

            }

            @Override
            public void setDateHeader(String s, long l)
            {

            }

            @Override
            public void addDateHeader(String s, long l)
            {

            }

            @Override
            public void setHeader(String s, String s1)
            {

            }

            @Override
            public void addHeader(String s, String s1)
            {

            }

            @Override
            public void setIntHeader(String s, int i)
            {

            }

            @Override
            public void addIntHeader(String s, int i)
            {

            }

            @Override
            public void setStatus(int i)
            {

            }

            @Override
            public void setStatus(int i, String s)
            {

            }

            @Override
            public int getStatus()
            {
                return 0;
            }

            @Override
            public String getHeader(String s)
            {
                return null;
            }

            @Override
            public Collection<String> getHeaders(String s)
            {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames()
            {
                return null;
            }

            @Override
            public String getCharacterEncoding()
            {
                return null;
            }

            @Override
            public String getContentType()
            {
                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException
            {
                return null;
            }

            @Override
            public PrintWriter getWriter() throws IOException
            {
                return null;
            }

            @Override
            public void setCharacterEncoding(String s)
            {

            }

            @Override
            public void setContentLength(int i)
            {

            }

            @Override
            public void setContentLengthLong(long l)
            {

            }

            @Override
            public void setContentType(String s)
            {

            }

            @Override
            public void setBufferSize(int i)
            {

            }

            @Override
            public int getBufferSize()
            {
                return 0;
            }

            @Override
            public void flushBuffer() throws IOException
            {

            }

            @Override
            public void resetBuffer()
            {

            }

            @Override
            public boolean isCommitted()
            {
                return false;
            }

            @Override
            public void reset()
            {

            }

            @Override
            public void setLocale(Locale locale)
            {

            }

            @Override
            public Locale getLocale()
            {
                return null;
            }
        };
        viewController = Mockito.mock(ViewController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
        // Creates a login Error
        Mockito.when(viewController.registerUser(newUser, newResult, newResponse)).thenReturn(new ModelAndView("/"));
        // Now tests to make sure the file is there, if so it'll return a 200 instead of a 404
        mockMvc.perform(MockMvcRequestBuilders.post("/register", newUser, newResult, newResponse)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser(value = "admin")
    @Test
    void editProfile() throws Exception
    {
        // Mocks our services
        State newState = new State("testState");
        ArrayList<State> newList = new ArrayList<>();
        ModelAndView newModandView = new ModelAndView("/");
        newModandView.addObject("stateList", newList);
        newList.add(newState);
        stateService = Mockito.mock(StateService.class);
        viewController = Mockito.mock(ViewController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
        Mockito.when(stateService.findAllStates()).thenReturn(newList);
        Mockito.when(viewController.editProfile()).thenReturn(newModandView);
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        mockMvc.perform(MockMvcRequestBuilders.get("/member/accountProfile")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("stateList"));
    }

    @Test
    void resetPassword() throws Exception
    {
        // Mocks our services
        viewController = Mockito.mock(ViewController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
        UserProfileDto newDTO = new UserProfileDto();
        BindingResult newResult = new BindingResult()
        {
            @Override
            public Object getTarget()
            {
                return null;
            }

            @Override
            public Map<String, Object> getModel()
            {
                return null;
            }

            @Override
            public Object getRawFieldValue(String s)
            {
                return null;
            }

            @Override
            public PropertyEditor findEditor(String s, Class<?> aClass)
            {
                return null;
            }

            @Override
            public PropertyEditorRegistry getPropertyEditorRegistry()
            {
                return null;
            }

            @Override
            public String[] resolveMessageCodes(String s)
            {
                return new String[0];
            }

            @Override
            public String[] resolveMessageCodes(String s, String s1)
            {
                return new String[0];
            }

            @Override
            public void addError(ObjectError objectError)
            {

            }

            @Override
            public String getObjectName()
            {
                return null;
            }

            @Override
            public void setNestedPath(String s)
            {

            }

            @Override
            public String getNestedPath()
            {
                return null;
            }

            @Override
            public void pushNestedPath(String s)
            {

            }

            @Override
            public void popNestedPath() throws IllegalStateException
            {

            }

            @Override
            public void reject(String s)
            {

            }

            @Override
            public void reject(String s, String s1)
            {

            }

            @Override
            public void reject(String s, Object[] objects, String s1)
            {

            }

            @Override
            public void rejectValue(String s, String s1)
            {

            }

            @Override
            public void rejectValue(String s, String s1, String s2)
            {

            }

            @Override
            public void rejectValue(String s, String s1, Object[] objects, String s2)
            {

            }

            @Override
            public void addAllErrors(Errors errors)
            {

            }

            @Override
            public boolean hasErrors()
            {
                return false;
            }

            @Override
            public int getErrorCount()
            {
                return 0;
            }

            @Override
            public List<ObjectError> getAllErrors()
            {
                return null;
            }

            @Override
            public boolean hasGlobalErrors()
            {
                return false;
            }

            @Override
            public int getGlobalErrorCount()
            {
                return 0;
            }

            @Override
            public List<ObjectError> getGlobalErrors()
            {
                return null;
            }

            @Override
            public ObjectError getGlobalError()
            {
                return null;
            }

            @Override
            public boolean hasFieldErrors()
            {
                return false;
            }

            @Override
            public int getFieldErrorCount()
            {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors()
            {
                return null;
            }

            @Override
            public FieldError getFieldError()
            {
                return null;
            }

            @Override
            public boolean hasFieldErrors(String s)
            {
                return false;
            }

            @Override
            public int getFieldErrorCount(String s)
            {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors(String s)
            {
                return null;
            }

            @Override
            public FieldError getFieldError(String s)
            {
                return null;
            }

            @Override
            public Object getFieldValue(String s)
            {
                return null;
            }

            @Override
            public Class<?> getFieldType(String s)
            {
                return null;
            }
        };
        // Creates a login Error
        Mockito.when(viewController.resetPassword()).thenReturn(new ModelAndView("member/accountProfile"));
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        mockMvc.perform(MockMvcRequestBuilders.get("/member/accountProfile")).andExpect(MockMvcResultMatchers.status().isOk());
    }
    //endregion

    //region POST tests
    @Test
    void saveUserProfile() throws Exception
    {
        // Mocks our services
        viewController = Mockito.mock(ViewController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
        UserProfileDto newDTO = new UserProfileDto();
        BindingResult newResult = new BindingResult()
        {
            @Override
            public Object getTarget()
            {
                return null;
            }

            @Override
            public Map<String, Object> getModel()
            {
                return null;
            }

            @Override
            public Object getRawFieldValue(String s)
            {
                return null;
            }

            @Override
            public PropertyEditor findEditor(String s, Class<?> aClass)
            {
                return null;
            }

            @Override
            public PropertyEditorRegistry getPropertyEditorRegistry()
            {
                return null;
            }

            @Override
            public String[] resolveMessageCodes(String s)
            {
                return new String[0];
            }

            @Override
            public String[] resolveMessageCodes(String s, String s1)
            {
                return new String[0];
            }

            @Override
            public void addError(ObjectError objectError)
            {

            }

            @Override
            public String getObjectName()
            {
                return null;
            }

            @Override
            public void setNestedPath(String s)
            {

            }

            @Override
            public String getNestedPath()
            {
                return null;
            }

            @Override
            public void pushNestedPath(String s)
            {

            }

            @Override
            public void popNestedPath() throws IllegalStateException
            {

            }

            @Override
            public void reject(String s)
            {

            }

            @Override
            public void reject(String s, String s1)
            {

            }

            @Override
            public void reject(String s, Object[] objects, String s1)
            {

            }

            @Override
            public void rejectValue(String s, String s1)
            {

            }

            @Override
            public void rejectValue(String s, String s1, String s2)
            {

            }

            @Override
            public void rejectValue(String s, String s1, Object[] objects, String s2)
            {

            }

            @Override
            public void addAllErrors(Errors errors)
            {

            }

            @Override
            public boolean hasErrors()
            {
                return false;
            }

            @Override
            public int getErrorCount()
            {
                return 0;
            }

            @Override
            public List<ObjectError> getAllErrors()
            {
                return null;
            }

            @Override
            public boolean hasGlobalErrors()
            {
                return false;
            }

            @Override
            public int getGlobalErrorCount()
            {
                return 0;
            }

            @Override
            public List<ObjectError> getGlobalErrors()
            {
                return null;
            }

            @Override
            public ObjectError getGlobalError()
            {
                return null;
            }

            @Override
            public boolean hasFieldErrors()
            {
                return false;
            }

            @Override
            public int getFieldErrorCount()
            {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors()
            {
                return null;
            }

            @Override
            public FieldError getFieldError()
            {
                return null;
            }

            @Override
            public boolean hasFieldErrors(String s)
            {
                return false;
            }

            @Override
            public int getFieldErrorCount(String s)
            {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors(String s)
            {
                return null;
            }

            @Override
            public FieldError getFieldError(String s)
            {
                return null;
            }

            @Override
            public Object getFieldValue(String s)
            {
                return null;
            }

            @Override
            public Class<?> getFieldType(String s)
            {
                return null;
            }
        };
        // Creates a login Error
        Mockito.when(viewController.saveUserProfile(newDTO, newResult)).thenReturn(new ModelAndView("member/accountProfile"));
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        mockMvc.perform(MockMvcRequestBuilders.post("/member/accountProfile", newDTO, newResult)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void saveResetPassword() throws Exception
    {
        // Mocks our services
        viewController = Mockito.mock(ViewController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
        UserPasswordDto newDTO = new UserPasswordDto();
        BindingResult newResult = new BindingResult()
        {
            @Override
            public Object getTarget()
            {
                return null;
            }

            @Override
            public Map<String, Object> getModel()
            {
                return null;
            }

            @Override
            public Object getRawFieldValue(String s)
            {
                return null;
            }

            @Override
            public PropertyEditor findEditor(String s, Class<?> aClass)
            {
                return null;
            }

            @Override
            public PropertyEditorRegistry getPropertyEditorRegistry()
            {
                return null;
            }

            @Override
            public String[] resolveMessageCodes(String s)
            {
                return new String[0];
            }

            @Override
            public String[] resolveMessageCodes(String s, String s1)
            {
                return new String[0];
            }

            @Override
            public void addError(ObjectError objectError)
            {

            }

            @Override
            public String getObjectName()
            {
                return null;
            }

            @Override
            public void setNestedPath(String s)
            {

            }

            @Override
            public String getNestedPath()
            {
                return null;
            }

            @Override
            public void pushNestedPath(String s)
            {

            }

            @Override
            public void popNestedPath() throws IllegalStateException
            {

            }

            @Override
            public void reject(String s)
            {

            }

            @Override
            public void reject(String s, String s1)
            {

            }

            @Override
            public void reject(String s, Object[] objects, String s1)
            {

            }

            @Override
            public void rejectValue(String s, String s1)
            {

            }

            @Override
            public void rejectValue(String s, String s1, String s2)
            {

            }

            @Override
            public void rejectValue(String s, String s1, Object[] objects, String s2)
            {

            }

            @Override
            public void addAllErrors(Errors errors)
            {

            }

            @Override
            public boolean hasErrors()
            {
                return false;
            }

            @Override
            public int getErrorCount()
            {
                return 0;
            }

            @Override
            public List<ObjectError> getAllErrors()
            {
                return null;
            }

            @Override
            public boolean hasGlobalErrors()
            {
                return false;
            }

            @Override
            public int getGlobalErrorCount()
            {
                return 0;
            }

            @Override
            public List<ObjectError> getGlobalErrors()
            {
                return null;
            }

            @Override
            public ObjectError getGlobalError()
            {
                return null;
            }

            @Override
            public boolean hasFieldErrors()
            {
                return false;
            }

            @Override
            public int getFieldErrorCount()
            {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors()
            {
                return null;
            }

            @Override
            public FieldError getFieldError()
            {
                return null;
            }

            @Override
            public boolean hasFieldErrors(String s)
            {
                return false;
            }

            @Override
            public int getFieldErrorCount(String s)
            {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors(String s)
            {
                return null;
            }

            @Override
            public FieldError getFieldError(String s)
            {
                return null;
            }

            @Override
            public Object getFieldValue(String s)
            {
                return null;
            }

            @Override
            public Class<?> getFieldType(String s)
            {
                return null;
            }
        };
        // Creates a login Error
        Mockito.when(viewController.saveResetPassword(newDTO, newResult)).thenReturn(new ModelAndView("member/accountProfile"));
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        mockMvc.perform(MockMvcRequestBuilders.post("/member/accountProfile")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser("admin")
    @Test
    void closeAccount() throws Exception
    {
        // Mocks our services
        viewController = Mockito.mock(ViewController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
        UUID newID = UUID.randomUUID();
        BindingResult newResult = new BindingResult()
        {
            @Override
            public Object getTarget()
            {
                return null;
            }

            @Override
            public Map<String, Object> getModel()
            {
                return null;
            }

            @Override
            public Object getRawFieldValue(String s)
            {
                return null;
            }

            @Override
            public PropertyEditor findEditor(String s, Class<?> aClass)
            {
                return null;
            }

            @Override
            public PropertyEditorRegistry getPropertyEditorRegistry()
            {
                return null;
            }

            @Override
            public String[] resolveMessageCodes(String s)
            {
                return new String[0];
            }

            @Override
            public String[] resolveMessageCodes(String s, String s1)
            {
                return new String[0];
            }

            @Override
            public void addError(ObjectError objectError)
            {

            }

            @Override
            public String getObjectName()
            {
                return null;
            }

            @Override
            public void setNestedPath(String s)
            {

            }

            @Override
            public String getNestedPath()
            {
                return null;
            }

            @Override
            public void pushNestedPath(String s)
            {

            }

            @Override
            public void popNestedPath() throws IllegalStateException
            {

            }

            @Override
            public void reject(String s)
            {

            }

            @Override
            public void reject(String s, String s1)
            {

            }

            @Override
            public void reject(String s, Object[] objects, String s1)
            {

            }

            @Override
            public void rejectValue(String s, String s1)
            {

            }

            @Override
            public void rejectValue(String s, String s1, String s2)
            {

            }

            @Override
            public void rejectValue(String s, String s1, Object[] objects, String s2)
            {

            }

            @Override
            public void addAllErrors(Errors errors)
            {

            }

            @Override
            public boolean hasErrors()
            {
                return false;
            }

            @Override
            public int getErrorCount()
            {
                return 0;
            }

            @Override
            public List<ObjectError> getAllErrors()
            {
                return null;
            }

            @Override
            public boolean hasGlobalErrors()
            {
                return false;
            }

            @Override
            public int getGlobalErrorCount()
            {
                return 0;
            }

            @Override
            public List<ObjectError> getGlobalErrors()
            {
                return null;
            }

            @Override
            public ObjectError getGlobalError()
            {
                return null;
            }

            @Override
            public boolean hasFieldErrors()
            {
                return false;
            }

            @Override
            public int getFieldErrorCount()
            {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors()
            {
                return null;
            }

            @Override
            public FieldError getFieldError()
            {
                return null;
            }

            @Override
            public boolean hasFieldErrors(String s)
            {
                return false;
            }

            @Override
            public int getFieldErrorCount(String s)
            {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors(String s)
            {
                return null;
            }

            @Override
            public FieldError getFieldError(String s)
            {
                return null;
            }

            @Override
            public Object getFieldValue(String s)
            {
                return null;
            }

            @Override
            public Class<?> getFieldType(String s)
            {
                return null;
            }
        };
        // Creates a login Error
        Mockito.when(viewController.closeAccount(newID)).thenReturn(new ModelAndView("member/closeAccount"));
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        mockMvc.perform(MockMvcRequestBuilders.get("/member/closeAccount/" + newID, newID)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void closeAccount2() throws Exception
    {
        // Mocks our services
        viewController = Mockito.mock(ViewController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
        UUID newID = UUID.randomUUID();
        BindingResult newResult = new BindingResult()
        {
            @Override
            public Object getTarget()
            {
                return null;
            }

            @Override
            public Map<String, Object> getModel()
            {
                return null;
            }

            @Override
            public Object getRawFieldValue(String s)
            {
                return null;
            }

            @Override
            public PropertyEditor findEditor(String s, Class<?> aClass)
            {
                return null;
            }

            @Override
            public PropertyEditorRegistry getPropertyEditorRegistry()
            {
                return null;
            }

            @Override
            public String[] resolveMessageCodes(String s)
            {
                return new String[0];
            }

            @Override
            public String[] resolveMessageCodes(String s, String s1)
            {
                return new String[0];
            }

            @Override
            public void addError(ObjectError objectError)
            {

            }

            @Override
            public String getObjectName()
            {
                return null;
            }

            @Override
            public void setNestedPath(String s)
            {

            }

            @Override
            public String getNestedPath()
            {
                return null;
            }

            @Override
            public void pushNestedPath(String s)
            {

            }

            @Override
            public void popNestedPath() throws IllegalStateException
            {

            }

            @Override
            public void reject(String s)
            {

            }

            @Override
            public void reject(String s, String s1)
            {

            }

            @Override
            public void reject(String s, Object[] objects, String s1)
            {

            }

            @Override
            public void rejectValue(String s, String s1)
            {

            }

            @Override
            public void rejectValue(String s, String s1, String s2)
            {

            }

            @Override
            public void rejectValue(String s, String s1, Object[] objects, String s2)
            {

            }

            @Override
            public void addAllErrors(Errors errors)
            {

            }

            @Override
            public boolean hasErrors()
            {
                return false;
            }

            @Override
            public int getErrorCount()
            {
                return 0;
            }

            @Override
            public List<ObjectError> getAllErrors()
            {
                return null;
            }

            @Override
            public boolean hasGlobalErrors()
            {
                return false;
            }

            @Override
            public int getGlobalErrorCount()
            {
                return 0;
            }

            @Override
            public List<ObjectError> getGlobalErrors()
            {
                return null;
            }

            @Override
            public ObjectError getGlobalError()
            {
                return null;
            }

            @Override
            public boolean hasFieldErrors()
            {
                return false;
            }

            @Override
            public int getFieldErrorCount()
            {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors()
            {
                return null;
            }

            @Override
            public FieldError getFieldError()
            {
                return null;
            }

            @Override
            public boolean hasFieldErrors(String s)
            {
                return false;
            }

            @Override
            public int getFieldErrorCount(String s)
            {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors(String s)
            {
                return null;
            }

            @Override
            public FieldError getFieldError(String s)
            {
                return null;
            }

            @Override
            public Object getFieldValue(String s)
            {
                return null;
            }

            @Override
            public Class<?> getFieldType(String s)
            {
                return null;
            }
        };
        // Creates a login Error
        Mockito.when(viewController.closeAccount(newID)).thenReturn(new ModelAndView("member/closeAccount"));
        // Now tests to make sure we can't post to this, we should get an HTTP access error
        mockMvc.perform(MockMvcRequestBuilders.post("/member/closeAccount/" + newID, newID)).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
    //endregion
}