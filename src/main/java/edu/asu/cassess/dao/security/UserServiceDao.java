package edu.asu.cassess.dao.security;

import edu.asu.cassess.dao.IUserQueryDao;
import edu.asu.cassess.model.rest.AdminCount;
import edu.asu.cassess.model.rest.StudentCount;
import edu.asu.cassess.persist.entity.UserID;
import edu.asu.cassess.persist.entity.registerUser;
import edu.asu.cassess.persist.entity.rest.*;
import edu.asu.cassess.persist.entity.security.Authority;
import edu.asu.cassess.persist.entity.security.User;
import edu.asu.cassess.persist.entity.security.UsersAuthority;
import edu.asu.cassess.persist.repo.AuthorityRepo;
import edu.asu.cassess.persist.repo.UserRepo;
import edu.asu.cassess.persist.repo.UsersAuthorityRepo;
import edu.asu.cassess.service.security.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
@Transactional
public class UserServiceDao {

    protected EntityManager entityManager;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AuthorityRepo authorityRepo;
    @Autowired
    private UsersAuthorityRepo usersAuthorityRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserService usersService;
    @Autowired
    private IUserQueryDao userQuery;
    @Autowired
    private List<Object> userCreateList;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T> Object createUser(User userInput, long role) {
        User user = userRepo.findByEmail(userInput.getEmail());
        if (user == null) {
            userInput.setEnabled();
            userRepo.save(userInput);
            UsersAuthority usersAuth = new UsersAuthority(userInput.getId(), role);
            usersAuthorityRepo.save(usersAuth);
            return userInput;
        } else {
            return new RestResponse("User " + userInput.getEmail() + " already exists in database");
        }
    }

    public <T> Object createUsersByAdmins(List<Admin> admins) {

        for (Admin admin : admins) {
            User user = usersService.adminUser(admin);
            long role = 1;
            Object userCreate = usersService.createUser(user, role);
            userCreateList.add(userCreate);
        }
        return userCreateList;
    }

    public User adminUser(Admin admin) {
        //System.out.println("--------------------!!!!!!!!!!!!!!!!!!!!!!------------Got into AdminUser");
        String array[] = admin.getFull_name().split("\\s+");
        UserID userID = userQuery.getUserID();
        String firstName = "";
        String lastName= "";
        if(array.length == 0)
        {
        		firstName = admin.getFull_name();
        }
        else if(array.length == 2)
        {
        		firstName = array[0];
        		lastName = array[1];
        }
        
        User user = new User(firstName, lastName, admin.getEmail(), null, "en", null, admin.getEmail(), passwordEncoder.encode(admin.getPassword()), null, (long) userID.getMax() + 1);
        return user;
    }

    public <T> Object createUsersByStudents(List<Student> students) {
        for (Student student : students) {
            User user = usersService.studentUser(student);
            long role = 2;
            Object userCreate = usersService.createUser(user, role);
            userCreateList.add(userCreate);
        }
        return userCreateList;
    }

    public User studentUser(Student student) {
        //System.out.println("--------------------!!!!!!!!!!!!!!!!!!!!!!------------Got into StudentUser");
        String array[] = student.getFull_name().split("\\s+");
        UserID userID = userQuery.getUserID();
        String firstName = "";
        String lastName= "";
        if(array.length == 0)
        {
        		firstName = student.getFull_name();
        }
        else if(array.length == 2)
        {
        		firstName = array[0];
        		lastName = array[1];
        }

        User user = new User(firstName, lastName, student.getEmail(), null, "en", null, student.getEmail(), passwordEncoder.encode(student.getPassword()), null, (long) userID.getMax() + 1);
        return user;
    }

    public <T> Object registerUser(String first_name, String family_name, String email, String password, String role) {
        registerUser register_user = new registerUser(first_name, family_name, email, email, password);
        User user = new User(register_user.getFirstName(), register_user.getFamilyName(), register_user.getLogin(), register_user.getPhone(), register_user.getLanguage(), register_user.getPictureId(), register_user.getLogin(), register_user.getPassword(), register_user.getBirthDate());
        Authority auth = new Authority();
        UserID userID = userQuery.getUserID();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId((long) userID.getMax() + 1);
        User userFind = userRepo.findByEmail(user.getEmail());
        long user_authValue = 0;
        if (userFind == null) {
            userRepo.save(user);
            if (role.equalsIgnoreCase("admin")) {
                user_authValue = 1;
            }
            if (role.equalsIgnoreCase("student")) {
                user_authValue = 2;
            }
            if (role.equalsIgnoreCase("super_user")) {
                user_authValue = 3;
            }
            if (role.equalsIgnoreCase("rest")) {
                user_authValue = 4;
            }
            if (!authorityRepo.exists(user_authValue)) {
                if (user_authValue == 1) {
                    auth.setId((long) 1);
                    auth.setName("admin");
                    authorityRepo.save(auth);
                } else if (user_authValue == 2) {
                    auth.setId((long) 2);
                    auth.setName("student");
                    authorityRepo.save(auth);
                } else if (user_authValue == 3) {
                    auth.setId((long) 3);
                    auth.setName("super_user");
                    authorityRepo.save(auth);
                } else if (user_authValue == 4) {
                    auth.setId((long) 4);
                    auth.setName("rest");
                    authorityRepo.save(auth);
                }
            }
            UsersAuthority usersAuth = new UsersAuthority(user.getId(), user_authValue);
            usersAuthorityRepo.save(usersAuth);
            return user;
        } else {
            return new RestResponse("User " + user.getEmail() + " already exists in database");
        }

    }

