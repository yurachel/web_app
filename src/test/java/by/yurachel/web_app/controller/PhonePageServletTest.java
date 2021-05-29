package by.yurachel.web_app.controller;

import by.yurachel.web_app.HttpInit;
import by.yurachel.web_app.dao.IDao;
import by.yurachel.web_app.dao.jdbc.DAOProvider;
import by.yurachel.web_app.entity.Phone;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PhonePageServletTest extends HttpInit {

    @InjectMocks
    private PhonePageServlet phonePageServlet;

    @Mock
    private DAOProvider phoneProvider;
    @Mock
    private IDao<Phone> IDao;
    @Mock
    Phone phone;

    @Test
    void doGet() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("phone");
        when(IDao.findByName("phone")).thenReturn(phone);
        doNothing().when(request).setAttribute(eq("phone"), eq(phone));
        phonePageServlet.doGet(request, response);
        verify(request).getParameter(anyString());
        verify(IDao).findByName(anyString());
        verify(request).setAttribute(anyString(), any(Phone.class));
    }

    @Test
    void doGetWhenPhoneNameIsNull() {
        when(request.getParameter("name")).thenReturn(null);
        when(IDao.findByName(null))
                .thenThrow(new IllegalArgumentException("Can't find such phone"));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                phonePageServlet.doGet(request, response));
        assertNotNull(ex);
        assertEquals("Can't find such phone", ex.getMessage());
        verifyNoInteractions(requestDispatcher);
    }

    @Test
    void doGetWhenPhoneIsNull() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("anyPhone");
        when(IDao.findByName("anyPhone")).thenReturn(null);
        phonePageServlet.doGet(request, response);
        verify(request).getParameter(anyString());
        verify(IDao).findByName(anyString());
    }

    @Test
    void doGetWhenSetAttributeIsFailed() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("anyPhone");
        when(IDao.findByName("anyPhone")).thenReturn(phone);
        doThrow(new IllegalArgumentException("exception"))
                .when(request).setAttribute("anyPhone", phone);
        phonePageServlet.doGet(request, response);
        verify(request).getParameter(anyString());
        verify(IDao).findByName(anyString());
    }

}