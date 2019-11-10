package si.fri.paw.entities;

import javax.persistence.*;

@Entity(name = "user")
@NamedQueries(value =
        {
                @NamedQuery(name = "User.getAll", query = "SELECT u FROM user u"),
                @NamedQuery(name = "User.getByUsername", query = "SELECT u FROM user u WHERE u.username = :username"),
                @NamedQuery(name = "User.getByEmail", query = "SELECT u FROM user u WHERE u.email = :email"),
        })

public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String username;

        private String email;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }
}
