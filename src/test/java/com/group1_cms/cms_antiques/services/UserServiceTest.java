package com.group1_cms.cms_antiques.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.group1_cms.cms_antiques.models.Address;
import com.group1_cms.cms_antiques.models.City;
import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.models.State;
import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.models.UserDataDto;
import com.group1_cms.cms_antiques.models.UserProfileDto;
import com.group1_cms.cms_antiques.repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {

	
	private UserService userService;
	
	private UserRepository userRepository;
	private RoleService roleService;
	private StateService stateService;
	private CityService cityService;
	private AddressService addressService;
	private PasswordEncoder passwordEncoder;
	
	private User user;
	
	@BeforeEach
	public void setup() {
		userRepository = Mockito.mock(UserRepository.class);
		roleService = Mockito.mock(RoleService.class);
		stateService =Mockito.mock(StateService.class);
		cityService = Mockito.mock(CityService.class);
		addressService = Mockito.mock(AddressService.class);
		passwordEncoder = Mockito.mock(PasswordEncoder.class);
		userService = new UserService(userRepository, roleService, stateService, cityService, addressService, passwordEncoder);
		
		user = new User();
		
		
	}
	
	//saveUser Tests
	@Test
	public void saveUserWithIdSamePasswordTest() {
		user.setUsername("Username");
		UUID randomId = UUID.randomUUID();
		user.setId(randomId);
		user.setPassword("password");
		
		Mockito.when(userRepository.findUserByUserName(Mockito.anyString())).thenReturn(user);
		Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		userService.saveUser(user, true);
		Mockito.verify(userRepository).save(user);
	}
	
	@Test
	public void saveUserWithIdNewPasswordTest() {
		user.setUsername("Username");
		UUID randomId = UUID.randomUUID();
		user.setId(randomId);
		user.setPassword("password");
		
		User dbUser = new User();
		dbUser.setUsername("Username");
		dbUser.setId(UUID.randomUUID());
		dbUser.setPassword("different password");
		
		Mockito.when(userRepository.findUserByUserName(Mockito.anyString())).thenReturn(dbUser);
		//Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		userService.saveUser(user, true);
		
		Mockito.verify(userRepository).save(user);
		Mockito.verify(passwordEncoder).encode("password");
	}
	
	@Test
	public void saveUserNoIdTest() {
		user.setUsername("Username");
		user.setPassword("password");
		
		Mockito.when(userRepository.findUserByUserName(Mockito.anyString())).thenReturn(new User());
		
		userService.saveUser(user, true);
		Mockito.verify(userRepository).save(user);
		Mockito.verify(passwordEncoder).encode("password");
		Assert.assertNotEquals(null, user.getId());
	}
	
	//tests saveNewUser
	@Test
	public void saveNewUser() {
		UserService spyUserService = Mockito.spy(userService);
		user.setUsername("Username");
		user.setPassword("password");
		
		Role role = new Role();
		role.setName("Test Role");
		role.setId(UUID.randomUUID());
		
		Mockito.when(roleService.findRoleByName("ROLE_Member")).thenReturn(role);
		Mockito.doReturn(user).when(spyUserService).saveUser(Mockito.any(User.class), Mockito.eq(true));
		Mockito.doReturn(user).when(spyUserService).addUserRole(Mockito.any(User.class), Mockito.any(Role.class));
		spyUserService.saveNewUser(user);
		
		
		Mockito.verify(spyUserService).saveUser(Mockito.eq(user), Mockito.eq(true));
		Mockito.verify(spyUserService).addUserRole(Mockito.eq(user), Mockito.any(Role.class));
	}
	
	//tests saveUserWithRolesFromUserDataDto
	@Test
	public void saveUserWithRolesFromUserDataDtoUserIdNullTest() {
		UserDataDto userDto = new UserDataDto();
		List<String> roles = new ArrayList<String>();
		roles.add("Role");
		roles.add("Role");
		userDto.setUserRoles(roles);
		
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		
		userService.saveUserWithRolesFromUserDataDto(userDto);
		
		Mockito.verify(userRepository, Mockito.times(2)).addToUser_Role(Mockito.anyString(), Mockito.anyString());
		Mockito.verify(userRepository).save(Mockito.any(User.class));
	}
	
	//tests saveDefaultAdminUser
	@Test
	public void saveDefaultAdminUserTest() {
		UserService spyUserService = Mockito.spy(userService);
		Role role = new Role();
		role.setName("Test Role");
		role.setId(UUID.randomUUID());
		
		Mockito.when(roleService.findRoleByName("ROLE_Admin")).thenReturn(role);
		Mockito.doReturn(user).when(spyUserService).saveUser(Mockito.any(User.class), Mockito.eq(true));
		Mockito.doReturn(user).when(spyUserService).addUserRole(Mockito.any(User.class), Mockito.any(Role.class));
		
		spyUserService.saveDefaultAdminUser(user);
		
		Mockito.verify(spyUserService).saveUser(Mockito.eq(user), Mockito.eq(true));
		Mockito.verify(spyUserService).addUserRole(Mockito.eq(user), Mockito.any(Role.class));
	}
	
	//tests addUserRole
	@Test
	public void addUserRoleHasIdTest() {
		user.setUsername("Username");
		UUID randomId = UUID.randomUUID();
		user.setId(randomId);
		user.setPassword("password");
		Role role = new Role();
		role.setName("Test Role");
		role.setId(UUID.randomUUID());
		
		userService.addUserRole(user, role);
		
		Mockito.verify(userRepository).addRoles(user, role);
	}
	
	@Test
	public void addUserRoleNoId() {
		user.setUsername("Username");
		Role role = new Role();
		role.setName("Test Role");
		role.setId(UUID.randomUUID());
		
		User dbUser = new User();
		dbUser.setId(UUID.randomUUID());
		dbUser.setUsername("Username");
		
		Mockito.when(userRepository.findUserByUserName(Mockito.anyString())).thenReturn(dbUser);
		
		userService.addUserRole(user, role);
		
		Mockito.verify(userRepository).addRoles(dbUser, role);
	}
	
	@Test
	public void addUserRoleUserNullTest() {
		Role role = new Role();
		role.setName("Test Role");
		role.setId(UUID.randomUUID());
		
		User returnedUser = userService.addUserRole(null, role);
		
		Assert.assertEquals(null, returnedUser);
	}
	
	//Test getUserWithProfileInfo
	@Test
	public void getUserWithProfileInfoNoProfileInfoTest() {
		Mockito.when(userRepository.findUserAndAddressAndCityAndStateByUserName(Mockito.anyString())).thenReturn(user);
		
		UserProfileDto userProfile= userService.getUserWithProfileInfo("test");
		
		Assert.assertEquals(user.getFirstName(), userProfile.getFirstName());
		Assert.assertEquals(user.getLastName(), userProfile.getLastName());
		Assert.assertEquals(user.getUsername(), userProfile.getUserName());
		Assert.assertEquals(user.getPhoneNum(), userProfile.getPhoneNum());
		Assert.assertEquals(false, userProfile.isAddressProvided());
	}
	
	@Test
	public void getUserWithProfileInfoWithInfoTest() {
		user.setFirstName("Bob");
		user.setLastName("Loblaw");
		user.setUsername("Username");
		user.setEmail("email");
		user.setPhoneNum("phoneNumber");
		user.setImagePath("Image");
		Address address = new Address();
		address.setStreetAddress("street");
		City city = new City();
		city.setName("City");
		city.setPostalCode("Postal Code");
		city.setState(new State());
		
		address.setCity(city);
		user.setAddress(address);
		Mockito.when(userRepository.findUserAndAddressAndCityAndStateByUserName(Mockito.anyString())).thenReturn(user);
		
		UserProfileDto userProfile= userService.getUserWithProfileInfo("test");
		
		Assert.assertEquals(user.getFirstName(), userProfile.getFirstName());
		Assert.assertEquals(user.getLastName(), userProfile.getLastName());
		Assert.assertEquals(user.getUsername(), userProfile.getUserName());
		Assert.assertEquals(user.getPhoneNum(), userProfile.getPhoneNum());
		Assert.assertEquals(user.getAddress().getStreetAddress(), userProfile.getAddressLine1());
		Assert.assertEquals(true, userProfile.isAddressProvided());
	}
	
	//Tests saveUserProfile
	@Test
	public void saveUserProfileTest() {
		UserProfileDto userProfileDto = new UserProfileDto();
		userProfileDto.setFirstName("First");
        userProfileDto.setLastName("Last");
        userProfileDto.setUserName("Username");
        userProfileDto.setEmail("Email");
        userProfileDto.setPhoneNum("Phone");
        userProfileDto.setImagePath("Image Path");
        userProfileDto.setAddressLine1("Address");
        userProfileDto.setCityName("City");
        userProfileDto.setZipcode("Zip Code");
        userProfileDto.setStateName("State");
        userProfileDto.setAddressProvided(true);
		
		Mockito.when(userRepository.findUserAndAddressAndCityAndStateByUserName(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);
		Mockito.when(stateService.findStateByName(Mockito.anyString())).thenReturn(new State());
		Mockito.when(cityService.saveCity(Mockito.any(City.class), Mockito.any(State.class))).thenReturn(new City());
		
		User returnedUser = userService.saveUserProfile(userProfileDto);
		
		Mockito.verify(stateService).findStateByName(Mockito.anyString());
		Mockito.verify(cityService).saveCity(Mockito.any(City.class), Mockito.any(State.class));
		Mockito.verify(addressService).saveAddress(Mockito.any(Address.class), Mockito.any(City.class));
	}
	
}