    public User updateStudent(Student student, User user) {
        String array[] = student.getFull_name().split("\\s+");
        user.setEmail(student.getEmail());
        user.setLogin(student.getEmail());
        user.setPassword(passwordEncoder.encode(student.getPassword()));
        user.setFirstName(array[0]);
        user.setFamilyName(array[1]);
        userRepo.save(user);
        return user;
    }

    public User updateAdmin(Admin admin, User user) {
        String array[] = admin.getFull_name().split("\\s+");
        user.setEmail(admin.getEmail());
        user.setLogin(admin.getEmail());
        user.setPassword(passwordEncoder.encode(admin.getPassword()));
        user.setFirstName(array[0]);
        user.setFamilyName(array[1]);
        userRepo.save(user);
        return user;
    }

    public User deleteUser(User user) {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.users_authority WHERE id_user = ?1");
        query.setParameter(1, user.getId());
        query.executeUpdate();
        Query queryDelete = getEntityManager().createNativeQuery("DELETE FROM cassess.users WHERE e_mail = ?1");
        queryDelete.setParameter(1, user.getEmail());
        queryDelete.executeUpdate();
        return user;
    }

    public Course courseDelete(Course course) {
        if (course.getAdmins() != null) {
            for (Admin admin : course.getAdmins()) {
                Query query = getEntityManager().createNativeQuery("SELECT COUNT(email) AS 'Total' FROM cassess.admins WHERE email = ?1", AdminCount.class);
                query.setParameter(1, admin.getEmail());
                AdminCount adminCount = (AdminCount) query.getSingleResult();
                int result = adminCount.getTotal();
                if (result == 1) {
                    Query queryDelete = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.users WHERE e_mail = ?1", User.class);
                    queryDelete.setParameter(1, admin.getEmail());
                    User user = (User) queryDelete.getSingleResult();
                    deleteUser(user);
                }
            }
        }
        if (course.getTeams() != null) {
            for (Team team : course.getTeams()) {
                for (Student student : team.getStudents()) {
                    Query query = getEntityManager().createNativeQuery("SELECT COUNT(email) AS 'total' FROM cassess.students WHERE email = ?1", StudentCount.class);
                    query.setParameter(1, student.getEmail());
                    StudentCount studentCount = (StudentCount) query.getSingleResult();
                    int result = studentCount.getTotal();
                    if (result == 1) {
                        Query queryDelete = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.users WHERE e_mail = ?1", User.class);
                        queryDelete.setParameter(1, student.getEmail());
                        User user = (User) queryDelete.getSingleResult();
                        deleteUser(user);
                    }
                }
            }
        }
        return course;
    }

    public Course courseUpdate(Course course) {
        if (course.getAdmins() != null) {
            for (Admin admin : course.getAdmins()) {
                Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.users WHERE e_mail = ?1", User.class);
                query.setParameter(1, admin.getEmail());
                User user = (User) query.getSingleResult();
                if (user != null) {
                    usersService.updateAdmin(admin, user);
                }
            }
        }
        if (course.getTeams() != null) {
            for (Team team : course.getTeams()) {
                for (Student student : team.getStudents()) {
                    Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.users WHERE e_mail = ?1", User.class);
                    query.setParameter(1, student.getEmail());
                    User user = (User) query.getSingleResult();
                    if (user != null) {
                        usersService.updateStudent(student, user);
                    }
                }
            }
        }
        return course;
    }

    public Team teamUpdate(Team team) {
        if (team.getStudents() != null) {
            for (Student student : team.getStudents()) {
                Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.users WHERE e_mail = ?1", User.class);
                query.setParameter(1, student.getEmail());
                User user = (User) query.getSingleResult();
                if (user != null) {
                    usersService.updateStudent(student, user);
                }
            }
        }
        return team;
    }

    public Team teamDelete(Team team) {
        if (team.getStudents() != null) {
            for (Student student : team.getStudents()) {
                Query query = getEntityManager().createNativeQuery("SELECT COUNT(email) AS 'total' FROM cassess.students WHERE email = ?1", StudentCount.class);
                query.setParameter(1, student.getEmail());
                StudentCount studentCount = (StudentCount) query.getSingleResult();
                int result = studentCount.getTotal();
                if (result == 1) {
                    Query queryDelete = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.users WHERE e_mail = ?1", User.class);
                    queryDelete.setParameter(1, student.getEmail());
                    User user = (User) queryDelete.getSingleResult();
                    deleteUser(user);
                }
            }
        }
        return team;
    }
}
